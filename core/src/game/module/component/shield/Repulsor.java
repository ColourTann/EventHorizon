package game.module.component.shield;


import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;

public class Repulsor extends Shield{
	public Repulsor(int tier){
		super("Repulsor",Gallery.repulsor,3, new int[]{7,9,14}, tier);
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.repulsorCard[i];
		}

		name[0]="Repulse";
		cost[0]=2;
		effect[0]=calc(2);
		rules[0]="";
		code[0].add(Special.AddShieldPoints);
		code[0].add(AI.RegularShield);
		
		name[1]="Reinforce";
		cost[1]=3;
		effect[1]=calc(4);
		rules[1]="Shields single system";
		code[1].add(Special.ModuleChooser);
		code[1].add(Special.ShieldChosenModule);
		code[1].add(AI.SingleModuleIncoming,calc(3));
		code[1].setPriority(1);
		
		name[2]="Charge";
		cost[2]=4;
		effect[2]=0;
		rules[2]="Self boost infinite: +"+calc(0)+" |iconshield|";
		code[2].add(AI.BeforeTurn, 3);
		code[2].add(AI.LowChance);
		code[2].setPriority(1);		
		
		code[2].add(Special.BuffSelf);
		code[2].setBuff(new Buff(BuffType.BonusEffeect, true, 1, -1));
		
		// gotta replace ooh shield an undamaged  module! cheaper costs 1
		name[3]="Shimmer";
		cost[3]=1;
		effect[3]=calc(2);
		rules[3]="Shields only undamaged modules";
		code[3].add(Special.AddShieldPoints);
		code[3].add(AI.RegularShield);
		code[3].add(Special.ShieldOnlyPristine);
		code[3].add(AI.IncomingUndamaged, calc(2));
		
		name[4]="Encase";
		cost[4]=5;
		effect[4]=calc(1);
		rules[4]="Shields all modules";
		code[4].add(Special.ShieldAll);
		code[4].add(AI.ShieldAll,calc(5));
		code[4].setPriority(1);
	}
}
