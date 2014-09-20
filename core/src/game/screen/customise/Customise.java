package game.screen.customise;

import java.util.ArrayList;

import game.Main;
import game.assets.Gallery;
import game.card.Card;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.Component;
import game.module.component.shield.Deflector;
import game.module.component.shield.Shield;
import game.module.component.weapon.Pulse;
import game.module.component.weapon.Ray;
import game.module.component.weapon.Tesla;
import game.module.component.weapon.Weapon;
import game.module.junk.ModuleInfo;
import game.module.junk.ModuleStats;
import game.module.utility.FluxAlternator;
import game.module.utility.Utility;
import game.module.utility.armour.Armour;
import game.module.utility.armour.BasicArmour;
import game.module.utility.armour.RegenArmour;
import game.module.utility.armour.ShieldArmour;
import game.screen.battle.interfaceJunk.HelpPanel;
import game.screen.customise.Reward.RewardType;
import game.ship.Ship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.Screen;
import util.update.Timer;
import util.update.Timer.Interp;

public class Customise extends Screen{
	static float fadeInSpeed=0f;
	static float fadeOutSpeed=.3f;
	static Ship ship;
	public ArrayList<ModuleStats> stats=new ArrayList<ModuleStats>();
	public ArrayList<Reward> rewards=new ArrayList<Reward>();
	public ArrayList<Slot> slots=new ArrayList<Slot>();
	ConsumableContainer consumables;

	public static Reward selectedReward;

	ModuleInfo infoBox;
	ModuleInfo oldInfoBox;
	private static Customise me;
	HelpPanel panel;
	HelpPanel oldPanel;
	static float energyX=1050; 
	static float centerEnergy=.8f;
	static float downMultiplier=500f;
	static float upMuliplier=180f;
	static float shipX=(int)(Main.width/2-50);
	Timer meterPosition=new Timer(centerEnergy, centerEnergy, 0, Interp.SQUARE);
	public Customise(Ship s){
		me=this;
		Customise.ship=s;
	}

	@Override
	public void init() {
		resetModuleStats();
		addRewards(1);
		slots.add(new Slot(new Pair(170, 100), 2));
		slots.add(new Slot(new Pair(170, 350), 0));
		slots.add(new Slot(new Pair(170, 500), 1));
		setPanel(PanelType.Choose);
		retimeMeter(ship.getStats().energyUsage);
		consumables=new ConsumableContainer();
	}

	public enum PanelType{Choose, Install, Add, None}; 
	public void setPanel(PanelType pt){
		oldPanel=panel;
		if(oldPanel!=null)oldPanel.fadeOut(.3f, Interp.LINEAR);
		switch(pt){
		case Choose:
			panel=new HelpPanel("Choose a salvaged reward!", (int) shipX, 540);
			break;
		case Install:
			panel=new HelpPanel("Install the module on your ship", (int) shipX, 540);
			break;
		case Add:
			panel=new HelpPanel("Add the cards to your pool", (int) shipX, 540);
			break;
		case None:
			break;
		default:
			break;

		}
	}

	public void resetModuleStats(){
		for(ModuleStats ms:stats)ms.demousectivate();
		stats.clear();
		for(Component c:ship.components){
			stats.add(new ModuleStats(c));
		}
	}

	
	
	public void addRewards(int tier){

		rewards.clear();
		
		Draw.shuffle(Reward.typeList);

		for(int i=0;i<3;i++){
			RewardType type= Reward.typeList[i];
			
			Reward r = null;
			
			switch(type){
			case Armour:
				r=new Reward(Armour.getRandomArmour(1), i);
				break;
			case Booster:
				r=new Reward(new Card[]{new Deflector(3).getNextCard(),
						new Deflector(3).getNextCard(),
						new Deflector(3).getNextCard()},i);
				break;
			case Utility:
				r=new Reward(Utility.getRandomUtility(1), i);
				break;
			case Shield:
				r=new Reward(Shield.getRandomShield(1), i);
				break;
			case Weapon:
				r=new Reward(Weapon.getRandomWeapon(1), i);
				break;
			default:
				break;
			
			
			}
			rewards.add(r);
		}
		/*rewards.add(new Reward(new Pulse(1), 0));
		rewards.add(new Reward(new Tesla(1), 1));
		rewards.add(new Reward(new Card[]{new Deflector(0).getNextCard(),
				new Deflector(0).getNextCard(),
				new Deflector(0).getNextCard()},2));*/
	}

	public static void changeStats(ModuleInfo info){
		if(info==null){

			me.infoBox.fadeOut(fadeOutSpeed, Interp.LINEAR);
			return;
		}

		if(me.infoBox!=null){
			me.oldInfoBox=me.infoBox;
			me.oldInfoBox.fadeOut(fadeOutSpeed, Interp.LINEAR);
		}
		info.fadeIn(fadeInSpeed, Interp.LINEAR);
		info.setPosition(new Pair(shipX,280));
		me.infoBox=info;
	}

	public static void mouseOver(Module mod) {
		changeStats(new ModuleInfo(mod));
	}

	public static void checkEnergy(Module[] remove, Module[]add){
		retimeMeter(ship.calculateSpecialStats(remove, add).energyUsage);

	}

	public static void deselect() {
		Customise.selectedReward=null;
		me.setPanel(PanelType.Choose);
	}

	public static void unMouse(Module module){
		if(me.infoBox.mod==module||module==null){
			me.oldInfoBox=me.infoBox;
			me.oldInfoBox.fadeOut(fadeOutSpeed, Interp.LINEAR);
			retimeMeter(ship.getStats().energyUsage);
		}
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
	}

	@Override
	public void render(SpriteBatch batch) {
		for(ModuleStats ms:stats) ms.render(batch);

		for(Reward r:rewards) r.render(batch);
		for(Slot s:slots) s.render(batch);
		Draw.drawCentered(batch, ship.getGraphic().composite.get(), shipX, 140);

		if(infoBox!=null)infoBox.render(batch);
		if(oldInfoBox!=null)oldInfoBox.render(batch);
		if(panel!=null)panel.render(batch);
		if(oldPanel!=null)oldPanel.render(batch);

		Font.big.setColor(Colours.light);
		Font.drawFontCentered(batch, "Energy Usage", Font.big, energyX, 50);
		Font.medium.setColor(Colours.light);

		Font.drawFontCentered(batch, "Shortage", Font.medium, energyX-135, 95);
		Font.drawFontCentered(batch, "Surplus", Font.medium, energyX+145, 95);
		Draw.drawCenteredScaled(batch, Gallery.energyDial.get(), energyX, 170,4,4);

		Draw.drawCenteredScaled(batch, Gallery.energyMeter.get(), 
				energyX+meterPosition.getFloat(), 
				170, 4, 4);
		consumables.render(batch);

	}

	public static void select(Reward reward) {
		if(selectedReward!=null){
			System.out.println("deselcting");
			//	me.setPanel(PanelType.Choose);
			selectedReward.deselect();
		}
		selectedReward=reward;
		if(consumableSelected()) me.setPanel(PanelType.Add);
		else me.setPanel(PanelType.Install);
	}

	public static void retimeMeter(float to){
		to-=centerEnergy;
		to=Math.min(1.1f, to); // have to set a maximum or it goes off the edge
		boolean bigger=to>0;
		to=to*(bigger?upMuliplier:downMultiplier); // weird thing but it's important 
		me.meterPosition=new Timer(me.meterPosition.getFloat(), to, .5f, Interp.SQUARE);
	}

	public static ModuleType getReplaceableType() {
		if(selectedReward==null)return null;
		if(selectedReward.module==null)return null;
		return selectedReward.module.type;
	}

	public static void replace(Module m){
		if(m instanceof Component){
			Component comp=(Component) m;
			(comp).getStats().demousectivate();
			if(m instanceof Weapon){
				ship.setWeapon((Weapon) selectedReward.module, comp.getIndex());	
			}
			if(m instanceof Shield){
				ship.setShield((Shield) selectedReward.module);
			}
			rewardChosen();
		}
	}

	public static void rewardChosen(){
		if(consumableSelected())me.infoBox.noDrawCards=true;
		unMouse(null);
		ship.recalculateStats();
		retimeMeter(ship.getStats().energyUsage);
		ship.recalculateThresholds();
		me.resetModuleStats();
		ship.getGraphic().drawMap();
		me.chosen();

	}

	public void chosen(){
		for(Reward rw:rewards){
			rw.fadeOut(.3f, Interp.LINEAR);
			rw.demousectivate();
		}
		selectedReward=null;
		setPanel(PanelType.None);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void keyPress(int keycode) {
		addRewards(1);
	}

	@Override
	public void keyUp(int keyCode) {
	}

	@Override
	public void mousePressed(Pair location, boolean left) {
	}

	@Override
	public void scroll(int amount) {
	}

	public static boolean consumableSelected() {
		return selectedReward!=null&&selectedReward.cards!=null;

	}


}
