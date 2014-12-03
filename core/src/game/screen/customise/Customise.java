package game.screen.customise;

import java.util.ArrayList;

import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.card.Card;
import game.card.CardCode;
import game.card.ConsumableCard;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.Component;
import game.module.component.shield.Shield;
import game.module.component.weapon.Weapon;
import game.module.junk.ModuleInfo;
import game.module.junk.ModuleStats;
import game.module.utility.Utility;
import game.module.utility.armour.Armour;
import game.screen.battle.interfaceJunk.HelpPanel;
import game.screen.customise.Reward.RewardType;
import game.screen.map.Map;
import game.screen.preBattle.PreBattle;
import game.ship.Ship;
import game.ship.shipClass.Aurora;
import game.ship.shipClass.Comet;
import game.ship.shipClass.Eclipse;
import game.ship.shipClass.Hornet;
import game.ship.shipClass.Nova;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.Mouser;
import util.update.Screen;
import util.update.SimpleButton;
import util.update.Timer.Finisher;
import util.update.Updater;
import util.update.SimpleButton.*;
import util.update.Timer;
import util.update.Timer.Interp;

public class Customise extends Screen{
	public static float fadeInSpeed=0f;
	static float fadeOutSpeed=.3f;
	public static Ship ship;
	public ArrayList<ModuleStats> stats=new ArrayList<ModuleStats>();
	public ArrayList<Reward> rewards=new ArrayList<Reward>();
	
	ConsumableContainer consumables;

	public static Reward selectedReward;

	ModuleInfo infoBox;
	ModuleInfo oldInfoBox;
	private static Customise me;
	HelpPanel panel;
	HelpPanel oldPanel;
	static int panelY=560;
	static float energyX=1050; 
	static float centerEnergy=.8f;
	static float downMultiplier=500f;
	static float upMuliplier=180f;
	static float shipX=(int)(Main.width/2-50);
	Timer meterPosition=new Timer(centerEnergy, centerEnergy, 0, Interp.SQUARE);
	boolean first;
	ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();
	int shipNumber=2;
	public static float power=0;
	public static int totalShipsDefeated=0;
	public static boolean repairing=false;
	public boolean map;
	public Customise(Ship s, boolean first, boolean map){
		this.map=map;
		me=this;
		this.first=first;

		if(first){
			Customise.ship=Ship.makeIntegerShip(true, 0, shipNumber);
		}
		else Customise.ship=s;
		Customise.ship.cleanupAfterFight();
		Customise.ship.resetGraphics();
	}



	@Override
	public void init() {
		resetModuleStats();
		if(!first){
			totalShipsDefeated++;
			addRewards((int)(power/3)+1,false);
		}
		if(first){
			totalShipsDefeated=0;
			setPanel(PanelType.PickShip);
			buttons.add(new SimpleButton(new Pair(shipX-250, Main.height-108), "", Gallery.leftButton, new Code() {

				@Override
				public void onPress() {
					Sounds.cardDeselect.overlay();
					ship.dispose();
					shipNumber++;
					shipNumber=shipNumber%Ship.classes.length;
					ship=Ship.makeIntegerShip(true, 0, shipNumber);
					resetModuleStats();

				}
			}));
			buttons.add(new SimpleButton(new Pair(shipX-170, Main.height-108), "", Gallery.leftButton.getFlipped(true), new Code() {

				@Override
				public void onPress() {
					
					Sounds.cardSelect.overlay();
					ship.dispose();
					shipNumber--;
					shipNumber+=Ship.classes.length;
					shipNumber=shipNumber%Ship.classes.length;
					ship=Ship.makeIntegerShip(true, 0, shipNumber);
					resetModuleStats();
				}
			}));

			buttons.add(new SimpleButton(new Pair(shipX+120, Main.height-108), "", Gallery.tickButton, new Code() {

				@Override
				public void onPress() {
					if(map){
						System.out.println(ship);
						Main.changeScreen(new Map(ship));
						return;
					}
					Sounds.shieldUse.overlay();
					first=false;
					setPanel(PanelType.Choose);
					
					for(SimpleButton butt:buttons){
						butt.demousectivate();
						butt.fadeOut(.2f, Interp.LINEAR);
					}
					addRewards(0, true);
				}

				
			}));

			for(SimpleButton butt:buttons)butt.setScale(4);
		}


		retimeMeter(ship.getStats().energyUsage);

		consumables=new ConsumableContainer();
		
		ship.resetGraphics();
	}
	
	public static Ship makeEnemyShip() {
		Ship result =Ship.makeRandomShip(false, power);
		
		if(result instanceof Eclipse&&(power<.5f||ship instanceof Aurora)){
			return makeEnemyShip();
		}
		if(result instanceof Aurora&&ship instanceof Eclipse){
			return makeEnemyShip();
		}
		if(result.getClass()==ship.getClass()&&power<1){
			return makeEnemyShip();
		}
		
		power+=.7f;
		return result;
		
	}

	public enum PanelType{Choose, Install, Add, None, PickShip}; 
	public void setPanel(PanelType pt){
		oldPanel=panel;
		if(oldPanel!=null)oldPanel.fadeOut(.3f, Interp.LINEAR);
		switch(pt){
		case PickShip:
			panel=new HelpPanel("Pick your ship", (int) shipX, panelY);
			break;
		case Choose:
			panel=new HelpPanel("Choose a salvaged reward!", (int) shipX, panelY);
			break;
		case Install:
			panel=new HelpPanel("Install the module on your ship", (int) shipX, panelY);
			break;
		case Add:
			panel=new HelpPanel("Add the cards to your pool", (int) shipX, panelY);
			break;
		case None:
			break;
		default:
			break;

		}
	}

	public void clearStats(){

	}

	public void resetModuleStats(){
		stats.clear();
		
		for(Component c:ship.components){
			stats.add(c.getStats());
		}
		ship.resetGraphics();

		retimeMeter(ship.getStats().energyUsage);

	}

	public void addDeclineButton(){
		final SimpleButton b = new SimpleButton(new Pair(250,Main.height-Reward.height-8), "", Gallery.crossButton, new Code() {
			@Override
			public void onPress() {
				rewardChosen();
				Sounds.cardDeselect.play();
			}
		});
		b.setScale(4);
		buttons.add(b);
	}

	public void addRewards(int tier, boolean start){

		rewards.clear();
		tier=Math.min(2, tier);
		Draw.shuffle(Reward.typeList);
		int bonus=0;
		for(int i=0;i<3;i++){
			
			RewardType type= Reward.typeList[(i+bonus)%6];

			Reward reward = null;
			boolean cancel=false;
			if(start&&i==0)type=RewardType.Utility;
			if(start&&i==1)type=RewardType.Booster;
			if(start&&i==2)type=RewardType.Utility;
			switch(type){
			case Armour:
				reward=new Reward(Armour.getRandomArmour(tier), i);
				break;
			case Booster:
				if(ship.getConsumables().size()>3){
					System.out.println("canceling booster");
					cancel=true;
				}
				reward=new Reward(new Card[]{ConsumableCard.get(tier),ConsumableCard.get(tier),ConsumableCard.get(tier)},i);
				break;
			case Utility:
				reward=new Reward(Utility.getRandomUtility(tier), i);
				while(ship.containsModule(reward.module, true)){
					reward=new Reward(Utility.getRandomUtility(tier), i);
				}
				break;
			case Shield:
				reward=new Reward(Shield.getRandomShield(tier), i);
				while(ship.containsModule(reward.module, false)){
					reward=new Reward(Shield.getRandomShield(tier), i);
				}
				break;
			case Weapon:
				reward=new Reward(Weapon.getRandomWeapon(tier), i);
				while(ship.containsModule(reward.module, false)){
					reward=new Reward(Weapon.getRandomWeapon(tier), i);
				}
				break;
			default:
				break;
			}

			
			for(Reward rew:rewards){
				if(rew!=null && rew.module!=null&& reward.module!=null && rew!=reward && rew.module.getClass()==reward.module.getClass()){
					cancel=true;
				}
			}
			
			if(cancel){
				i--;
				bonus+=1;
				continue;
			}

			rewards.add(reward);
			reward.confirm();
			bonus=0;
			setPanel(PanelType.Choose);
		}
		addDeclineButton();
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
		if(me.infoBox==null)return;
		if(me.infoBox.mod==module){
			me.infoBox.fadeOut(fadeOutSpeed, Interp.LINEAR);
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
		for(ModuleStats ms:ship.getUtilityStats())ms.render(batch);
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
		

		if(first){
			Font.drawFontCentered(batch, ship.shipName, Font.big, shipX, Main.height-80);
			String s="Unset";
			if(ship instanceof Aurora)s="Very hard";
			if(ship instanceof Nova)s="Hard";
			if(ship instanceof Hornet)s="Medium";
			if(ship instanceof Comet)s="Easy";
			if(ship instanceof Eclipse)s="Very Easy";
			Font.drawFontCentered(batch, s, Font.medium, shipX, Main.height-40);
		}

		batch.setColor(1,1,1,1);
		for(SimpleButton butt:buttons){
			butt.render(batch);
		}
		
		
		Font.drawFontCentered(batch, "Ships defeated: "+totalShipsDefeated, Font.big, ConsumableContainer.position.x+ConsumableContainer.width/2, 280);
		consumables.render(batch);
	}

	public static void select(Reward reward) {
		if(selectedReward!=null){
			selectedReward.deselect(false);
		}
		selectedReward=reward;
		if(consumableSelected()) me.setPanel(PanelType.Add);
		else me.setPanel(PanelType.Install);
	}

	public static void retimeMeter(float to){
		to-=centerEnergy;
		to=Math.min(1.1f, to); // have to set a maximum or it goes off the edge
		boolean bigger=to>0;
		to=to*(bigger?upMuliplier:downMultiplier); // weird thing but it looks nicer like this
		to=Math.max(-180, to); // set a minimum
		me.meterPosition=new Timer(me.meterPosition.getFloat(), to, .5f, Interp.SQUARE);
	}

	public static ModuleType getReplaceableType() {
		if(selectedReward==null)return null;
		if(selectedReward.module==null)return null;
		return selectedReward.module.type;
	}

	public static void replace(Module m, int utilIndex){
		if(m instanceof Component){
			Component comp=(Component) m;
			(comp).getStats().demousectivate();
			if(m instanceof Weapon){
				ship.setWeapon((Weapon) selectedReward.module, comp.getIndex());	
			}
			if(m instanceof Shield){
				ship.setShield((Shield) selectedReward.module);
			}
			
			
			
		}
		if(selectedReward.module instanceof Utility){
			ship.setUtility((Utility) selectedReward.module, utilIndex);
		}
		rewardChosen();
	}

	public static void rewardChosen(){
		if(me.infoBox!=null)me.infoBox.noDrawCards=true;
		if(me.oldInfoBox!=null)me.oldInfoBox.noDrawCards=true;
		unMouse(null);
		ship.recalculateStats();
		retimeMeter(ship.getStats().energyUsage);
		ship.recalculateThresholds();
		me.resetModuleStats();
		ship.getGraphic().drawMap(true);
		me.chosen();
		Timer t= new Timer(0,1,1,Interp.LINEAR);
		t.addFinisher(new Finisher() {
			
			@Override
			public void finish() {
				Main.changeScreen(new PreBattle(ship, makeEnemyShip()),.5f);
			}
		});
	}

	public void chosen(){
		for(Reward rw:rewards){
			rw.fadeOut(.3f, Interp.LINEAR);
			rw.demousectivate();
		}
		for(SimpleButton butt:buttons){
			butt.fadeOut(.3f, Interp.LINEAR);
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
