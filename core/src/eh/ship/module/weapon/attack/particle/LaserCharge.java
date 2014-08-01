package eh.ship.module.weapon.attack.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Gallery;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.Junk.BlendType;
import eh.util.maths.Pair;
import eh.util.particleSystem.Particle;

public class LaserCharge extends Particle{
	float ticks=0;
	
	public LaserCharge(Pair location){
		this.position=new Pair(location.x-10,location.y);
		colour=Colours.redWeaponCols4[(int) (Math.random()*3)+1];
		maxLife=1;
		life=maxLife;
	}

	@Override
	public void update(float delta) {
		life-=delta;
		if(life<=0){
			dead=true;
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		Junk.setBlend(sb, BlendType.Additive);
		sb.setColor(Colours.withAlpha(colour,life/maxLife));
		Junk.drawTextureScaledCentered(sb, Gallery.fuzzBall.get(), position.x, position.y, .7f*(1-life/maxLife),.7f*(1-life/maxLife));
		Junk.setBlend(sb, BlendType.Normal);
	
	}

}
