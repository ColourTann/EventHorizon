package game.module.component.shield;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;
import util.image.Pic;

public class Blocker extends Shield{

	public Blocker(int tier){
		super("Blocker",Gallery.blocker, 6, new int[]{4,9,15}, tier);
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
		name[1]="Zap";
		cost[1]=0;
		effect[1]=calc(2);
		rules[1]="Damages your generator for "+calc(1);
		code[1].add(Special.AddShieldPoints);
		code[1].add(AI.RegularShield);
		code[1].add(AI.DamageGenerator, calc(1));
		code[1].add(Special.DamageGenerator, calc(1));
		code[1].add(AI.EvenChance);
				
		//bonus per major damage (maybe -cost)
		name[2]="Block";
		cost[2]=5;
		effect[2]=calc(4);
		rules[2]="-1 cost per major damage taken";
		code[2].add(Special.AddShieldPoints);
		code[2].add(Special.ReduceCostPerMajorDamage);
		code[2].add(AI.RegularShield);
		code[2].add(AI.MajorDamageTaken, 2);
		
		name[3]="Overheat";
		cost[3]=2;
		effect[3]=calc(7);
		rules[3]="Self Drain 4: disables system";
		code[3].add(Special.AddShieldPoints);
		code[3].add(Special.DrainSelf);
		code[3].setBuff(new Buff(BuffType.Disabled, false, 1, 4));
		code[3].add(AI.RegularShield);
		code[3].add(AI.MajorDamageTaken, 2);
		code[1].add(AI.ShieldAll, calc(3)*4);
		
		//shields all damaged systems
		name[4]="Block";
		cost[4]=1;
		effect[4]=calc(1);
		rules[4]="Shields all modules with major damage";
		code[4].add(Special.ShieldAllDamaged);
		code[4].add(AI.ModulesWithMajorDamageAndIncoming, 2);
	}

}
