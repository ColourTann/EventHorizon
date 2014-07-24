package eh.grid.hex;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import eh.assets.Font;
import eh.grid.Grid;
import eh.grid.hexContent.HexContent;
import eh.grid.hexContent.Planet;
import eh.grid.hexContent.Star;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.ship.Ship;
import eh.ship.mapThings.MapShip;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.maths.Sink;

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


	boolean moused; 
	static Hex mousedHex;

	int x;
	int y;

	public MapShip mapShip;
	private boolean blocked;



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

	public Sink getPixel(){
		return new Sink((float) (x*xGap+y*yGap/sqr3),y*yGap);
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
		Sink origin=getPixel();
		Sink target=h.getPixel();
		Sink distance=target.subtract(origin);
		return (float) (Math.sqrt(distance.x*distance.x+distance.y*distance.y))/Hex.size;
	}
	public void mouse() {
		mousedHex.unMouse();
		mousedHex=this;
		moused=true;
	}

	private void unMouse() {
		moused=false;
	}

	public void click(){
		if(getDistance(Map.player.hex)>Grid.viewDist)return;
		if(Map.getState()!=MapState.PlayerChoosing){
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
		System.out.println(howGood(null));
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
		Junk.shuffle(possibles);
		int planets=(int) (3+Math.random()*4);
		for(int i=0;i<planets;i++){
			Hex h=possibles.remove(0);
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

	public void makeMapShip(){
		if(blocked)return;
		mapShip=new MapShip(this);
	}

	public ArrayList<Hex> pathFind(Hex target){
		class Node{
			Hex hex;
			Node previous;
			Node(Hex hex, Node previous){
				this.hex=hex;
				this.previous=previous;
			}
		}
		ArrayList<Node> queue=new ArrayList<Node>();
		ArrayList<Hex> checked=new ArrayList<Hex>();
		queue.add(new Node(this,null));
		for(int i=0;i<1000;i++){
			if(queue.size()==0)return null;
			Node n=queue.remove(0);
			if(n.hex==target){
				ArrayList<Hex> result= new ArrayList<Hex>();
				while(n.previous!=null){
					result.add(0,n.hex);
					n=n.previous;
				}
				return result;
			}
			for(Hex h:n.hex.getHexesWithin(1, false)){
				if(checked.contains(h))continue;
				if(h.blocked)continue;
				checked.add(h);
				queue.add(new Node(h, n));
			}
		}
		System.out.println("Can't find path, took too long");return null;
	}
	
	private boolean isSwallowed() {
		return(getLineDistance(Map.explosion)<Map.explosionSize+.5f);
	}

	public void renderFilled(ShapeRenderer shape){
		shape.setColor(Colours.dark);
		if(moused)shape.setColor(Colours.light);	
		if(isSwallowed())shape.setColor(Colours.redWeaponCols4[0]);
		
		Sink s=getPixel();
		shape.triangle(s.x+points[0], s.y+points[1], s.x+points[2], s.y+points[3], s.x+points[4], s.y+points[5]);
		shape.triangle(s.x+points[4], s.y+points[5], s.x+points[6], s.y+points[7], s.x+points[8], s.y+points[9]);
		shape.triangle(s.x+points[8], s.y+points[9], s.x+points[10], s.y+points[11], s.x+points[0], s.y+points[1]);
		shape.triangle(s.x+points[0], s.y+points[1], s.x+points[4], s.y+points[5], s.x+points[8], s.y+points[9]);
	}
	
	public float howGood(MapShip ship){
		//adjusted for how long it takes to move//
		float result=0;
		float distance=getDistanceFromExplosion()-Map.growthRate;
		System.out.println();
		System.out.println();
		
		//Too close to explosion//
		if(distance<0)return -999;
		
		System.out.println("Base distance: "+distance);
		System.out.println("Adjusted distance "+-1/Math.pow(distance, 2));
		
		result+=-1/Math.pow(distance, 2);
		
		float playerMultiplier=.07f;
		float playerDistance = getDistance(Map.player.hex);
		if(playerDistance==0)result+=10;
		else result+=(1/playerDistance)*playerMultiplier;
		
		return result;
	}


	public void renderBorder(ShapeRenderer shape) {
		Sink s=getPixel();
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
		Font.small.draw(batch, this.toString(), getPixel().x-Font.small.getBounds(s).width/2, getPixel().y-Font.small.getBounds(s).height/2);
	}

	public boolean isBlocked(){
		//if(mapShip!=null)return true;
			return blocked;
	}

	public String toString(){
		return x+":"+y;
	}

	public Hex getRandomAdjacentUnblocked() {
		ArrayList<Hex> hexes=getHexesWithin(1, false);
		Junk.shuffle(hexes);
		for(Hex h:hexes){
			if(!h.isBlocked())return h;
		}
		return this;
	}



	
}
