package eh.screen.test;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eh.Main;
import eh.screen.Screen;
import eh.util.Draw;
import eh.util.PerleyBabes;
import eh.util.Timer;
import eh.util.Timer.Interp;
import eh.util.assets.Gallery;
import eh.util.maths.Pair;

public class Test extends Screen{
	Timer t=new Timer(0,9999,.001f, Interp.LINEAR);

	@Override
	public void update(float delta) {
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {

	
	}

	@Override
	public void render(SpriteBatch batch) {
		Draw.drawTexture(batch, Gallery.apple.getCut(), 350, 100);
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void keyPress(int keycode) {
		switch(keycode){
		case Input.Keys.SPACE:
			Gallery.apple.reset();
			break;
		}
	}

	@Override
	public void keyUp(int keyCode) {
	}

	@Override
	public void mousePressed(Pair location, boolean left) {
	}

	@Override
	public void scroll(int amount) {
	}

}
