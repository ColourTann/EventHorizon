package game.module.junk;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.TextWriter;
import util.assets.Font;
import util.maths.Pair;
import util.update.Mouser;
import util.update.Screen;
import util.update.Timer.Interp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import game.Main;
import game.assets.Gallery;
import game.assets.TextBox;
import game.card.Card;
import game.card.CardGraphic;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.computer.Computer;
import game.module.component.shield.Shield;
import game.module.component.weapon.Weapon;
import game.module.utility.Utility;
import game.module.utility.armour.Armour;
import game.screen.customise.Customise;

public class ModuleInfo extends TextBox{
	//279373
	float cardOffset=4;
	float width;
	float height=CardGraphic.height*2-1;
	public Module mod;
	public Card[] consumableCards;
	public ArrayList<CardGraphic> graphics = new ArrayList<CardGraphic>();
	public boolean noDrawCards;
	public static ModuleInfo top;
	float offset=3;
	TextWriter statsWriter;
	public ModuleInfo(Module m) {
		if(m==null)return;
		mod=m;
		width=CardGraphic.width*2-2;
		if(mod instanceof Shield||mod instanceof Weapon)width=CardGraphic.width*3-3;
		if(mod.getPic(1)==null){
			width=CardGraphic.width-1;
		}
		width+=9;
		height+=17;
		if(m instanceof Utility){
			Font.medium.setColor(Colours.player2[0]);
			statsWriter=new TextWriter(Font.medium, ((Utility)m).passive);
			statsWriter.setWrapWidth((int) (CardGraphic.width-offset*2));
			statsWriter.setPassiveReplacements();
		}
		if(mod.ship==null)return;
		setPosition(new Pair(mod.ship.player?130+width/2:Main.width-130-width/2, 0));
		
		
	}
	
	public ModuleInfo(Card[] cards){
		this.consumableCards=cards;
		width=CardGraphic.width*3-3+11;
		height+=18;
	}

	public void setPosition(Pair pos){
		
		this.position=pos.add(-width/2,0);
		this.position=position.floor();
		if(mod==null){
			
			for (int i=0;i<consumableCards.length;i++){
				
				CardGraphic cg=consumableCards[i].getGraphic();
				cg.setPosition(new Pair(position.x+i*Gallery.cardBase.getWidth()+cardOffset, position.y+cardOffset));
				cg.override=true;
				graphics.add(cg);
				cg.demousectivate();
			}
			
			
			return;
		}
		graphics.clear();
		
		if(mod instanceof Weapon||mod instanceof Shield){

			for(int i=0;i<=3;i++){
				CardGraphic cg=mod.getCard(i+1).getHalfGraphic(true);
				cg.setPosition(new Pair(position.x+(i%2)*139+139+cardOffset, position.y+((i/2))*124+cardOffset));
				graphics.add(cg);
			}
			CardGraphic cg=mod.getCard(0).getHalfGraphic(false);
			cg.setPosition(new Pair(position.x+cardOffset,position.y+124+cardOffset));
			graphics.add(cg);
		}
		else if(mod.numCards==0){
			
		}
		else{
			//height-=124;

			CardGraphic cg=mod.getCard(0).getHalfGraphic(true);
			cg.setPosition(new Pair(position.x+139+cardOffset, position.y+cardOffset));
			graphics.add(cg);

			CardGraphic cg1=mod.getCard(1).getHalfGraphic(true);
			cg1.setPosition(new Pair(position.x+139+cardOffset,position.y+124+cardOffset));
			graphics.add(cg1);
		}

		alpha=0;
	}

	@Override
	public void update(float delta) {
	}

	public void fadeAll(){
		for(CardGraphic cg: graphics){
			cg.fadeOut(CardGraphic.fadeSpeed/1.2f, CardGraphic.fadeType);
		}
		fadeOut(CardGraphic.fadeSpeed/1.2f, Interp.LINEAR);
	}


	public void render(SpriteBatch batch) {

		if (alpha<=0)return;
		Font.small.setColor(Colours.withAlpha(Colours.light,alpha));
		batch.setColor(1,1,1,alpha);
		renderBox(batch, width, height/2f);

		if(consumableCards!=null&&alpha>0){
			if(noDrawCards)return;
			for(Card c:consumableCards){
				CardGraphic cg=c.getGraphic();
				cg.finishFlipping();
				cg.alpha=alpha;
				cg.render(batch);
			}
			return;
		}
		
		Font.medium.setColor(Colours.withAlpha(Colours.light,alpha));
		String s=mod.moduleName;
		float nameHeight=Font.medium.getWrappedBounds(s, CardGraphic.width).height;
		Font.medium.drawWrapped(batch, s, position.x+cardOffset, position.y+25-nameHeight/2, CardGraphic.width, HAlignment.CENTER);
		//Font.drawFontCentered(batch, s, Font.medium, position.x+CardGraphic.width/2, position.y+25);
		//Font.small.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+17);
		s="Cards:";
		Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+80);
		s=""+mod.numCards;
		if(mod.ship!=null&&mod.numCards>0){
			s+="/"+mod.ship.getTotalDeckSize();
		}
		Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2+cardOffset, position.y+100);

		if(mod instanceof Armour){
	
			
			s="HP multiplier "+((Armour)mod).multiplier;
			Font.small.drawWrapped(batch, s, position.x, position.y+50+nameHeight/2+cardOffset, CardGraphic.width, HAlignment.CENTER);
		}

		if(mod.type==ModuleType.GENERATOR){
			s="Energy";
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2+cardOffset, position.y+155);
			s="Income:";
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2+cardOffset, position.y+180);
			s=""+mod.ship.getIncome();
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2+cardOffset, position.y+205);
		}
		if(mod.type==ModuleType.COMPUTER){
			s="Hand";
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2+cardOffset, position.y+155);
			s="Size:";
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2+cardOffset, position.y+180);
			s=""+((Computer)mod).maxCards;
			Font.medium.draw(batch, s, position.x+CardGraphic.width/2-Font.medium.getBounds(s).width/2, position.y+205);
		}
		if(mod instanceof Utility){
			float fHeight=statsWriter.maxHeight;
			statsWriter.drawText(batch, (int)(position.x+offset+cardOffset), (int)(position.y+CardGraphic.height*3/4-fHeight/2));
		}
	
		String words=mod.type+(mod.tier==-1?"":" Tier "+mod.tier);
		Font.drawFontCentered(batch, words, Font.small, position.x+CardGraphic.width/2+cardOffset, position.y+42+nameHeight/2);
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
