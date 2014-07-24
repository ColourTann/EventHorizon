package eh.ship.module.weapon.attack.particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Gallery;
import eh.util.Junk;
import eh.util.maths.Sink;
import eh.util.particleSystem.Particle;

public class LaserBody extends Particle{
	@SuppressWarnings("unused")
	private Sink origin,target,vector;
	private float alpha=0,distance,rotation;
	private float ticks=(float) (-Math.PI/2);
	
	public LaserBody(Sink origin, Sink target){
		
		this.origin=new Sink(origin.x-10,origin.y);
		this.target=target;
		vector=Sink.getVector(this.origin, target);
		rotation=(float) Math.atan2(vector.y, vector.x);
		distance=vector.getDistance();
		vector=vector.normalise();
	}
	@Override
	public void update(float delta) {
		ticks+=delta*6;
		alpha=(float) Math.sin(ticks);
		alpha+=1;
		alpha/=2;
		if(ticks>Math.PI*3/2){
			dead=true;
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(new Color(1,1,1,alpha));
		Junk.drawTextureRotatedCentered(sb, Gallery.laserBody[0].get(), origin.x+vector.x*-3, origin.y+vector.y*-3, rotation);
		for(int i=0;i<distance;i++){
			Junk.drawTextureRotatedCentered(sb, Gallery.laserBody[1].get(), origin.x+vector.x*i, origin.y+vector.y*i, rotation);
		}
		Junk.drawTextureRotatedCentered(sb, Gallery.laserBody[2].get(), origin.x+vector.x*(distance+2), origin.y+vector.y*(distance+2), rotation);
	}
	
}
