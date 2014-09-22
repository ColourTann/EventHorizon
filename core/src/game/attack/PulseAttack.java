package game.attack;

import util.update.Timer;
import util.update.Timer.*;
import util.maths.Pair;
import util.particleSystem.Particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Sounds;
import game.assets.particles.PulseParticle;

public class PulseAttack extends AttackGraphic{


	Pair position;
	float gravity=15; 
	float frequency=80;
	boolean goingRight;


	public PulseAttack(Pair origin) {
		super(origin.add(new Pair(5,0)));
		this.origin=origin;
		vector=Pair.randomUnitVector().multiply(20);
		position=this.origin.add(Pair.randomUnitVector().multiply(17));

	}

	@Override
	public void fire(final Pair targett) {
	

		t=new Timer(0,1, 1/(10f/(order+.01f)), Interp.LINEAR);
		t.addFinisher(new Finisher() {


			@Override
			public void finish() {
				Sounds.pulse.overlay();
				goingRight=targett.x>origin.x;
				target=targett;

				frequency=700;
				gravity=6;
				fired=true;
			}
		});
	}

	@Override
	public void impact() {

		
		if(atk.activateDamage()){
			Sounds.damageMinor.play();
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


		ticks+=delta*frequency;
		while(ticks>=1){
			ticks-=1;
			particles.add(new PulseParticle(position));
		}

		if(!fired){
			Pair difference=position.subtract(origin);
			vector=vector.subtract(difference.multiply(delta*gravity));	
		}

		if(fired){
			Pair targetVector=target.subtract(position).normalise().multiply(1100);
			vector=vector.add(targetVector.subtract(vector).multiply(.006f));
			if((position.x>=target.x)==goingRight){
				particles.add(new PulseParticle(position));
				
				impact();
			}
		}

		position=position.add(vector.multiply(delta));


	}

	@Override
	public void render(SpriteBatch sb) {
		for(Particle p:particles){

			if(p.dead)continue;
			p.render(sb);
		}
	}

	@Override
	public boolean finishedAttacking() {
		return particles.size()==0;
	}

}
