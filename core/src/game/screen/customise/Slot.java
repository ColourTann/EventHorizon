package game.screen.customise;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.junk.ModuleInfo;
import game.module.utility.Utility;
import game.module.utility.armour.Armour;
import util.Draw;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Mouser;
import util.update.Screen;

public class Slot extends Mouser{
	ModuleType type;
	int index;
	static int width=Gallery.rewardOutline.getWidth()*4;
	static int height=Gallery.rewardOutline.getHeight()*4;
	
	public Slot(Pair position, int index){
		this.position=position;
		this.index=index;
		type=index==2?ModuleType.ARMOUR:ModuleType.UTILITY;
		mousectivate(new BoxCollider(position.x, position.y, width, height));
	}

	@Override
	public void mouseDown() {
		Module m=getModule();
		if(m!=null)Customise.mouseOver(m);


		if(Customise.getReplaceableType()==type){
			Customise.checkEnergy(new Module[]{getModule()}, new Module[]{Customise.selectedReward.module});
		}



	}

	@Override
	public void mouseUp() {
		Customise.unMouse(getModule());
	}

	@Override
	public void mouseClicked(boolean left) {
		if(type==ModuleType.ARMOUR){
			if(Customise.getReplaceableType()==type){
				Customise.ship.setArmour((Armour) Customise.selectedReward.module);
				Customise.rewardChosen();
				Customise.mouseOver(Customise.ship.getArmour());
			}
			return;
		}
		if(Customise.getReplaceableType()==type){
			Customise.ship.setUtility((Utility) Customise.selectedReward.module, index);
			Customise.rewardChosen();
			Customise.mouseOver(Customise.ship.getUtility(index));
		}
	}

	@Override
	public void update(float delta) {
	}

	public Module getModule(){
		if(index<2)return Customise.ship.getUtility(index);
		return Customise.ship.getArmour();
	}

	public void render(SpriteBatch batch){

		Draw.drawScaled(batch, Gallery.rewardOutline.get(), position.x, position.y, 4, 4);
		batch.setColor(Reward.selectedColor);
		if(Customise.getReplaceableType()==type){
			Draw.drawScaled(batch, Gallery.rewardOutline.getOutline(), position.x, position.y, 4, 4);
		}

		batch.setColor(1,1,1,1);
		if(getModule()!=null){
			Draw.drawCenteredScaled(batch, getModule().getPic(0).get(), position.x+width/2, position.y+height/2, 4, 4);
		}

		batch.setColor(1,1,1,1);
	}

}
