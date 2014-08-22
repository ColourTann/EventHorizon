package eh.ship;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.Main;
import eh.module.Module;
import eh.module.Module.ModuleType;
import eh.screen.battle.Battle;
import eh.screen.battle.tutorial.Tutorial;
import eh.ship.niche.Niche;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.assets.Animation;
import eh.util.assets.Explosion1;
import eh.util.assets.Gallery;
import eh.util.assets.Pic;
import eh.util.assets.PicCut;
import eh.util.assets.PicCut.Shard;
import eh.util.maths.Pair;

public class ShipGraphic extends Bonkject{
	Ship ship;
	public static Pair offset=new Pair(175, 90);
	public static int height=270;
	Pic composite;
	PicCut picCut;
	ArrayList<Animation> animations= new ArrayList<Animation>();
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
		picCut=new PicCut(composite, Colours.black);
		
		
		//shipMap.dispose();
	}

	public void damage(){
		Pair loc=picCut.replaceSection(Gallery.shipDamage[(int) (Math.random()*9)].get());
		if(loc!=null){
			for(int i=0;i<2;i++)
		animations.add(new Explosion1(loc.add(offset)));
		}
		
	}

	public void render(SpriteBatch batch){
		for(int i=0;i<animations.size();i++){
			Animation a= animations.get(i);
			if(a.isDone()){
				animations.remove(a);
				i--;
			}
		}


		batch.setColor(1, 1, 1, 1);
		if(ship.player)Draw.drawTexture(batch, picCut.get(), offset.x, offset.y);
		if(!ship.player)	Draw.drawTextureScaledFlipped(batch, composite.get(), 
				500+Main.width-offset.x-composite.getWidth(), 
				offset.y,
				1,1,true, false);

		//Shitty tutorial shit shit shit//
		if(ship.player&&Tutorial.index==0&&Battle.tutorial){
			batch.setColor(Colours.withAlpha(Colours.light, (float)Math.sin(Battle.ticks*4)/5+.3f));
			for(int x=-1;x<=1;x+=2){
				for(int y=-1;y<=1;y+=2){

					Draw.drawTexture(batch, composite.getGlow(), offset.x+x, offset.y+y);
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
		
		

	}





	@Override
	public void mouseDown() {
	}



	@Override
	public void mouseUp() {
	}



	@Override
	public void mouseClicked(boolean left) {
	}



	@Override
	public void update(float delta) {

	}
}
