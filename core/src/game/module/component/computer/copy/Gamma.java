package game.module.component.computer.copy;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;

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
