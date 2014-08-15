package eh.grid.hex;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;

import eh.grid.Grid;
import eh.grid.hexContent.HexContent;
import eh.grid.hexContent.Planet;
import eh.grid.hexContent.Star;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.ship.mapThings.MapShip;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.Timer;
import eh.util.Timer.Interp;
import eh.util.assets.Font;
import eh.util.maths.Pair;

public class Hex {
	//Defining geometry and stuff//
	public static float sqr3=(float) Math.sqrt(3);
	private static Polygon p;
	private static float[] points = new float[12];
	public static float size=30;
	static float width; static float height;
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
	float ticks=0;
	//Pathfinding stuff//
	Hex parent;
	float idealDist=-1;
	float moves=-1;
	Timer mapAbilityFadeTimer;


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
		return new Pair((float) (x*xGap+y*yGap/sqr3),y*yGap);
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
		return(getLineDistance(Map.explosion)-(Map.explosionSize+.5f));
	}

	public float getLineDistance(Hex h){
		Pair origin=getPixel();
		Pair target=h.getPixel();
		Pair distance=target.subtract(origin);
		return (float) (Math.sqrt(distance.x*distance.x+distance.y*distance.y))/Hex.size;
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
		System.out.println(howGood(Map.player));
	}

	public void addShip(MapShip ship) {
		this.mapShip=ship;
		ship.hex=this;
	}
	public boolean makeSolarSystem() {
		if(solarSystemWithin16||blocked)return false;
		content=new Star(this);
		for(Hex h:getHexesWithin(2, true))h.blocked=true;
		for(Hex h:getHexesWithin(16, true)){
			h.solarSystemWithin16=true;
		}
		ArrayList<Hex> possibles=getHexesWithin(6, false);
		Draw.shuffle(possibles);
		int planets=(int) (3+Math.random()*4);
		for(int i=0;i<planets;i++){
			Hex h=possibles.remove(0);
			if(h.getDistance(this)<3)continue;
			h.content=new Planet(h);
		}
		return true;
	}
	public void update(float delta){
		ticks+=delta ;
		if(mapShip!=null){
			mapShip.update(delta);
		}
	}

	public void hexTurn(){
		forceField--;
	}

	public void makeMapShip(){
		if(blocked)return;
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
		while(open.size()>0){
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
		System.out.println("Can't find path, took too long");return null;
	}

	private boolean isSwallowed() {
		return(getLineDistance(Map.explosion)<Map.explosionSize+.5f);
	}



	public float howGood(MapShip ship){
		float result=0;
		float myPower=ship.ship.getPowerLevel();



		//Distance from explosion!//

		float explosionDistance=getDistanceFromExplosion()-Map.growthRate;	//adjusted for how long it takes to move//
		if(explosionDistance<0)return -999;	//Too close to explosion//
		float distanceMult=2;
		float adjustedDistance=(float) (-1/Math.pow(explosionDistance, 2))*distanceMult;
		result+=adjustedDistance;


		//Nearby Ships//
		float ignoreRange=.01f;
		int distanceCutoff=4;			//Past this distance will be ignored//
		float playerAttack=500;			//except for attacking//
		float enemyAttack=700;
		float attackMultuplier=.02f;
		float fleeMultiplier=-.04f;

		for(Hex h:getHexesWithin(distanceCutoff, true)){
			MapShip hexShip= h.mapShip;
			if(hexShip==null||hexShip==ship)continue;
			if(hexShip.ship==null)hexShip.init();
			boolean player=hexShip.ship.player;
			float theirPower=hexShip.getPowerLevel();
			//Flee decision//
			float fleeMult=0;
			if(Math.abs(theirPower-myPower)<ignoreRange)fleeMult=0;
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

	public void mapAbilityChoiceFadein(){
		mapAbilityFadeTimer=new Timer(0, 1, 2, Interp.SQUARE);
	}

	public void mapAbilityChoiceFadeout(){
		mapAbilityFadeTimer=new Timer(mapAbilityFadeTimer.getFloat(), 0, 2, Interp.SQUARE);
	}



	public void renderFilled(ShapeRenderer shape){
		shape.setColor(Colours.dark);

		if(mapAbilityFadeTimer!=null&&mapAbilityFadeTimer.getFloat()>0){
			shape.setColor(Colours.withAlpha(Colours.blueWeaponCols4[2],mapAbilityFadeTimer.getFloat()));
		}
	//	if(Map.using.isValidChoice(Map.player.hex, this))shape.setColor(Colours.withAlpha(Colours.blueWeaponCols4[2],ticks%1));

		if(highlight)shape.setColor(Colours.blueWeaponCols4[2]);
		if(moused)shape.setColor(Colours.light);	
		if(forceField>0)shape.setColor(Colours.withAlpha(Colours.blueWeaponCols4[0], forceField/3f));
		
		if(isSwallowed())shape.setColor(Colours.redWeaponCols4[0]);

		Pair s=getPixel();
		shape.triangle(s.x+points[0], s.y+points[1], s.x+points[2], s.y+points[3], s.x+points[4], s.y+points[5]);
		shape.triangle(s.x+points[4], s.y+points[5], s.x+points[6], s.y+points[7], s.x+points[8], s.y+points[9]);
		shape.triangle(s.x+points[8], s.y+points[9], s.x+points[10], s.y+points[11], s.x+points[0], s.y+points[1]);
		shape.triangle(s.x+points[0], s.y+points[1], s.x+points[4], s.y+points[5], s.x+points[8], s.y+points[9]);
	}

	public void renderBorder(ShapeRenderer shape) {
		Pair s=getPixel();
		p.setPosition(s.x, s.y);
		shape.polygon(p.getTransformedVertices());

	}

	public void renderBackGround(SpriteBatch batch) {
		if(content!=null)content.render(batch);
	}

	public void renderContents(SpriteBatch batch) {
		if(mapShip!=null)mapShip.render(batch);
	}

	public void renderLocation(SpriteBatch batch){
		String s=this.toString();
		Font.small.draw(batch, s, getPixel().x-Font.small.getBounds(s).width/2, getPixel().y-Font.small.getBounds(s).height/2);
	}

	public boolean isBlocked(boolean shipsBlock){
		if(shipsBlock&&mapShip!=null)return true;
		if(forceField>0)return true;
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




}
