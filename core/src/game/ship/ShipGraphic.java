package game.ship;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.image.Pic;
import util.image.PicCut;
import util.image.PicCut.Shard;
import util.maths.Pair;
import util.update.Animation;
import util.update.Updater;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.assets.animations.Explosion1;
import game.module.Module;
import game.module.Module.ModuleType;
import game.screen.battle.Battle;
import game.screen.battle.interfaceJunk.Star;
import game.screen.battle.tutorial.Tutorial;
import game.ship.niche.Niche;

public class ShipGraphic extends Updater{
	Ship ship;
	public static Pair offset=new Pair(175, 90);
	public static int height=270;
	Pic composite;
	PicCut picCut;
	ArrayList<Animation> animations= new ArrayList<Animation>();

	public static Pair topRightEnemyShipPosition= new Pair(500+Main.width-offset.x, offset.y);
	public ShipGraphic(Ship s){
		ship=s;
		Pixmap shipMap=new Pixmap(450, 270, Format.RGBA8888);
		Pixmap.setBlending(Blending.SourceOver);

		for(Niche n:ship.niches){
			if(n.mod.type==ModuleType.WEAPON)
				shipMap.drawPixmap(n.mod.modulePic.getPixMap(),(int)(n.relativeTopLeft.x), (int)(n.relativeTopLeft.y)-n.mod.modulePic.get().getHeight()/2, 0, 0,  n.mod.modulePic.get().getWidth(), n.mod.modulePic.get().getHeight());
		}

		shipMap.drawPixmap(ship.shipPic.getPixMap(), 0, 0, 0, 0, 390, 270);




		for(Niche n:ship.niches){
			if(n.mod.type==ModuleType.WEAPON)continue;

			if(n.mod.type==ModuleType.GENERATOR||n.mod.type==ModuleType.COMPUTER){

				shipMap.drawPixmap(n.mod.modulePic.getPixMap(),
						(int)(n.p.getBoundingRectangle().x), 
						(int)(n.p.getBoundingRectangle().y), 
						0, 0,  n.mod.modulePic.get().getWidth(), n.mod.modulePic.get().getHeight());

			}

			if(n.mod.type==ModuleType.SHIELD){
				shipMap.drawPixmap(n.mod.modulePic.getPixMap(),
						(int)(n.relativeTopLeft.x), 
						(int)(n.relativeTopLeft.y-n.mod.modulePic.get().getHeight()/2), 
						0, 0,  n.mod.modulePic.get().getWidth(), n.mod.modulePic.get().getHeight());
			}
		}

		composite=new Pic(new Texture(shipMap));
		picCut=new PicCut(composite, new Color(0,0,0,0));


		//shipMap.dispose();
	}

	public void damage(Pair damageLoc){
		Shard s=null;
		if(Math.random()>.7){

			for(int i=0;i<50;i++){
				s=picCut.replaceSection(damageLoc.add(Pair.randomAnyVector().multiply(100)), Gallery.shipDamage[(int) (Math.random()*9)].get());
				if(s!=null) break;
			}
		}



		if(s!=null){
			s.vector=Pair.randomAnyVector().multiply(250);
			s.dr=(float) ((Math.random()-.5f)*15);
			if(ship.player){
				s.vector=s.vector.add(-150,0);
				s.position=s.position.add(offset);
			}
			else{
				s.vector=s.vector.add(150,0);
				s.position=topRightEnemyShipPosition.add(-s.position.x,s.position.y);
			}
			
			for(int i=0;i<2;i++)
				animations.add(new Explosion1(s.position));
		}
		else{
			animations.add(new Explosion1(damageLoc.add(Pair.randomAnyVector().multiply(40))));
		}

	}

	public void render(SpriteBatch batch){
		
		for(int i=0;i<animations.size();i++){
			Animation a= animations.get(i);
			if(a.isDone()){
				a.dispose();
				animations.remove(a);
				i--;
			}
		}


		batch.setColor(1, 1, 1, 1);
		if(ship.player)Draw.draw(batch, picCut.get(), offset.x, offset.y);
		if(!ship.player)	Draw.drawRotatedScaledFlipped(batch, picCut.get(), 
				500+Main.width-offset.x-composite.getWidth(), 
				offset.y,
				1,1,0, true, false);

		//Shitty tutorial shit shit shit//
		if(ship.player&&Tutorial.index==0&&Battle.isTutorial()){
			batch.setColor(Colours.withAlpha(Colours.light, (float)Math.sin(Battle.ticks*4)/5+.3f));
			for(int x=-1;x<=1;x+=2){
				for(int y=-1;y<=1;y+=2){

					Draw.draw(batch, composite.getGlow(), offset.x+x, offset.y+y);
				}
			}
		}
		batch.setColor(1,1,1,1);
		for(Shard s:picCut.shards){
			s.render(batch);
		}
		for(Animation a:animations){
			a.render(batch);
		}

		for(Module m:ship.modules){
			m.drawShield(batch);
		
		}


	}

	@Override
	public void update(float delta) {
		for(Shard s:picCut.shards){
			s.position=s.position.add((ship.player?Star.playerSpeed:Star.enemySpeed)*delta*2, 0);
		}

	}
}
