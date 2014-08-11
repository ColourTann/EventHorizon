package eh.module.shield;


import eh.assets.Gallery;
import eh.card.CardCode.AI;
import eh.card.CardCode.Special;

public class Repulsor extends Shield{
	public Repulsor(int tier){
		super("Repulsor",Gallery.repulsor,3, new int[]{7,9,14}, tier);
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.repulsorCard[i];
		}

		name[0]="Repulse";
		cost[0]=2;
		cooldown[0]=1;
		effect[0]=3;
		rules[0]="";
		code[0].add(Special.AddShieldPoints);
		code[0].add(AI.RegularShield);
		
		name[1]="Reinforce";
		cost[1]=3;
		cooldown[1]=0;
		effect[1]=5;
		rules[1]="Shields single system";
		code[1].add(Special.ModuleChooser);
		code[1].add(Special.ShieldChosenModule);
		code[1].add(AI.SingleModuleIncoming,4);
		code[1].setPriority(1);
		
		name[2]="Charge";
		cost[2]=4;
		cooldown[2]=0;
		effect[2]=0;
		rules[2]="+1 effect to this module until end of battle";
		code[2].add(AI.Ignore);
		code[2].add(Special.PermanentIncreaseEffect, 1);
		
			
		name[3]="Overcycle";
		cost[3]=2;
		cooldown[3]=1;
		effect[3]=7;
		rules[3]="Triple scrambles this system";
		code[3].add(Special.SelfScramble,3);
		code[3].add(Special.AddShieldPoints);
		code[3].add(AI.RegularShield);
		code[3].add(AI.DamagedModules, 3);
		
		name[4]="Encase";
		cost[4]=5;
		cooldown[4]=1;
		effect[4]=2;
		rules[4]="Shields all modules";
		code[4].add(Special.Bubble);
		code[4].add(AI.ShieldAll,6);
		code[4].setPriority(1);
	}
}
