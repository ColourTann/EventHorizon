package eh.ship.module.weapon.attack;

import eh.util.maths.Sink;
import eh.util.particleSystem.ParticleSystem;

public abstract class AttackGraphic extends ParticleSystem{
	public boolean fired;
	float frequency;
	public Sink origin;
	public Sink target;
	public float intensity=0;
	public Attack atk;
	public AttackGraphic(Sink origin){
		this.origin=origin;
	}
	public void disable(){
		disabled=true;
	}
	public abstract void fire(Sink target);
	public abstract void impact();
}
