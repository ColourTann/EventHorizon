package game.module.component.generator;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;

public class Four extends Generator{
	public Four(){
		super("Positron",null,4, new int[]{6,10,13});
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.cardGenerator[i*2];
		}

		name[1]="Stoke";
		cost[1]=0;
		effect[1]=0;
		rules[1]="+2 energy, 2 damage to this system";
		code[1].add(Special.GainEnergy, 2);
		code[1].add(Special.selfDamage, 2);
		code[1].add(AI.DamageSelf, 2);
		code[1].add(AI.LowChance);
		code[1].setPriority(2);
	}
}
