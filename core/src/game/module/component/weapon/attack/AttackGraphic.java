package game.module.component.weapon.attack;


import util.maths.Pair;
import util.particleSystem.ParticleSystem;
import util.update.Timer;

public abstract class AttackGraphic extends ParticleSystem{
	public boolean fired;
	float frequency;
	public Pair origin;
	public Pair target;
	public float intensity=0;
	public Attack atk;
	public int order;
	Timer t;
	public AttackGraphic(Pair origin){
		this.origin=origin;

	}
	public void disable(){
		disabled=true;
	}
	public abstract void fire(Pair target);
	public abstract void impact();
}
