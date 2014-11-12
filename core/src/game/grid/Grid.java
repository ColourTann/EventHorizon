package game.grid;

import java.util.ArrayList;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
import util.Colours;
import util.maths.Pair;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import game.Main;
import game.grid.hex.Hex;
import game.screen.map.Map;
import game.ship.mapThings.MapShip;


public class Grid {
	public static int size=400;

	private Hex[][] hexes;
	public ArrayList<Hex> drawableHexes;
	public static int viewDist;
	public static final int interestingDist=8;
	public static final int goodNumberOfShips=9;
	public static int activeDist=15;
	private static ArrayList<MapShip> closeShips=new ArrayList<MapShip>();
	public static Grid MakeGrid(){
		Grid g= new Grid();
		g.setupGrid();
		return g;
	}

	public void setupGrid(){
		hexes=new Hex[size][];
		for(int i=0;i<size;i++)hexes[i]=new Hex[size];
		for(int x=0;x<size;x++)for(int y=0;y<size;y++)hexes[x][y]= new Hex(x,y,this);
		for(int i=0;i<1500;i++){
			getRandomHex().makeSolarSystem();
		}
		for(int i=0;i<2000;i++){
			getRandomHex().makeMapShip();
		}
		
		for(int i=0;i<000;i++){
			getRandomHex().startNebula(0);
		}
		
	}

	private Hex getRandomHex(){
		int x=(int)(Math.random()*size);
		int y=(int)(Math.random()*size);
		return getHex(x, y);
	}

	public Hex getHex(int x, int y){
		if(x<0||x>=Grid.size||y<0||y>=Grid.size)return null;
		return hexes[x][y];
	}
	public Hex getHexUnderMouse(Pair location) {
		return getHexUnderMouse(location.x, location.y);
	}
	public Hex getHexUnderMouse(float x, float y) {
		return pixelToHex((int)((x+Main.getCam().x)), (int)(y+Main.getCam().y));
	}
	public ArrayList<MapShip> getActiveEnemies() {
		//Sort the enemies by distance so the farthest enemy goes first//
		ArrayList<MapShip> result= new ArrayList<MapShip>();
		for(Hex h:Map.player.hex.getHexesWithin(activeDist, false)){
			if(h.mapShip!=null){
				float dist=h.getDistanceFromExplosion();
				boolean found=false;
				for(int i=0;i<result.size();i++){
					if(result.get(i).hex.getDistanceFromExplosion()<dist){
						result.add(i, h.mapShip);
						found=true;
						break;
					}
				}
				if(found)continue;
				result.add(h.mapShip);
			}
		}
		return result;
	}
	private Hex pixelToHex(int x, int y){
		//Pretty horrible//
		
		float gradient=1.732f;
		//the estimated hexY//
		int hy=(int)((y+Hex.size)/Hex.yGap);
		//The amount you are inside the hex//
		int yInside=(int) ((y+Hex.size)%Hex.yGap);
		int xInside=(int) ((x-(hy%2==0?Hex.xGap/2:0))%Hex.xGap);
		//the estimated hexX//
		int hx=(int)((x+Hex.sqr3*Hex.size/2-hy*Hex.xGap/2)/Hex.xGap);
		//Checking the annoying bottom box of each hex and refiddling//
		if(yInside<Hex.size/2){
			if(xInside/Hex.size-Math.sqrt(3)/2<0){
				if(xInside+yInside*gradient<Hex.xGap/2)hy--;
			}
			else if (xInside-Hex.xGap/2-yInside*gradient>0){
				hy--;
				hx++;
			}
		}
		return getHex(hx, hy);
	}

	public void update(float delta){
		
		viewDist=(int) (Main.height/Hex.yGap/2);
		Hex start =pixelToHex((int)Main.getCam().x+Main.width/2, (int)Main.getCam().y+Main.height/2);
		drawableHexes=null;
		start=Map.player.hex;
		if(start!=null)drawableHexes=start.getHexesWithin(viewDist+2, true);
		if(drawableHexes==null)return;
		for(Hex h:start.getHexesWithin(viewDist+2, true)){
			h.update(delta);
		}
	}

	public void turn(){
		updateCloseShips();
		for(Hex h:drawableHexes)h.hexTurn();
	}
	
	public static ArrayList<MapShip> getCloseShips(){
		return closeShips;
	}
	
	public void addShip(MapShip ship){
		closeShips.add(ship);
	}
	
	private void updateCloseShips() {
		closeShips.clear();
		for(Hex h:Map.player.hex.getHexesWithin(interestingDist, false)){
			if(h.mapShip!=null)closeShips.add(h.mapShip);
		}
	}

	public void shapeRender(ShapeRenderer shape){
		shape.begin(ShapeType.Filled);
		shape.setColor(0, 0, 0, 1);
		if(drawableHexes!=null){
			for(Hex h:drawableHexes)h.renderFilled(shape);
			shape.end();
			shape.begin(ShapeType.Line);
			shape.setColor(Colours.player2[0]);
			for(Hex h:drawableHexes)h.renderBorder(shape);
		}
		shape.end();
	}

	public void render(SpriteBatch batch) {
		if(drawableHexes==null)return;
		for(Hex h:drawableHexes)	h.renderBackGround(batch);
		for(Hex h:drawableHexes)	h.renderContents(batch);
		//for(Hex h:drawableHexes) h.renderLocation(batch);
	}


}
