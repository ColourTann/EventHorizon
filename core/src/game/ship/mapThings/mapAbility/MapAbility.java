package game.ship.mapThings.mapAbility;


import util.Colours;
import util.Draw;
import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;
import util.maths.PolygonCollider;
import util.update.Mouser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import game.assets.Gallery;
import game.assets.Sounds;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.ship.mapThings.MapShip;


public abstract class MapAbility extends Mouser{
	
	public int baseCooldown, cooldown;
	Pair location;
	public MapShip mapShip;
	public int range;
	public float effort;
	
	public MapAbility(Pic p, int cooldown, int fuelCost, int range, float effort) {
		abilityPic=p;
		this.range=range;
		this.effort=effort;
		this.baseCooldown=cooldown;
	}

	@Override
	public void mouseClicked(boolean left) {
		if(!isUsable()){
			Sounds.error.play();
			return;
		}
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
		if(location==null){
			System.out.println("null location");
			return;
		}
		
		Draw.drawCenteredScaled(batch, abilityPic.get(), location.x, location.y, 1, 1);
		Draw.drawScaled(batch, Gallery.mapAbilityCooldown.get(), (int)location.x+50, (int)location.y-36, 2, 2);
		Font.medium.setColor(Colours.genCols5[3]);
		Font.drawFontCentered(batch, ""+baseCooldown, Font.medium, (int)location.x+64,  (int)location.y+8);
		
		if(cooldown==0){
			Font.medium.setColor(Colours.player2[1]);
		}
		else Font.medium.setColor(Colours.genCols5[2]);
			Font.drawFontCentered(batch, ""+cooldown, Font.medium,(int)location.x+64,  (int)location.y-24);
	}

	public void use() {
		cooldown=baseCooldown+1;
	}
	
	public boolean isUsable(){
		return cooldown<=0;
	}

	public void turn(){
		cooldown--;
		if(cooldown<0)cooldown=0;
	}


}
