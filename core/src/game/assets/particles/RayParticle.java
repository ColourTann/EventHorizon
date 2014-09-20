package game.assets.particles;

import util.Colours;
import util.Draw;
import util.maths.Pair;
import util.particleSystem.Particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Gallery;
import game.attack.RayAttack;

public class RayParticle extends Particle{
	static Texture t=Gallery.whiteSquare.get();
	Color c= Colours.redWeaponCols4[(int)(Math.random()*3)+1];
	private Pair origin;
	private boolean gravity;
	public RayParticle(RayAttack em, Pair vector, float lifeMult, boolean gravity) {
		this.gravity=gravity;
		position.x=em.location.x;
		position.y=em.location.y;
		this.vector=vector;
		origin=em.location;
		maxLife=.9f*lifeMult;
		life=maxLife;
	}

	@Override
	public void update(float delta) {
		life-=delta;
		if(life<=0){
			dead=true;
		}
		float gravitation=7;
		if(gravity){
		vector.x+=(origin.x-position.x)*delta*gravitation;
		vector.y+=(origin.y-position.y)*delta*gravitation;
		}
		position.x+=vector.x*delta;
		position.y+=vector.y*delta;
	}

	@Override
	public void render(SpriteBatch sb) {
		if(dead){
			return;
		}
		sb.setColor(Colours.withAlpha(Colours.redWeaponCols4[(int)(life/maxLife*4)], life/maxLife));
		//Junk.drawTexture(sb, t, x, y, life/maxLife*5f, life/maxLife*3f);
		Draw.drawScaled(sb, t, position.x, position.y, life/maxLife*5f, life/maxLife*3f);
	}

}
