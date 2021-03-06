package game.module.component.weapon;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;

public class Pulse extends Weapon{

	public Pulse(int tier){
		super("Pulse",Gallery.pulse,8,0, new int[]{7,10,12}, tier);
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.pulseCard[i];
		}
		name[0]="Pulse";
		cost[0]=0;
		effect[0]=calc(0);
		shots[0]=1;
		rules[0]="";
		
		name[1]="Rapid Fire";
		cost[1]=5;
		effect[1]=calc(0);
		shots[1]=5;
		rules[1]="";
		code[1].add(AI.SurplusEnergy,2);
		
		name[2]="Heavy Shot";
		cost[2]=2;
		effect[2]=calc(0,2);
		shots[2]=1;
		rules[2]="";
		code[2].add(AI.SurplusEnergy,1);
		
		name[3]="Shatter";
		cost[3]=1;
		effect[3]=calc(0);
		shots[3]=1;
		rules[3]="+"+calc(0,2)+" |icondamage| if target has taken at least one major damage";
		code[3].add(Special.BonusVsMajorDamaged,calc(0,2));
		code[3].add(AI.MajorDamagedEnemySystems,3);
		code[3].setPriority(-1);
		
		name[4]="Resonate";
		cost[4]=1;
		effect[4]=0;
		shots[4]=0;
		rules[4]="Self boost 1: +1 |iconshot| to all pulse attacks";
		code[4].add(Special.BuffSelf);
		code[4].setBuff(new Buff(BuffType.BonusShot, true, 1, 1));
		code[4].add(AI.OtherCardsThisSystem,2);
		code[4].setPriority(1);
		
		name[5]="";
		cost[5]=0;
		effect[5]=0;
		shots[5]=0;
		rules[5]="";
		cardPic[5]=Gallery.nothing;
		
		name[6]="Pulse";
		cost[6]=1;
		effect[6]=5;
		shots[6]=1;
		rules[6]="";
		cardPic[6]=Gallery.nothing;
		code[6].add(Special.Targeted);
		cardPic[6]=Gallery.pulseCard[3];
	}
}
