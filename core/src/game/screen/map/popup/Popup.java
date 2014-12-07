package game.screen.map.popup;

import util.update.Updater;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Popup extends Updater{
	
	
	public abstract void render(SpriteBatch batch);
	public abstract void dispose();
	
}
