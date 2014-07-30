package eh.ship.mapThings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import eh.assets.Gallery;
import eh.assets.Pic;
import eh.grid.hex.Hex;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.util.Bonkject;
import eh.util.Junk;
import eh.util.maths.Collider;
import eh.util.maths.PolygonCollider;
import eh.util.maths.Sink;

public class MapAbility extends Bonkject{
	public int cooldown;
	public Pic abilityPic;
	Sink location;
	public enum MapAbilityType{
		//Generator abilities
		Teleport, Move, Diagonal,

		//Computer Abilities
		Cloak, Beam, Forcefield

	}
	public MapAbility(Sink location) {
		super(null);
		this.location=location;
		Polygon translated=new Polygon(basePolygon.getVertices());
		collider=new PolygonCollider(translated);
		deactivate();
		mousectivate();
	}

	public void setLocation(Sink location){
		this.location=location;
		Polygon translated=new Polygon(basePolygon.getVertices());
		translated.translate(location.x, location.y);
		collider=new PolygonCollider(translated);
	}

	private static Polygon basePolygon;
	public static float height;
	public static float width;
	public static void init(){
		float size=48;
		float points[] = new float[12];
		for(int i=0;i<12;i+=2){
			points[i]=(float) Math.cos(i*Math.PI/6)*size;
			points[i+1]=(float) Math.sin(i*Math.PI/6)*size;
		}
		basePolygon=new Polygon(points);
		height=basePolygon.getBoundingRectangle().height;
		width=basePolygon.getBoundingRectangle().width;
	}

	@Override
	public void mouseDown() {
	}
	@Override
	public void mouseUp() {
	}
	@Override
	public void mouseClicked(boolean left) {
		Map.setState(MapState.PickHex);
		Map.using=this;
	}

	public boolean isValidChoice(Hex origin, Hex target){
		int dist=origin.getDistance(target);
		return dist>1&&dist<=4;
	}



	public void pickHex(Hex hex){
		if(!isValidChoice(Map.player.hex, hex))return;
		Map.setState(MapState.PlayerMoving);
		Map.player.moveTo(hex);

		Map.using=null;	

	}

	@Override
	public void update(float delta) {
	}
	@Override
	public void render(SpriteBatch batch) {
		Junk.drawTextureScaledCentered(batch, Gallery.mapAbilityTeleport.get(), location.x, location.y, 1, 1);
	}

}
