package game.module.component.weapon;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;

public class Tesla extends Weapon{
	public Tesla(int tier){
		super("Tesla",Gallery.tesla,4,0, new int[]{4,11,15}, tier);
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.teslaCard[i];
		}
		name[0]="Tesla";
		cost[0]=3;
		effect[0]=calc(3);
		shots[0]=1;
		rules[0]="";
		
		name[1]="Spark";
		cost[1]=2;
		effect[1]=0;
		shots[1]=0;
		rules[1]="Augment tesla card: +"+calc(1)+" |icondamage|";
		code[1].add(Special.Augment);
		code[1].add(Augment.AugmentThis);
		code[1].add(Augment.AugmentDamage,calc(1));
		code[1].add(AI.OtherCardsThisSystem,1);
		code[1].setPriority(1);
		
		name[2]="Thunder";
		cost[2]=4;
		effect[2]=calc(4,1)+1;
		shots[2]=1;
		rules[2]="Damages this module for "+calc(5)/2;
		code[2].add(Special.selfDamage,calc(5)/2);
		code[2].add(AI.DamageSelf, calc(5)/2);
		code[2].add(AI.EvenChance);
		
		name[3]="Lightning";
		cost[3]=2;
		effect[3]=calc(4);
		shots[3]=1;
		rules[3]="Play only if this module has taken major damage";
		code[3].add(Special.MustBeMajorDamaged);
		
		name[4]="Fork";
		cost[4]=7;
		effect[4]=calc(3);
		shots[4]=2;
		rules[4]="";
		code[4].add(AI.SurplusEnergy,1);
		
		name[5]="";
		cost[5]=0;
		effect[5]=0;
		shots[5]=0;
		rules[5]="";
		cardPic[5]=Gallery.nothing;
	}
}
