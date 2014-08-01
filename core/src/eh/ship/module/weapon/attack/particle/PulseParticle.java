package eh.ship.module.weapon.attack.particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Gallery;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.maths.Pair;
import eh.util.particleSystem.Particle;
import eh.util.particleSystem.ParticleSystem;

public class PulseParticle extends Particle{
	Pair origin;
	Color c;
	Pair explodeVector;
	public PulseParticle(Pair start){
		position=start.add(new Pair(0,0));
		maxLife=.5f;
		life=maxLife-.0001f;
	}
	public PulseParticle(Pair explodeFrom, Pair baseVector){
		c=Colours.blueWeaponCols4[(int) (Math.random()*4)];
		position=explodeFrom.copy();
		explodeVector=baseVector.multiply(.10f).add(Pair.randomAnyVector().multiply(90).multiply(Pair.randomUnitVector()));
		maxLife=.5f;
		life=maxLife-.0001f;

	}
	@Override
	public void update(float delta) {
		life-=delta;
		if(life<=0)dead=true;
		if(explodeVector!=null){
			position=(position.add(explodeVector.multiply(delta)));
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(dead)return;
		float ratio=life/maxLife;
		
		if(c==null)batch.setColor(Colours.withAlpha(Colours.blueWeaponCols4[(int)(ratio*4)],ratio*.3f));
		else batch.setColor(Colours.withAlpha(c,ratio));
	
		Junk.drawTextureScaledCentered(batch, Gallery.circle32.get(), position.x, position.y, .17f,.17f);
	}

}
