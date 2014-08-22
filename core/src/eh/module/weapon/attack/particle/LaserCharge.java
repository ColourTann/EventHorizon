package eh.module.weapon.attack.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.util.Colours;
import eh.util.Draw;
import eh.util.Draw.BlendType;
import eh.util.assets.Gallery;
import eh.util.maths.Pair;
import eh.util.particleSystem.Particle;

public class LaserCharge extends Particle{
	float ticks=0;
	
	public LaserCharge(Pair location){
		this.position=new Pair(location.x,location.y);
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
		Draw.setBlend(sb, BlendType.Additive);
		sb.setColor(Colours.withAlpha(colour,life/maxLife));
		Draw.drawTextureScaledCentered(sb, Gallery.fuzzBall.get(), position.x, position.y, .7f*(1-life/maxLife),.7f*(1-life/maxLife));
		Draw.setBlend(sb, BlendType.Normal);
	
	}

}
