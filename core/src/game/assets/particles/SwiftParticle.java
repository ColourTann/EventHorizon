package game.assets.particles;

import java.util.ArrayList;

import game.assets.Gallery;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.maths.Pair;
import util.particleSystem.Particle;

public class SwiftParticle extends Particle{
	float frequency=(float) (25);
	float amplitude=(float) (0);
	float orbitSpawner=0;
	float spinTicks;
	ArrayList<Orbiter> orbiters= new ArrayList<SwiftParticle.Orbiter>();
	private boolean exploded;
	public SwiftParticle(Pair position, Pair target) {
		
		vector=target.subtract(position).multiply(.3f);
		ticks=(float) (Math.random()*100);
		this.position=position.add(0, -(float)Math.sin(ticks)*amplitude);
		for(int i=0;i<10;i++){
			Orbiter o = new Orbiter();
			o.update(i*100);
			orbiters.add(o);
		}
		
	}

	@Override
	public void update(float delta) {
		if(!exploded){
		position=position.add(vector.multiply(delta));
		ticks+=delta*frequency;
		orbitSpawner+=delta*30;
		
		if(orbitSpawner>1){
			orbitSpawner-=1;
			orbiters.add(new Orbiter());
		}
		
		}
		for(Orbiter o:orbiters)o.update(delta);
		
	}

	

	@Override
	public void render(SpriteBatch batch) {
		if(!exploded)Draw.drawCenteredScaled(batch, Gallery.swiftParticle.get(), position.x, position.y+(float)Math.sin(ticks)*amplitude, 5, 5);
		for(Orbiter o:orbiters)o.render(batch);
	}
	
	class Orbiter extends Particle{
		float spinnerFrequency=-20;
		float spinnerAmplitude=30;
		float spinnerSpeed=50;
		public Orbiter(){
			ticks+=Math.random()*10;
			maxLife=.7f;
			life=maxLife;
		}
		@Override
		public void update(float delta) {
			ticks+=delta*spinnerFrequency;
			spinnerAmplitude+=delta*spinnerSpeed;
			life-=delta;
			ratio=life/maxLife;
		}

		@Override
		public void render(SpriteBatch batch) {
			batch.setColor(Colours.withAlpha(Colours.light, ratio));
			Pair parent=SwiftParticle.this.position.add(0, (float)Math.sin(SwiftParticle.this.ticks)*SwiftParticle.this.amplitude);
			Draw.drawCenteredScaled(batch, Gallery.whiteSquare.get(), 
					parent.x+(float)Math.cos(ticks)*spinnerAmplitude,  parent.y+(float)Math.sin(ticks)*spinnerAmplitude, 6, 6);
		}
		
	}

}
