package game.assets.particles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.maths.Pair;
import util.particleSystem.Particle;
import util.particleSystem.ParticleSystem;

public class SwiftSystem extends ParticleSystem{
	
	public SwiftSystem(){
		position=new Pair(200,200);
	}
	
	@Override
	public void update(float delta) {
		ticks+=delta*3;
		while(ticks>1){
			ticks-=1;
			particles.add(new SwiftParticle(position));
			particles.add(new SwiftParticle(position));
		}
		updateParticles(delta);
	}

	@Override
	public void render(SpriteBatch batch) {
		for(Particle p:particles)p.render(batch);
	}

}
