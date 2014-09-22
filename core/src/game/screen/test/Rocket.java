package game.screen.test;

import java.util.ArrayList;

import game.assets.Gallery;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Draw;
import util.Draw.BlendType;
import util.maths.Pair;
import util.particleSystem.Particle;
import util.update.Updater;

public class Rocket extends Updater{
	Pair vector=new Pair(0,1).normalise();
	Pair position;
	Pair target;
	float rotation=0;
	float ticks=0;
	float speed=250;
	float rotationSpeed=50.5f;
	ArrayList<Fire> fires = new ArrayList<Fire>();
	float scale;
	public Rocket(Pair position, Pair target, float scale){
		this.position=position;
		this.target=target;
		this.scale=scale;
		vector=target.subtract(position);
		float rotate=(float) (Math.random()*1+.5);
	
		if(Math.random()>.5)rotate=-rotate;
		vector=vector.rotate(rotate);
		vector=vector.normalise();
	
	}
	public void update(float delta) {
		for(Fire f:fires){
			f.update(delta);
		}
		for(int i=fires.size()-1;i>=0;i--){
			Fire f= fires.get(i);
			if(f.dead)fires.remove(f);
		}
		if(dead)return;
		ticks+=delta*100;
		if(ticks>1){
			ticks-=1;

			fires.add(new Fire(position.add(vector.multiply(-5*scale)), scale));
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
		
		
		vector=vector.rotate(angler*delta/(closeness*closeness*closeness*closeness*closeness));
		//vector=vector.rotate(delta);
		System.out.println(closeness);
		if(position.subtract(target).getDistance()<8||(closeness<2&&position.subtract(target).getDistance()<550)){
			explode();
		}
	}

	private void explode() {
		dead=true;
		
		for(int i=0;i<10;i++){
			Fire f=new Fire(position.add(Pair.randomUnitVector().multiply((float)Math.random()*5*scale)), scale*4);
			f.setLife((float)Math.random()*.8f);
			f.vector=f.vector.multiply(4);
			fires.add(f);
			
		}
	}
	public void move (float forwards, float rotation){


	}
	public void render(SpriteBatch batch){
		
		Draw.setBlend(batch, BlendType.Additive);
		for(Fire f:fires){
			f.render(batch);
		}
		Draw.setBlend(batch, BlendType.Normal);
		
		if(dead)return;
		
		batch.setColor(1,1,1,1f);
		Draw.drawCenteredRotatedScaled(batch, Gallery.rocket.get(), position.x, position.y, scale, scale, rotation);
	}
}
