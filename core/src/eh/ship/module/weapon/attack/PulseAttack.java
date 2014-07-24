package eh.ship.module.weapon.attack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Clip;
import eh.card.Card;
import eh.ship.module.weapon.attack.particle.PulseParticle;
import eh.util.Bonkject;
import eh.util.Bonkject.Finisher;
import eh.util.maths.Sink;
import eh.util.particleSystem.Particle;
import eh.util.particleSystem.ParticleSystem;

public class PulseAttack extends AttackGraphic{


	Sink position;
	float gravity=15; 
	float frequency=80;
	boolean goingRight;
	public PulseAttack(Sink origin) {
		super(origin.add(new Sink(5,0)));

		vector=Sink.randomUnitVector().multiply(20);
		position=this.origin.add(Sink.randomUnitVector().multiply(17));
	}

	@Override
	public void fire(Sink target) {
		goingRight=target.x>origin.x;
		origin=target;
		frequency=700;
		gravity=6;
		fired=true;
		Bonkject.time((float)Math.random()/4, new Finisher() {

			@Override
			public void finish() {
				Clip.pulse.overlay();
			}
		});
	}

	@Override
	public void impact() {
		System.out.println("imapcting");
		if(atk.activateDamage()){
			for(int i=0;i<50;i++){
				particles.add(new PulseParticle(position, vector));
			}
		}
		disable();
	}

	@Override
	public void update(float delta) {
		for(Particle p:particles){
			p.update(delta);
		}
		if(disabled)return;
		Sink difference=position.subtract(origin);
		vector=vector.subtract(difference.multiply(delta*gravity));	
		position=position.add(vector.multiply(delta));

		ticks+=delta*frequency;
		while(ticks>=1){
			ticks-=1;
			particles.add(new PulseParticle(position));
		}

		if(fired){
			if((position.x>=origin.x)==goingRight){
				particles.add(new PulseParticle(position));
				impact();
			}
		}

	}

	@Override
	public void render(SpriteBatch sb) {
		for(Particle p:particles){

			if(p.dead)continue;
			p.render(sb);
		}
	}

}
