package game.screen.map.panels;

import game.Main;
import game.assets.Gallery;
import util.Draw;
import util.update.Mouser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerStatsPanel extends SidePanel{

	@Override
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch) {
		Draw.drawCenteredScaled(batch, Gallery.fuel.get(), 50, (int)(Main.height/2+80), 2, 2);
	}

}
