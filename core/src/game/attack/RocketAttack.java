package game.attack;

import game.assets.Gallery;
import game.assets.Sounds;
import game.assets.particles.Fire;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Draw;
import util.Draw.BlendType;
import util.maths.Pair;
import util.particleSystem.Particle;
import util.update.Timer;
import util.update.Timer.Finisher;
import util.update.Timer.Interp;

public class RocketAttack extends AttackGraphic{
	float scale;
	float rotation;
	float speed=600;
	float rotationSpeed=50;
	public RocketAttack(Pair origin, float scale) {
		super(origin);
		this.scale=scale;
		vector=new Pair(1,1);
		position=origin.copy();
	}

	@Override
	public void fire(Pair target) {
		
		this.target=target;
		
		t=new Timer(0,1, 1/(10f/(order+.01f)), Interp.LINEAR);
		t.addFinisher(new Finisher() {


			@Override
			public void finish() {
				fired=true;
				float rotate=(float) (Math.random()*1+.5);
				if(Math.random()>.5)rotate=-rotate;
				vector=vector.rotate(rotate);
				vector=vector.normalise();
				Sounds.rocket.play();
			}
		});

	}

	@Override
	public void impact() {
		if(atk.activateDamage()){
			for(int i=0;i<10;i++){
				Fire f=new Fire(position.add(Pair.randomUnitVector().multiply((float)Math.random()*5*scale)), scale*4);
				f.setLife((float)Math.random()*.8f);
				f.vector=f.vector.multiply(4);
				particles.add(f);
				
			}
		}
	}

	@Override
	public void update(float delta) {
		if(!fired)return;
		updateParticles(delta);
		if(disabled)return;
		ticks+=delta*100;
		if(ticks>1){
			ticks-=1;

			particles.add(new Fire(position.add(vector.multiply(-5*scale)), scale));
		}
		position=position.add(vector.multiply(speed*delta));


		rotation=vector.getAngle();
		float targetRotation=target.getAngle(position);
		if(Math.abs(targetRotation-rotation)>Math.PI){
			if(targetRotation>rotation)rotation+=Math.PI*2;
			else targetRotation+=Math.PI*2;
		}

		
		float angler=(targetRotation>rotation?-1:1)*rotationSpeed;
		float closeness=Math.abs(rotation-targetRotation);

		if(Math.abs(targetRotation-rotation)<2){
			rotation=targetRotation;
		}
		vector=vector.rotate(angler*delta/(Math.pow(closeness, 3)));
		//vector=vector.rotate(delta);
		
		if(position.subtract(target).getDistance()<15){
			disable();
			impact();	
			}
	}

	@Override
	public void render(SpriteBatch batch) {
		if(!fired)return;
		Draw.setBlend(batch, BlendType.Additive);
		for(Particle p:particles){
			p.render(batch);
		}
		Draw.setBlend(batch, BlendType.Normal);
		if(disabled)return;
		batch.setColor(1,1,1,1f);
		Draw.drawCenteredRotatedScaled(batch, Gallery.rocket.get(), position.x, position.y, scale, scale, rotation);
	}

	@Override
	public boolean finishedAttacking() {
		return disabled&&particles.size()==0;
	}

}
