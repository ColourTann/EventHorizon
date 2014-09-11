package game.module.component.weapon.attack;

import java.util.ArrayList;

import util.update.Timer.*;
import util.update.Timer;
import util.assets.SoundClip;
import util.maths.Pair;
import util.particleSystem.Particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Sounds;
import game.module.component.weapon.attack.particle.Lightning;
import game.module.component.weapon.attack.particle.Spark;

public class LightningAttack extends AttackGraphic{
	static float frequency=15;
	float alpha=1;
	ArrayList<Lightning> lightning= new ArrayList<Lightning>();
	public LightningAttack(Pair origin) {
		super(origin);

	}

	/*public LightningAttack(Sink start, Sink finish){
		this.start=start;
		this.finish=finish;

	}*/
	@Override
	public void fire(Pair target) {
		this.target=target;
		t=new Timer(0,1,1/2.5f, Interp.LINEAR);
		t.addFinisher(new Finisher() {

			@Override
			public void finish() {

				fired=true;
				Sounds.lightning.play();

				impact();
			}

		});


	}
	@Override
	public void impact() {
		if(atk.activateDamage()){
			for(int i=0;i<200;i++)particles.add(new Spark(target, Particle.random(.3f)));
			for(Particle p:particles){
				p.update((float)Math.random()*.15f);
			}
		}
	}
	@Override
	public void update(float delta) {

		if(!fired&&!disabled){
			ticks+=delta*frequency*6;
			while(ticks>1){
				ticks-=1;
				particles.add(new Spark(origin,0));	
			}

		}
		for(Particle p:particles){
			p.update(delta);
		}

		if(fired){
			if(alpha<=0){
				particles.clear();	
				disable();
				return;
			}

			alpha-=delta*1.4f;
			ticks+=frequency*delta;

			if(ticks<1)return;
			ticks-=1;

			particles.removeAll(lightning);
			lightning.clear();

			Pair vector=target.subtract(origin);
			//float distance=vector.getDistance();
			int parts=(int) (vector.getDistance()*.04f);
			float mult=1f/(parts+1f);

			ArrayList<Pair> split = new ArrayList<Pair>();
			split.add(origin);
			for(int i=1;i<=parts;i++){
				split.add(origin.add(vector.multiply((mult*i)).add(Pair.randomUnitVector().multiply((float)Math.random()*30)))); 
				//split.add(origin.add(vector.multiply(mult)).add(Sink.randomAnyVector().multiply(20)));
			}
			split.add(target);
			for(int i=0;i<split.size()-1;i++){
				int cap=1;
				if(i==0)cap=0;
				if(i==split.size()-2)cap=2;
				Lightning l = new Lightning(split.get(i),split.get(i+1),cap,alpha);
				particles.add(l);
				lightning.add(l);
			}
		}

	}
	@Override
	public void render(SpriteBatch sb) {
		for(Particle p:particles){
			p.render(sb);
		}
	}

}
