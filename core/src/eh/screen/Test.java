package eh.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eh.Main;
import eh.assets.Gallery;
import eh.util.Junk;
import eh.util.maths.Pair;

public class Test extends Screen{
	Pair camera=new Pair(Main.width/2,Main.height/2);
	@Override
	public void update(float delta) {
		float x=0;
		float y=0;
		float scrollSpeed=50;
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))x=-delta*scrollSpeed;
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))x=delta*scrollSpeed;
		if(Gdx.input.isKeyPressed(Input.Keys.UP))y=delta*scrollSpeed/2;
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN))y=-delta*scrollSpeed;
		camera=camera.add(new Pair(x,y));
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(Gallery.star.get(), 200+camera.x, 200+camera.y);
		Junk.drawTextureRotatedScaledCentered(batch, Gallery.shipAurora.get(), (int)camera.x, (int)camera.y,.2f,.2f, new Pair(Main.width/2,Main.height/2).getAngle(camera));
		
	}

	@Override
	public void keyPress(int keycode) {
	}

	@Override
	public void keyUp(int keyCode) {
	}

	@Override
	public void mousePressed(Pair location, boolean left) {
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

}
