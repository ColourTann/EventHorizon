package game.grid.hexContent;

import util.Draw;
import util.maths.Pair;
import game.assets.Gallery;
import game.grid.hex.Hex;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceStation extends HexContent{

	public SpaceStation(Hex hex) {
		super(hex);
	}

	@Override
	public void turn() {
		if(Math.random()>.4f)chanceToMakeShip();
	}

	@Override
	public void render(SpriteBatch batch) {
		Pair loc = hex.getPixel();
		Draw.drawCentered(batch, Gallery.spaceStation.get(), loc.x, loc.y);
	}

	@Override
	public String toString() {
		return "Space Station";
	}

}
