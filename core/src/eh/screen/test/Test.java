package eh.screen.test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eh.Main;
import eh.assets.Gallery;
import eh.screen.Screen;
import eh.util.Draw;
import eh.util.maths.Pair;

public class Test extends Screen{

	@Override
	public void update(float delta) {
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
	}

	@Override
	public void render(SpriteBatch batch) {
			Draw.drawTextureScaled(batch, Gallery.replacedHP.get(), 200,200,5,5);
	}

	@Override
	public void postRender(SpriteBatch batch) {
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

}
