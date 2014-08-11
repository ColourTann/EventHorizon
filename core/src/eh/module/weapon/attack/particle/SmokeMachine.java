package eh.module.weapon.attack.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.module.weapon.attack.particle.Smoke.SmokeType;
import eh.util.maths.Pair;
import eh.util.particleSystem.Particle;
import eh.util.particleSystem.ParticleSystem;

public class SmokeMachine extends ParticleSystem{
	Pair location;
	float freq;
	SmokeType type;
	public SmokeMachine(Pair startPosition, float maxLife, float frequency, SmokeType type){
		this.type=type;
		location=startPosition;
		this.maxLife=maxLife;
		life=maxLife;
		freq=frequency;
		ticks=1.01f;
	}
	@Override
	public void update(float delta) {
		ticks+=delta*freq;
		life-=delta;
		if(ticks>1&&!disabled){
			ticks-=1;
			particles.add(new Smoke(location, type));
		}
		for(Particle p:particles){
			p.update(delta);
		}
		if(life<=0)disabled=true;
	}

	@Override
	public void render(SpriteBatch sb) {
		for(Particle p:particles){
			p.render(sb);
		}
	}
	

}
