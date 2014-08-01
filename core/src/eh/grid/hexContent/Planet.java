package eh.grid.hexContent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Gallery;
import eh.grid.hex.Hex;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.maths.Pair;
import eh.util.particleSystem.Particle;

public class Planet extends HexContent{
	public Planet(Hex hex) {
		super(hex);
	}
	Color color=Colours.shieldCols6[(int) (Math.random()*6)];
	float scale=(float) (.6f+Math.random()/2);
	Pair offset= new Pair(Particle.random(5),Particle.random(5));
	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(color);
		Pair loc=hex.getPixel();
		Junk.drawTextureScaledCentered(batch, Gallery.circle32.get(), loc.x+offset.x, loc.y+offset.y, scale*Hex.size/30f, scale*Hex.size/30f);
	}

}
