package game.card;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.SpecialComponent;
import game.module.junk.ModuleInfo;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;

import java.util.ArrayList;
import java.util.Collections;

import util.image.Pic;

public class ConsumableCard {

	//Consumable cards -4 cost +/- a bit
	//Util cards -1 cost

	private static Module blankModule = new SpecialComponent();
	private static String[] name = new String[2];
	private static String[] rules = new String[2];
	private static Pic[] pic = new Pic[2];
	private static int[] cost = new int[2];
	private static int[] effect = new int[2];
	private static int[] shots = new int[2];
	private static int rocketSize=0;
	private static CardCode[] code = new CardCode[]{new CardCode(), new CardCode()};
	private static ModuleType type;
	static int calcTier=0;
	private static ArrayList<Card>[] decks = new ArrayList[5];

	public static Card get(int tier){
		if(decks[tier]==null||decks[tier].size()==0) setup(tier);
		return decks[tier].remove(0);
	}
	private static void setup(int tier){
		calcTier=tier;
		if(decks[tier]==null)decks[tier]=new ArrayList<Card>();

		type=ModuleType.SHIELD;

		name[0]= "Phase";
		rules[0]="Target module becomes immune this turn";
		pic[0]= Gallery.phase;
		cost[0]=0;
		effect[0]=0;
		code[0].add(Special.ModuleChooser);
		code[0].add(Special.ImmuneChosenModule);

		name[1]= "Flow";
		rules[1]="Shields all modules";
		pic[1]= Gallery.flow;
		cost[1]=1;
		effect[1]=calc(1);
		code[1].add(Special.ShieldAll);

		makeCard(tier, 1);
		
		///////////////////////////////////////////////////////////////////

		type=ModuleType.UTILITY;

		name[0]= "Inverse Sort";
		rules[0]="Draw 4 cards";
		pic[0]= Gallery.inverseSort;
		cost[0]=0;
		effect[0]=0;
		code[0].add(Special.DrawCard, 4);
		code[0].add(Special.DiscardWhenPlayed);

		name[1]= "Radial Sort";
		rules[1]="Get 3 cards from any module";
		pic[1]= Gallery.radialSort;
		cost[1]=1;
		effect[1]=0;
		code[1].add(Special.ModuleChooser);
		code[1].add(Special.GetCardFromChosenModule, 3);
		code[1].add(Special.DiscardWhenChosen);

		makeCard(tier, 1);

		///////////////////////////////////////////////////////////////////

		type=ModuleType.UTILITY;

		name[0]= "Consume";
		rules[0]="Steal 2 energy";
		pic[0]= Gallery.consume;
		cost[0]=0;
		effect[0]=0;
		code[0].add(Special.StealEnergy, 2);

		name[1]= "Ignite";
		rules[1]="Boost generator 20: +1 income";
		pic[1]= Gallery.ignite;
		cost[1]=0;
		effect[1]=0;
		code[1].add(Special.BoostGenerator);
		code[1].setBuff(new Buff(BuffType.BonusIncome, true, 1, 20));

		makeCard(tier, 1);

		///////////////////////////////////////////////////////////////////

		type=ModuleType.UTILITY;

		name[0]= "Refresh";
		rules[0]="+3 energy, reset cycle cost";
		pic[0]= Gallery.refresh;
		cost[0]=0;
		effect[0]=0;
		code[0].add(Special.GainEnergy, 3);
		code[0].add(Special.ResetCycle);

		name[1]= "Kindle";
		rules[1]="+4 energy";
		pic[1]= Gallery.kindle;
		cost[1]=0;
		effect[1]=0;
		code[1].add(Special.GainEnergy, 4);

		makeCard(tier, 1);

		///////////////////////////////////////////////////////////////////

		type=ModuleType.WEAPON;

		name[0]= "Swarm";
		rules[0]="";
		pic[0]= Gallery.swarm;
		cost[0]=4;
		effect[0]=calc(0,1);
		shots[0]=6;

		name[1]= "Replicate";
		rules[1]="Augment weapon card: +2 shots";
		pic[1]= Gallery.replicate;
		cost[1]=2;
		effect[1]=0;
		code[1].add(Special.Augment);
		code[1].add(Augment.AugmentWeapon);
		code[1].add(Augment.AugmentAddShot, 2);

		rocketSize=2;
		
		makeCard(tier, 2);

		///////////////////////////////////////////////////////////////////


		type=ModuleType.WEAPON;

		name[0]= "Bomb";
		rules[0]="";
		pic[0]= Gallery.bomb;
		cost[0]=2;
		effect[0]=calc(6);
		shots[0]=1;

		name[1]= "Magnify";
		rules[1]="Augment weapon card: +"+calc(2)+" damage";
		pic[1]= Gallery.magnify;
		cost[1]=1;
		effect[1]=0;
		code[1].add(Special.Augment);
		code[1].add(Augment.AugmentWeapon);
		code[1].add(Augment.AugmentDamage, calc(2));
		
		rocketSize=4;
		
		makeCard(tier, 2);

		
		///////////////////////////////////////////////////////////////////


		// Good names - cascade

		Collections.shuffle(decks[tier]);
	}
	private static void makeCard(int tier, int amount){
		for(int i=0;i<amount;i++){
			Card c = new Card(name, pic, cost, effect, shots, rules, code, type);
			c.rocketSize=rocketSize;
			c.mod=blankModule;
			decks[tier].add(c);
		}
		name=new String[]{"Setme!","Setme!"};
		pic=new Pic[2];
		cost= new int[]{777,888};
		effect=new int[]{777,888};
		shots=new int[]{0,0};
		rules=new String[]{"Setme!", "Setme!"};
		code= new CardCode[]{new CardCode(), new CardCode()};
		rocketSize=0;
		
	}

	private static double calcDouble(int cost){
		// 1+ because a card is worth a similar amount to an energy //
		return ((1+cost)*(calcTier/2d+1));
	}

	private static int calc(int cost){
		return (int) calcDouble(cost);
	}

	private static int calc(int baseCost, int extraCost){
		double base= calcDouble(baseCost);
		base+=(extraCost*(calcTier/2d+1))/2d;
		return (int) base;
	}
}
