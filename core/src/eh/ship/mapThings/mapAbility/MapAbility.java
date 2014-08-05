package eh.ship.mapThings.mapAbility;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import eh.assets.Pic;
import eh.grid.hex.Hex;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.util.Bonkject;
import eh.util.Draw;
import eh.util.maths.PolygonCollider;
import eh.util.maths.Pair;


public abstract class MapAbility extends Bonkject{
	public static int gap = 90;
	public int cooldown;
	public Pic abilityPic;
	Pair location;
	protected boolean choosing;
	public enum MapAbilityType{
		//Generator abilities
		Teleport, Move, Diagonal,

		//Computer Abilities
		Cloak, Beam, Forcefield

	}
	public MapAbility(Pic p) {
		abilityPic=p;
		debonktivate();
	}




	@Override
	public void mouseDown() {
	}
	@Override
	public void mouseUp() {
	}
	@Override
	public void mouseClicked(boolean left) {
		if(choosing){
			fadeHexesOut();
			deactivate(); 
			
			return;
		}
		if(Map.getState()==MapState.PlayerTurn&&!choosing){
			activate(); 
fadeHexesIn();
			return;
		}

	}

	

	public void fadeHexesIn(){
		for(Hex h:Map.grid.drawableHexes)if(isValidChoice(Map.player.hex, h))h.mapAbilityChoiceFadein();
	}
	
	public void fadeHexesOut(){
		for(Hex h:Map.grid.drawableHexes)if(isValidChoice(Map.player.hex, h))h.mapAbilityChoiceFadeout();
	}

	public void activate() {
		Map.using=this;
		Map.setState(MapState.PickHex);
		choosing=true;
	}

	public void deactivate() {
		Map.returnToPlayerTurn();
		Map.using=null;
		choosing=false;
	}

	public abstract boolean isValidChoice(Hex origin, Hex target);

	public abstract void pickHex(Hex hex);

	@Override
	public void update(float delta) {
	}


	//drawing stuff//

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

	public void showAt(Pair location){
		this.location=location;
		Polygon translated=new Polygon(basePolygon.getVertices());
		translated.translate(location.x, location.y);
		mousectivate(new PolygonCollider(translated));
	}

	public void render(SpriteBatch batch) {
		Draw.drawTextureScaledCentered(batch, abilityPic.get(), location.x, location.y, 1, 1);
	}



}
