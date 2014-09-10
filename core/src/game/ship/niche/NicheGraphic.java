package game.ship.niche;


import util.Colours;
import util.Draw;
import util.maths.Pair;
import util.maths.PolygonCollider;
import util.particleSystem.ParticleSystem;
import util.update.Updater;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import game.module.weapon.attack.particle.SmokeMachine;
import game.module.weapon.attack.particle.Smoke.SmokeType;
import game.screen.battle.Battle;
import game.ship.ShipGraphic;

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
		Texture t=niche.mod.modulePic.get();
	
		
		Pair bonus=new Pair();
		if(!niche.ship.player){
			bonus=new Pair(500, 0);
		}
		bonus=bonus.floor();
		
		Draw.drawRotatedScaledFlipped(batch, t, niche.location.x+bonus.x, niche.location.y+bonus.y, 1, 1, 0, !niche.mod.ship.player, false);

		batch.setColor(1,1,1,1);
		
		}

	public String toString(){
		return "Graphic of "+niche;
	}



	@Override
	public void update(float delta) {

	

		ticks+=delta*area*.00025f*niche.mod.currentThreshold;
		int shield=Math.min(3,niche.mod.getShield());

		float target=(float) (.15f*shield+(Math.sin(Battle.ticks*4)/2+.5f)/(5-shield));
		if(shield==0)target=0;
	

		if(ticks>1){
			ticks-=Math.random();
			//ticks=1;
			Pair s=new Pair();
			Rectangle r =niche.p.getBoundingRectangle();
			

			//s.x= niche.location.x+(float) (Math.random()*r.width*.8f)+r.width*.1f+ShipGraphic.offset.x;
			//s.y= niche.location.y+(float) (Math.random()*r.height*.8f)+r.height*.1f+ShipGraphic.offset.y;
			float bonusX=(float) (r.getWidth()*Math.random())-r.getWidth()/2;
			float bonusY=(float) (r.getHeight()*Math.random())-r.getHeight()/2;
			
			
			Pair p=niche.mod.getCenter().add(bonusX, bonusY);
			ParticleSystem.systems.add(new SmokeMachine(p, .2f, 20, SmokeType.Damage));
		}


	}
}
