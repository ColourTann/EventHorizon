package game.attack;

import game.assets.Gallery;


import game.assets.Sounds;
import game.assets.particles.Orbiter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Draw;
import util.maths.Pair;
import util.particleSystem.Particle;
import util.update.Timer;
import util.update.Timer.Finisher;
import util.update.Timer.Interp;

public class SwiftAttack extends AttackGraphic{
	float alpha=0;
	public SwiftAttack(Pair origin) {
		super(origin);
		position=origin.copy();
	}

	@Override
	public void fire(Pair target) {
		this.target=target;
		vector=target.subtract(origin);
		vector=vector.normalise().multiply(800);


		t=new Timer(0,1, 1/(4f/(order+.01f)), Interp.LINEAR);
		t.addFinisher(new Finisher() {


			@Override
			public void finish() {
				fired=true;
				Sounds.swift.overlay();
				for(int i=0;i<10;i++){
					Orbiter o = new Orbiter(position, true);
					o.update(i*100);
					particles.add(o);
					o.update(i*10);
				}
			}
		});


	}

	@Override
	public void impact() {
		if(disabled) return;
		disabled=true;
		
		for(Particle p: particles){
			
			Orbiter o=(Orbiter) p;
			o.trails.clear();
			o.trail=false;
			o.spinnerFrequency=0;
			
			
			
		}
		
		if(atk.activateDamage()){
			for(int i=0;i<50;i++){
				Orbiter o=new Orbiter(position, true);
				o.maxLife=(float)Math.random();
				o.life=o.maxLife;
				o.spinnerSpeed=(float) (Math.random()*150);
				o.spinnerFrequency=0;
				particles.add(o);
			}
		}
		
	}

	@Override
	public void update(float delta) {
		for(Particle p:particles)p.update(delta);
		ticks+=delta*15;
		if(disabled) return;		

		if(fired){

			alpha+=delta*5;
			alpha=Math.min(1, alpha);
			if(ticks>1){
				ticks-=1;
				particles.add(new Orbiter(position, true));
			}


			position.x+=vector.x*delta;
			position.y+=vector.y*delta;


			if(vector.x>0&&target.subtract(position).x<0)impact();
			if(vector.x<0&&target.subtract(position).x>0)impact();
		}
		else{

			if(ticks>2){
				ticks-=2;
				Orbiter o = new Orbiter(position, false);
				o.spinnerFrequency/=4;
				//o.spinnerSpeed/=2;
				o.spinnerAmplitude=0;
				particles.add(o);
			}
		}

	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(1,1,1,alpha);
		if(fired&&!disabled){
			Draw.drawCenteredScaled(batch, Gallery.swiftParticle.get(), position.x, position.y, 3, 3);
		}
		batch.setColor(1,1,1,1);
		for(Particle p:particles)p.render(batch);
	}

	@Override
	public boolean finishedAttacking() {
		return particles.size()==0;
	}

}
