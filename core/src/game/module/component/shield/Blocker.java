package game.module.component.shield;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import util.image.Pic;

public class Blocker extends Shield{

	public Blocker(int tier){
		super("Blocker",Gallery.deflector, 6, new int[]{4,9,15}, tier);
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.deflectorCard[i];
		}
		name[0]="Block";
		cost[0]=1;
		effect[0]=calc(1);
		rules[0]="";
		code[0].add(Special.AddShieldPoints);
		code[0].add(AI.RegularShield);
		
		// Damage your generator //
		//shield target system//
		name[1]="Zap";
		cost[1]=0;
		effect[1]=calc(1,1);
		rules[1]="Shield all systems. Damages you generator for "+calc(0,1);
		code[1].add(Special.ShieldAll);
		code[1].add(Special.DamageGenerator, calc(0,1));
		
		code[1].add(AI.ShieldAll, calc(0,1)*4);
		
		//bonus per major damage (maybe -cost)
		name[2]="Block";
		cost[2]=3;
		effect[2]=calc(2);
		rules[2]="-1 cost per major damage taken";
		code[2].add(Special.AddShieldPoints);
		code[2].add(AI.RegularShield);
		
		//shields all
		name[3]="Overheat";
		cost[3]=2;
		cooldown[3]=4;
		effect[3]=calc(6);
		rules[3]="Warning: disables this system for 4 turns";
		code[3].add(Special.AddShieldPoints);
		code[3].add(AI.RegularShield);
		code[3].add(AI.DamagedModules, 3);
		
		//just a big shield
		name[4]="Block";
		cost[4]=1;
		effect[4]=calc(1);
		rules[4]="";
		code[4].add(Special.AddShieldPoints);
		code[4].add(AI.RegularShield);
	}

}
