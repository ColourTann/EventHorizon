package game.ship.niche;


import util.Draw;
import util.maths.Pair;
import util.particleSystem.ParticleSystem;
import util.update.Updater;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import game.assets.particles.SmokeMachine;
import game.assets.particles.Smoke.SmokeType;

public class NicheGraphic extends Updater{
	Niche niche;

	float ticks;
	float area;
	public NicheGraphic(Niche n) {
		deactivate();
		//mousectivate(new PolygonCollider(n.p));
		niche=n;
		area=n.p.getBoundingRectangle().width*n.p.getBoundingRectangle().height;
	}



	public void render(SpriteBatch batch){

		batch.setColor(1,1,1,1);
		Texture t=niche.component.modulePic.get();


		Pair bonus=new Pair();
		if(!niche.ship.player){
			bonus=new Pair(500, 0);
		}
		bonus=bonus.floor();

		Draw.drawRotatedScaledFlipped(batch, t, niche.location.x+bonus.x, niche.location.y+bonus.y, 1, 1, 0, !niche.component.ship.player, false);

		batch.setColor(1,1,1,1);

	}

	public String toString(){
		return "Graphic of "+niche;
	}

	@Override
	public void update(float delta) {
		ticks+=delta*area*.0001f*niche.component.currentThreshold;
		if(ticks>1&&!niche.ship.dead){
			ticks-=Math.random();
			Rectangle r =niche.p.getBoundingRectangle();
			float bonusX=(float) (r.getWidth()*Math.random())-r.getWidth()/2;
			float bonusY=(float) (r.getHeight()*Math.random())-r.getHeight()/2;
			Pair p=niche.component.getCenter().add(bonusX, bonusY);
			ParticleSystem.systems.add(new SmokeMachine(p, .2f, 20, SmokeType.Damage));
		}
	}



	public void dispose() {
		deactivate();
	}
}
