package game.screen.map.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.update.Updater;

public abstract class SidePanel extends Updater{

	@Override
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);
	
}
