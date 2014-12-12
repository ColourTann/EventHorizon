package game.screen.map.stuff;



import java.util.ArrayList;

import game.Main;
import game.assets.Gallery;
import game.assets.TextBox;
import game.card.CardGraphic;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.Component;
import game.module.component.shield.Shield;
import game.module.component.weapon.Weapon;
import game.module.junk.InfoBox;
import game.module.junk.ModuleStats;
import game.module.utility.Utility;
import game.screen.customise.Reward;
import game.screen.map.Map;
import game.screen.map.panels.PlayerStatsPanel;
import game.ship.Ship;
import game.ship.ShipGraphic;
import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.SimpleButton;
import util.update.Updater;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Base extends Updater{
	
	final static Pair size=new Pair(850,600);
	final static Pair location=new Pair(Main.width/2-size.x/2, Main.height/2-size.y/2).floor();
	final static Pair statsLoc= new Pair(location.x+65,location.y+377);
	final static Pair infoLoc=new Pair(location.x+473+5,location.y+size.y-TextBox.gap*(4)-CardGraphic.height);
	final static Pair shipLoc=new Pair(location.x+275, location.y+20);


	final static Pair scrollerLoc=new Pair(location.x+size.x-Scroller.width-TextBox.gap*2, location.y+TextBox.gap*2+70);
	static Item selectedItem;
	ArrayList<ModuleStats> stats= new ArrayList<ModuleStats>();
	ShipGraphic playerGraphic;
	static Ship player;
	InfoBox oldInfo;
	InfoBox info;
	ModuleStats newStats;
	ModuleStats oldStats;
	public Scroller inv;
	public static Base me;
	Trasher trasher;
	public Base(){
		me=this;
		player=Map.player.getShip();
		refreshStats();
		inv=new Scroller(player.getInv(), scrollerLoc, 4);
		trasher=new Trasher();
	}

	public void render(SpriteBatch batch){
		TextBox.renderBox(batch, location, size.x, size.y, util.TextWriter.Alignment.Left, false);
		for(ModuleStats ms:stats)ms.render(batch);
		for(ModuleStats ms:player.getUtilityStats()){
			ms.render(batch);
		}
		batch.setColor(1,1,1,1);
		Draw.draw(batch, playerGraphic.picCut.get(), shipLoc.x, shipLoc.y);
		inv.render(batch);

		if(info!=null)info.render(batch);
		if(oldInfo!=null)oldInfo.render(batch);

		if(newStats!=null)newStats.render(batch);
		if(oldStats!=null)oldStats.render(batch);
		Font.big.setColor(Colours.light);
		Font.drawFontCentered(batch, "INV", Font.big, 982, location.y+40);
		trasher.render(batch);
		//Draw.drawScaled(batch, Gallery.whiteSquare.get(), location.x, location.y, size.x, size.y);
	}

	public void mouseInfo(InfoBox moused) {
		oldInfo=info;
		info=moused;
		moused.setPosition(infoLoc);
		moused.unFade();
	}
	public void unMouse(){
		oldStats=newStats;
		if(oldStats!=null)oldStats.fade();
		oldInfo=info;
	}

	public void mouseItem(Item i){
		if(i.mod instanceof Component){
			Component c = (Component) i.mod;
			c.pretend(player);
			newStats=c.getStats();
			newStats.collider.position=statsLoc;
			newStats.stopFading();
			newStats.alpha=1;
		}
		else{
			newStats=null;
		}
	}
	
	
	
	public void select(Item i) {
		if(selectedItem==i){
			i.unSelect();
			selectedItem=null;		
			return;
		}
		if(selectedItem!=null)selectedItem.unSelect();
		selectedItem=i;
		i.select();
	}

	public static ModuleType getReplaceableType() {
		
		if(selectedItem!=null&&selectedItem.mod!=null){
			
			return selectedItem.mod.type;
		}
		return null;
	}

	public void replace(Module m, int i) {
		Module old = null;
		if(m instanceof Component){
			Component comp=(Component) m;
			comp.getStats().demousectivate();
			Component replacer = ((Component)selectedItem.mod);
			info.fadeAll();
			//mouseInfo(replacer.getInfo());
			replacer.pretending=false;
			if(m instanceof Weapon){
				old=player.setWeapon((Weapon) selectedItem.mod, comp.getIndex());	
			}
			if(m instanceof Shield){
				old=player.setShield((Shield) selectedItem.mod);
			}	
		}
		if(selectedItem.mod instanceof Utility){
			
			old=player.setUtility((Utility) selectedItem.mod, i);
			
		}
		refreshStats();
		inv.removeItem(selectedItem);
		if(old!=null)inv.addItem(new Item(old));
		inv.updateLocations(false);
		selectedItem=null;
	}

	private void refreshStats() {
		stats.clear();
		player.resetGraphics();
		
		for(Component c:player.components){
			stats.add(c.getStats());
			c.getStats().activate();
			c.getStats().mousectivate(null);
			c.getStats().stopFading();
			c.getStats().alpha=1;
		}
		
		for(ModuleStats ms:player.getUtilityStats()){
			ms.collider.position=ms.collider.position.add(220,-273);
			ms.position=ms.collider.position.copy();
			ms.mousectivate(null);
		}
		int x=(int) (location.x+TextBox.gap+ModuleStats.width);
		int y=(int) (location.y+TextBox.gap);
		for(int i=0;i<stats.size();i++){
			ModuleStats ms=stats.get(i);
			ms.mousectivate(new BoxCollider(x, y, ModuleStats.width, ModuleStats.height));
			y+=ModuleStats.height;
			if(i==2){
				y=(int) (location.y+TextBox.gap);
				x-=ModuleStats.width;
			}
		}
		player.getGraphic().drawMap(true);
		playerGraphic=player.getGraphic();
		playerGraphic.position=shipLoc;
	}

	public void scroll(int amount) {
		for(Item i:inv.items){
			if(i.moused){
				if(amount==1)inv.bot.code.onPress();
				if(amount==-1)inv.top.code.onPress();
				return;
			}
		}
	}
	
	public void trashItem(Item i){
		inv.removeItem(i);
		player.addFuel(i.getBinValue());
		inv.updateLocations(false);
		selectedItem=null;
	}
	
	class Trasher{
		final Pair buttonLocation= new Pair(location.x+20,570);
		final Pair fuelLocation= new Pair(location.x+100,580);
		final Pair textLocation= new Pair(location.x+160,595);
		
		Module mod;
		int fuel;
		SimpleButton button;
		
		public Trasher(){
			button=new SimpleButton(buttonLocation, "", Gallery.binButton, new SimpleButton.Code() {
				
				@Override
				public void onPress() {
					if(selectedItem!=null) trashItem(selectedItem);
				}
			});
			button.setScale(2);
		
		}
		
		public void render(SpriteBatch batch){
			button.render(batch);
			if(selectedItem!=null){
				batch.setColor(Reward.selectedColor);
				Draw.drawScaled(batch, Gallery.binButton.getOutline(), buttonLocation.x, buttonLocation.y, 2, 2);
				batch.setColor(1,1,1,1);
				Draw.drawScaled(batch, Gallery.fuel.get(), fuelLocation.x, fuelLocation.y,2,2);
				Font.medium.setColor(PlayerStatsPanel.fuelCol);
				Font.medium.draw(batch, ":"+selectedItem.getBinValue(), textLocation.x, textLocation.y);
			}
			
			
			
		}

		public void dispose() {
			button.demousectivate();
			button.deactivate();
		}
	}

	public boolean keyPress(int keycode) {
		switch(keycode){
		
		case Input.Keys.ESCAPE:
			Map.closeBase();
			return true;
		}
		
		return false;
	}

	public void dispose() {
		//stats trasher inv
		for(ModuleStats ms:stats){
			ms.demousectivate();
			ms.deactivate();
		}
		trasher.dispose();
		inv.dispose();
	}
}
