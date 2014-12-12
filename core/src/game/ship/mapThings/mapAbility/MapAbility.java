package game.ship.mapThings.mapAbility;


import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.TextWriter;
import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;
import util.maths.PolygonCollider;
import util.update.Mouser;
import util.update.Timer;
import util.update.Timer.Finisher;

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
	private int fuelCost;
	Pair location;
	public MapShip mapShip;
	public int range;
	public float effort;
	public TextWriter writer;
	public String text;
	Pair writerRenderLocation;
	public int hotkey;
	public MapAbility(Pic p, int cooldown, int fuelCost, int range, float effort) {
		abilityPic=p;
		this.range=range;
		this.effort=effort;
		this.baseCooldown=cooldown;
		this.fuelCost=fuelCost;
	}

	public abstract String getText();
	
	public int getFuel(){
		return fuelCost;
	}
	
	@Override
	public void mouseClicked(boolean left) {
		select();
	}

	public void select(){
		
		
		if(!isUsable()){
			Sounds.error.play();
			return;
		}
		
		if(Map.using==this){
			deselect(); 
			fadeOutHighlights();
			return;
		}
		if(Map.using!=null){
			 Map.using.fadeOutHighlights();
			Map.using.deselect();
		}
		if(Map.getState()==MapState.PlayerTurn){
			Map.using=this;
			selectAction(); 
			return;
		}
		if(Map.getState()==MapState.PlayerMoving||Map.getState()==MapState.EnemyMoving){
			interrupt();
		}
		
	}
	
	protected abstract void interrupt();
		

	public abstract void selectAction();

	public void deselect() {
		Map.returnToPlayerTurn();
	}

	public abstract HexChoice getBestTarget();

	public abstract boolean isValidChoice(Hex target);

	public abstract void pickHex(Hex hex);

	public void afterPlayerUse(){
		fadeOutHighlights();
		Map.setState(MapState.PlayerMoving);
		Map.using=null;
	}

	public void endPlayerTurn(){
		Timer.event(1/Map.phaseSpeed, new Finisher() {
			@Override
			public void finish() {
				Map.setState(MapState.EnemyMoving);
			}
		});
	}

	@Override
	public void mouseDown(){
		if(Map.getState()!=MapState.PlayerTurn)return;
		if(Map.using==null)mouseDownEffect();
	}
	@Override
	public void mouseUp(){
		if(Map.using==null)mouseUpEffect();
	}
	
	public abstract void mouseUpEffect();
	public abstract void mouseDownEffect();
	
	public void regularMouseDown(){
		if(Map.using==this)return;
		fadeOutHighlights();
		fadeHexesIn();
	}
	
	public void regularMouseUp(){
		if(Map.using==this)return;
		fadeOutHighlights();
	}
	
	@Override
	public void update(float delta) {
	}

	//drawing stuff//

	public static int gap = 90;
	public Pic abilityPic;
	public int index;
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

	public ArrayList<Hex> getValidHexes(){
		ArrayList<Hex> result = new ArrayList<Hex>();
		for(Hex h:mapShip.hex.getHexesWithin(range, false)){
			if(isValidChoice(h))result.add(h);
		}
		return result;
	}

	public void fadeHexesIn(){
		for(Hex h:Map.grid.drawableHexes){
			h.highlight=false;
		}
		for(Hex h:getValidHexes())h.mapAbilityChoiceFadein();
	}

	public void fadeOutHighlights(){
		for(Hex h:mapShip.hex.getHexesWithin(range, true))h.mapAbilityChoiceFadeout();
	}

	public void showAt(Pair location){
		this.location=location.floor();
		Polygon translated=new Polygon(basePolygon.getVertices());
		translated.translate(location.x, location.y);
		mousectivate(new PolygonCollider(translated));
	}

	public void render(SpriteBatch batch) {
		if(location==null){
			//System.out.println("null location");
			return;
		}
		
		//hotkey//
		Font.small.setColor(Colours.light);
		if(mapShip.getShip().player){
			Font.small.draw(batch, index+")", location.x-43, location.y-33);
		}

		Draw.drawCenteredScaled(batch, abilityPic.get(), location.x, location.y, 1, 1);
//		Draw.drawScaled(batch, Gallery.mapAbilityCooldown.get(), (int)location.x+44, (int)location.y-36, 2, 2);
		Draw.drawScaled(batch, Gallery.mapAbilityCooldown.get(), (int)location.x+46, (int)location.y-16, 2, 2);
		Font.medium.setColor(Colours.genCols5[3]);
		Font.drawFontCentered(batch, ""+baseCooldown, Font.medium, (int)location.x+66,  (int)location.y+28);

		if(cooldown==0){
			Font.medium.setColor(Colours.player2[1]);
		}
		else Font.medium.setColor(Colours.genCols5[2]);
		Font.drawFontCentered(batch, ""+cooldown, Font.medium,(int)location.x+66,  (int)location.y-4);
		
		Draw.draw(batch, Gallery.fuel.get(), location.x+38, location.y-42);
		Font.medium.setColor(Colours.weaponCols8[0]);
		Font.medium.draw(batch, ":"+getFuel(), location.x+68, location.y-38);
		
		
		if(moused){
			if(writer==null)setupWriter();
			if(writerRenderLocation==null){
				writerRenderLocation=mapShip.getShip().player?new Pair(150,10):new Pair(100,300);
			}
			writer.render(batch, writerRenderLocation.x, writerRenderLocation.y);
		}
		
	}

	private void setupWriter() {
		text= getText();
		Font.medium.setColor(Colours.light);
		if(range>0){
			text+="|n|Range "+range;
		}
		if(mapShip.getShip().player){
			
		}
		//text+="|n|Fuel "+fuelCost;
		writer=new TextWriter(Font.medium, text);
		writer.setWrapWidth(260);
		writer.setupTexture();
	}

	public void use() {
		cooldown=baseCooldown+1;
		mapShip.getShip().spendFuel(getFuel());
		if(mapShip.getShip().player)Map.using=null;		
	}

	public boolean isUsable(){
		return cooldown<=0;
	}

	public void turn(){
		cooldown--;
		if(cooldown<0)cooldown=0;
	}


}
