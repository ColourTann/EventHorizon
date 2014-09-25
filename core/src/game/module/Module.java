package game.module;

import java.util.ArrayList;

import util.Draw;
import util.image.Pic;
import util.maths.Pair;
import game.Main;
import game.card.Card;
import game.card.CardCode;
import game.module.Module.ModuleType;
import game.module.component.SpecialComponent;
import game.module.junk.ModuleInfo;
import game.module.junk.Buff.BuffType;
import game.ship.Ship;
import game.ship.ShipGraphic;


public abstract class Module {

	public enum ModuleType{WEAPON, SHIELD, GENERATOR, COMPUTER, UTILITY, ARMOUR, BLANK}


	public Ship ship;

	public ModuleType type;
	public ModuleType cardType;

	public String moduleName;


	public Pic modulePic;

	//Card stuff//
	public int variants;
	public int numCards;
	protected String[] name = new String[7];
	protected Pic[] cardPic= new Pic[7];
	protected int[] cost = new int[7];
	protected int[] cooldown = new int[7];
	protected int[] effect = new int[7];
	protected int[] shots = new int[7];
	protected String[] rules = new String[7];
	protected CardCode[] code = new CardCode[7];
	protected ArrayList<Integer> cardOrder= new ArrayList<Integer>();
	private ArrayList<Integer> nextCards= new ArrayList<Integer>();
	protected int currentCooldown=0;
	public int tier=-5;
	public boolean destroyed;





	private ModuleInfo info;

	public boolean moused;



	//Buff stuff//

	public Module(int tier, String name, Pic modulePic, int variants, int numCards){
		this.tier=tier;
		this.moduleName=name;
		this.modulePic=modulePic;
		this.variants=variants;
		this.numCards=numCards;
		for(int i=1;i<=variants;i++){
			cardOrder.add(i);
		}
		Draw.shuffle(cardOrder);
		for(int i=0;i<code.length;i++){
			code[i]=new CardCode();
		}
	}

	//Card stuff//

	public Card getCard(int number){
		Card c=new Card(this);
		c.remakeCard(number);
		return c;
	}

	public Card getNextCard(){
		if(destroyed){
			System.out.println("trying to get card from destroyed system");
			return null;
		}
		return getCard(getNextCardSide());
	}

	public Card makeCard(){
		return new Card(this);
	}

	@SuppressWarnings("unchecked")
	public int getNextCardSide(){
		if(nextCards.size()==0)nextCards=(ArrayList<Integer>) cardOrder.clone();
		return nextCards.remove(0);
	}

	//Damage stuff//

	public void cardIconMoused(Card card) {
	}
	public void cardIconUnmoused() {
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


	private double calcDouble(int cost){
		// 1+ because a card is worth a similar amount to an energy //
		return ((1+cost)*(tier/2d+1));
	}

	public int calc(int cost){
		return (int) calcDouble(cost);
	}

	public int calc(int baseCost, int extraCost){
		double base= calcDouble(baseCost);
		base+=(extraCost*(tier/2d+1))/2d;
		return (int) base;
	}

	public static int statiCalc(int cost, int extraCost, int tier){
		double base = ((1+cost)*(tier/2d+1));
		base+=(extraCost*(tier/2d+1))/2d;
		return (int)base;
	}


	public String toString(){
		if(this instanceof SpecialComponent)return "Special Component";
		return name[0];
	}

	public Pair getBarrel(){
		if(ship.player){
			return new Pair(
					ShipGraphic.offset.x+ShipGraphic.width/2,
					ShipGraphic.offset.y+ShipGraphic.height/2);
		}
		else{
			return new Pair(
					500+Main.width-ShipGraphic.offset.x-ShipGraphic.width/2,
					ShipGraphic.offset.y+ShipGraphic.height/2);
		}
	}

	public int getBuffAmount(BuffType check) {
		return 0;
	}
	public boolean isDead() {
		return false;
	}

	public int getShots(int i){	
		return shots[i]==0?0:shots[i]+getBuffAmount(BuffType.BonusShot);
	}


}

