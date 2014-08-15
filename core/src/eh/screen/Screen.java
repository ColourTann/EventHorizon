package eh.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eh.util.maths.Pair;

public abstract class Screen {
	public boolean fadingIn=true;;
	public abstract void update(float delta);
	public abstract void shapeRender(ShapeRenderer shape);
	public abstract void render(SpriteBatch batch);
	public abstract void postRender(SpriteBatch batch);
	public abstract void keyPress(int keycode);
	public abstract void keyUp(int keyCode);
	public abstract void mousePressed(Pair location, boolean left);
	public abstract void scroll(int amount);
}
