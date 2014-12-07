package game.module.component.generator;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import util.image.Pic;

public class Two extends Generator{

	public Two() {
		super("Spark",null,2, new int[]{4,7,10});
		
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.cardGenerator[i*1];
		}
		
		name[1]="Refill";
		cost[1]=0;
		effect[1]=0;
		rules[1]="+2 |iconenergy| at end of phase if you use up all energy";
		code[1].add(Special.EnergyIfEmpty,2);
		code[1].add(AI.Ignore);
		

	}
}
