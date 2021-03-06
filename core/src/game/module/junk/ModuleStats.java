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
import util.update.Timer.Interp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.card.CardGraphic;
import game.card.CardCode.Special;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.Component;
import game.module.junk.buff.BuffList;
import game.module.utility.Utility;
import game.screen.battle.Battle;
import game.screen.battle.tutorial.Tutorial;
import game.screen.customise.Customise;
import game.screen.customise.Reward;
import game.screen.map.Map;
import game.screen.map.stuff.Base;
import game.screen.preBattle.PreBattle;

public class ModuleStats extends Mouser{
	public static int height=Main.height/5-17;
	public static int width=128;
	public Component component;
	public InfoBox info;
	static Pair hpLoc=new Pair(14, 9);
	static Pair hpGap=new Pair(16,15);
	static int row=6;

	//utility stuff//
	boolean utilityStats;
	Utility fUtil;
	int index;
	boolean player;
	ModuleType type;

	static int uWidth=60;
	static int uHeight=32;
	static int uYGap=8;
	static int uXGap=2;

	//Just for tutorial//
	TextWisp nameWisp;
	public BuffList buffList;

	public ModuleStats(Component c) {
		component=c;
		moveToDefaultPosition();
		info=new InfoBox(c);
		type=c.type;
	}

	public void moveToDefaultPosition(){
		boolean left=true;
		if(component.ship!=null){
			left=component.ship.player;
		}
		mousectivate(new BoxCollider(left?0:Main.width-width, height*component.getIndex(), width, height));
		if(collider.position.y<0)collider.position.y=0;
	}

	public ModuleStats(Utility aUtil, int aIndex, boolean player){
		utilityStats=true;
		this.player=player;
		fUtil=aUtil;
		index=aIndex;
		if(index==2)type=ModuleType.ARMOUR;
		else type=ModuleType.UTILITY;
		int x=uXGap;
		int y=Main.height-uHeight-uYGap;
		switch(index){
		case 0:
			x+=uXGap+uWidth;
			break;
		case 1:
			x+=uXGap+uWidth;
			y-=uHeight;
			y-=uYGap;
			break;
		case 2:
			y-=uHeight/2;
			y-=uYGap/2;
			break;
		}

		if(!player) x=Main.width-x-uWidth;
		position=new Pair(x,y);
		mousectivate(new BoxCollider(position.x, position.y, uWidth, uHeight));
		if(aUtil!=null)info=new InfoBox(aUtil);		
	}

	@Override
	public void mouseClicked(boolean left) {
		if(Screen.isActiveType(Customise.class)){
			if(utilityStats){
				if(Customise.getReplaceableType()==type){
					Customise.replace(fUtil, index);
				}
			}
			else if(Customise.getReplaceableType()==component.type){
				Customise.replace(component,-1);
			}

		}
		if(component!=null)component.clicked(left);

		if(Screen.isActiveType(Map.class)){
			if(utilityStats){
				if(Base.getReplaceableType()==type){
					Base.me.replace(fUtil, index);
				}
			}
			else if(Base.getReplaceableType()==component.type){
				Base.me.replace(component, -1);
			}
		}
	}
	@Override
	public void mouseDown() {
		if(component==null&&fUtil==null)return;
		if(component!=null)component.moused();

		InfoBox.top=info;

		if(!Battle.isTutorial()){
			info.stopFading();
			info.alpha=1;
			InfoBox.top=info;
			if(buffList!=null){
				buffList.stopFading();
				buffList.alpha=1;
			}
		}

		if(Screen.isActiveType(Customise.class)){
			if(component!=null){
				Customise.mouseOver(component);
				if(Customise.getReplaceableType()==component.type){
					Customise.checkEnergy(new Module[]{component}, new Module[]{Customise.selectedReward.module});
				}
			}
			else if(fUtil!=null){
				Customise.mouseOver(fUtil);
			}
			else Customise.unMouse(null);
		}
		if(Screen.isActiveType(Map.class)){
			Map.mouseStats(info);
		}

	}
	@Override
	public void mouseUp() {
		if(component==null&&fUtil==null)return;
		if(Main.currentScreen instanceof Customise){
			if(component!=null)Customise.unMouse(component);
			Customise.unMouse(fUtil);
		}
		if(component!=null)component.unmoused();
		if(info!=null)info.fadeAll();
		if(buffList!=null)buffList.fadeOut(CardGraphic.fadeSpeed/1.5f, Interp.LINEAR);

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
		if(Main.currentScreen instanceof Battle|| Main.currentScreen instanceof PreBattle){
			if(info!=null&&info!=InfoBox.top)info.render(batch);
			if(component!=null){
				if(buffList==null){
					buffList=new BuffList(component);
				}
				buffList.render(batch);
			}

		}


		if(utilityStats){
			Draw.drawScaled(batch, Gallery.baseUtilityStats.get(), position.x, position.y, 2, 2);

			if(fUtil!=null){
				Draw.drawScaled(batch, fUtil.getPic(0).get(), position.x, position.y, 2, 2);
			}

			Draw.drawScaled(batch, Gallery.baseUtilityStatsOutline.get(), position.x, position.y, 2, 2);
			if(moused){
				batch.setColor(Colours.light);
				Draw.drawScaled(batch, Gallery.baseUtilityStats.getOutline(),  position.x, position.y, 2, 2);
				batch.setColor(1,1,1,1);	
			}

			if(Base.getReplaceableType()==type||Customise.getReplaceableType()==type){
				batch.setColor(Reward.selectedColor);
				Draw.drawScaled(batch, Gallery.baseUtilityStats.getOutline(), collider.position.x, collider.position.y,2,2);
				batch.setColor(Colours.white);
			}
			return;
		}

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
		batch.setColor(1,1,1,alpha);
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
		if(Main.currentScreen instanceof Map){
			if(Base.getReplaceableType()==type){
				if(!component.pretending){
					batch.setColor(Reward.selectedColor);
					Draw.drawScaled(batch, Gallery.statsMoused.get(), collider.position.x, collider.position.y,1,1);
					batch.setColor(1, 1, 1, alpha);
				}
			}
		}

		if(component.targeteds>0)	Draw.draw(batch, Gallery.statsTargeted.get(), collider.position.x, collider.position.y);
		if(component.immune)Draw.draw(batch, Gallery.statsImmune.get(), collider.position.x, collider.position.y);

		if(component.type==ModuleType.WEAPON){
			batch.setColor(1, 1, 1, .5f*alpha);
			Draw.drawCenteredScaled(batch, component.modulePic.get(), collider.position.x+37, collider.position.y+94, 2f/3f,2f/3f);
			batch.setColor(1, 1, 1, alpha);
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
			boolean absorb=false;
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
		if(component.type==ModuleType.WEAPON){
			Font.medium.setColor(Colours.withAlpha(Colours.weaponCols8[6], alpha));
			Font.drawFontCentered(batch, (component.tier+1)+"", Font.medium, collider.position.x+92, collider.position.y+height-30);
		}
		if(component.type==ModuleType.SHIELD){
			Font.medium.setColor(Colours.withAlpha(Colours.shieldCols6[1], alpha));
			Font.drawFontCentered(batch, (component.tier+1)+"", Font.medium, collider.position.x+63, collider.position.y+height-32);
		}
	}

	public void reset() {
		activate();
		mousectivate(null);
		moveToDefaultPosition();
	}

	public void dispose(){
		deactivate();
		demousectivate();
		if(info!=null){
			info.deactivate();
			for(CardGraphic cg:info.graphics){
				cg.demousectivate();
				cg.deactivate();
			}
		}

	}



	public void fade() {
		fadeOut(.2f, Interp.LINEAR);
	}
}
