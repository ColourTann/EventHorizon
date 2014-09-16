package game.ship;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.image.Pic;
import util.image.PicCut;
import util.image.PicCut.Shard;
import util.maths.Pair;
import util.particleSystem.Particle;
import util.update.Animation;
import util.update.Timer;
import util.update.Timer.*;
import util.update.Updater;
import util.update.Timer.Interp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.assets.animations.Explosion1;
import game.module.Module.ModuleType;
import game.module.component.Component;
import game.screen.battle.Battle;
import game.screen.battle.interfaceJunk.Star;
import game.screen.battle.tutorial.Tutorial;
import game.ship.niche.Niche;

public class ShipGraphic extends Updater{
	Ship ship;
	public static Pair offset=new Pair(175, 90);
	public static int height=270;
	public static int width=390;
	public Pic composite;
	PicCut picCut;
	ArrayList<Animation> animations= new ArrayList<Animation>();
	ArrayList<Shard> shards= new ArrayList<Shard>();
	Pixmap shipMap;
	public static Pair topRightEnemyShipPosition= new Pair(500+Main.width-offset.x, offset.y);
	
	public ShipGraphic(Ship s){
		ship=s;
		drawMap();
	}

	public void drawMap(){
		if(composite!=null)composite.dispose();
		if(picCut!=null)picCut.dispose();
		if(shipMap!=null)shipMap.dispose();
		shipMap=new Pixmap(450, 270, Format.RGBA8888);
		Pixmap.setBlending(Blending.SourceOver);

		for(Niche n:ship.niches){
			if(n.component.type==ModuleType.WEAPON){
				Pixmap map=n.component.modulePic.getPixMap();
				if(n.component.currentThreshold==3){
					map=Pic.getPixMap(n.component.modulePic.getMonochrome());
				}
				shipMap.drawPixmap(map,
						(int)(n.relativeTopLeft.x), 
						(int)(n.relativeTopLeft.y)-n.component.modulePic.get().getHeight()/2, 
						0, 0,  
						n.component.modulePic.get().getWidth(), 
						n.component.modulePic.get().getHeight());
			}
		}

		shipMap.drawPixmap(ship.shipPic.getPixMap(), 0, 0, 0, 0, 390, 270);




		for(Niche n:ship.niches){
			if(n.component.type==ModuleType.WEAPON)continue;

			if(n.component.type==ModuleType.GENERATOR||n.component.type==ModuleType.COMPUTER){
				Pixmap map=n.component.modulePic.getPixMap();
				if(n.component.currentThreshold==3){
					map=Pic.getPixMap(n.component.modulePic.getMonochrome());
				}
				shipMap.drawPixmap(map,
						(int)(n.p.getBoundingRectangle().x), 
						(int)(n.p.getBoundingRectangle().y), 
						0, 0,  n.component.modulePic.get().getWidth(), n.component.modulePic.get().getHeight());

			}

			if(n.component.type==ModuleType.SHIELD){
				Pixmap map=n.component.modulePic.getPixMap();
				if(n.component.currentThreshold==3){
					map=Pic.getPixMap(n.component.modulePic.getMonochrome());
				}
				shipMap.drawPixmap(map,
						(int)(n.relativeTopLeft.x), 
						(int)(n.relativeTopLeft.y-n.component.modulePic.get().getHeight()/2), 
						0, 0,  n.component.modulePic.get().getWidth(), n.component.modulePic.get().getHeight());
			}
		}

		composite=new Pic(new Texture(shipMap));
		picCut=new PicCut(composite, new Color(0,0,0,0));

	}

	public void damage(Pair damageLoc){
		Shard s=null;
		if(Math.random()>.7){

			for(int i=0;i<50;i++){
				s=picCut.replaceSection(damageLoc.add(Pair.randomAnyVector().multiply(120)), Gallery.shipDamage[(int) (Math.random()*9)].get());
				if(s!=null){
					shards.add(s);
					break;
				}
			}
		}



		if(s!=null){

			shards.add(s);
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

			for(int i=0;i<2;i++)animations.add(new Explosion1(s.position));

		}
		else animations.add(new Explosion1(damageLoc.add(Pair.randomAnyVector().multiply(40))));



	}

	public void destroy(){


		crack(true);
		float speed=1f;
		Timer t=new Timer(0,1,1/(speed/.5f),Interp.LINEAR);
		t.addFinisher(new Finisher() {
			@Override
			public void finish() {
				crack(true);
			}
		});

		t=new Timer(0,1,1/speed,Interp.LINEAR);
		t.addFinisher(new Finisher() {
			@Override
			public void finish() {
				crack(true);
			}
		});

		t=new Timer(0,1,1/(speed/1.5f),Interp.LINEAR);
		t.addFinisher(new Finisher() {
			@Override
			public void finish() {
				crack(true);
			}
		});

		t=new Timer(0,1,1/(speed/2f),Interp.LINEAR);
		t.addFinisher(new Finisher() {
			@Override
			public void finish() {
				release();
			}
		});




	}

	private void crack(boolean shake){

		Sounds.shatter.play();
		for(int i=0;i<2;i++){
			Pair position= picCut.addShatter();
			if(ship.player)position=position.add(ShipGraphic.offset);
			else position=new Pair(topRightEnemyShipPosition.x-position.x, topRightEnemyShipPosition.y+position.y);
			damage(position);damage(position);damage(position);damage(position);damage(position);
			for(int j=0;j<5;j++)animations.add(new Explosion1(position.add(Pair.randomAnyVector().multiply(60))));
		}
		for(int i=0;i<1;i++){
			Shard s=picCut.removeCut();
			setupShard(s);
		}

		if(shake)Battle.shake(ship.player, 7);
	}

	private void release(){
		Sounds.explode.play();
		Battle.shake(ship.player, 20);
		ship.exploded=true;
		Shard s=picCut.removeCut();

		while(s!=null){
			setupShard(s);
			if(s==null||s.position==null)continue;
			Pair position= s.position;
			animations.add(new Explosion1(position.add(Pair.randomAnyVector().multiply(60))));
			s=picCut.removeCut();
		}
	}

	public void setupShard(Shard s){
		if(s.size==0)return;
		s.estimatePosition();
		if(ship.player)s.position=s.position.add(ShipGraphic.offset);
		else s.position=new Pair(topRightEnemyShipPosition.x-s.position.x, topRightEnemyShipPosition.y+s.position.y);
		s.vector=Pair.randomAnyVector().multiply((float)Math.random()*220);
		s.dr=Particle.random(5);


		shards.add(s);
	}

	@Override
	public void update(float delta) {

		for(Shard s:shards){

			s.update(delta);
			s.position.x+=(ship.player?Star.playerSpeed:Star.enemySpeed)*delta*2;

		}
		for(int i=0;i<animations.size();i++){
			
			Animation a= animations.get(i);
			if(a.isDone()){
				a.dispose();
				animations.remove(a);
				i--;
			}
		}

	}

	public void render(SpriteBatch batch){




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
		for(Shard s:shards){
			s.render(batch);
		}
		for(Animation a:animations){
			a.render(batch);
		}


		for(Component c:ship.components){
			c.drawShield(batch);
		}



	}

	public void dispose() {
		for(Shard s:shards)s.texture.dispose();
		for(Animation a:animations)a.dispose();
		picCut.dispose();
		composite.dispose();
		if(shipMap!=null)shipMap.dispose();
		shards.clear();
		animations.clear();
		picCut.shards.clear();
	}




}
