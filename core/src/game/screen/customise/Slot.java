//package game.screen.customise;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//
//import game.Main;
//import game.assets.Gallery;
//import game.assets.Sounds;
//import game.module.Module;
//import game.module.Module.ModuleType;
//import game.module.component.shield.Shield;
//import game.module.component.weapon.Weapon;
//import game.module.junk.ModuleInfo;
//import game.module.utility.Utility;
//import game.module.utility.armour.Armour;
//import game.screen.preBattle.PreBattle;
//import game.ship.Ship;
//import util.Colours;
//import util.Draw;
//import util.maths.BoxCollider;
//import util.maths.Pair;
//import util.update.Mouser;
//import util.update.Screen;
//
//public class Slot extends Mouser{
//	ModuleType type;
//	int index;
//	Ship ship;
//	public static int width=Gallery.rewardOutline.getWidth()*4;
//	public static int height=Gallery.rewardOutline.getHeight()*4;
//
//	public Slot(Ship ship, Pair position, int index){
//		this.ship=ship;
//		this.position=position;
//		this.index=index;
//		type=index==2?ModuleType.ARMOUR:ModuleType.UTILITY;
//		mousectivate(new BoxCollider(position.x, position.y, width, height));
//	}
//
//	@Override
//	public void mouseDown() {
//		
//
//
//	}
//
//	@Override
//	public void mouseUp() {
//		if(Screen.isActiveType(Customise.class)){
//			Customise.unMouse(getModule());
//		}
//		if(Screen.isActiveType(PreBattle.class)){
//			PreBattle.unmouseSlot(this);
//		}
//	}
//
//	@Override
//	public void mouseClicked(boolean left) {
//		if(Screen.isActiveType(Customise.class)){
//			if(type==ModuleType.ARMOUR){
//				if(Customise.getReplaceableType()==type){
//					Customise.ship.setArmour((Armour) Customise.selectedReward.module);
//					Customise.rewardChosen();
//					Customise.mouseOver(Customise.ship.getArmour());
//					Sounds.shieldUse.overlay();
//				}
//				return;
//			}
//			if(Customise.getReplaceableType()==type){
//				Customise.ship.setUtility((Utility) Customise.selectedReward.module, index);
//				Customise.rewardChosen();
//				Customise.mouseOver(Customise.ship.getUtility(index));
//				Sounds.shieldUse.overlay();
//			}
//		}
//	}
//
//	@Override
//	public void update(float delta) {
//	}
//
//	public Module getModule(){
//		if(index<2)return ship.getUtility(index);
//		return ship.getArmour();
//	}
//
//	public void render(SpriteBatch batch){
//
//		Draw.drawScaled(batch, Gallery.rewardOutline.get(), position.x, position.y, 4, 4);
//		
//		Color col=Colours.black;
//		if(type==ModuleType.UTILITY)col=Colours.compCols6[2];
//		if(type==ModuleType.ARMOUR)col=Colours.shipHull7[2];
//			
//		batch.setColor(col);
//		Draw.drawScaled(batch, Gallery.rewardHighlights.get(), position.x, position.y,4,4);
//		
//		batch.setColor(1,1,1,1);
//		batch.setColor(Reward.selectedColor);
//
//		if(Screen.isActiveType(Customise.class)){
//			if(Customise.getReplaceableType()==type){
//				Draw.drawScaled(batch, Gallery.rewardOutline.getOutline(), position.x, position.y, 4, 4);
//			}
//		}
//
//		batch.setColor(Colours.light);
//		if(moused) Draw.drawScaled(batch, Gallery.rewardOutline.getOutline(), position.x, position.y, 4, 4);
//
//		batch.setColor(1,1,1,1);
//		if(getModule()!=null){
//			Draw.drawCenteredScaled(batch, getModule().getPic(0).get(), position.x+width/2, position.y+height/2, 4, 4);
//		}
//
//
//	}
//
//}
