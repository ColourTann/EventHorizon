package game.screen.map.panels;

import game.Main;
import game.assets.Gallery;
import game.screen.map.Map;
import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.Mouser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerStatsPanel extends SidePanel{
	static final Pair fuelLoc=new Pair(50,Main.height/2+80);
	@Override
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch) {
		Draw.drawCenteredScaled(batch, Gallery.fuel.get(), fuelLoc.x, fuelLoc.y, 2, 2);
		Font.big.setColor(Colours.weaponCols8[0]);
		Font.drawFontCentered(batch, ":"+Map.player.getShip().getFuel(), Font.big, fuelLoc.x+58, fuelLoc.y);
	}

}
