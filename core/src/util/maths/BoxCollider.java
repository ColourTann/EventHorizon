package util.maths;

import game.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class BoxCollider extends Collider{

	public float w;
	public float h;
	
	public BoxCollider(float x, float y, float w, float h){
		position=new Pair(x,y);
		this.h=h;
		this.w=w;
	}
	
	@Override
	public boolean collidePoint(Pair s) {
		if(s.x<position.x||s.x>position.x+w||s.y<position.y||s.y>position.y+h) return false;
		
		return true;
	}

	@Override
	public void debugDraw() {
		ShapeRenderer sr = new ShapeRenderer();
		sr.setProjectionMatrix(Main.mainCam.combined);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeType.Filled);

		sr.setColor(1, 0, 1, .1f);
		if(override!=null) sr.setColor(override);
		
		sr.rect(position.x, position.y, w, h);
		sr.end();
		sr.dispose();
		
	}

	@Override
	public boolean collideWith(Collider c) {
		if(c instanceof BoxCollider){
			BoxCollider bc =(BoxCollider) c;
			if(bc.position.x>position.x+w) return false;
			if(bc.position.x+bc.w<position.x) return false;
			if(bc.position.y>position.y+h) return false;
			if(bc.position.y+bc.h<position.y) return false;
			return true;
		}
		if(c instanceof CircleCollider){
			CircleCollider cc = (CircleCollider) c;
			return circleBoxCollide(cc, this);
		}
		return false;
	}
	
}
