package game.ship;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;
import util.particleSystem.ParticleSystem;
import util.update.TextWisp;
import util.update.TextWisp.WispType;
import util.update.Timer;
import util.update.Timer.Interp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Sounds;
import game.attack.Attack;
import game.card.Card;
import game.card.CardCode;
import game.card.CardGraphic;
import game.card.HoverCard;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.card.ConsumableCard;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.Component;
import game.module.component.SpecialComponent;
import game.module.component.computer.Computer;
import game.module.component.generator.Generator;
import game.module.component.shield.Shield;
import game.module.component.weapon.Weapon;
import game.module.junk.ModuleStats;
import game.module.junk.ShieldPoint;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;
import game.module.utility.Utility;
import game.module.utility.armour.Armour;
import game.module.utility.armour.Plating;
import game.screen.battle.Battle;
import game.screen.battle.Battle.Phase;
import game.screen.battle.interfaceJunk.FightStats;
import game.ship.mapThings.MapShip;
import game.ship.mapThings.mapAbility.MapAbility;
import game.ship.niche.Niche;
import game.ship.shipClass.Aurora;
import game.ship.shipClass.Comet;
import game.ship.shipClass.Eclipse;
import game.ship.shipClass.Hornet;
import game.ship.shipClass.Nova;

public abstract class Ship {
	public static Class[] classes= new Class[]{Aurora.class, Nova.class, Hornet.class, Comet.class, Eclipse.class};
	private static ArrayList<Integer> shipGenPool = new ArrayList<Integer>();
	public boolean player;
	public Pic shipPic;

	private int turnsTaken;


	//Battle stuff//
	//0-1 weapon 2 shield 3 gen 4 com
	public Niche[] niches= new Niche[5]; 
	public Component[] components= new Component[5];
	private Armour armour;
	private Utility[] utilities=new Utility[3];

	public ArrayList<Card> deck = new ArrayList<Card>();
	public ArrayList<Card> hand = new ArrayList<Card>();
	public ArrayList<Card> playList= new ArrayList<Card>();
	private ArrayList<Attack> attacks=new ArrayList<Attack>();
	public ArrayList<ShieldPoint> shieldPoints= new ArrayList<ShieldPoint>();
	private ArrayList<Card> consumableStore= new ArrayList<Card>();

	public FightStats fightStats;
	private ShipStats shipStats;
	private ShipGraphic battleGraphic;
	private ModuleStats[] utilityStats= new ModuleStats[3];
	
	private int currentEnergy;
	public boolean dead=false;
	public boolean exploded=false;
	private int majorDamageTaken=0;
	private int energyAtEndOfPhase=0;
	int maxCards;
	public boolean doubleHP;
	private Component specialComponent; //this is a bit shit...//

	//Enemy ai stuff//
	public Component focusTarget;
	public Timer turnTimer = new Timer();

	//Map stuff//
	private MapShip mapShip;

	public enum ShipType{Aurora,Comet,Eclipse,Nova}
	public abstract void placeNiches();
	float tier;
	public String shipName;
	public Ship(boolean player, float tier, String shipName, Pic shipPic, Pic genPic, Pic comPic){
		this.shipName=shipName;
		this.tier=Math.min(12f, tier);
		this.player=player;
		this.shipPic=shipPic;
		
		setupNiches();
		placeNiches();


		setArmour(new Plating(0));
		//if(!player)setUtility(new Exploiter(0), 1);

		setupTiers();
		getGenerator().modulePic=genPic;
		getComputer().modulePic=comPic;


		specialComponent= new SpecialComponent();
		specialComponent.ship=this;

		for(int i=0;i<0;i++){
			addConsumableCard(ConsumableCard.get(1));
		}
	}


	private void setupTiers() {

		int baseTier=(int) (tier/3);


		int shieldTier=baseTier;
		int[] weaponTier = new int[]{baseTier,baseTier};
		int[] utilityTier= new int[]{-1,-1};



		float bigRemainder=(int) (tier%3);
		float smallRemainder=tier%1;

		if(bigRemainder>=1){
			weaponTier[(int)(Math.random()*2)]++;
		}
		if(bigRemainder>=2){
			shieldTier++;
		}
		if(smallRemainder>.3){
			utilityTier[0]=baseTier+1;
		}
		if(smallRemainder>.6){
			utilityTier[1]=baseTier+1;
		}
		/*System.out.println("setting up tiers "+tier);
		System.out.println("basetier: "+baseTier );
		System.out.println("shiledtier " +shieldTier);
		System.out.println("weapontiers");
		for(int i:weaponTier)System.out.println(i);
		System.out.println("utility tiers");
		for(int i:utilityTier)System.out.println(i);

		System.out.println("-------------------");*/




		try {

			setShield((Shield) getShield().getClass().getConstructor(int.class).newInstance(shieldTier));
			setWeapon(getWeapons()[0].getClass().getConstructor(int.class).newInstance(weaponTier[0]), 0);
			setWeapon(getWeapons()[1].getClass().getConstructor(int.class).newInstance(weaponTier[1]), 1);
			for(int i=0;i<2;i++){
				if(utilityTier[i]>-1){
					setUtility(Utility.getRandomUtility(utilityTier[i]), i);
				}
			}
			if(baseTier>0){
				setArmour(Armour.getRandomArmour(baseTier));
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		recalculateThresholds();
	}


	//Phase stuff//
	public void startTurn () {
		attacks.clear();
		if(player)drawToMaximum();
		addEnergy(getIncome(), true);
		resetMaximumHandSize();
		for(Utility u:utilities) if(u!=null) u.beginTurnEffect();
	}

	private void resetMaximumHandSize() {
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
		for(Component c:components)c.endAdmin();
		shieldPoints.clear();
		for(Utility u:utilities){
			if(u!=null)u.endTurnEffect();
		}
		playList.clear();
		turnsTaken++;
	}

	public void checkDefeat(){
		if(majorDamageTaken>=5&&!dead&&!overrideDefeat()){
			Battle.battleWon(getEnemy());
		}
	}

	private boolean overrideDefeat() {
		for(Utility u:utilities) if(u!=null) if(u.overrideDefeat())return true;
		return false;
	}

	public void playCards() {
		lockAttackEffects();
		for(Card c:playList){
			hand.remove(c);
			if(player)c.playerPlay();
			else c.play();

		}
		if(player)updateCardPositions();
	}

	public void lockAttackEffects(){
		for(Attack a:attacks)a.lockEffect();
	}

	public void notifyIncoming(){
		for(Attack a:attacks){
			a.activateIncoming();
		}
	}

	public void addAttack(Card card){addAttack(new Attack(card));}
	public void addAttack(Card card, Component target){addAttack(new Attack(card,target));}

	private void addAttack(Attack a){

		a.atkgrphc.order=attacks.size();
		System.out.println(attacks.size());

		attacks.add(a);
		ParticleSystem.systems.add(a.atkgrphc);
		updateIntensities();

	}

	public void removeAttack(Card card){

		for(int i=0;i<attacks.size();i++){

			Attack a=attacks.get(i);
			if(a.card==card){
				if(a.target!=null)a.target.targeteds--;
				attacks.remove(i);
				a.disable();
				i--;

			}
		}
		updateIntensities();
	}

	public ArrayList<Attack> getAttacks(){return attacks;}

	public void fireAll() {

		for(Attack atk:attacks){
			atk.fire();
		}

	}

	public void updateIntensities(){
		for(Weapon w:getWeapons())w.updateIntensity();
		specialComponent.updateIntensity();
	}



	public boolean finishedAttacking() {

		for (int i=0;i<attacks.size();i++){
			Attack a=attacks.get(i);
			if(a.atkgrphc.finishedAttacking()){
				attacks.remove(i);
				i--;
			}
		}
		return attacks.size()==0;

	}


	public void endPhase() {	

		if(player) playCards();
		addEnergy(energyAtEndOfPhase, true);
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
		for(Component c:getEnemy().components){
			c.targeteds=0;
		}
		endTurn();
	}

	public void enemyEndTurn(){

		checkTooManyShields();
		endTurn();
		drawToMaximum();
	}

	//This is to stop abusing the enemy by only playing unshieldable//
	private void checkTooManyShields() {
		for(Card c:hand){
			if (c.type==ModuleType.SHIELD){
				discard(c);
				return;
			}
		}

	}

	public void discardHand(){
		while(hand.size()>0)discard(hand.get(0));
	}

	//Enemy AI stuff//



	public void enemyPickAllCards(Phase p){


		Card c=pickCard(p);
		int i=0;
		float gap=20;
		while(c!=null){
			c.enemySelect();
			if(c.getCode().contains(AI.RegularShield)){
				enemySpendShields(true);
				enemySpendShields(false);
			}
			
			c.getGraphic().slide(new Pair(
					975-(i/2)*(gap+CardGraphic.width),
					80+(i%2)*(CardGraphic.height/2+gap)),
					.67f, 
					Interp.SQUARE);

			c=pickCard(p);
			i++;
		}
		
		HoverCard.enemyHoverPosition = new Pair(
					975-(i/2)*(gap+CardGraphic.width),
					80+(i%2)*(CardGraphic.height/2+gap));
	
		if(i==0){
			endPhase();
		}


		turnTimer=new Timer(1, 0, 1/10f, Interp.LINEAR);

		playCards();

		notifyIncoming();
	}


	public void enemyFadeAll(){
		if(turnTimer.getFloat()>0){
			System.out.println("returning due to turn timer");
			return;
		}
		for(Card c:playList) c.fadeAndAddIcon();
		
		endPhase();
	}

	public Card pickCard(Phase p){

		//Checks to see if card is valid to play//
		boolean shield=true;
		boolean weapon=true;
		if(p==Phase.EnemyShieldPhase||p==Phase.ShieldPhase) weapon=false;
		if(p==Phase.EnemyWeaponPhase||p==Phase.WeaponPhase) shield=false;
		//First play any scrambled cards//
		for(Card c:hand){
			if(c.selected) continue;
			if(c.mod.getBuffAmount(BuffType.Scrambled)>0){
				return c;
			}
		}

		//Check the cards in order and pass to the enemyDecide method//


		//First check the alternate side//
		Card c;
		for(int pri=2;pri>=-1;pri--){

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

			Card c=decideBest(0, weapon, shield, true);
			if(c!=null)return c;

		}

		for(Card c:hand){
			if(c.selected)continue; //Probably just for debugging//
			if(c.type==ModuleType.WEAPON&&!weapon) continue;
			if(c.type==ModuleType.SHIELD&&!shield) continue;


			for(int side=1;side>=0;side--){
				CardCode code= c.getCode(side);

				//cancelling due to targeted//
				if(targetedOnly&&!code.contains(Special.Targeted))break;


				if(code.getPriority()==priority){
					if(c.enemyDecide(side)) return c;

				}

				if(focusTarget!=null&&code.contains(Special.Targeted)){

					if(c.enemyDecide(side)) return c;

				}



			}
		}
		return null;
	}

	public void enemySpendShields(boolean priority){
		if(shieldPoints.size()==0)return;



		for(Component c:getRandomisedModules()){
			if(shieldPoints.size()==0)return;
			//Need to put in reasons not to shield//
			if(shieldPoints.get(0).card.getCode().contains(Special.ShieldOnlyDamaged)){
				if(c.currentThreshold==0)continue;
			}

			//Priority checking//
			if(priority){
				c.getShieldsRequiredToAvoidMajor();
				while(c.getShieldsRequiredToAvoidMajor()<=shieldPoints.size()){ //not a hopeless cause// //Only shield until it's barely surviving//
					System.out.println("found priority shield");
					System.out.println("Going to shield "+c+" because requires "+c.getShieldsRequiredToAvoidMajor()+" and I have "+shieldPoints.size());
					c.shield(shieldPoints.remove(0),false);
				}
				continue;
			}

			//Non-priority//
			while(c.getShieldableIncoming()>0&&shieldPoints.size()>0){
				c.shield(shieldPoints.remove(0),false);
			}

		}
	}

	//Shielding stuff//

	public void addShield(Card card, int effect) {
		for(int i=0;i<effect;i++)shieldPoints.add(new ShieldPoint(card,false));
	}

	public void shieldAll(Card card, int effect) {
		for(Component c:components){
			for(int i=0;i<effect;i++)c.shield(new ShieldPoint(card,i==0),false);
		}
	}

	public void unShield(Card card) {
		for(Component c:components){
			c.unshield(card);
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
		while(hand.size()>getComputer().getMaximumHandSize()){
			putBack(hand.get(hand.size()-1));
		}
		getComputer().resetBonusCards();
	}

	public void putBack(Card c){
		discard(c);
		deck.add(0, c);
	}
	
	public void drawCard(int number){
		if(number<=0)return;
		for(int i=0;i<number;i++){
			if(deck.size()==0) makeDeck();
			drawCard(deck.remove(0));
		}
	}

	public void drawCard(Card card) {
		card.selected=false;
		card.active=true;
		card.addToDeck=false;
		card.getGraphic().activate();
		card.getGraphic().override=false;
		if(card.specialSide==-1)card.finaliseSide(); //for rigged draws//

		hand.add(card);
		card.getGraphic().activate();
		card.getGraphic().mousectivate(null);
		card.getGraphic().setPosition(CardGraphic.startPosition);
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
		for(Module m:components){
			m.cardIconMoused(card);
		}
		for(Module m:getEnemy().components){
			m.cardIconMoused(card);
		}
	}

	public void cardOrIconUnmoused() {
		for(Module m:components){
			m.cardIconUnmoused();
		}
		for(Module m:getEnemy().components){
			m.cardIconUnmoused();
		}
	}

	//Setup junk//

	public static void init(){
		/*classes.add(Aurora.class);
		classes.add(Nova.class);
		classes.add(Comet.class);
		classes.add(Eclipse.class);*/
	}

	public void makeDeck(){
		deck.clear();

		for(Module m:components){
			if(m.destroyed)continue;
			for(int i=0;i<m.numCards;i++)deck.add(m.makeCard());
		}
		for(Module m:utilities){
			if(m==null)continue;
			for(int i=0;i<m.numCards;i++)deck.add(m.makeCard());
		}
		if(armour!=null)for(int i=0;i<armour.numCards;i++)deck.add(armour.makeCard());

		for(int i=consumableStore.size()-1;i>=0;i--){
			Card c=consumableStore.get(i);
			if(c.addToDeck){
				c.selected=false;
				c.addToDeck=false;
				consumableStore.remove(c);
				deck.add(c);
			}
		}

		Draw.shuffle(deck);
	}

	public void cleanupAfterFight(){
		for(Component c:components){
			c.repair(0);
			c.finishBattle();
		}
		getComputer().resetBonusCards();
		majorDamageTaken=0;
	}

	public void startFight(boolean goingFirst){
		if(!Battle.isTutorial())hand.clear();
		turnsTaken=0;
		attacks.clear();
		deck.clear();
		initModuleStats();
		initFightStats();

		if(goingFirst)currentEnergy=getGenerator().getIncome();
		else currentEnergy=(int) Math.ceil(getGenerator().getIncome()/2f);

		for(Utility u:utilities)if(u!=null)u.startBattleEffect();

		if(!Battle.isTutorial()) drawToMaximum();;



	}

	private void initFightStats() {
		fightStats=new FightStats(this);
	}

	private void initModuleStats(){
		for(Component c:components){
			c.getStats().info.alpha=0;;
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
		if(getArmour()!=null)getArmour().onTakeMajorDamage();
		Battle.shake(player,3);
		Sounds.damageMajor.overlay();
		majorDamageTaken++;
	}
	public int getMajorDamage(){
		return majorDamageTaken;
	}
	public int getTotalShieldableIncoming(){
		int total=0;
		for(Component c:components){
			total+=c.getShieldableIncoming();
		}
		return total;
	}
	public Component getRandomUndestroyedComponent(){
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for(int i=0;i<5;i++){
			ints.add(i);
		}
		Draw.shuffle(ints);
		for(int i:ints){
			if(components[i].currentThreshold<3){
				return components[i];
			}
		}
		System.out.println("all mods destroyed?");
		return null;
	}

	public Ship getEnemy(){
		if(player)return Battle.getEnemy();
		return Battle.getPlayer();
	}
	public void addEnergy(int amount, boolean notify){
		if(notify&&amount!=0){
			TextWisp.wisps.add(new TextWisp(
					""+(amount>0?"+":"")+amount,
					Font.big,
					player?FightStats.playerEnergy.add(30,-20):FightStats.enemyEnergy.add(30,-20),
							WispType.Regular,
							Colours.genCols5[3]
					));
		}
		currentEnergy+=amount;
		currentEnergy=Math.min(99, currentEnergy);

	}
	public int getEnergy(){return currentEnergy;}
	public Weapon[] getWeapons(){return new Weapon[]{(Weapon) components[0],(Weapon) components[1]};}
	public ArrayList<Component> getRandomisedModules(){
		ArrayList<Component> result=new ArrayList<Component>();
		for(Component c:components){
			if(c.currentThreshold<2)result.add(c);
		}
		Draw.shuffle(result);
		return result;
	}
	public Component getComponent(int index){return components[index];}
	public Component getShield(){return components[2];}
	public Generator getGenerator(){return (Generator) components[3];}
	public Computer getComputer(){return (Computer) components[4];}

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
		components[i]=w;
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
		utilities[2]=a;
		this.armour=a;
		a.ship=this;
		recalculateThresholds();
	}

	public void setUtility(Utility u, int position){
		if(u instanceof Armour) System.out.println("Watch out, setting armour as utility wuhohh!");
		utilities[position]=u;
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



	public boolean hasSpendableShields() {
		
		for(ShieldPoint sp:shieldPoints){
			if(sp.card.getCode().contains(Special.ShieldOnlyDamaged)){
				return false;
			}
			if(sp.card.getCode().contains(Special.ShieldOnlyPristine)){
				return false;
			}
			
		}
		
		if(shieldPoints.size()>0){
			for(Component c:components){
				if(c.getShieldableIncoming()>0){
					new TextWisp("Spend all your shields first!", Font.medium, new Pair(Main.width/2,300), WispType.Regular);
					return true;
				}
			}
		}
		return false;
	}

	public double getArmourMultiplier(){
		return armour.getMultuplier();
	}

	public int getTotalDeckSize(){
		int result=0;

		for(Module m:components){
			result+=m.numCards;
		}

		for(Utility u:utilities){
			if(u!=null)result+=u.numCards;
		}
		if(armour!=null)result+=armour.numCards;

		return result;
	}

	public void recalculateThresholds() {
		doubleHP=false;
		for(Component c:components){
			if(c.getMaxHP()>17){
				doubleHP=true;
			}
		}
		for(Component c:components)c.recalculateThresholds();	
	}


	public abstract ArrayList<MapAbility> getMapAbilities();

	public void setMapShip(MapShip mapShip) {
		this.mapShip=mapShip;
	}
	public void clearShields() {
		for(Component c:components)c.clearShields();
	}
	public Module getSpecialComponent() {
		return specialComponent;
	}

	public int getBonusEffect(Card c, int side, int effect) {
		int bonus=0;
		for(Utility u:utilities){
			if(u!=null)bonus+=u.getBonusEffect(c, effect);
		}
		
		if(c.type==ModuleType.SHIELD&&c.mod!=getShield()){
			
		}
		
		return bonus;
	}

	public int getBonusCost(Card c, int side, int effect) {
		int bonus=0;

		for(Utility u:utilities){
			if(u!=null)bonus+=u.getBonusCost(c, effect);
		}

		return bonus;
	}

	public int getBonusShots(Card c, int side, int effect) {
		int bonus=0;
		for(Utility u:utilities){
			if(u!=null)bonus+=u.getBonusShots(c, effect);
		}
		return bonus;
	}

	public Utility getUtility(int index) {
		return utilities[index];
	}

	public Armour getArmour() {
		return armour;
	}

	public int getCurrentTurn(){
		return turnsTaken;
	}

	public ShipStats getStats(){
		if(shipStats==null) recalculateStats();

		return shipStats;
	}

	public void recalculateStats(){
		shipStats=calculateStats(null, null);
	}

	public ShipStats calculateSpecialStats(Module[] remove, Module[] add){
		return calculateStats(remove, add);
	}

	private ShipStats calculateStats(Module[] remove, Module[] add){

		deck.clear();
		makeDeck();
		float totalDefence=0;
		float totalAttack=0;

		float totalCost=0;

		if(remove!=null){
			for(int i=deck.size()-1;i>=0;i--){
				Card c=deck.get(i);
				for(Module m:remove)if(c.mod==m)deck.remove(c);
			}
		}

		if(add!=null){
			for(Module m:add){
				for(int i=0;i<m.numCards;i++){
					deck.add(m.getNextCard());
				}
			}
		}

		float deckSize=deck.size();
		for(Card c:deck){
			c.remakeCard(1);
			CardCode code=c.getCode();
			int thisCardEffect=c.getEffect();
			if(c.mod.overridePowerLevel>0) thisCardEffect=c.mod.overridePowerLevel;
			if(c.mod.getShots(0)>0)thisCardEffect*=c.mod.getShots(0);

			if(c.mod.type==ModuleType.WEAPON){
				totalAttack+=thisCardEffect;
			}
			if(c.mod.type==ModuleType.SHIELD){
				totalDefence+=thisCardEffect;
			}
			totalCost+=c.getCost();

			deckSize-=code.getAmount(Augment.AugmentDrawCard);
			deckSize-=code.getAmount(Special.DrawCard);
			totalCost-=code.getAmount(Special.GainEnergy);
		}

		float averageCardCost=totalCost/deckSize;
		float averageCardDefence=totalDefence/deckSize;
		float averageCardAttack=totalAttack/deckSize;

		float handCost=averageCardCost*maxCards;
		float totalHandDefence=averageCardDefence*maxCards;
		float totalHandAttack=averageCardAttack*maxCards;

		float ratio=getGenerator().getIncome()/handCost;

		float refinedRatio=ratio;

		if(refinedRatio>1)refinedRatio-=(refinedRatio-1)/2f;

		float attackPower=totalHandAttack*refinedRatio;
		float defPower=totalHandDefence*refinedRatio;

		return new ShipStats(ratio, attackPower, defPower, attackPower+defPower);
		/*

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
		return powerLevel;*/
	}

	private void setupConsumableCard(Card c){
		c.mod=getSpecialComponent();
	}

	public void addConsumableCard(Card c){
		setupConsumableCard(c);
		consumableStore.add(c);
	}

	public ArrayList<Card> getConsumables(){
		return consumableStore;
	}



	public static Ship makeIntegerShip(boolean player, float power, int number){
		try {
			return (Ship) classes[number].getConstructor(boolean.class, float.class).newInstance(player, power);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Ship makeRandomShip(boolean player, float power){
		if(shipGenPool.size()==0){
			for(int i=0;i<classes.length;i++){
				shipGenPool.add(i);
			}
			Collections.shuffle(shipGenPool);
		}
		return makeIntegerShip(player, power, shipGenPool.remove(0));
	}

	public void dispose(){
		for(Component c:components){
			c.getStats().dispose();
		}
		getGraphic().dispose();
		for(Niche n:niches){
			n.getGraphic().dispose();
		}
		for(Card c:deck)c.getGraphic().deactivate();
		for(ModuleStats ums:utilityStats){
			ums.deactivate();
		}
	}
	
	public void onScramble(Component c){
		for(Utility u:utilities) if(u!=null) u.onScramble(c);
	}

	public void endOfBattleCelebrations() {
		for(Card c:hand){
			if(!c.selected&&c.consumable)addConsumableCard(c);
		}
		for(Card c:deck){
			if(!c.selected&&c.consumable)addConsumableCard(c);
		}
	}


	public ModuleStats[] getUtilityStats() {
		if(utilityStats[0]==null){
			for(int index=0;index<3;index++){
				utilityStats[index]= new ModuleStats(utilities[index], index, player);
			}
		}
		return utilityStats;
		
			
			
	}

	public void resetGraphics(){
		if(utilityStats[0]!=null)for(ModuleStats ms:utilityStats)ms.dispose();
		utilityStats=new ModuleStats[3];
		for(Component c:components){
			c.getStats().mousectivate(null);
			c.getStats().alpha=0;
			c.getStats().info.alpha=0;
			c.getStats().buffList=null;
			
		}
	}

	
}
