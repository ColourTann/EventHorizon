package game.module.weapon.attack.particle;

import game.assets.Gallery;
import util.Draw;
import util.maths.Pair;
import util.particleSystem.Particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LaserBody extends Particle{
	@SuppressWarnings("unused")
	private Pair origin,target,vector;
	private float alpha=0,distance,rotation;
	private float ticks=(float) (-Math.PI/2);
	
	public LaserBody(Pair origin, Pair target){
		
		this.origin=new Pair(origin.x,origin.y);
		this.target=target;
		vector=Pair.getVector(this.origin, target);
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
		Draw.drawRotatedCentered(sb, Gallery.laserBody[0].get(), origin.x+vector.x*-3, origin.y+vector.y*-3, rotation);
		for(int i=0;i<distance;i++){
			Draw.drawRotatedCentered(sb, Gallery.laserBody[1].get(), origin.x+vector.x*i, origin.y+vector.y*i, rotation);
		}
		Draw.drawRotatedCentered(sb, Gallery.laserBody[2].get(), origin.x+vector.x*(distance+2), origin.y+vector.y*(distance+2), rotation);
	}
	
}
