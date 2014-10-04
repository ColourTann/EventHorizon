package game.module.junk;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.image.Pic;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Mouser;
import util.update.Screen;
import util.update.TextWisp;
import util.update.TextWisp.WispType;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.card.CardGraphic;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.Component;
import game.screen.battle.Battle;
import game.screen.battle.tutorial.Tutorial;
import game.screen.customise.Customise;
import game.screen.customise.Reward;
import game.screen.preBattle.PreBattle;

public class ModuleStats extends Mouser{
	static int height=Main.height/5;
	public static int width=128;
	public Component component;
	public ModuleInfo info;
	static Pair hpLoc=new Pair(14, 9);
	static Pair hpGap=new Pair(16,15);
	static int row=6;

	//Just for tutorial//
	TextWisp nameWisp;

	public ModuleStats(Component c) {
		mousectivate(new BoxCollider(c.ship.player?0:Main.width-width, height*c.getIndex(), width, height));
		component=c;
		info=new ModuleInfo(c);

	}



	@Override
	public void mouseClicked(boolean left) {
		if(Screen.isActiveType(Customise.class)){

			if(Customise.getReplaceableType()==component.type){
				Customise.replace(component);
			}

		}
		component.clicked(left);
	}
	@Override
	public void mouseDown() {
		component.moused();
		if(!Battle.isTutorial()){
			info.stopFading();
			ModuleInfo.top=info;
		}
		
		if(Screen.isActiveType(Customise.class)){
			Customise.mouseOver(component);
			if(Customise.getReplaceableType()==component.type){
				Customise.checkEnergy(new Module[]{component}, new Module[]{Customise.selectedReward.module});
			}
		}

	}
	@Override
	public void mouseUp() {
		if(Main.currentScreen instanceof Customise){
			Customise.unMouse(component);
		}
		component.unmoused();
		if(info!=null)info.fadeAll();

	}
	@Override
	public void update(float delta) {
	}

	public void showNameWisp(){
		nameWisp=new TextWisp(component.getClass().getSuperclass().getSimpleName(), Font.test, collider.position.add(63, 73), WispType.HoldUntilFade);
	}

	public void hideNameWisp(){
		nameWisp.release();
	}

	public void render(SpriteBatch batch) {
		Pic overlay=null;
		switch(component.type){
		case COMPUTER:
			overlay=Gallery.statsComputer;
			break;
		case GENERATOR:
			overlay=Gallery.statsGenerator;
			break;
		case SHIELD:
			overlay=Gallery.statsShield;
			break;
		case WEAPON:
			overlay=Gallery.statsWeapon;
			break;
		default:
			overlay=Gallery.baseModuleStats;
		}
		batch.setColor(1,1,1,1);
		Draw.draw(batch, Gallery.baseModuleStats.get(), collider.position.x, collider.position.y);
		Draw.draw(batch, overlay.get(), collider.position.x, collider.position.y);

		if(component.moused)Draw.draw(batch, Gallery.statsMoused.get(), collider.position.x, collider.position.y);
		if(Screen.isActiveType(Customise.class)){
			if(Customise.getReplaceableType()==component.type){
				batch.setColor(Reward.selectedColor);
				Draw.draw(batch, Gallery.statsMoused.get(), collider.position.x, collider.position.y);
				batch.setColor(Colours.white);
			}


		}

		if(component.targeteds>0)	Draw.draw(batch, Gallery.statsTargeted.get(), collider.position.x, collider.position.y);
		if(component.immune)Draw.draw(batch, Gallery.statsImmune.get(), collider.position.x, collider.position.y);

		if(component.type==ModuleType.WEAPON){
			batch.setColor(1, 1, 1, .5f);
			Draw.drawCenteredScaled(batch, component.modulePic.get(), collider.position.x+37, collider.position.y+110, 2f/3f,2f/3f);
			batch.setColor(1, 1, 1, 1);
			//Draw.drawTexture(batch, mod.modulePic.get(),collider.x+5,collider.y+5);
		}


		int damage=component.getDamage();
		int unshieldable=component.getUnshieldableIncoming();
		int incoming=component.getSimpleIncoming();
		int shields=component.getShield();
		if(component.immune){
			unshieldable=0;
			incoming=0;
		}
		int index;
		Pic[] p;

		int twin=0;
		int slotLoc=0;
		for(int i=0;i<component.maxHP;i++){
			twin--;

			if(component.ship.doubleHP&&twin<=0) {
				boolean doit=true;
				for(int thre=0;thre<3;thre++){
					if(component.thresholds[thre]==i+1)doit=false;
				}
				if(doit)twin=2;
				
			}

			p=Gallery.greenHP;
			index=0;
			boolean moused=false;
			if(damage>i){
				if(component.damage.get(i).moused)moused=true;
				p=Gallery.redHP;
			}
			else if(damage+unshieldable>i){
				if(component.unshieldableIcoming.get(i-damage).moused)moused=true;
				p=Gallery.greyHP;
			}
			else if(damage+incoming+unshieldable>i){
				if(component.incomingDamage.get(i-damage-unshieldable).moused)moused=true;
				p=Gallery.orangeHP;
				if(i>=damage+incoming+unshieldable-shields){
					p=Gallery.blueHP;
				}
			}
			if(component.thresholds[0]==i+1||component.thresholds[1]==i+1){
				index=1;
			}
			if(i==component.maxHP-1){
				index=2;
			}
			Draw.draw(batch, p[twin>0?2+twin:index].get(),collider.position.x+hpLoc.x+hpGap.x*(slotLoc%row),collider.position.y+hpLoc.y+hpGap.y*(slotLoc/row));
			if(moused){
				Draw.draw(batch, Gallery.mousedHP.get(), collider.position.x+hpLoc.x+hpGap.x*(slotLoc%row),collider.position.y+hpLoc.y+hpGap.y*(slotLoc/row));
			}

			if(twin!=2)slotLoc++;


		}

		//Off the edge damage//
		Pic ext=Gallery.orangeHP[5];

		if(damage+unshieldable+incoming-shields<=component.maxHP){
			ext=Gallery.blueHP[5];
		}
		if(damage+unshieldable+incoming>component.maxHP){
			Draw.draw(batch, ext.get(), collider.position.x+hpLoc.x+hpGap.x*((slotLoc)%row),collider.position.y+hpLoc.y+hpGap.y*((slotLoc)/row));
			//Off the edge number//
			
			if(ext==Gallery.orangeHP[5]){
				Font.small.setColor(Colours.weaponCols8[6]);
				String s=damage+unshieldable+incoming-shields-component.maxHP+"";
				Font.small.draw(batch, s, collider.position.x+hpLoc.x+hpGap.x*((slotLoc)%row)+11-Font.small.getBounds(s).width/2,  collider.position.y+hpLoc.y+hpGap.y*((slotLoc)/row)+6);
			}
		}

		batch.setColor(Colours.white);
		float gap=20;
		int pos=0;
		for(int i=0;i<component.buffs.size();i++){

			if(component.buffs.get(i).getPic()==null){
				continue;
			}
			Draw.draw(batch, component.buffs.get(i).getPic().get(), collider.position.x+8+pos*gap, collider.position.y+70);
			pos++;
		}

		if(Main.currentScreen instanceof Battle|| Main.currentScreen instanceof PreBattle)info.render(batch);
		

	}



	public void reset() {
		activate();
		mousectivate(null);
	}
	
	public void dispose(){
		deactivate();
		demousectivate();
		info.deactivate();
		info.demousectivate();
		for(CardGraphic cg:info.graphics){
			cg.demousectivate();
			cg.deactivate();
		}
	}
}
