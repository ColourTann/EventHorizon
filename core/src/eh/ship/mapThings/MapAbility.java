package eh.ship.mapThings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import eh.assets.Gallery;
import eh.assets.Pic;
import eh.util.Bonkject;
import eh.util.Junk;
import eh.util.maths.Collider;
import eh.util.maths.PolygonCollider;
import eh.util.maths.Sink;

public class MapAbility extends Bonkject{
	public int cooldown;
	public Pic abilityPic;
	Sink location;
	
	public MapAbility(Sink location) {
		super(null);
		this.location=location;
		Polygon translated=new Polygon(basePolygon.getVertices());
		translated.translate(location.x, location.y);
		collider=new PolygonCollider(translated);
		deactivate();
		mousectivate();
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
		System.out.println("flub");
	}
	@Override
	public void mouseUp() {
	}
	@Override
	public void mouseClicked(boolean left) {
	}
	@Override
	public void update(float delta) {
	}
	@Override
	public void render(SpriteBatch batch) {
		Junk.drawTextureScaledCentered(batch, Gallery.mapAbilityTeleport.get(), location.x, location.y, 1, 1);
		//debugRender(batch);
	}
}
