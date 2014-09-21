package game.assets.particles;

import game.assets.Gallery;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.maths.Pair;
import util.particleSystem.Particle;

public class Orbiter extends Particle{
	

		public float spinnerFrequency=-20;
		public float spinnerAmplitude=18;
		public float spinnerSpeed=50;
		Pair parent;
		public Orbiter(Pair parent){
			this.parent=parent;
			ticks+=Math.random()*10;
			maxLife=.7f;
			life=maxLife;
		}
		@Override
		public void update(float delta) {
			ticks+=delta*spinnerFrequency;
			spinnerAmplitude+=delta*spinnerSpeed;
			life-=delta;
			if(life<=0)dead=true;
			ratio=life/maxLife;
		}

		@Override
		public void render(SpriteBatch batch) {
			batch.setColor(Colours.withAlpha(Colours.light, ratio));
		
			Draw.drawCenteredScaled(batch, Gallery.circle32.get(), 
					parent.x+(float)Math.cos(ticks)*spinnerAmplitude,  parent.y+(float)Math.sin(ticks)*spinnerAmplitude, .1f, .1f);
		}
		
	
}
