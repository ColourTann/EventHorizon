package game.ship;

import java.util.ArrayList;

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
import game.assets.Gallery;
import game.assets.Sounds;
import game.attack.Attack;
import game.card.Card;
import game.card.CardCode;
import game.card.CardGraphic;
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
import game.module.junk.ShieldPoint;
import game.module.junk.Buff.BuffType;
import game.module.utility.AuxiliaryDrive;
import game.module.utility.Furnace;
import game.module.utility.Scrambler;
import game.module.utility.ArcSocket;
import game.module.utility.PhaseArray;
import game.module.utility.Repeater;
import game.module.utility.Utility;
import game.module.utility.armour.Armour;
import game.module.utility.armour.BasicArmour;
import game.module.utility.armour.ChargedHull;
import game.module.utility.armour.CrystalLattice;
import game.module.utility.armour.GalvanicSkin;
import game.module.utility.armour.ShockbackHull;
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
	private static ArrayList<Class> classes= new ArrayList<Class>();

	public boolean player;
	public Pic shipPic;




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

	public Ship(boolean player, Pic shipPic, Pic genPic, Pic comPic){
		this.player=player;
		this.shipPic=shipPic;

		setupNiches();
		placeNiches();
		getGenerator().modulePic=genPic;
		getComputer().modulePic=comPic;
		setArmour(new ShockbackHull(0));
		//setUtility(new Shieldier(0), 1);
		specialComponent= new SpecialComponent();
		specialComponent.ship=this;

		for(int i=0;i<6;i++){
			addConsumableCard(ConsumableCard.get(1));
		}

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
		if(player)playList.clear();
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

		attacks.add(a);
		ParticleSystem.systems.add(a.atkgrphc);
		updateIntensities();

	}

	public void removeAttack(Card card){

		System.out.println("removing attack from "+card);
		System.out.println(attacks.size());
		for(int i=0;i<attacks.size();i++){

			Attack a=attacks.get(i);
			if(a.card==card){
				if(a.target!=null)a.target.targeteds--;
				attacks.remove(i);
				a.disable();
				i--;
				System.out.println("removing attack");
			}
		}
		updateIntensities();
	}

	public ArrayList<Attack> getAttacks(){return attacks;}

	public void fireAll() {
		System.out.println(attacks.size());
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
		int shields=0;
		for(Card c:hand){
			if(c.mod.type==ModuleType.SHIELD)shields++;
		}
		if(shields>=hand.size()-1&&hand.size()==getComputer().getMaximumHandSize()){
			discardHand();
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
			c.enemySelect();
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


		turnTimer=new Timer(1, 0, 1/10f, Interp.LINEAR);

		playCards();

		notifyIncoming();
	}


	public void enemyFadeAll(){
		if(turnTimer.getFloat()>0)return;
		for(Card c:playList) c.fadeAndAddIcon();
		playList.clear();
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
			if(c.type==ModuleType.WEAPON&&!weapon) continue;
			if(c.type==ModuleType.SHIELD&&!shield) continue;


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



		for(Component c:getRandomisedModules()){
			if(shieldPoints.size()==0)return;
			//Need to put in reasons not to shield//
			if(shieldPoints.get(0).card.getCode().contains(Special.ShieldOnlyDamaged)){
				System.out.println("can't use shield becaus not major damaged");
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
		for(int i=0;i<effect;i++)shieldPoints.add(new ShieldPoint(card,i==0));
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
			discard(hand.get(hand.size()-1));
		}
		getComputer().resetBonusCards();
	}

	public void drawCard(int number){
		if(number<=0)return;
		for(int i=0;i<number;i++){
			if(deck.size()==0) makeDeck();
			drawCard(deck.remove(0));
		}
	}

	public void drawCard(Card card) {
		card.active=true;
		card.addToDeck=false;
		card.getGraphic().activate();

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
		classes.add(Aurora.class);
		classes.add(Nova.class);
		classes.add(Comet.class);
		classes.add(Eclipse.class);
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
				consumableStore.remove(c);
				deck.add(c);
			}
		}

		for(int i=0;i<0;i++){
			Card c= new Card(
					new String[]{"Rocket", "Rocket"},
					new Pic[]{Gallery.armour, Gallery.armour},
					new int[]{0,3}, 
					new int[]{1,5},
					new int[]{0,0},
					new int[]{5,5},
					new String[]{"",""},
					new CardCode[]{new CardCode(), new CardCode()}, 
					ModuleType.WEAPON);
			deck.add(c);
			setupConsumableCard(c);
		}
		Draw.shuffle(deck);
	}

	public void startFight(boolean goingFirst){
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
			c.getStats();
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
		Sounds.damageMajor.play();
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

	public void addIncome(int amount) {
		getGenerator().addIncome(amount);
	}

	public boolean hasSpendableShields() {
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

	public float getArmourMultiplier(){
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
			if(c.getMaxHP()>18){
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

	public boolean preventScramble(){
		for(Utility u:utilities){
			if(u instanceof Scrambler)return true;
		}
		return false;
	}

}
