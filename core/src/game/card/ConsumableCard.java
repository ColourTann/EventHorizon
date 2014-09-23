package game.card;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.SpecialComponent;
import game.module.junk.ModuleInfo;

import java.util.ArrayList;
import java.util.Collections;

import util.image.Pic;

public class ConsumableCard {
	private static Module blankModule = new SpecialComponent();
	private static String[] name = new String[2];
	private static String[] rules = new String[2];
	private static Pic[] pic = new Pic[2];
	private static int[] cost = new int[2];
	private static int[] effect = new int[2];
	private static int[] shots = new int[2];

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
		pic[0]= Gallery.armour;
		cost[0]=0;
		effect[0]=0;
		code[0].add(Special.ModuleChooser);
		code[0].add(Special.ImmuneChosenModule);

		name[1]= "Flow";
		rules[1]="Shields all modules";
		pic[1]= Gallery.armour;
		cost[1]=1;
		effect[1]=calc(1);
		code[1].add(Special.ShieldAll);

		makeCard(tier);

		// Good names - cascade

		Collections.shuffle(decks[tier]);
	}
	private static void makeCard(int tier){
		Card c = new Card(name, pic, cost, effect, new int[]{0,0}, shots, rules, code, type);
		decks[tier].add(c);
		name=new String[]{"Setme!","Setme!"};
		pic=new Pic[2];
		cost= new int[]{777,888};
		effect=new int[]{777,888};
		shots=new int[]{0,0};
		rules=new String[]{"Setme!", "Setme!"};
		code= new CardCode[]{new CardCode(), new CardCode()};
		c.mod=blankModule;
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
