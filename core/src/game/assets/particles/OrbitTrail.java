package game.assets.particles;

import game.assets.Gallery;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.particleSystem.Particle;

public class OrbitTrail extends Particle{

	public OrbitTrail(Orbiter o){
		position=o.getPosition();
		alpha=o.ratio;
	}
	
	@Override
	public void update(float delta) {
		alpha-=delta*5;
		if(alpha<=0)dead=true;
	}

	@Override
	public void render(SpriteBatch batch) {
		if(dead) return;
		batch.setColor(Colours.withAlpha(Colours.light, alpha));
		Draw.drawCenteredScaled(batch,  Gallery.circle32.get(), position.x, position.y, .05f, .05f);
	}

}
