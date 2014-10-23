package game.module.component.weapon;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;

public class Laser extends Weapon{

	public Laser(int tier) {
		super("Laser",Gallery.laser, 4,0, new int[]{5,10,14}, tier);
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.laserCard[i];
		}
		name[0]="Laser";
		cost[0]=2;
		effect[0]=calc(2);
		shots[0]=1;
		rules[0]="";
		
		name[1]="Narrow beam";
		cost[1]=1;
		effect[1]=calc(0,1);
		shots[1]=1;
		rules[1]="Unshieldable";
		code[1].add(Special.Unshieldable);
		code[1].add(AI.OtherCardsThisSystem,1);
		
		name[2]="Incinerate";
		cost[2]=5;
		effect[2]=calc(4);
		shots[2]=1;
		rules[2]="";
		code[2].add(Special.Targeted);
		code[2].add(AI.SurplusEnergy,1);
		
		name[3]="Targeting Laser";
		cost[3]=1;
		effect[3]=0;
		shots[3]=0;
		rules[3]="Augment Weapon: +"+calc(0,1)+" |icondamage| and |icontarget|";
		code[3].add(Special.Augment);
		code[3].add(Augment.AugmentWeapon);
		code[3].add(Augment.AugmentDamage,calc(0,1));
		code[3].add(Augment.AugmentTargeted);
		code[3].add(AI.OtherCardsThisSystem,1);
		code[3].setPriority(1);

		name[4]="Weaken";
		cost[4]=4;
		effect[4]=0;
		shots[4]=0;
		rules[4]="Targeted drain |iconinfinity|: Takes +"+calc(0)+" |icondamage| from each shot";
		code[4].add(Special.DrainTarget);
		code[4].setBuff(new Buff(BuffType.TakesExtraDamage, false, calc(0), -1));
		code[4].add(AI.BeforeTurn, 3);
		code[4].setPriority(1);
		
		name[5]="Laser";
		cost[5]=1;
		effect[5]=7;
		shots[5]=1;
		rules[5]="";
		code[5].add(Special.Targeted);
		code[5].add(Special.BonusVsGenerator);
		code[5].add(AI.BetterAgainstSpecificSystem);
		cardPic[5]=Gallery.laserCard[0];
		
		name[6]="Laser";
		cost[6]=1;
		effect[6]=7;
		shots[6]=1;
		rules[6]="";
		code[6].add(Special.Targeted);
		code[6].add(Special.BonusVsComputer);
		code[6].add(AI.BetterAgainstSpecificSystem);
		cardPic[6]=Gallery.laserCard[0];
	}
	
	
}
