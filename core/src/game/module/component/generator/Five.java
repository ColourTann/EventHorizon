package game.module.component.generator;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;

public class Five extends Generator{
	public Five(){
		super("Negatron",null,5, new int[]{5,9,15});
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.cardGenerator[i*3];
		}

		name[1]="Power up";
		cost[1]=4;
		effect[1]=0;
		rules[1]="+1 energy income";
		code[1].add(Special.EnergyIncome, 1);
		code[1].add(AI.Ignore);
	}

}
