package game.ship;

import java.util.ArrayList;

import util.Draw;
import util.assets.SoundClip;
import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;
import util.update.TextWisp;
import util.update.TextWisp.WispType;
import util.update.Timer;
import util.update.Timer.Interp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Sounds;
import game.card.Card;
import game.card.CardCode;
import game.card.CardGraphic;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.computer.Computer;
import game.module.generator.Generator;
import game.module.shield.Shield;
import game.module.stuff.ShieldPoint;
import game.module.stuff.Buff.BuffType;
import game.module.utility.Utility;
import game.module.utility.armour.Armour;
import game.module.utility.armour.BasicArmour;
import game.module.weapon.Weapon;
import game.module.weapon.attack.Attack;
import game.screen.battle.Battle;
import game.screen.battle.Battle.Phase;
import game.screen.battle.interfaceJunk.FightStats;
import game.ship.mapThings.MapShip;
import game.ship.mapThings.mapAbility.MapAbility;
import game.ship.niche.Niche;
import game.ship.shipClass.Aurora;
import game.ship.shipClass.Comet;
import game.ship.shipClass.Eclipse;
import game.ship.shipClass.Nova;

public abstract class Ship {
	//0-1 weapon 2 shield 3 gen 4 com
	public static ArrayList<Class<? extends Ship>> classes = new ArrayList<Class<? extends Ship>>();
	public Niche[] niches= new Niche[5]; 
	public Module[] modules= new Module[5];
	private ShipGraphic battleGraphic;
	public Pic shipPic;
	//Deck is stored as a list of modules so you can generate nice and easily!!//
	public ArrayList<Module> deck = new ArrayList<Module>();
	public ArrayList<Card> hand = new ArrayList<Card>();
	public ArrayList<Card> playList= new ArrayList<Card>();
	public ArrayList<ShieldPoint> shieldPoints= new ArrayList<ShieldPoint>();
	public boolean player;
	//int energyIncome;
	private int currentEnergy;
	int maxCards;
	private int majorDamageTaken=0;
	private int energyAtEndOfPhase=0;
	private float powerLevel=0;
	
	//private 
	private Armour armour= new BasicArmour(1);
	private ArrayList<Utility> utilities=new ArrayList<Utility>();

	//Enemy ai stuff//
	public Module focusTarget;
	public FightStats fightStats;

	//Map stuff//
	private MapShip mapShip;

	public boolean dead=false;
	public boolean exploded=false;



	public Timer timer = new Timer();
	public enum ShipType{Aurora,Comet,Eclipse,Nova}
	public abstract void placeNiches();
	public Ship(boolean player, Pic shipPic, Pic genPic, Pic comPic){
		this.player=player;
		this.shipPic=shipPic;
		setupNiches();
		placeNiches();
		getGenerator().modulePic=genPic;
		getComputer().modulePic=comPic;
		recalculateThresholds();
	}


	//Phase stuff//
	public void startTurn() {
		for(Weapon w:getWeapons()){
			w.attacks.clear();
		}	
		if(player)drawToMaximum();
		currentEnergy+=getGenerator().getIncome();
		
		
		for(Utility u:utilities)u.beginTurnEffect();
		
	}

	public void enemyStartTurn(){	
		startTurn();
	}

	public void playerStartTurn(){
		startTurn();
	}

	public void enemyStartPhase(){
		if(Battle.getPhase()==Phase.End)return;
		if(Battle.getPhase()==Phase.EnemyShieldPhase)enemyStartTurn();
		enemyPickAllCards(Battle.getPhase());
	}

	private void endTurn(){
		focusTarget=null;
		for(Module m:modules)m.endAdmin();

		shieldPoints.clear();
	}

	public void checkDefeat(){
		if(majorDamageTaken>=5&&!dead){
			Battle.battleWon(getEnemy());
		}
	}

	public void playCards() {
		for(Card c:playList){
			hand.remove(c);
			if(player)c.playerPlay();

		}
		playList.clear();
		if(player)updateCardPositions();
	}

	public void notifyIncoming(){
		for(Weapon w:getWeapons()){
			w.notifyIncoming();
		}
	}

	public void fireAll() {
		for(Weapon w:getWeapons()){
			for(Attack atk:w.attacks){
				atk.fire();
			}
		}
	}

	public boolean finishedAttacking() {
		for(Weapon w:getWeapons()){
			if(!w.checkFinished())return false;
		}
		return true;
	}

	public void endPhase() {	

		addEnergy(energyAtEndOfPhase);
		energyAtEndOfPhase=0;
		switch (Battle.getPhase()){
		case WeaponPhase:
			Battle.setPhase(Phase.EnemyShieldPhase);
			break;
		case EnemyShieldPhase:
			Battle.setPhase(Phase.PlayerWeaponsFiring);
			break;
		case PlayerWeaponsFiring:
			Battle.setPhase(Phase.EnemyWeaponPhase);
			break;
		case EnemyWeaponPhase:
			Battle.setPhase(Phase.ShieldPhase);
			break;
		case ShieldPhase:
			Battle.setPhase(Phase.EnemyWeaponsFiring);
			break;
		case EnemyWeaponsFiring:
			Battle.setPhase(Phase.WeaponPhase);
			break;



		default:
			break;
		}
	}

	public void playerEndTurn(){
		for(Module m:getEnemy().modules){
			m.targeteds=0;
		}
		endTurn();
	}

	public void enemyEndTurn(){
		drawToMaximum();
		checkTooManyShields();
		endTurn();
	}

	//This is to stop abusing the enemy by only playing unshieldable//
	private void checkTooManyShields() {
		int shields=0;
		for(Card c:hand){
			if(c.mod.type==ModuleType.SHIELD)shields++;
		}
		if(shields>=hand.size()-1){
			discardHand();
			drawToMaximum();
		}

	}

	public void discardHand(){
		while(hand.size()>0)discard(hand.get(0));
	}

	//Enemy AI stuff//



	public void enemyPickAllCards(Phase p){


		Card c=pickCard(p);
		int i=0;
		while(c!=null){
			c.enemySelectAndPlay();
			if(c.getCode().contains(AI.RegularShield)){
				enemySpendShields(true);
				enemySpendShields(false);
			}
			float gap=20;
			c.getGraphic().slide(new Pair(
					975-(i/2)*(gap+CardGraphic.width),
					80+(i%2)*(CardGraphic.height/2+gap)),
					.67f, 
					Interp.SQUARE);

			c=pickCard(p);
			i++;
		}
		if(i==0){
			endPhase();
		}
		notifyIncoming();

		timer=new Timer(1, 0, 1/10f, Interp.LINEAR);

	}


	public void enemyFadeAll(){
		if(timer.getFloat()>0)return;
		for(Card c:playList) c.fadeAndAddIcon();
		playList.clear();
		endPhase();
	}

	public Card pickCard(Phase p){
		System.out.println();
		//Checks to see if card is valid to play//
		boolean shield=true;
		boolean weapon=true;
		if(p==Phase.EnemyShieldPhase||p==Phase.ShieldPhase) weapon=false;
		if(p==Phase.EnemyWeaponPhase||p==Phase.WeaponPhase) shield=false;
		//First play any scrambled cards//
		for(Card c:hand){
			if(c.mod.getBuffAmount(BuffType.Scrambled)>0){
				return c;
			}
		}

		//Check the cards in order and pass to the enemyDecide method//


		//First check the alternate side//
		Card c;
		for(int pri=2;pri>=-1;pri--){
			System.out.println("Priority: "+pri);
			c=decideBest(pri, weapon, shield, false);
			if(c!=null){
				return c;
			}
		}


		return null;
	}

	private Card decideBest(int priority, boolean weapon, boolean shield, boolean targetedOnly){

		//if priority == 0, check targeted then normal//
		if(priority==0&&targetedOnly==false){
			System.out.println("checking targeted");
			Card c=decideBest(0, weapon, shield, true);
			if(c!=null)return c;
			System.out.println("checking others");
		}

		for(Card c:hand){
			if(c.selected)continue; //Probably just for debugging//
			if(c.mod.type==ModuleType.WEAPON&&!weapon) continue;
			if(c.mod.type==ModuleType.SHIELD&&!shield) continue;


			for(int side=1;side>=0;side--){
				CardCode code= c.getCode(side);

				//cancelling due to targeted//
				if(targetedOnly&&!code.contains(Special.Targeted))break;


				if(code.getPriority()==priority){
					if(c.enemyDecide(side)) return c;
					System.out.println();	
				}

				if(focusTarget!=null&&code.contains(Special.Targeted)){
					System.out.println("overriding priority due to focus");
					if(c.enemyDecide(side)) return c;
					System.out.println();
				}



			}
		}
		return null;
	}

	public void enemySpendShields(boolean priority){
		if(shieldPoints.size()==0)return;



		for(Module m:getRandomisedModules()){
			if(shieldPoints.size()==0)return;
			//Need to put in reasons not to shield//
			if(shieldPoints.get(0).card.getCode().contains(Special.ShieldOnlyDamaged)){
				System.out.println("can't use shield becaus not major damaged");
				if(m.currentThreshold==0)continue;
			}

			//Priority checking//
			if(priority){
				m.getShieldsRequiredToAvoidMajor();
				while(m.getShieldsRequiredToAvoidMajor()<=shieldPoints.size()){ //not a hopeless cause// //Only shield until it's barely surviving//
					System.out.println("found priority shield");
					System.out.println("Going to shield "+m+" because requires "+m.getShieldsRequiredToAvoidMajor()+" and I have "+shieldPoints.size());
					m.shield(shieldPoints.remove(0),false);
				}
				continue;
			}

			//Non-priority//
			while(m.getShieldableIncoming()>0&&shieldPoints.size()>0){
				m.shield(shieldPoints.remove(0),false);
			}

		}
	}

	//Shielding stuff//

	public void addShield(Card card, int effect) {
		for(int i=0;i<effect;i++)shieldPoints.add(new ShieldPoint(card,i==0));
	}

	public void shieldAll(Card card, int effect) {
		for(Module m:modules){
			for(int i=0;i<effect;i++)m.shield(new ShieldPoint(card,i==0),false);
		}
	}

	public void unShield(Card card) {
		for(Module m:modules){
			m.unshield(card);
		}

		for(int i=0;i<shieldPoints.size();i++){
			ShieldPoint sp = shieldPoints.get(i);
			if(sp.card==card){
				shieldPoints.remove(sp);
				i--;
			}
		}
	}

	//Card drawing stuff//

	public void drawToMaximum(){
		if(Battle.isTutorial())return;
		drawCard(getComputer().getMaximumHandSize()-hand.size());
	}

	public void drawCard(int number){
		if(number<=0)return;
		for(int i=0;i<number;i++){
			if(deck.size()==0) makeDeck();
			drawCard(deck.remove(0).getNextCard());
		}
	}

	public void drawCard(Card card) {
		hand.add(card);
		card.getGraphic().mousectivate(null);
		if(player)updateCardPositions();
	}

	public void discard(Card card) {
		hand.remove(card);
		if(player){
			card.getGraphic().fadeOut(CardGraphic.fadeSpeed, CardGraphic.fadeType);
			updateCardPositions();
		}
	}

	public void updateCardPositions(){
		if(!player)return;
		float start=130;
		float width=Main.width-(start*2)+CardGraphic.width;
		float gap=width/(hand.size()+1);
		if(hand.size()<=7){
			for(int i=0;i<hand.size();i++){
				CardGraphic c=hand.get(i).getGraphic();
				c.slide(new Pair(start+gap*(i+1)-CardGraphic.width, Main.height-CardGraphic.height), .5f, Interp.SQUARE);
				c.finishFlipping();
			}
		}
		else{
			width=Main.width-(start*2)-CardGraphic.width;
			gap=width/(hand.size()-1);
			for(int i=0;i<hand.size();i++){
				CardGraphic c=hand.get(i).getGraphic();
				c.slide(new Pair(start+gap*i, Main.height-CardGraphic.height), .5f, Interp.SQUARE);
				c.finishFlipping();
			}
		}
	}

	public void cardOrIconMoused(Card card) {
		for(Module m:modules){
			m.cardIconMoused(card);
		}
		for(Module m:getEnemy().modules){
			m.cardIconMoused(card);
		}
	}

	public void cardOrIconUnmoused() {
		for(Module m:modules){
			m.cardIconUnmoused();
		}
		for(Module m:getEnemy().modules){
			m.cardIconUnmoused();
		}
	}

	//Setup junk//

	public static void init(){
		classes.add(Aurora.class);
		classes.add(Nova.class);
		classes.add(Comet.class);
		classes.add(Eclipse.class);
	}

	public void makeDeck(){
		for(Module m:modules){
			if(m.destroyed)continue;
			for(int i=0;i<m.numCards;i++)deck.add(m);
		}
		Draw.shuffle(deck);
	}

	public void startFight(boolean goingFirst){
		initModuleStats();
		initFightStats();
		if(!Battle.isTutorial())		drawCard(maxCards); //No leak here!//

		if(!goingFirst){
			currentEnergy=(int) Math.ceil(getGenerator().getIncome()/2f);
		}
		else currentEnergy=getGenerator().getIncome();
		for(Utility u:utilities)u.startBattleEffect();
	}

	private void initFightStats() {
		fightStats=new FightStats(this);
	}

	private void initModuleStats(){
		for(Module m:modules){
			m.getStats();
		}
	}

	public void setupNiches(){
		niches[0]=new Niche(this,ModuleType.WEAPON);
		niches[1]=new Niche(this,ModuleType.WEAPON);
		niches[2]=new Niche(this,ModuleType.SHIELD);
		niches[3]=new Niche(this,ModuleType.GENERATOR);
		niches[4]=new Niche(this,ModuleType.COMPUTER);
	}

	//Rendering junk//



	public void renderFightStats(SpriteBatch batch){
		fightStats.render(batch);
	}

	public void renderShip(SpriteBatch batch){
		getGraphic().render(batch);
	}


	//Setters and getters//
	public void addToEnergyAtEndOfPhase(int amount){energyAtEndOfPhase+=amount;}
	public void majorDamage() {
		Battle.shake(player,3);
		Sounds.damageMajor.play();
		majorDamageTaken++;
	}
	public int getMajorDamage(){
		return majorDamageTaken;
	}
	public int getTotalShieldableIncoming(){
		int total=0;
		for(Module m:modules){
			total+=m.getShieldableIncoming();
		}
		return total;
	}
	public Module getRandomUndestroyedModule(){
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for(int i=0;i<5;i++){
			ints.add(i);
		}
		Draw.shuffle(ints);
		for(int i:ints){
			if(modules[i].currentThreshold<3){
				return modules[i];
			}
		}
		System.out.println("all mods destroyed?");
		return null;
	}
	
	
	
	/*public static Ship getRandomShip(boolean player){
		Class<? extends Ship> ship= classes.get((int)(Math.random()*classes.size()));
		Ship s=null;
		try {
			s=(Ship) ship.getConstructors()[0].newInstance(player);
		} catch(Exception e)  {
			System.out.println("Broke when trying to make ship");
		}
		return s;
	}*/
	public Ship getEnemy(){
		if(player)return Battle.getEnemy();
		return Battle.getPlayer();
	}
	public void addEnergy(int amount){currentEnergy+=amount;}
	public int getEnergy(){return currentEnergy;}
	public Weapon[] getWeapons(){return new Weapon[]{(Weapon) modules[0],(Weapon) modules[1]};}
	public ArrayList<Module> getRandomisedModules(){
		ArrayList<Module> result=new ArrayList<Module>();
		for(Module m:modules){
			if(m.currentThreshold<2)result.add(m);
		}
		Draw.shuffle(result);
		return result;
	}
	public Module getModule(int index){return modules[index];}
	public Module getShield(){return modules[2];}
	public Generator getGenerator(){return (Generator) modules[3];}
	public Computer getComputer(){return (Computer) modules[4];}
	public ShipGraphic getGraphic(){
		if(battleGraphic==null)battleGraphic=new ShipGraphic(this);
		return battleGraphic;
	}
	public MapShip getMapShip(){
		return mapShip;
	}
	public void setWeapon(Weapon w, int i){
		if(i!=0&&i!=1){
			System.out.println("bad set weapon index");
			return;
		}
		niches[i].install(w);
		modules[i]=w;
	}
	public void setShield(Shield s){
		niches[2].install(s);
	}
	public void setGenerator(Generator g){
		niches[3].install(g);
	}
	public void setComputer(Computer c){
		maxCards=c.maxCards;
		niches[4].install(c);
	}
	public void setArmour(Armour a){
		utilities.remove(this.armour);
		this.armour=a;
		a.ship=this;
		utilities.add(this.armour);
		recalculateThresholds();
	}
	
	public void setUtility(Utility u){
		if(u instanceof Armour) System.out.println("Watch out, setting armour as utility wuhohh!");
		utilities.add(u);
		u.ship=this;
	}
	
	public Pic getPic(){
		return shipPic;
	}
	public int getIncome(){
		return getGenerator().getIncome();
	}
	public String toString(){
		return "Ship, player:"+player;
	}

	public void addIncome(int amount) {
		getGenerator().addIncome(amount);
	}

	public boolean hasSpendableShields() {
		if(shieldPoints.size()>0){
			for(Module m:modules){
				if(m.getShieldableIncoming()>0){
					new TextWisp("Spend all your shields first!", Font.medium, new Pair(Main.width/2,300), WispType.Regular);
					return true;
				}
			}
		}
		return false;
	}

	public float getArmourMultiplier(){
		return armour.getMultuplier();
	}
	
	public int getTotalDeckSize(){
		int result=0;

		for(Module m:modules){
			result+=m.numCards;
		}

		return result;
	}
	
	private void recalculateThresholds() {
	 for(Module m:modules)m.recalculateThresholds();	
	}
	
	public float getPowerLevel(){
		if(powerLevel!=0)return powerLevel;
		deck.clear();
		makeDeck();
		float totalEffect=0;
		float totalCost=0;
		float deckSize=deck.size();
		for(Module m:deck){
			Card c=m.getNextCard();
			CardCode code=c.getCode();

			totalEffect+=c.getEffect();
			totalCost+=c.getCost();

			deckSize-=code.getAmount(Augment.AugmentDrawCard);
			deckSize-=code.getAmount(Special.DrawCard);
			totalCost-=code.getAmount(Special.GainEnergy);
		}

		float averageCardCost=totalCost/deckSize;
		float averageCardEffect=totalEffect/deckSize;

		float handCost=averageCardCost*maxCards;
		float handEffect=averageCardEffect*maxCards;

		float ratio=getGenerator().getIncome()/handCost;

		float handPower=handEffect*ratio;
		boolean debug=false;
		if(debug){
			System.out.println("Power levels for "+this);
			System.out.println("Total effect: "+totalEffect);
			System.out.println("Total cost: "+totalCost);
			System.out.println("Deck size: "+deckSize);
			System.out.println("Average Card cost: "+averageCardCost);
			System.out.println("Average Card effect: "+averageCardEffect);
			System.out.println("Hand cost: "+handCost);
			System.out.println("Hand effect: "+handEffect);
			System.out.println("Proportion of hand played: "+ratio);
			System.out.println("Final power level: "+handPower);
		}

		powerLevel=handPower;
		return powerLevel;
	}



	public abstract ArrayList<MapAbility> getMapAbilities();

	public void setMapShip(MapShip mapShip) {
		this.mapShip=mapShip;
	}
	public void clearShields() {
		for(Module m:modules)m.clearShields();
	}





}
