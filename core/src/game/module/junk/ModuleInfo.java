package game.module.junk;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.Mouser;
import util.update.Timer.Interp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.card.CardGraphic;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.computer.Computer;

public class ModuleInfo extends Mouser{
	//279373
	float width;
	float height=373;
	public Module mod;
	public ArrayList<CardGraphic> graphics = new ArrayList<CardGraphic>();
	public static ModuleInfo top;
	public ModuleInfo(Module m) {
		mod=m;
		width=CardGraphic.width*2;
		if(mod.type==ModuleType.SHIELD||mod.type==ModuleType.WEAPON)width=CardGraphic.width*3;
		
		if(mod.ship==null)return;
		setPosition(new Pair(mod.ship.player?130+width/2:Main.width-130-width/2, 0));



	}

	public void setPosition(Pair pos){
		graphics.clear();
		this.position=pos.add(-width/2,0);
		if(mod.type==ModuleType.WEAPON||mod.type==ModuleType.SHIELD){
			for(int i=0;i<=3;i++){
				CardGraphic cg=mod.getCard(i+1).getHalfGraphic(true);
				cg.setPosition(new Pair(position.x+(i%2)*139+139, position.y+((i/2))*124));
				graphics.add(cg);
			}
			CardGraphic cg=mod.getCard(0).getHalfGraphic(false);
			cg.setPosition(new Pair(position.x,position.y+124));
			graphics.add(cg);
		}
		else{
			height-=124;

			CardGraphic cg=mod.getCard(0).getHalfGraphic(true);
			cg.setPosition(new Pair(position.x+139, position.y));
			graphics.add(cg);

			CardGraphic cg1=mod.getCard(1).getHalfGraphic(true);
			cg1.setPosition(new Pair(position.x+139,position.y+CardGraphic.height/2));
			graphics.add(cg1);
		}

		alpha=0;
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

	public void fadeAll(){

		for(CardGraphic cg: graphics){
			cg.fadeOut(CardGraphic.fadeSpeed/1.5f, CardGraphic.fadeType);
		}
		fadeOut(CardGraphic.fadeSpeed/1.5f, Interp.LINEAR);
	}


	public void render(SpriteBatch batch) {

		if (alpha<=0)return;
		batch.setColor(1,1,1,alpha);
		if(mod.type==ModuleType.SHIELD||mod.type==ModuleType.WEAPON){
			Draw.draw(batch, Gallery.cardBase.getMask(Colours.dark), position.x, position.y);
		}
		else{
			Draw.draw(batch, Gallery.cardBase.getMask(Colours.dark), position.x, position.y);
			Draw.draw(batch, Gallery.cardBase.getMask(Colours.dark), position.x, position.y+CardGraphic.height/2);
		}

		Font.medium.setColor(Colours.withAlpha(Colours.light,alpha));
		String s=mod.moduleName;
		Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+17);
		s="Cards:";
		Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+63);
		s=""+mod.numCards;
		if(mod.ship!=null){
			s+="/"+mod.ship.getTotalDeckSize();
		}
		Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+88);

		if(mod.type==ModuleType.GENERATOR){
			s="Energy";
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+155);
			s="Income:";
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+180);
			s=""+mod.ship.getIncome();
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+205);
		}
		if(mod.type==ModuleType.COMPUTER){
			s="Hand";
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+155);
			s="Size:";
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+180);
			s=""+((Computer)mod).maxCards;
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+205);
		}
		Font.small.setColor(Colours.withAlpha(Colours.light,alpha));
		String words=mod.type+(mod.tier==-1?"":" Tier "+mod.tier);
		Font.drawFontCentered(batch, words, Font.small, position.x+CardGraphic.width/2, position.y+42);
		//Font.medium.drawWrapped(batch, s, width/4, 20, 500, HAlignment.CENTER);
		if(alpha>0){
			for(CardGraphic cg:graphics){
				cg.alpha=alpha;
				cg.render(batch);
			}
		}
		batch.setColor(1,1,1,1);
	}
}
