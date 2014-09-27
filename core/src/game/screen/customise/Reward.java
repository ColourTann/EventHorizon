package game.screen.customise;


import util.Colours;
import util.Draw;
import util.Draw.BlendType;
import util.image.Pic;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Mouser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.card.Card;
import game.module.Module;
import game.module.component.shield.Shield;
import game.module.component.weapon.Weapon;
import game.module.junk.ModuleInfo;
import game.module.utility.Utility;
import game.module.utility.armour.Armour;
import game.screen.battle.interfaceJunk.HelpPanel;

public class Reward extends Mouser{
	public Module module;
	public Card[] cards;
	static int width=Gallery.rewardOutline.getWidth()*4;
	static int height=Gallery.rewardOutline.getHeight()*4;
	static int offset=15;
	static int y=640;
	private ModuleInfo info;
	public boolean selected;
	public enum RewardType{Weapon, Shield, Armour, Utility, Booster}
	public static RewardType[] typeList = new RewardType[]{RewardType.Weapon, RewardType.Weapon, RewardType.Shield, RewardType.Armour, RewardType.Utility, RewardType.Booster};
	public static Color selectedColor=Colours.genCols5[3];
	int index;
	public Reward(Card[] cards, int index){
		this.cards=cards;
		this.index=index;
	}

	public Reward (Module m, int index){
		this.module=m;
		this.index=index;
	}
	
	public void confirm() {
		this.position=new Pair(Customise.shipX-width-offset+width*index+offset*index-width/2, y-height/2);
		mousectivate(new BoxCollider(position.x, position.y, width, height));
	}

	public ModuleInfo getInfo(){
		if(info==null){
			if(module!=null)info=new ModuleInfo(module);
			if(cards!=null)info=new ModuleInfo(cards);
		}
		return info;
	}

	public void render(SpriteBatch batch){
		batch.setColor(1,1,1,alpha);

		Draw.drawScaled(batch, Gallery.rewardOutline.get(), position.x, position.y,4,4);
		
		Color col=null;
		if(module!=null){
			if(module instanceof Weapon)col=Colours.weaponCols8[4];
			if(module instanceof Shield)col=Colours.shieldCols6[4];
			if(module instanceof Utility)col=Colours.compCols6[2];
			if(module instanceof Armour)col=Colours.shipHull7[2];
		}
		if(col==null)col=Colours.orangeHPCols[1];
		batch.setColor(Colours.withAlpha(col, alpha));
		
		Draw.drawScaled(batch, Gallery.rewardHighlights.get(), position.x, position.y,4,4);
		
		batch.setColor(1,1,1,alpha);
		
		if(module!=null){
			Pic p=module.getPic(0);
			Draw.drawCenteredScaled(batch, p.get(), position.x+width/2, position.y+height/2, 4, 4);
		}
		else{
			Draw.drawCenteredScaled(batch, Gallery.threeCards.get(), position.x+width/2, position.y+height/2, 4, 4);
		}
		if(moused){
			batch.setColor(Colours.withAlpha(Colours.light, alpha));
			Draw.drawScaled(batch, Gallery.rewardOutline.getOutline(), position.x, position.y,4,4);
		}
		if(selected){
			batch.setColor(Colours.withAlpha(selectedColor, alpha));
			Draw.drawScaled(batch, Gallery.rewardOutline.getOutline(), position.x, position.y,4,4);
		}
		batch.setColor(1,1,1,1);
	}


	@Override
	public void mouseDown() {
		Customise.changeStats(getInfo());

	}



	@Override
	public void mouseUp() {
		Customise.unMouse(module);
	}


	@Override
	public void mouseClicked(boolean left) {
		if(!selected)select();
		else{
			Customise.deselect();
			deselect();
		}
	}


	@Override
	public void update(float delta) {
	}

	public void select(){
		Sounds.cardSelect.play();
		selected=true;
		Customise.select(this);
	}

	public void deselect() {
		Sounds.cardDeselect.play();
		moused=false;
		selected=false;

	}

	public Module getModule() {
		return module;
	}

	
	
	
}
