package game.grid.hexContent;

import util.Draw;
import util.maths.Pair;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Gallery;
import game.grid.hex.Hex;

public class Asteroid extends HexContent{
	float rotation;
	public Asteroid(Hex hex) {
		super(hex);
		rotation=(float) (Math.random()*10000);
	}

	@Override
	public void turn() {
		if(Math.random()>.9)chanceToMakeShip();
	}

	@Override
	public void render(SpriteBatch batch) {
		Pair loc = hex.getPixel();
		Draw.drawCenteredRotated(batch, Gallery.asteroid.get(), loc.x, loc.y, rotation);
	}

	@Override
	public String toString() {
		return "Asteroid";
	}

}
