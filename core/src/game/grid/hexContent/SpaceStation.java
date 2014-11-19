package game.grid.hexContent;

import util.Draw;
import util.image.Pic;
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

	@Override
	public void action() {
	}

	@Override
	public String getFlavour() {
		return "A trade hub, you can buy or sell modules for fuel here. Docking here takes no time.";
	}

	@Override
	public String getActionName() {
		return "Visit Station";
	}

	@Override
	public Pic getPic() {
		return Gallery.spaceStation;
	}

}
