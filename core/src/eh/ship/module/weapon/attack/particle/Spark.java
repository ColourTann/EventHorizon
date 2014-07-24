package eh.ship.module.weapon.attack.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Gallery;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.Junk.BlendType;
import eh.util.maths.Sink;
import eh.util.particleSystem.Particle;

public class Spark extends Particle{
	float switchFreq=(float) (.05+Math.random()/10);
	public Spark(Sink origin, float bonusLife){
		this.position=origin;
		vector=Sink.randomAnyVector().multiply(.3f);
		maxLife=1+bonusLife;
		life=maxLife;
	}
	@Override
	public void update(float delta) {
		float spd=200;
		
		
		position=position.add(vector.multiply(delta*spd));
		ticks+=delta;
		if(ticks>switchFreq){
			ticks=0;
			vector=vector.multiply(.3f);
			vector=vector.add(Sink.randomAnyVector().multiply(.3f));
		}
		life-=delta;
		if(life<=0)dead=true;
	}

	@Override
	public void render(SpriteBatch batch) {
		if(dead)return;
		Junk.setBlend(batch, BlendType.Additive);
		double angle = Math.atan2(vector.y, vector.x);
		batch.setColor(Colours.withAlpha(Colours.light,.3f*(life/maxLife)));
		Junk.drawTextureRotatedScaledCentered(batch, Gallery.lightning.get(), position.x, position.y, 5, .1f, (float)angle);
		Junk.setBlend(batch, BlendType.Normal);
		//batch.draw(Gallery.square2.get(), position.x, position.y);
	}

}
