package game.grid.hexContent;

import util.Colours;
import util.Draw;
import util.maths.Pair;
import util.particleSystem.Particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Gallery;
import game.grid.hex.Hex;

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
		Draw.drawCenteredScaled(batch, Gallery.circle32.get(), loc.x+offset.x, loc.y+offset.y, scale*Hex.size/30f, scale*Hex.size/30f);
	}
	
	public void turn(){
		if(Math.random()>.75f)chanceToMakeShip();
	}
	
	@Override
	public String toString() {
		return "Planet";
	}

}
