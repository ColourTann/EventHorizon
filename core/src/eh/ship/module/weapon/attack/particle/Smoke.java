package eh.ship.module.weapon.attack.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Gallery;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.maths.Sink;
import eh.util.particleSystem.Particle;

public class Smoke extends Particle{
	float rotation=(float)Math.random()*1000;
	public enum SmokeType{Laser, Damage}
	public SmokeType type;
	public Smoke(Sink position, SmokeType type){
		this.type=type;
		this.position=position.add(Sink.randomAnyVector().multiply(0));
		
		
		maxLife=.5f;
		life=maxLife;
		vector=new Sink(Particle.random(50),Particle.random(50)+200);
		vector=vector.multiply(.3f);
		
		if(type==SmokeType.Damage){
			
			maxLife=(float) (Math.random()/2f)+.2f;
			life=maxLife;
			vector=vector.multiply(.5f);
		}
	}
	@Override
	public void update(float delta) {
		delta=delta/2;
		life-=delta;
		rotation+=delta;
		if(life<=0)dead=true;
		position.x+=delta*vector.x;
		position.y+=delta*vector.y;
	}

	@Override
	public void render(SpriteBatch sb) {
		if(dead)return;
		if(type==SmokeType.Damage)sb.setColor(Colours.withAlpha(Colours.enemy2[1], Math.min(1, life/maxLife)));
		if(type==SmokeType.Laser)sb.setColor(Colours.withAlpha(Colours.white, Math.min(1, life/maxLife)));
		float divisor = 10;
		if(type==SmokeType.Damage){
			divisor=18;
		}
		Junk.drawTextureRotatedScaledCentered(sb, Gallery.greySmoke.get(), position.x, position.y, (1-life/maxLife)/divisor+.01f, (1-life/maxLife)/divisor+.01f, rotation);
		sb.setColor(1,1,1,1);
	}

}
