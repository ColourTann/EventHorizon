package game.grid.hex;

import java.util.ArrayList;
import java.util.HashMap;

import util.Colours;
import util.Draw;
import util.Noise;
import util.assets.Font;
import util.image.PairPic;
import util.image.Pic;
import util.maths.Pair;
import util.update.Timer;
import util.update.Timer.Interp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;

import game.assets.Gallery;
import game.grid.Grid;
import game.grid.hexContent.HexContent;
import game.grid.hexContent.Planet;
import game.grid.hexContent.Star;
import game.screen.battle.tutorial.PicLoc;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.ship.mapThings.MapShip;

public class Hex {
	//Defining geometry and stuff//
	public static double sqr3=Math.sqrt(3);
	private static Polygon p;
	private static float[] points = new float[12];

	//	public static float size=26.6f; //for 8 hexes
	public static float size=30;

	public static float width, height;
	public static float xGap;
	public static float yGap;

	//Map gen stuff//
	public Grid grid;
	public boolean solarSystemWithin16;

	public HexContent content;

	public boolean highlight;
	public boolean moused; 
	public static Hex mousedHex;

	public int x;
	public int y;

	public MapShip mapShip;
	public int forceField;
	private boolean blocked;
	//
	//Pathfinding stuff//
	Hex parent;
	float idealDist=-1;
	float moves=-1;


	Timer mapAbilityFadeTimer;
	private Timer empTimer;
	private Timer forceFieldTimer;
	private boolean nebula;
	Texture nebulaTexture;
	private Hex nebulaOrigin;
	private ArrayList<Hex> nebulaList;

	public static void init(){
		for(int i=0;i<12;i+=2){
			points[i]=(float) Math.sin(i*Math.PI/6)*size;
			points[i+1]=(float) Math.cos(i*Math.PI/6)*size;
		}
		width=size*2;
		height=(float) (Math.sqrt(3)/2*width);
		xGap=height;
		yGap=width*3f/4f;
		p=new Polygon(points);
		if(mousedHex==null)mousedHex=new Hex(-999,-999,null);
	}



	public static void resize(float reSize){
		size=reSize;
		init();
	}



	public Hex(int x, int y, Grid g){
		this.grid=g;
		this.x=x;
		this.y=y;
	}

	public Pair getPixel(){

		//return new Pair((float) Math.round((x*xGap+y*yGap/sqr3)),(float)Math.round(y*yGap));
		return new Pair((float) (x*xGap+y*yGap/sqr3),(y*yGap));
	}
	public ArrayList<Hex> getHexesWithin(int dist, boolean includeSelf){
		ArrayList<Hex> result= new ArrayList<Hex>();
		for(int dx=-dist;dx<=dist;dx++){
			for(int dy=-dist;dy<=dist;dy++){
				if(x+dx<0||x+dx>=Grid.size||y+dy<0||y+dy>=Grid.size)continue;
				Hex h=grid.getHex(this.x+dx,this.y+dy);
				if(h==null)continue;
				if(getDistance(h)>dist)continue;
				result.add(h);
			}
		}
		if(!includeSelf)result.remove(this);
		return result;
	}
	public int getDistance(Hex h){
		int xDist=h.x-x;
		int yDist=h.y-y;
		return Math.max(Math.abs(xDist), Math.max(Math.abs(yDist), Math.abs(xDist+yDist)));
	}

	public float getDistanceFromExplosion() {
		
		return(getLineDistance(Map.explosion)-(Map.getExplosionSize()));
	}

	public float getLineDistance(Hex h){
		Pair origin=getPixel();
		Pair target=h.getPixel();
		Pair distance=target.subtract(origin);
		return (float) (Math.sqrt(distance.x*distance.x+distance.y*distance.y))/Hex.height;
	}
	public void mouse() {
		if(getDistance(Map.player.hex)>Grid.viewDist){
			mousedHex.unMouse();
			return;
		}
		if(moused)return;
		moused=true;
		mousedHex.unMouse();
		mousedHex=this;
		if(Map.getState()==MapState.PlayerTurn){
			ArrayList<Hex> path=Map.player.hex.pathFind(this);
			Map.path=path;
			if(path!=null){
				for (Hex h: path){
					h.highlight=true;
				}
			}
		}
		Map.mouseOverHex(this);

	}

	private void unMouse() {

		moused=false;
		if(Map.getState()!=MapState.PlayerTurn)return;
		if(Map.path!=null){
			for (Hex h: Map.path){
				h.highlight=false;
			}
		}
	}

	public void click(){
		mouse();
		if(getDistance(Map.player.hex)>Grid.viewDist)return;
		if(Map.using!=null){
			Map.using.pickHex(this);
			return;
		}

		if(Map.getState()!=MapState.PlayerTurn){
			Map.player.resetPath();
			return;
		}
		ArrayList<Hex> path=Map.player.hex.pathFind(this);
		if(path!=null){
			Map.setState(MapState.PlayerMoving);
			Map.player.setPath(path);
		}
	}

	public void rightClick(){
		System.out.println(getDistanceFromExplosion());
		//System.out.println(howGood(Map.player));
		if(nebulaTexture!=null){
			for(Hex h:getHexesWithin(10, true)){
				h.nebula=false;
			}
			startNebula(0);
		}
	}

	public void addShip(MapShip ship) {
		if(this.mapShip!=null){
			System.out.println("a battle!");
			this.mapShip.battle();
		}
		this.mapShip=ship;
		ship.hex=this;
	}
	public boolean makeSolarSystem() {
		if(solarSystemWithin16||isBlocked(true))return false;
		for(Hex h:getHexesWithin(3, true))if(h.isBlocked(true))return false;
		content=new Star(this);
		for(Hex h:getHexesWithin(2, true))h.blocked=true;
		for(Hex h:getHexesWithin(16, true)){
			h.solarSystemWithin16=true;
		}
		ArrayList<Hex> possibles=getHexesWithin(6, false);
		Draw.shuffle(possibles);
		int planets=(int) (7+Math.random()*4);
		for(int i=0;i<planets;i++){
			Hex h=possibles.remove(0);
			if(h.isBlocked(true))continue;
			if(h.getDistance(this)<3)continue;
			h.content=new Planet(h);
		}
		return true;
	}
	public void update(float delta){
		if(mapShip!=null){
			mapShip.update(delta);
		}
	}

	public void hexTurn(){
		forceField--;
		if(forceField>=0){
			forceFieldTimer=new Timer(forceFieldTimer.getFloat(), forceField/3f, .3f, Interp.LINEAR);
		}
		if(content!=null)content.turn();




	}

	public void makeMapShip(){
		if(isBlocked(true))return;
		mapShip=new MapShip(this);
	}

	public ArrayList<Hex> pathFind(Hex target){

		if(target.isBlocked(false)||this==target)return null;

		ArrayList<Hex> open=new ArrayList<Hex>();
		ArrayList<Hex> closed=new ArrayList<Hex>();

		idealDist=getDistance(target);
		moves=0;
		open.add(this);
		Pair totalDist=target.getPixel().subtract(getPixel());
		totalDist=totalDist.absolute();
		totalDist.x+=.0001f;
		totalDist.y+=.0001f;
		int maxChecks=1000;
		int checks=0;
		while(open.size()>0){
			checks++;
			if(checks>=maxChecks)break;
			float closest=9999;
			Hex check=null;
			for(Hex h:open){
				float bonus=h.getLineDistance(target)/10;
				float score=h.moves+h.idealDist-bonus;
				if(score<closest){
					closest=score;
					check=h;
				}
			}
			open.remove(check);
			closed.add(check);
			for(Hex h:check.getHexesWithin(1, false)){

				if(h.isBlocked(false))continue;

				if(closed.contains(h)||open.contains(h)){
					if(check.moves+1<=h.moves){
						h.parent=check;
						h.moves=check.moves+1;
					}
					continue;
				}


				if(h==target){
					ArrayList<Hex> result=new ArrayList<Hex>();
					h.parent=check;
					Hex add=h;
					result.add(add);
					while(add.parent!=null){
						add=add.parent;
						if(add==this)break;
						result.add(add);
					}
					for(Hex reset:open){
						reset.parent=null;
						reset.idealDist=-1;
						reset.moves=-1;
					}
					for(Hex reset:closed){
						reset.parent=null;
						reset.idealDist=-1;
						reset.moves=-1;
					}
					h.parent=null;
					closed.clear();
					open.clear();

					return result;
				}
				h.idealDist=h.getDistance(target);
				h.parent=check;
				h.moves=check.moves+1f;

				open.add(h);
			}			
		}
		for(Hex reset:open){
			reset.parent=null;
			reset.idealDist=-1;
			reset.moves=-1;
		}
		for(Hex reset:closed){
			reset.parent=null;
			reset.idealDist=-1;
			reset.moves=-1;
		}
		System.out.println("Can't find path, took too long");return new ArrayList<Hex>();
	}




	public float howGood(MapShip ship){
		//Ensure won't instantly die//
		float baseDistance=getDistanceFromExplosion()-Map.growthRate;
		if(baseDistance<0)return -999;	
		
		//
		float result = 0;
		int analyseDistance=6;
		SurroundingAnalysis lyse = analyse(analyseDistance);
		result+=lyse.furthestDistance;
		if(true)return result;
		
		//Distance from explosion!//
	
		
		
		//Get furthest from black hole in <=6 steps
		int maxSteps=12;
		HashMap<Hex, Integer> checked = new HashMap<Hex, Integer>();
		ArrayList<Hex> toCheck = new ArrayList<Hex>();
		checked.put(this, 0);
		toCheck.add(this);
		while(toCheck.size()>0){
			Hex current = toCheck.remove(0);

			int steps=checked.get(current);
			if(steps>=maxSteps)continue;
			for(Hex h:current.getHexesWithin(1, false)){
				if(h.isBlocked(false))continue;
				if(checked.get(h)!=null)continue;
				if(current.getDistance(this)>maxSteps)continue;
				checked.put(h, steps+1);
				toCheck.add(toCheck.size(), h);
			}
		}
		//Find the best one
		//This is the furthest you can get in <=maxsteps moves//
		float explosionDistance=0;
		float bestTurns=0;
		for(Hex h:checked.keySet()){
			float thisDistance=h.getDistanceFromExplosion();
			if(thisDistance>explosionDistance){
				explosionDistance=thisDistance;
				bestTurns=checked.get(h);
			}
		}
		//Subtract steps+1 turns of black hole movement
		explosionDistance-=Map.growthRate*(bestTurns+1);
		if(explosionDistance<=0)return -999;

		float distanceMultiplier=2;

		float adjustedExplosionDistance=(float) (-1/Math.pow(explosionDistance, 2))*distanceMultiplier;

		//System.out.println("Adjusted explosion: "+adjustedExplosionDistance+", best turns: "+bestTurns);
		result += adjustedExplosionDistance;

		//http://i.imgur.com/dMOkeTK.png cool weights//




		//Nearby Ships//

		float myPower=ship.getPowerLevel();
		int distanceCutoff=4;			//Past this distance will be ignored//
		float playerAttack=1;			//except for attacking//
		float enemyAttack=1.1f;
		float attackMultuplier=.01f;
		float fleeMultiplier=-.02f;

		for(Hex h:getHexesWithin(distanceCutoff, true)){
			MapShip hexShip= h.mapShip;
			if(hexShip==null||hexShip==ship)continue;

			boolean player=hexShip.getShip().player;
			float theirPower=hexShip.getPowerLevel();
			//Flee decision//
			float fleeMult=0;
			if(Math.abs(theirPower-myPower)<MapShip.ignoreRange)fleeMult=0;
			else fleeMult=myPower>theirPower?attackMultuplier:fleeMultiplier;	//Run away or not?
			float shipDistance=h.getDistance(this);


			if(shipDistance==0){
				if(fleeMult<0)return -9;			//Don't attack ships you can't take on
				result+=fleeMult*(player?playerAttack:enemyAttack);
			}



			result+=fleeMult*(1/shipDistance);
		}

		
		return result;
	}

	private SurroundingAnalysis analyse(int range){
		SurroundingAnalysis result = new SurroundingAnalysis();
		if(isBlocked(false))return result;
		int currentDistance=0;
		ArrayList<Hex> open = new ArrayList<Hex>();
		ArrayList<Hex> future = new ArrayList<Hex>();
		ArrayList<Hex> closed = new ArrayList<Hex>();
		
		open.add(this);
		closed.add(this);
		if(mapShip!=null)result.addShip(mapShip, currentDistance);
		result.setDistance(getDistanceFromExplosion());
		while(currentDistance<range){
			while(open.size()>0){
				Hex check=open.remove(0);
				for(Hex h:check.getHexesWithin(1, false)){
					if(h.swallowed(currentDistance+1))continue;
					if(h.isBlocked(false))continue;
					if(closed.contains(h))continue;
					if(future.contains(h))continue;
					future.add(h);
					closed.add(h);
					if(h.mapShip!=null)result.addShip(h.mapShip, currentDistance);
					result.setDistance(h.getDistanceFromExplosion());
				}
				
			}
			currentDistance++;
			open=future;
			future= new ArrayList<Hex>();
		}

		return result;
	}

	private boolean swallowed(int turns) {
		return getDistanceFromExplosion()-turns<.5f;
	}



	public void mapAbilityChoiceFadein(){
		if(mapAbilityFadeTimer!=null){
			mapAbilityFadeTimer=new Timer(mapAbilityFadeTimer.getFloat(), 1, 1/2f, Interp.SQUARE);
		}
		else mapAbilityFadeTimer=new Timer(0, 1, 1/2f, Interp.SQUARE);

	}

	public void mapAbilityChoiceFadeout(){
		if(mapAbilityFadeTimer==null)return;
		mapAbilityFadeTimer=new Timer(mapAbilityFadeTimer.getFloat(), 0, 1/2f, Interp.SQUARE);
	}

	public void renderFilled(ShapeRenderer shape){
		shape.setColor(Colours.dark);

		if(mapAbilityFadeTimer!=null&&mapAbilityFadeTimer.getFloat()>0){
			shape.setColor(Colours.withAlpha(Colours.blueWeaponCols4[2],mapAbilityFadeTimer.getFloat()));
		}
		//	if(Map.using.isValidChoice(Map.player.hex, this))shape.setColor(Colours.withAlpha(Colours.blueWeaponCols4[2],ticks%1));

		if(highlight)shape.setColor(Colours.blueWeaponCols4[2]);
		if(moused)shape.setColor(Colours.light);	
		if(forceFieldTimer!=null&&forceFieldTimer.getFloat()>0){
			shape.setColor(Colours.shiftedTowards(Colours.dark, Colours.blueWeaponCols4[1], forceFieldTimer.getFloat()));
		}

		if(swallowed(0))shape.setColor(Colours.redWeaponCols4[0]);

		if(empTimer!=null&&empTimer.getFloat()>0){
			shape.setColor(Colours.shiftedTowards(Colours.dark, Colours.genCols5[3], empTimer.getFloat()/1.5f));
		}



		//		if(mapShip!=null&&mapShip.isStunned()){
		//			shape.setColor(Colours.shiftedTowards(Colours.dark, Colours.genCols5[3], empTimer.getFloat()/1.5f));
		//		}

		Pair s=getPixel();
		shape.triangle(s.x+points[0], s.y+points[1], s.x+points[2], s.y+points[3], s.x+points[4], s.y+points[5]);
		shape.triangle(s.x+points[4], s.y+points[5], s.x+points[6], s.y+points[7], s.x+points[8], s.y+points[9]);
		shape.triangle(s.x+points[8], s.y+points[9], s.x+points[10], s.y+points[11], s.x+points[0], s.y+points[1]);
		shape.triangle(s.x+points[0], s.y+points[1], s.x+points[4], s.y+points[5], s.x+points[8], s.y+points[9]);



	}

	public void renderBorder(ShapeRenderer shape) {

		//		if(true)return;

		Pair s=getPixel();
		p.setPosition((float)Math.round(s.x), (float)Math.round(s.y));
		float[] vertices=p.getTransformedVertices();
		shape.line(vertices[0], vertices[1], vertices[2], vertices[3]);
		shape.line(vertices[4], vertices[5], vertices[2], vertices[3]);
		shape.line(vertices[4], vertices[5], vertices[6], vertices[7]);
		//				shape.polygon(p.getTransformedVertices());

	}

	public void renderBackGround(SpriteBatch batch) {
		if(content!=null)content.render(batch);
		batch.setColor(1,1,1,1);

	}

	public void renderContents(SpriteBatch batch) {
		if(mapShip!=null)mapShip.render(batch);
		if(nebulaOrigin!=null){
			Map.grid.addFarRenderHex(nebulaOrigin);
		}
	}

	public void renderFar(SpriteBatch batch) {

		setupNebulaTexture();
		Draw.draw(batch, nebulaTexture, (getPixel().x+nebulaDrawOffset.x-nebulaOffset), (getPixel().y+nebulaOffset+nebulaDrawOffset.y-nebulaOffset-Hex.size));

	}



	public void renderLocation(SpriteBatch batch){
		String s=this.toString();
		Font.small.draw(batch, s, getPixel().x-Font.small.getBounds(s).width/2, getPixel().y-Font.small.getBounds(s).height/2);
	}

	public boolean isBlocked(boolean shipsBlock){
		if(shipsBlock&&mapShip!=null)return true;
		if(forceField>0)return true;
		if(nebula)return true;
		return blocked;
	}

	public String toString(){
		return x+":"+y;
	}

	public Hex getRandomAdjacentUnblocked() {
		ArrayList<Hex> hexes=getHexesWithin(1, false);
		Draw.shuffle(hexes);
		for(Hex h:hexes){
			if(!h.isBlocked(false))return h;
		}
		return this;
	}

	public void Emp(){
		empTimer=new Timer(1,0,1/Map.phaseSpeed,Interp.LINEAR);
		if(mapShip!=null){
			mapShip.stun(2);
			empTimer=new Timer(1,1,0,Interp.LINEAR);
		}
	}

	public void clearEMP(){
		empTimer=new Timer(1,0,.3f,Interp.LINEAR);
	}

	public void forceField(int turns) {
		forceField=turns;
		forceFieldTimer=new Timer(1,1,0,Interp.LINEAR);
		mapAbilityChoiceFadeout();
	}

	public void unForceField(){
		forceField=0;
		forceFieldTimer=new Timer(0,0,0,Interp.LINEAR);
		mapAbilityChoiceFadein();
		forceFieldTimer.update(1);
		System.out.println(forceFieldTimer);
	}

	public void addNebula(){
		nebulae.add(this);
		nebula=true;
	}

	public boolean getNebula(){
		return nebula;
	}

	private static int nebulaSize=0;
	private static ArrayList<Hex> nebulae= new ArrayList<Hex>();
	private static final int maxNebulaDistance=6;
	public void startNebula(int size){
		if(size==0){
			nebulaSize=0;
			nebulae.clear();
		}
		addNebula();
		nebulaSize++;
		for(Hex h:getHexesWithin(1, false)){
			if(h.getNebula())continue;
			if(Math.random()*nebulaSize<1.5){
				h.startNebula(nebulaSize);
			}
		}
		if(size==0){
			for(int i=nebulae.size()-1;i>=0;i--){
				Hex h=nebulae.get(i);
				h.nebulaOrigin=this;
			}
			nebulaList=(ArrayList<Hex>) nebulae.clone();
		}

	}


	private void setupNebulaTexture() {
		//get boudns first//
		if(nebulaTexture!=null)return;
		Pair topLeft=null;
		Pair botRight=null;

		for(Hex h:nebulaList){
			Pair loc = h.getPixel();
			if(topLeft==null){

				topLeft=loc.copy();
				botRight=loc.copy();
				continue;
			}
			if(loc.x<topLeft.x){
				topLeft.x=loc.x;
			}
			if(loc.y<topLeft.y){
				topLeft.y=loc.y;
			}
			if(loc.x>botRight.x){
				botRight.x=loc.x;
			}
			if(loc.y>botRight.y){
				botRight.y=loc.y;
			}
		}

		//bounds got!

		ArrayList<Pair> foci = new ArrayList<Pair>();
		for(Hex h:nebulaList){
			foci.add(h.getPixel().subtract(topLeft));
		}
		makeNoiseTexture((int)(botRight.x-topLeft.x), (int)(botRight.y-topLeft.y), foci);
		nebulaDrawOffset=topLeft.subtract(getPixel());

	}
	private Pair nebulaDrawOffset;
	private static int nebulaOffset=(int) Hex.size;
	private void makeNoiseTexture(int width, int height, ArrayList<Pair> foci) {

		Pixmap map = new Pixmap(width+nebulaOffset*2,height+nebulaOffset*2,Format.RGBA8888);
		Pixmap.setBlending(Blending.None);
		float scale=.031f;
		float offset=nebulaOffset;



		float maxDist=(float) Math.sqrt(Hex.size*Hex.size*2);
		//maxDist=40;
		int fidelity=2;
		for(int x=-nebulaOffset;x<map.getWidth();x+=fidelity){
			for(int y=-nebulaOffset;y<map.getHeight();y+=fidelity){
				float bestCloseness=0;
				for(Pair p:foci){
					float newDist = p.getDistance(new Pair(x,y));
					float closeness=newDist/maxDist;
					closeness=1-closeness;
					if(closeness>0){
						bestCloseness+=Math.pow(closeness, 1.09);
					}
				}
				bestCloseness=Math.min(1, bestCloseness);
				//				float bestCloseness=.5f;


				float noise =(float)Noise.noise(x*scale+offset, y*scale+offset, 8);

				//				float noise=1;
				noise++;
				noise/=2d;
				//noise+=dist*2;
				//noise-=2f;
				noise=Math.max(0, noise);
				noise=Math.min(1, noise);
				//noise*=bestCloseness;
				//noise=bestCloseness;

				//purple nebula//


				map.setColor((noise*noise)%.5f, noise*noise*.8f, .5f+noise/4f, bestCloseness*bestCloseness);


				//map.setColor(noise%.2f, (noise%.5f), noise, 1);
				map.fillRectangle(x+nebulaOffset, y+nebulaOffset, fidelity, fidelity);
			}
		}



		nebulaTexture=new Texture(map);
	}

}
