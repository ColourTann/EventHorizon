package game.assets.particles;

import java.util.ArrayList;

import game.assets.Gallery;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.maths.Pair;
import util.particleSystem.Particle;

public class Orbiter extends Particle{


	public float spinnerFrequency=-20;
	public float spinnerAmplitude=9;
	public float spinnerSpeed=30;
	Pair parent;
	float trailTick=0;
	public ArrayList<OrbitTrail> trails = new ArrayList<OrbitTrail>();
	public boolean trail;
	public Orbiter(Pair parent, boolean trail){
		this.parent=parent;
		ticks+=Math.random()*10;
		maxLife=.7f;
		life=maxLife;
		this.trail=trail;
	}
	@Override
	public void update(float delta) {
		ticks+=delta*spinnerFrequency;
		spinnerAmplitude+=delta*spinnerSpeed;
		life-=delta;
		if(life<=0)dead=true;
		ratio=life/maxLife;

		if(trail){
			trailTick+=delta*800;
			if(trailTick>1){
				trailTick-=1;
				trails.add(new OrbitTrail(this));
			}
		}

		for(OrbitTrail trail:trails)trail.update(delta);



	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(Colours.withAlpha(Colours.light, ratio));

		Draw.drawCenteredScaled(batch, Gallery.circle32.get(), 
				parent.x+(float)Math.cos(ticks)*spinnerAmplitude,  parent.y+(float)Math.sin(ticks)*spinnerAmplitude, .1f, .1f);

		for(OrbitTrail trail:trails)trail.render(batch);
	}
	public Pair getPosition() {
		return new Pair(parent.x+(float)Math.cos(ticks)*spinnerAmplitude,  parent.y+(float)Math.sin(ticks)*spinnerAmplitude);
	}


}
