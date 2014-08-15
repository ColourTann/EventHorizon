package eh.module.weapon.attack.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.util.Colours;
import eh.util.Draw;
import eh.util.Draw.BlendType;
import eh.util.assets.Gallery;
import eh.util.maths.Pair;
import eh.util.particleSystem.Particle;

public class Spark extends Particle{
	float switchFreq=(float) (.05+Math.random()/10);
	public Spark(Pair origin, float bonusLife){
		this.position=origin;
		vector=Pair.randomAnyVector().multiply(.3f);
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
			vector=vector.add(Pair.randomAnyVector().multiply(.3f));
		}
		life-=delta;
		if(life<=0)dead=true;
	}

	@Override
	public void render(SpriteBatch batch) {
		if(dead)return;
		Draw.setBlend(batch, BlendType.Additive);
		double angle = Math.atan2(vector.y, vector.x);
		batch.setColor(Colours.withAlpha(Colours.light,.3f*(life/maxLife)));
		Draw.drawTextureRotatedScaledCentered(batch, Gallery.lightning.get(), position.x, position.y, 5, .1f, (float)angle);
		Draw.setBlend(batch, BlendType.Normal);
		//Draw.drawTexture(batch, Gallery.square2.get(), position.x, position.y);
	}

}
