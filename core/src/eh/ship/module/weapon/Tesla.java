package eh.ship.module.weapon;

import eh.assets.Gallery;
import eh.card.CardCode.AI;
import eh.card.CardCode.Augment;
import eh.card.CardCode.Special;

public class Tesla extends Weapon{
	public Tesla(){
		super("Tesla",Gallery.tesla,4,0, new int[]{4,11,16});
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.teslaCard[i];
		}
		name[0]="Tesla";
		cost[0]=3;
		cooldown[0]=1;
		effect[0]=4;
		shots[0]=1;
		rules[0]="";
		
		name[1]="Spark";
		cost[1]=2;
		cooldown[1]=0;
		effect[1]=0;
		shots[1]=0;
		rules[1]="Augment tesla card: Targeted, +2 damage";
		code[1].add(Special.Augment);
		code[1].add(Augment.AugmentThis);
		code[1].add(Augment.AugmentDamage,2);
		code[1].add(Augment.AugmentTargeted);
		code[1].add(AI.OtherCardsThisSystem,1);
		code[1].setPriority(1);
		
		name[2]="Thunder";
		cost[2]=4;
		cooldown[2]=1;
		effect[2]=7;
		shots[2]=1;
		rules[2]="Damages this module for 3";
		code[2].add(Special.selfDamage, 3);
		code[2].add(AI.DamageSelf, 3);
		code[2].add(AI.EvenChance);
		
		name[3]="Lightning";
		cost[3]=2;
		cooldown[3]=1;
		effect[3]=5;
		shots[3]=1;
		rules[3]="Play only if this module has taken major damage";
		code[3].add(Special.MustBeMajorDamaged);
		
		name[4]="Fork";
		cost[4]=7;
		cooldown[4]=1;
		effect[4]=4;
		shots[4]=2;
		rules[4]="";
		code[4].add(AI.SurplusEnergy,1);
		
		name[5]="";
		cost[5]=0;
		cooldown[5]=0;
		effect[5]=0;
		shots[5]=0;
		rules[5]="";
		cardPic[5]=Gallery.nothing;
	}
}
