package eh.ship.module.weapon.attack.particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.ship.module.weapon.attack.RayAttack;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.maths.Pair;
import eh.util.particleSystem.Particle;

public class RayParticle extends Particle{
	static Texture t=new Texture("Particle/pixel.png");
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
		Junk.drawTextureScaled(sb, t, position.x, position.y, life/maxLife*5f, life/maxLife*3f);
	}

}
