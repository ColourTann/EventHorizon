package eh.module.weapon;

import eh.card.CardCode.AI;
import eh.card.CardCode.Augment;
import eh.card.CardCode.Special;
import eh.util.assets.Gallery;

public class Laser extends Weapon{

	public Laser(int tier) {
		super("Laser",Gallery.laser, 4,0, new int[]{5,10,14}, tier);
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.laserCard[i];
		}
		name[0]="Laser";
		cost[0]=2;
		cooldown[0]=1;
		effect[0]=calc(2);
		shots[0]=1;
		rules[0]="";
		
		name[1]="Narrow beam";
		cost[1]=1;
		cooldown[1]=0;
		effect[1]=calc(0,1);
		shots[1]=1;
		rules[1]="Unshieldable";
		code[1].add(Special.Targeted);
		code[1].add(Special.Unshieldable);
		code[1].add(AI.OtherCardsThisSystem,1);
		
		name[2]="Overheat";
		cost[2]=6;
		cooldown[2]=1;
		effect[2]=calc(2,4);
		shots[2]=1;
		rules[2]="";
		code[2].add(AI.SurplusEnergy,1);
		
		name[3]="Intensify";
		cost[3]=1;
		cooldown[3]=0;
		effect[3]=0;
		shots[3]=0;
		rules[3]="Augment laser: +"+calc(0,1)+" damage, targeted";
		code[3].add(Special.Augment);
		code[3].add(Augment.AugmentThis);
		code[3].add(Augment.AugmentDamage,calc(0,1));
		code[3].add(Augment.AugmentTargeted);
		code[3].add(AI.OtherCardsThisSystem,1);
		code[3].setPriority(1);
		
		name[4]="Overwhelm";
		cost[4]=3;
		cooldown[4]=1;
		effect[4]=calc(1);
		shots[4]=1;
		rules[4]="+"+calc(2)+" damage against undamaged modules";
		code[4].add(Special.BonusVsPristine, calc(2));
		code[4].add(AI.PlayerPristineSystems,3);
		
		name[5]="Laser";
		cost[5]=1;
		cooldown[5]=0;
		effect[5]=7;
		shots[5]=1;
		rules[5]="";
		code[5].add(Special.Targeted);
		code[5].add(Special.BonusVsGenerator);
		code[5].add(AI.BetterAgainstSpecificSystem);
		cardPic[5]=Gallery.laserCard[0];
		
		name[6]="Laser";
		cost[6]=1;
		cooldown[6]=0;
		effect[6]=7;
		shots[6]=1;
		rules[6]="";
		code[6].add(Special.Targeted);
		code[6].add(Special.BonusVsComputer);
		code[6].add(AI.BetterAgainstSpecificSystem);
		cardPic[6]=Gallery.laserCard[0];
	}
	
	
}
