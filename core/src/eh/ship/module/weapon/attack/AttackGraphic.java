package eh.ship.module.weapon.attack;

import eh.util.maths.Pair;
import eh.util.particleSystem.ParticleSystem;

public abstract class AttackGraphic extends ParticleSystem{
	public boolean fired;
	float frequency;
	public Pair origin;
	public Pair target;
	public float intensity=0;
	public Attack atk;
	public AttackGraphic(Pair origin){
		this.origin=origin;
	}
	public void disable(){
		disabled=true;
	}
	public abstract void fire(Pair target);
	public abstract void impact();
}
