package game.module;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.assets.Clip;
import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;
import util.update.TextWisp;
import util.update.TextWisp.WispType;
import util.update.Timer.Interp;
import util.update.Timer;
import game.Main;
import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode;
import game.card.CardCode.Special;
import game.module.utils.Buff;
import game.module.utils.DamagePoint;
import game.module.utils.ModuleInfo;
import game.module.utils.ModuleStats;
import game.module.utils.ShieldPoint;
import game.module.utils.Buff.BuffType;
import game.module.weapon.Tesla;
import game.module.weapon.Weapon;
import game.screen.battle.Battle;
import game.screen.battle.Battle.State;
import game.ship.Ship;
import game.ship.ShipGraphic;
import game.ship.niche.Niche;


public abstract class Module {

	public enum ModuleType{WEAPON,SHIELD,GENERATOR,COMPUTER}


	public Ship ship;
	public Niche niche;
	public int index=-1;
	public ModuleType type;
	private int currentCooldown=0;
	public String moduleName;

	//Damage and shields//
	int damageAtEnd;
	public boolean immune;
	public int maxHP;
	public int currentThreshold;
	public int[] thresholds;

	public ArrayList<DamagePoint> damage= new ArrayList<DamagePoint>();
	public ArrayList<DamagePoint> incomingDamage= new ArrayList<DamagePoint>();
	public ArrayList<DamagePoint> unshieldableIcoming= new ArrayList<DamagePoint>();
	public ArrayList<ShieldPoint> shieldPoints= new ArrayList<ShieldPoint>(); 

	//Card stuff//
	public int variants;
	public int numCards;
	protected String[] name = new String[7];
	protected Pic[] cardPic= new Pic[7];
	protected int[] cost = new int[7];
	protected int[] cooldown = new int[7];
	protected int[] effect = new int[7];
	protected String[] rules = new String[7];
	protected CardCode[] code = new CardCode[7];
	private ArrayList<Integer> cardOrder= new ArrayList<Integer>();
	private ArrayList<Integer> nextCards= new ArrayList<Integer>();
	public boolean destroyed;
	public int tier=5;
	//Bonuses//
	/*public int bonusCost;
	public int bonusCooldown;
	public int bonusEffect;
	public int bonusShots;*/

	//Graphics stuff//
	private ModuleStats stats;
	private ModuleInfo info;
	public Pic modulePic;
	public boolean moused;
	public int targeteds;
	public Timer alphaTimer=new Timer();


	//Buff stuff//
	public ArrayList<Buff> buffs=new ArrayList<Buff>();

	private Pair center;
	private Pair barrel;


	public Module(String name, Pic p, int variants, int numCards, int thresholds[], int tier){
		moduleName=name;
		this.tier=tier;
		this.thresholds=thresholds;
		maxHP=thresholds[2];
		this.numCards=numCards;
		this.modulePic=p;
		this.variants=variants;
		for(int i=1;i<=variants;i++){
			cardOrder.add(i);
		}
		Draw.shuffle(cardOrder);
		for(int i=0;i<code.length;i++){
			code[i]=new CardCode();
		}
	}

	@SuppressWarnings("unused")
	public void clicked(){
		//CLICKED ON PLAYER MODULE//
		if(destroyed)return;
		if(ship.player){

			if(Battle.getState()==State.ModuleChoose){
				CardCode code= Battle.moduleChooser.getCode();

				for(int i=0;i<Battle.moduleChooser.getEffect();i++)shield(new ShieldPoint(Battle.moduleChooser, i==0), false);
				if(code.contains(Special.GetCardFromChosenModule)){
					if(this==Battle.moduleChooser.mod){
						Clip.error.play();
						return;
					}
					else ship.drawCard(getNextCard());
				}

				Battle.moduleChooser.moduleChosen();
				return;
			}


			if(ship.shieldPoints.size()>0&&getShieldableIncoming()>0){
				for(ShieldPoint p: ship.shieldPoints){
					if(shield(ship.shieldPoints.get(0),true)){
						ship.shieldPoints.remove(0);
						return;
					}
				}
				Clip.error.play();

			}

		}

		//CLICKED ON ENEMY MODULE//

		if(!ship.player){
			if(Battle.getState()==State.Targeting){
				Card targeter=Battle.targetSource;
				Weapon source=(Weapon) targeter.mod;
				targeter.targetChosen();
				targeteds++;
				//Only the first shot is targeted//
				for(int i=0;i<targeter.getShots();i++){
					source.addAttack(targeter, this);
				}
			}
		}
	}

	public void moused(){
		moused=true;
	}

	public void unmoused(){
		moused=false;
	}



	//Card stuff//

	public Card getCard(int number){
		return new Card(this,number);
	}

	public Card getNextCard(){
		if(destroyed){
			System.out.println("trying to get card from destroyed system");
			return null;
		}
		return getCard(getNextCardSide());
	}

	@SuppressWarnings("unchecked")
	public int getNextCardSide(){
		if(nextCards.size()==0)nextCards=(ArrayList<Integer>) cardOrder.clone();
		return nextCards.remove(0);
	}

	//Damage stuff//

	public void damage(DamagePoint damagePoint) {
		if(immune)return;
		if(currentThreshold==3){
			System.out.println("overdamaging "+this+", already destroyed");
			return;
		}
		damage.add(damagePoint);

		if(getDamage()>=thresholds[currentThreshold]){
			majorDamage();

		}

		Battle.shake(ship.player,(float)(2.5f));
		ship.getGraphic().damage(niche.location);
		Clip.damageMinor.play();
		//if(damagePoint.card!=null&&damagePoint.card.mod instanceof Tesla) return;



	}

	private void majorDamage() {
		currentThreshold++;
		ship.majorDamage();
		if(currentThreshold==3){
			destroy();
			return;
		}
		new TextWisp("Scrambled", Font.medium, getCenter().add(new Pair(0,-40)), WispType.Regular); 
		scramble();
	}

	private void destroy(){

		destroyed=true;
		new TextWisp("Destroyed", Font.medium, getCenter().add(new Pair(0,-40)), WispType.Regular);
		for(int i=0;i<ship.hand.size();i++){
			Card c=ship.hand.get(i);
			if(c.mod==this){
				ship.discard(c);
				i--;
			}
		}
		for(int i=0;i<ship.deck.size();i++){
			Module m= ship.deck.get(i);
			if(m==this){
				ship.deck.remove(this);
				i--;
			}
		}
	}


	public void calculateDamage(int damage, boolean unshieldable) {
		System.out.println("calc");
		if(unshieldable){
			for(int i=0;i<damage;i++){
				DamagePoint p = unshieldableIcoming.remove(0);
				damage(p);
			}
		}
		if(!unshieldable){
			for(int i=0;i<damage;i++){
				DamagePoint p = incomingDamage.remove(0);
				if(shieldPoints.size()>0){
					Clip.shieldActivate.play();
					activateShield(shieldPoints.remove(0));
					continue;
				}
				damage(p);
			}

		}
	}

	private void calculateDamage() {
		for(DamagePoint p: incomingDamage){
			if(shieldPoints.size()>0){
				activateShield(shieldPoints.remove(0));
				continue;
			}
			damage(p);
		}
		for(DamagePoint p: unshieldableIcoming){
			damage(p);
		}
		incomingDamage.clear();
		shieldPoints.clear();
		unshieldableIcoming.clear();
	}

	private void activateShield(ShieldPoint shield) {
		if(immune)return;
		if(!shield.firstAdded)return;
		CardCode code=shield.card.getCode();
		ship.drawCard(code.getAmount(Special.AbsorbDraw));
		ship.addEnergy(code.getAmount(Special.AbsorbEnergy));
		alphaTimer=new Timer(1,0,1,Interp.SQUARE);
	}

	public boolean shield(ShieldPoint s, boolean overlapSound){
		//#1Reason why not to shield//
		System.out.println("shielding "+this);
		if(s.card.getCode().contains(Special.ShieldOnlyDamaged))if(currentThreshold==0)return false;

		shieldPoints.add(s);
		if(overlapSound)Clip.shieldUse.play();
		else Clip.shieldUse.play();
		return true;
	}

	public void unshield(Card card) {
		for(int i=0;i<shieldPoints.size();i++){
			ShieldPoint sp=shieldPoints.get(i);
			if(sp.card==card){
				shieldPoints.remove(sp);
				i--;
			}
		}
	}

	//Getters and setters//
	public void removeIncoming(Card card) {
		for(int i=0;i<incomingDamage.size();i++){
			DamagePoint p=incomingDamage.get(i);
			if(p.card!=null&&p.card==card){
				incomingDamage.remove(p);
				i--;
			}
		}
	}
	public void cardIconMoused(Card card) {
		for(DamagePoint p:damage)p.checkMoused(card);
		for(DamagePoint p:unshieldableIcoming)p.checkMoused(card);
		for(DamagePoint p:incomingDamage)p.checkMoused(card);
	}
	public void cardIconUnmoused() {
		for(DamagePoint p:damage)p.reset();
		for(DamagePoint p:unshieldableIcoming)p.reset();
		for(DamagePoint p:incomingDamage)p.reset();
	}
	public ArrayList<Card> getCardsJustForShowing(){
		ArrayList<Card> result = new ArrayList<Card>();
		for(int i=1;i<=variants;i++){
			result.add(getCard(i));
		}
		for(Card c:result){
			c.getGraphic().override=true;
			c.getGraphic().finishFlipping();
		}
		return result;
	}	
	public String getName(int i){
		return name[i];
	}
	public Pic getPic(int i) {
		return cardPic[i];
	}
	public int getCost(int i){
		return cost[i];
	}
	public int getCooldown(int i){
		return cooldown[i];
	}
	public int getEffect(int i){
		return effect[i];
	}
	public String getRules(int i){
		return rules[i];
	}
	public CardCode getCode(int i) {
		return code[i].copy();
	}
	public int getIndex(){
		if(index==-1)System.out.println(this+" isn't installed on a ship");
		return index;
	}
	public ModuleStats getStats(){
		if(stats==null)stats=new ModuleStats(this);
		return stats;
	}
	public ModuleInfo getInfo(){
		if(info==null)info=new ModuleInfo(this);
		return info;
	}
	public int getCurrentCooldown() {
		return currentCooldown;
	}
	public void increaseCooldown(int amount){
		currentCooldown+=amount;
	}
	public int getDamage(){
		return damage.size();
	}
	public int getShieldableIncoming() {
		if(immune)return 0;
		return incomingDamage.size()-shieldPoints.size();
	}
	public int getSimpleIncoming(){
		return incomingDamage.size();
	}
	public int getTotalIncoming(){
		if(immune)return 0;
		return incomingDamage.size()+unshieldableIcoming.size();
	}
	public int getUnshieldableIncoming(){
		return unshieldableIcoming.size();
	}
	public int getShield(){
		return shieldPoints.size();
	}
	public void addIncoming(DamagePoint d){
		if(immune)return;
		incomingDamage.add(d);
	}

	public void addUnshieldable(DamagePoint d){
		if(immune)return;
		unshieldableIcoming.add(d);
	}
	public Pair getBarrel(){
		if(barrel==null){
		float offset=12+niche.width/2;
		float x=getCenter().x+(ship.player?offset:-offset);
		float y=getCenter().y;
		barrel=new Pair(x,y);
		}

		return barrel;
	}

	public Pair getCorner(){
		return getCenter().add(-niche.width/2, -niche.height/2);
	}

	public Pair getCenter(){
		if(center==null){
			
			if(ship.player){
				center=new Pair(
						niche.location.x+niche.width/2+ShipGraphic.offset.x,
						niche.location.y+niche.height/2+ShipGraphic.offset.y);
			}

			if(!ship.player){
				center=new Pair(
						500+Main.width-ShipGraphic.offset.x-niche.location.x-niche.width/2+((type==ModuleType.WEAPON||type==ModuleType.SHIELD)?-niche.width:0),
						ShipGraphic.offset.y+niche.location.y+niche.height/2);
			}
		}
		return center;
	}
	public Pair getHitLocation(){
		return getCenter().add(Pair.randomAnyVector().multiply(3));
	}
	public int getBuffAmount(BuffType check) {
		int total=0;
		for(Buff b:buffs){
			if(check==b.type)total+=1;
		}
		return total;
	}

	public void removeSramble(){
		for(Buff b:buffs){
			if(b.type==BuffType.Scrambled){
				buffs.remove(b);
				return;
			}
		}
	}

	public void scramble(){
		buffs.add(new Buff(BuffType.Scrambled, 1, null, true));
	}

	public void addBuff(Buff b){
		buffs.add(b);
	}

	public void removeBuffs(Card c){
		for(int i=0;i<buffs.size();i++){
			Buff b = buffs.get(i);
			if(b.card==c){
				buffs.remove(b);
				i--;
			}
		}
	}

	public int getDamageUntilMajor(){

		if(immune||destroyed)return 999;
		if(getDamage()+getTotalIncoming()>=thresholds[2]){
			System.out.println("going to be destroyed");
			return 999;
		}
		int keyThreshold=0;

		int damageTotal=getTotalIncoming()+getDamage();
		for(int i=2;i>=0;i--){
			if(damageTotal>=thresholds[i]){
				keyThreshold=i+1;
				break;
			}
		}

		//System.out.println(this+" requires "+(thresholds[keyThreshold]-damageTotal)+" to damage");
		return thresholds[keyThreshold]-damageTotal;

	}

	public int getShieldsRequiredToAvoidMajor(){
		if(immune)return 999;
		int keyThreshold=-1;

		int damageTotal=getTotalIncoming()+getDamage()-Math.min(shieldPoints.size(), getSimpleIncoming());
		for(int i=2;i>=0;i--){
			if(damageTotal>=thresholds[i]&&i>=currentThreshold){
				keyThreshold=i;
				break;
			}
		}
		if(keyThreshold==-1){
			return 999; //99 if not taking
		}
		if(getUnshieldableIncoming()>=damageTotal-thresholds[keyThreshold]+1){
			System.out.println("unshieldable so can't");
			return 999;

		}
		return damageTotal-thresholds[keyThreshold]+1;
	}

	public boolean isDead() {
		return currentThreshold==3;
	}

	private double calcDouble(int cost){
		return ((1+cost)*(tier/2d+1));
	}

	public int calc(int cost){
		// 1+ because a card is worth a similar amount to an energy //
		return (int) calcDouble(cost);
	}

	public int calc(int baseCost, int extraCost){
		double base= calcDouble(baseCost);
		base+=(extraCost*(tier/2d+1))/2d;
		return (int) base;
	}

	public String toString(){
		return name[0];
	}

	public void endAdmin(){
		targeteds=0;
		currentCooldown=Math.max(0, currentCooldown-1);
		immune=false;
		for(int i=0;i<damageAtEnd;i++){
			damage(new DamagePoint(null));
		}
		damageAtEnd=0;
		calculateDamage();
		//DOT stuff here//
		for(int i=0;i<buffs.size();i++){
			Buff b = buffs.get(i);
			if(!b.permanent){
				buffs.remove(b);
				i--;
			}
		}
	}

	public void drawShield(SpriteBatch batch) {
		float alpha= (float) (Math.sin(Battle.ticks*3)+1)/4+.5f;
		alpha/=4;
		alpha*=Math.min(4, shieldPoints.size());
		batch.setColor(Colours.withAlpha(Colours.shieldCols6[3], alpha));

		Draw.drawRotatedScaledCenteredFlipped(batch, Gallery.shieldEffect.get(), getBarrel().x, getBarrel().y, 2, 6, 0,  !ship.player, false);
		batch.setColor(1,1,1,alphaTimer.getFloat());
		Draw.drawRotatedScaledCenteredFlipped(batch, Gallery.shieldEffect.get(), getBarrel().x, getBarrel().y, 2, 6, 0,  !ship.player, false);
	}









}

