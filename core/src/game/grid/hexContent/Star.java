package game.grid.hexContent;

import util.Draw;
import util.image.Pic;
import util.maths.Pair;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Gallery;
import game.grid.hex.Hex;

public class Star extends HexContent{
	
	public Star(Hex hex) {
		super(hex);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(1,1,1,1);
		Pair loc=hex.getPixel();
		Texture t=Gallery.star.get();
		Draw.drawCenteredScaled(batch, t, loc.x, loc.y, Hex.size/30f, Hex.size/30f);
	}

	@Override
	public String toString() {
		return "star";
	}

	@Override
	public void turn() {
	}

	@Override
	public void action() {
	}

	@Override
	public String getFlavour() {
		return null;
	}

	@Override
	public String getActionName() {
		return null;
	}

	@Override
	public Pic getPic() {
		return null;
	}

}
