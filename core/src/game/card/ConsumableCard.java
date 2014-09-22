package game.card;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.Module.ModuleType;

import java.util.ArrayList;
import java.util.Collections;

import util.image.Pic;

public class ConsumableCard {
	
	private static String[] name = new String[2];
	private static String[] rules = new String[2];
	private static Pic[] pic = new Pic[2];
	private static int[] cost = new int[2];
	private static int[] effect = new int[2];
	private static int[] shots = new int[2];
	
	private static CardCode[] code = new CardCode[]{new CardCode(), new CardCode()};
	private static ModuleType type;
	
	private static ArrayList<Card>[] decks = new ArrayList[5];
	
	public static Card get(int tier){
		if(decks[tier]==null||decks[tier].size()==0) setup(tier);
		return decks[tier].remove(0);
	}
	private static void setup(int tier){
		if(decks[tier]==null)decks[tier]=new ArrayList<Card>();
		
		name[0]= "Flow";
		rules[0]="Shield chosen system";
		pic[0]= Gallery.armour;
		cost[0]=0;
		effect[0]=5;
		code[0].add(Special.ModuleChooser);
		code[0].add(Special.ShieldChosenModule);

		name[0]= "Overshock";
		rules[0]="Destroy the enemy shield system";
		pic[0]= Gallery.armour;
		cost[0]=10;
		effect[0]=0;
		code[0].add(Special.DestroyEnemyShield);
		
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
	}
}
