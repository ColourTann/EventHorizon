package game.module;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.assets.SoundClip;
import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;
import util.update.TextWisp;
import util.update.TextWisp.WispType;
import util.update.Timer.Interp;
import util.update.Timer;
import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.card.Card;
import game.card.CardCode;
import game.card.CardCode.Special;
import game.module.component.SpecialComponent;
import game.module.component.weapon.Tesla;
import game.module.component.weapon.Weapon;
import game.module.stuff.Buff;
import game.module.stuff.DamagePoint;
import game.module.stuff.ModuleInfo;
import game.module.stuff.ModuleStats;
import game.module.stuff.ShieldPoint;
import game.module.stuff.Buff.BuffType;
import game.screen.battle.Battle;
import game.screen.battle.Battle.State;
import game.ship.Ship;
import game.ship.ShipGraphic;
import game.ship.niche.Niche;


public abstract class Module {

	public enum ModuleType{WEAPON,SHIELD,GENERATOR,COMPUTER}


	public Ship ship;

	public ModuleType type;

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

	public String toString(){
		if(this instanceof SpecialComponent)return "Special Component";
		return name[0];
	}

	public Pair getBarrel(){
	return new Pair(0,0);
	}

	public int getBuffAmount(BuffType check) {
		return 0;
	}
	public boolean isDead() {
		return false;
	}




}

