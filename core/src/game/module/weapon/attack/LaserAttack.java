package game.module.weapon.attack;



import util.assets.SoundClip;
import util.maths.Pair;
import util.particleSystem.Particle;
import util.particleSystem.ParticleSystem;
import util.update.Timer;
import util.update.Timer.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Sounds;
import game.module.weapon.attack.particle.LaserBody;
import game.module.weapon.attack.particle.LaserCharge;
import game.module.weapon.attack.particle.SmokeMachine;
import game.module.weapon.attack.particle.Smoke.SmokeType;

public class LaserAttack extends AttackGraphic{

	public LaserAttack(Pair origin) {
		super(origin);
		frequency=.2f;
	}

	@Override
	public void fire(final Pair target) {
		this.target=target;
		t=new Timer(0,1,1/1.5f, Interp.LINEAR);
		t.addFinisher(new Finisher() {
		
			
			@Override
			public void finish() {
				particles.add(new LaserBody(origin, target));
				fired=true;
				impact();
				
				disable();
				ticks=0;
				Sounds.laser.play();
			}
		});
		
	}

	@Override
	public void impact() {
		if(atk.activateDamage()){
			ParticleSystem.systems.add(new SmokeMachine(new Pair(target.x,target.y), .6f, 50, SmokeType.Laser));	
		}
	}

	@Override
	public void update(float delta) {
		ticks+=delta;

		if(ticks>frequency&&!fired&&!disabled){
			ticks-=frequency;
			particles.add(new LaserCharge(origin));	
		}
		
		for(Particle p:particles){
			p.update(delta);
		}
		/*if(fired&&ticks>.2f){
			impact();
		}*/
	}

	@Override
	public void render(SpriteBatch batch) {

		for(Particle p:particles){
			p.render(batch);
		}
	}

}
