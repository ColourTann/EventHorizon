package eh.ship.module.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.Main;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.assets.Pic;
import eh.screen.battle.Battle;
import eh.screen.battle.tutorial.Tutorial;
import eh.ship.module.Module;
import eh.ship.module.Module.ModuleType;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.maths.BoxCollider;
import eh.util.maths.Collider;
import eh.util.maths.Pair;

public class ModuleStats extends Bonkject{
	static int height=Main.height/5;
	static int width=128;
	Module mod;
	public ModuleInfo info;
	static Pair hpLoc=new Pair(14, 111);
	static Pair hpGap=new Pair(16,15);
	static int row=6;

	public ModuleStats(Module m) {
		super(new BoxCollider(m.ship.player?0:Main.width-width, Main.height-height*m.getIndex()-height, width, height));
		mousectivate();
		mod=m;
		info=new ModuleInfo(m);
	}



	@Override
	public void mouseClicked(boolean left) {mod.clicked();}
	@Override
	public void mouseDown() {
		mod.moused();
		if(!Battle.tutorial){
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
		batch.draw(base.get(), collider.position.x, collider.position.y);

		if(mod.moused)batch.draw(Gallery.statsMoused.get(), collider.position.x, collider.position.y);
		if(mod.targeteds>0)	batch.draw(Gallery.statsTargeted.get(), collider.position.x, collider.position.y);
		if(mod.immune)batch.draw(Gallery.statsImmune.get(), collider.position.x, collider.position.y);

		if(mod.type==ModuleType.WEAPON){
			batch.setColor(1, 1, 1, .5f);
			Draw.drawTextureScaledCentered(batch, mod.modulePic.get(), collider.position.x+37, collider.position.y+29, 2f/3f,2f/3f);
			batch.setColor(1, 1, 1, 1);
			//batch.draw(mod.modulePic.get(),collider.x+5,collider.y+5);
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

		for(int i=0;i<mod.maxHP;i++){
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
			if(mod.thresholds[0]==i+1||mod.thresholds[1]==i+1)index=1;
			if(i==mod.maxHP-1)index=2;
			batch.draw(p[index].get(),collider.position.x+hpLoc.x+hpGap.x*(i%row),collider.position.y+hpLoc.y-hpGap.y*(i/row));
			if(moused){
				batch.draw(Gallery.mousedHP.get(), collider.position.x+hpLoc.x+hpGap.x*(i%row),collider.position.y+hpLoc.y-hpGap.y*(i/row));
			}
		}

		//Off the edge damage//
		Pic ext=Gallery.orangeHP[3];

		if(damage+unshieldable+incoming-shields<=mod.maxHP){
			ext=Gallery.blueHP[3];
		}
		if(damage+unshieldable+incoming>mod.maxHP){
			batch.draw(ext.get(),collider.position.x+hpLoc.x+hpGap.x*(mod.maxHP%row),collider.position.y+hpLoc.y-hpGap.y*(mod.maxHP/row));
			//Off the edge number//
			if(ext==Gallery.orangeHP[3]){
				Font.small.setColor(Colours.weaponCols8[6]);
				String s=damage+unshieldable+incoming-shields-mod.maxHP+"";
				Font.small.draw(batch, s, collider.position.x+hpLoc.x+hpGap.x*(mod.maxHP%row)+8, collider.position.y+hpLoc.y-hpGap.y*(mod.maxHP/row)+15);
			}
		}

		batch.setColor(Colours.white);
		float gap=20;
		int pos=0;
		for(int i=0;i<mod.buffs.size();i++){

			if(mod.buffs.get(i).getPic()==null){
				continue;
			}
			batch.draw(mod.buffs.get(i).getPic().get(), collider.position.x+8+pos*gap, collider.position.y+49);
			pos++;
		}

		info.render(batch);


	}
}
