package eh.ship.module.computer;

import eh.assets.Gallery;
import eh.card.CardCode.AI;
import eh.card.CardCode.Augment;
import eh.card.CardCode.Special;

public class Gamma extends Computer{

	public Gamma() {
		super("Gamma",null,6, new int[]{7,10,12});
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.cardComputer[i*3];
		}
		
		name[1]="Focus";
		cost[1]=0;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="Augment all cards in hand: targeted";
		code[1].add(Special.DiscardWhenPlayed);
		code[1].add(Augment.AugmentAll);
		code[1].add(Augment.AugmentTargeted);
		code[1].add(AI.OtherUntargeted, 3);
		code[1].add(AI.Singleton);
		code[1].setPriority(2);
		
		
	}
}
