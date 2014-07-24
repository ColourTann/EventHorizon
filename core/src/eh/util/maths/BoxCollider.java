package eh.util.maths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;


public class BoxCollider extends Collider{

	public float w;
	public float h;
	
	public BoxCollider(float x, float y, float w, float h){
		this.x=x;
		this.y=y;
		this.h=h;
		this.w=w;
	}
	
	@Override
	public boolean collidePoint(Sink s) {
		if(s.x<x||s.x>x+w||s.y<y||s.y>y+h) return false;
		return true;
	}

	@Override
	public void debugDraw() {
		ShapeRenderer sr = new ShapeRenderer();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeType.Filled);

		sr.setColor(1, 1, 1, .3f);
		if(override!=null) sr.setColor(override);
		
		sr.rect(x, y, w, h);
		sr.end();
		sr.dispose();
		
	}

	@Override
	public boolean collideWith(Collider c) {
		if(c instanceof BoxCollider){
			BoxCollider bc =(BoxCollider) c;
			if(bc.x>x+w) return false;
			if(bc.x+bc.w<x) return false;
			if(bc.y>y+h) return false;
			if(bc.y+bc.h<y) return false;
			return true;
		}
		if(c instanceof CircleCollider){
			CircleCollider cc = (CircleCollider) c;
			return circleBoxCollide(cc, this);
		}
		return false;
	}
	
}
