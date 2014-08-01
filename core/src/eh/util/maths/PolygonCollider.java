package eh.util.maths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;

public class PolygonCollider extends Collider{
	public Polygon p;
	
	public PolygonCollider(Polygon p){
		this.p=p;
	}
	
	@Override
	public boolean collidePoint(Pair s) {
		return p.contains(s.x, s.y);
	}

	@Override
	public boolean collideWith(Collider c) {
		System.out.println("Uncoded polygon collider collision");
		return false;
	}

	@Override
	public void debugDraw() {
		ShapeRenderer sr = new ShapeRenderer();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeType.Line);
		
		
		
		sr.setColor(1, 1, 1, .3f);
		if(this instanceof PolygonCollider){
			sr.setColor(1, 0, 0, 1);
		}
		
		if(override!=null) sr.setColor(override);
		
		sr.polygon(p.getTransformedVertices());
		sr.end();
		sr.dispose();
	}
	
	public static void flipBasedOnShip(Polygon p){
		p.setOrigin(195, 135);
		p.setScale(1, -1);
	}

}
