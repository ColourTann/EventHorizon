package game.module.component.weapon;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;

public class Ray extends Weapon{

	public Ray(int tier) {
		super("Ray",Gallery.ray,8,0, new int[]{6,10,13}, tier);
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.rayCard[i];
		}
		name[0]="Ray";
		cost[0]=1;
		effect[0]=calc(1);
		shots[0]=1;
		rules[0]="";
		
		name[1]="Frazzle";
		cost[1]=1;
		effect[1]=calc(0);
		shots[1]=1;
		rules[1]="+"+calc(1)+" |icondamage| against undamaged modules";
		code[1].add(Special.BonusVsWeapon, calc(1));
		code[1].add(AI.PlayerPristineSystems, 3);
		
		
		name[2]="Current";
		cost[2]=2;
		effect[2]=calc(1);
		shots[2]=1;
		rules[2]="+"+calc(1)+" |icondamage| against weapons";
		code[2].add(Special.BonusVsWeapon, calc(1));
		code[2].add(AI.BetterAgainstSpecificSystem);

		name[3]="Initialise";
		cost[3]=1;
		effect[3]=0;
		shots[3]=0;
		rules[3]="Self boost 1:|n|-1 cost";
		code[3].add(Special.BoostSelf);
		code[3].setBuff(new Buff(BuffType.ReduceCost, true, 1, 1, null));
		code[3].add(AI.OtherCardsThisSystem, 2);
		code[3].add(AI.ReduceCost);
		code[3].setPriority(2);

	
		
		name[4]="Seek";
		cost[4]=4;
		effect[4]=calc(1,2);
		shots[4]=1;
		rules[4]="Unshieldable";
		code[4].add(Special.Unshieldable);
		code[4].add(AI.SurplusEnergy,1);
		
		//tutorishit//
		name[5]="Ray";
		cost[5]=1;
		effect[5]=2;
		shots[5]=1;
		rules[5]="";
		code[5].add(Special.Targeted);
		code[5].add(Special.BonusVsGenerator,0);
		code[5].add(AI.BetterAgainstSpecificSystem);
		code[5].setPriority(2);
		cardPic[5]=Gallery.rayCard[0];
		
		
	}
}