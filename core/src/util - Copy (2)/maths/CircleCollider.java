package util.maths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eh.Main;


public class CircleCollider extends Collider{
	float r;
	
	public CircleCollider(float x, float y, float r){
		position=new Pair(x,y);
		this.r=r;
	}
	
	@Override
	public boolean collidePoint(Pair s) {
		float xDist=s.x-position.x;
		float yDist=s.y-position.y;
		float dist=xDist*xDist+yDist*yDist;
		return dist<=r*r;
	}

	@Override
	public void debugDraw() {
		ShapeRenderer sr = new ShapeRenderer();
		sr.setProjectionMatrix(Main.mainCam.combined);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeType.Filled);
		
		
		sr.setColor(1, 1, 1, .3f);
		if(override!=null) sr.setColor(override);
		
		sr.circle(position.x, position.y, r);
		sr.end();
		sr.dispose();
	}

	@Override
	public boolean collideWith(Collider c) {
		if(c instanceof CircleCollider){
			CircleCollider circ = (CircleCollider) c;
			float maxDist=(r+circ.r)*(r+circ.r);
			float xDist=position.x-circ.position.x;
			float yDist=position.y-circ.position.y;
			float actualDist=xDist*xDist+yDist*yDist;
			
			if(actualDist<=maxDist){
				return true;
			}
			return false;
		}
		if(c instanceof BoxCollider){
			BoxCollider bc = (BoxCollider) c;
			return circleBoxCollide(this, bc);
		}
		return false;
	}
	

}
