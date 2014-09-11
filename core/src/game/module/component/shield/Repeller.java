package game.module.component.shield;


import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;

public class Repeller extends Shield{
	public Repeller(int tier){
		super("Repeller",Gallery.repeller, 5, new int[]{6,10,13}, tier);
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.repellerCard[i];
		}

		name[0]="Deflect";
		cost[0]=1;
		cooldown[0]=0;
		effect[0]=calc(1);
		rules[0]="";
		code[0].add(Special.AddShieldPoints);
		code[0].add(AI.RegularShield);
		
		name[1]="Consume";
		cost[1]=0;
		cooldown[1]=0;
		effect[1]=calc(0);
		rules[1]="Shields generator\nAbsorb: +1 energy";
		code[1].add(Special.AbsorbEnergy,1);
		code[1].add(Special.ShieldGenerator);
		code[1].add(AI.IncomingGenerator,1);
		
		name[2]="Phase";
		cost[2]=2;
		cooldown[2]=0;
		effect[2]=0;
		rules[2]="Your shield becomes immune to damage this turn";
		code[2].add(Special.ThisInvuln);
		code[2].add(AI.TotalIncomingThis,4);
		code[2].setPriority(1);
		
		name[3]="Precision";
		cost[3]=1;
		cooldown[3]=0;
		effect[3]=calc(2);
		rules[3]="Play only if your shield is undamaged";
		code[3].add(Special.AddShieldPoints);
		code[3].add(AI.RegularShield);
		code[3].add(Special.MustBeUndamaged);
		
		name[4]="Prioritise";
		cost[4]=2;
		cooldown[4]=0;
		effect[4]=calc(1);
		rules[4]="Shields both your weapons";
		code[4].add(Special.ShieldWeapons);
		code[4].add(AI.IncomingWeapons,calc(2));
		code[4].setPriority(1);
		
		//tutorishit//
		name[5]="";
		cost[5]=0;
		cooldown[5]=0;
		effect[5]=0;
		rules[5]="";
		cardPic[5]=Gallery.nothing;
	}
}
