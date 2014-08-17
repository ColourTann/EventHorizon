package eh.grid.hexContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.grid.hex.Hex;
import eh.util.Draw;
import eh.util.assets.Gallery;
import eh.util.maths.Pair;

public class Star extends HexContent{
	
	public Star(Hex hex) {
		super(hex);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(1,1,1,1);
		Pair loc=hex.getPixel();
		Texture t=Gallery.star.get();
		Draw.drawTextureScaledCentered(batch, t, loc.x, loc.y, Hex.size/30f, Hex.size/30f);
		
		
	}

}
