package game.attack;

import util.update.Timer;
import util.update.Timer.*;
import util.maths.Pair;
import util.particleSystem.Particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Sounds;
import game.assets.particles.RayParticle;

public class RayAttack extends AttackGraphic{
	public Pair location;
	public boolean gravity;

	public RayAttack(Pair origin) {
		super(origin);
		location=new Pair(origin.x,origin.y);
		frequency=35;
	}

	public void update(float delta) {
		if(!disabled){

			ticks+=delta*frequency;
			if(fired){
				location.x+=vector.x*delta;
				location.y+=vector.y*delta;
			}
			while(ticks>1&&!disabled){
				ticks-=1;
				Pair newVector=Pair.randomUnitVector();
				newVector=newVector.multiply(25+intensity*9);
				float mult=fired?.5f:1;
				particles.add(new RayParticle(this,newVector,mult,true));
			}
			if(fired){
				if(vector.x>0&&target.subtract(location).x<0)impact();
				if(vector.x<0&&target.subtract(location).x>0)impact();
			}
		}
		for(Particle p:particles){
			p.update(delta);
		}

	}

	@Override
	public void render(SpriteBatch sb) {
		for(Particle p:particles){

			if(p.dead)continue;
			p.render(sb);
		}
	}

	@Override
	public void fire(final Pair target) {
		this.target=target;
		t=new Timer(0,1, 1/(5f/(order+.01f)), Interp.LINEAR);
		t.addFinisher(new Finisher() {


			@Override
			public void finish() {
				fired=true;
				vector=target.subtract(location);
				vector=vector.normalise();
				vector=vector.multiply(800);
				intensity=0;
				frequency=350;
				Sounds.ray.overlay();
			}
		});




	}

	@Override
	public void impact() {

		disable();

		vector=vector.multiply(.4f);

		gravity=false;

		if(atk.activateDamage()){
			for(int i=0;i<35;i++){
				Pair newVector=vector.add(Pair.randomUnitVector().multiply((float)Math.random()*300)).multiply(.5f);
				particles.add(new RayParticle(this, newVector,1,false));
			}
		}
	}

	@Override
	public boolean finishedAttacking() {
		return particles.size()==0;
	}

}
