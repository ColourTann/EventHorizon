package game.ship.mapThings.mapAbility;


import util.Draw;
import util.image.Pic;
import util.maths.Pair;
import util.maths.PolygonCollider;
import util.update.Mouser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.ship.mapThings.MapShip;


public abstract class MapAbility extends Mouser{
	
	public int cooldown;
	Pair location;
	public MapShip mapShip;
	public int range;
	public float effort;
	public enum MapAbilityType{
		//Generator abilities
		Teleport, Move, Diagonal,

		//Computer Abilities
		Cloak, Beam, Forcefield
	}
	public MapAbility(Pic p, int range, float effort) {
		abilityPic=p;
		this.range=range;
		this.effort=effort;
		deactivate();
	}

	@Override
	public void mouseClicked(boolean left) {
		if(Map.using==this){
			fadeHexesOut();
			deselect(); 
			return;
		}
		else if(Map.getState()==MapState.PlayerTurn){
			select(); 
			fadeHexesIn();
			return;
		}
	}

	public void select() {
		Map.using=this;
		Map.setState(MapState.PickHex);
	}

	public void deselect() {
		Map.returnToPlayerTurn();
		Map.using=null;
	}
	
	public abstract HexChoice getBestTarget();

	public abstract boolean isValidChoice(Hex target);

	public abstract void pickHex(Hex hex);

	public void afterPlayerUse(){
		fadeHexesOut();
		Map.setState(MapState.PlayerMoving);
		Map.using=null;
	}

	@Override
	public void mouseDown() {
	}
	@Override
	public void mouseUp() {
	}
	@Override
	public void update(float delta) {
	}

	//drawing stuff//

	public static int gap = 90;
	public Pic abilityPic;
	private static Polygon basePolygon;
	public static float height;
	public static float width;

	public static void init(){
		float size=47;
		float points[] = new float[12];
		for(int i=0;i<12;i+=2){
			points[i]=(float) Math.cos(i*Math.PI/6)*size;
			points[i+1]=(float) Math.sin(i*Math.PI/6)*size;
		}
		basePolygon=new Polygon(points);
		height=basePolygon.getBoundingRectangle().height;
		width=basePolygon.getBoundingRectangle().width;
	}

	public void fadeHexesIn(){
		for(Hex h:Map.grid.drawableHexes)if(isValidChoice(h))h.mapAbilityChoiceFadein();
	}
	
	public void fadeHexesOut(){
		for(Hex h:Map.grid.drawableHexes)if(isValidChoice(h))h.mapAbilityChoiceFadeout();
	}
	
	public void showAt(Pair location){
		this.location=location;
		Polygon translated=new Polygon(basePolygon.getVertices());
		translated.translate(location.x, location.y);
		mousectivate(new PolygonCollider(translated));
	}
	
	public void render(SpriteBatch batch) {
		Draw.drawCenteredScaled(batch, abilityPic.get(), location.x, location.y, 1, 1);
	}



}
