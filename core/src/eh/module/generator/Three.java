package eh.module.generator;

import eh.card.CardCode.AI;
import eh.card.CardCode.Special;
import eh.util.assets.Gallery;

public class Three extends Generator{
	public Three(){
		super("Neutron",null,3, new int[]{5,10,12});
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.cardGenerator[i*1];
		}
		
		name[1]="Refill";
		cost[1]=0;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="+2 energy at end of phase if you use all energy";
		code[1].add(Special.EnergyIfEmpty,2);
		code[1].add(AI.Ignore);
	}

}
