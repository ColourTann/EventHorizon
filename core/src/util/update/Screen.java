package util.update;

import game.Main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import util.maths.Pair;

public abstract class Screen {

	public abstract void init();
	public abstract void dispose();
	public abstract void update(float delta);
	public abstract void shapeRender(ShapeRenderer shape);
	public abstract void render(SpriteBatch batch);
	public abstract void postRender(SpriteBatch batch);
	public abstract void keyPress(int keycode);
	public abstract void keyUp(int keyCode);
	public abstract void mousePressed(Pair location, boolean left);
	public abstract void scroll(int amount);
	public static boolean isActiveType(Class<?> cl){
		if(Main.currentScreen==null)return false;
		return Main.currentScreen.getClass()==cl;
	}
}
