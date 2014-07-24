package eh.ship.module.weapon.attack;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Clip;
import eh.ship.module.weapon.attack.particle.RayParticle;
import eh.util.maths.Sink;
import eh.util.particleSystem.Particle;

public class RayAttack extends AttackGraphic{
	public Sink location;
	public boolean gravity;

	public RayAttack(Sink origin) {
		super(origin);
		location=new Sink(origin.x,origin.y);
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
				Sink newVector=Sink.randomUnitVector();
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
	public void fire(Sink target) {
		this.target=target;
		fired=true;
		vector=target.subtract(location);
		vector=vector.normalise();
		vector=vector.multiply(800);
		intensity=0;
		frequency=350;
		Clip.ray.play();
		

	}

	@Override
	public void impact() {
		
		disable();
		
		vector=vector.multiply(.4f);
		
		gravity=false;
		
		if(atk.activateDamage()){
		for(int i=0;i<35;i++){
			Sink newVector=vector.add(Sink.randomUnitVector().multiply((float)Math.random()*300)).multiply(.5f);
			particles.add(new RayParticle(this, newVector,1,false));
		}
		}
	}

}
