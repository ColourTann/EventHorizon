package game.module.stuff;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.image.Pic;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Mouser;
import util.update.TextWisp;
import util.update.TextWisp.WispType;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.module.Module;
import game.module.Module.ModuleType;
import game.screen.battle.Battle;
import game.screen.battle.tutorial.Tutorial;

public class ModuleStats extends Mouser{
	static int height=Main.height/5;
	public static int width=128;
	Module mod;
	public ModuleInfo info;
	static Pair hpLoc=new Pair(14, 9);
	static Pair hpGap=new Pair(16,15);
	static int row=6;

	//Just for tutorial//
	TextWisp nameWisp;

	public ModuleStats(Module m) {
		mousectivate(new BoxCollider(m.ship.player?0:Main.width-width, height*m.getIndex(), width, height));
		mod=m;
		info=new ModuleInfo(m);
	}



	@Override
	public void mouseClicked(boolean left) {mod.clicked();}
	@Override
	public void mouseDown() {
		mod.moused();
		if(!Battle.isTutorial()){
			Tutorial.next();
			info.stopFading();
			ModuleInfo.top=info;
		}
	}
	@Override
	public void mouseUp() {
		mod.unmoused();
		if(info!=null)info.fadeAll();
	}
	@Override
	public void update(float delta) {
	}

	public void showNameWisp(){
		nameWisp=new TextWisp(mod.getClass().getSuperclass().getSimpleName(), Font.test, collider.position.add(63, 73), WispType.HoldUntilFade);
	}

	public void hideNameWisp(){
		nameWisp.release();
	}

	public void render(SpriteBatch batch) {
		Pic base=null;
		switch(mod.type){
		case COMPUTER:
			base=Gallery.statsComputer;
			break;
		case GENERATOR:
			base=Gallery.statsGenerator;
			break;
		case SHIELD:
			base=Gallery.statsShield;
			break;
		case WEAPON:
			base=Gallery.statsWeapon;
			break;
		default:
			base=Gallery.baseModuleStats;
		}
		batch.setColor(1,1,1,1);
		Draw.draw(batch, base.get(), collider.position.x, collider.position.y);

		if(mod.moused)Draw.draw(batch, Gallery.statsMoused.get(), collider.position.x, collider.position.y);
		if(mod.targeteds>0)	Draw.draw(batch, Gallery.statsTargeted.get(), collider.position.x, collider.position.y);
		if(mod.immune)Draw.draw(batch, Gallery.statsImmune.get(), collider.position.x, collider.position.y);

		if(mod.type==ModuleType.WEAPON){
			batch.setColor(1, 1, 1, .5f);
			Draw.drawScaledCentered(batch, mod.modulePic.get(), collider.position.x+37, collider.position.y+110, 2f/3f,2f/3f);
			batch.setColor(1, 1, 1, 1);
			//Draw.drawTexture(batch, mod.modulePic.get(),collider.x+5,collider.y+5);
		}


		int damage=mod.getDamage();
		int unshieldable=mod.getUnshieldableIncoming();
		int incoming=mod.getSimpleIncoming();
		int shields=mod.getShield();
		if(mod.immune){
			unshieldable=0;
			incoming=0;
		}
		int index;
		Pic[] p;
	
		int twin=0;
		int slotLoc=0;
		for(int i=0;i<mod.maxHP;i++){
			twin--;
			
			if(mod.doubles[slotLoc]&&twin<=0) twin=2;
			
			p=Gallery.greenHP;
			index=0;
			boolean moused=false;
			if(damage>i){
				if(mod.damage.get(i).moused)moused=true;
				p=Gallery.redHP;
			}
			else if(damage+unshieldable>i){
				if(mod.unshieldableIcoming.get(i-damage).moused)moused=true;
				p=Gallery.greyHP;
			}
			else if(damage+incoming+unshieldable>i){
				if(mod.incomingDamage.get(i-damage-unshieldable).moused)moused=true;
				p=Gallery.orangeHP;
				if(i>=damage+incoming+unshieldable-shields){
					p=Gallery.blueHP;
				}
			}
			if(mod.thresholds[0]==i+1||mod.thresholds[1]==i+1){
				index=1;
			}
			if(i==mod.maxHP-1){
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

		if(damage+unshieldable+incoming-shields<=mod.maxHP){
			ext=Gallery.blueHP[5];
		}
		if(damage+unshieldable+incoming>mod.maxHP){
			Draw.draw(batch, ext.get(), collider.position.x+hpLoc.x+hpGap.x*(mod.maxHP%row),collider.position.y+hpLoc.y+hpGap.y*(mod.maxHP/row));
			//Off the edge number//
			if(ext==Gallery.orangeHP[3]){
				Font.small.setColor(Colours.weaponCols8[6]);
				String s=damage+unshieldable+incoming-shields-mod.maxHP+"";
				Font.small.draw(batch, s, collider.position.x+hpLoc.x+hpGap.x*(mod.maxHP%row)+12-Font.small.getBounds(s).width/2,  collider.position.y+hpLoc.y+hpGap.y*(mod.maxHP/row)+4);
			}
		}

		batch.setColor(Colours.white);
		float gap=20;
		int pos=0;
		for(int i=0;i<mod.buffs.size();i++){

			if(mod.buffs.get(i).getPic()==null){
				continue;
			}
			Draw.draw(batch, mod.buffs.get(i).getPic().get(), collider.position.x+8+pos*gap, collider.position.y+70);
			pos++;
		}

		info.render(batch);


	}
}
