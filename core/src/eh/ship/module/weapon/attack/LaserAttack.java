package eh.ship.module.weapon.attack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Clip;
import eh.screen.battle.Battle;
import eh.ship.module.weapon.attack.particle.LaserBody;
import eh.ship.module.weapon.attack.particle.LaserCharge;
import eh.ship.module.weapon.attack.particle.Smoke.SmokeType;
import eh.ship.module.weapon.attack.particle.SmokeMachine;
import eh.util.maths.Pair;
import eh.util.particleSystem.Particle;
import eh.util.particleSystem.ParticleSystem;

public class LaserAttack extends AttackGraphic{

	public LaserAttack(Pair origin) {
		super(origin);
		frequency=.2f;
	}

	@Override
	public void fire(Pair target) {
		this.target=target;
		particles.add(new LaserBody(origin, target));
		fired=true;
		impact();
		
		disable();
		ticks=0;
		Clip.laser.play();
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
		if(fired&&ticks>.2f){
			impact();
		}
	}

	@Override
	public void render(SpriteBatch batch) {

		for(Particle p:particles){
			p.render(batch);
		}
	}

}
