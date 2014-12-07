package game.module.component.computer;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import util.image.Pic;

public class RubbishComputer extends Computer{

	public RubbishComputer() {
		super("TI-85", Gallery.auroraComputer, 3, new int[]{5,8,12});
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.cardComputer[i*1];
		}
		name[1]="Subroutine";
		cost[1]=0;
		effect[1]=0;
		rules[1]="Draw two cards";
		code[1].add(Special.DrawCard, 2);
		code[1].add(Special.DiscardWhenPlayed);
		code[1].add(AI.CheckOriginalFirst);
		code[1].setPriority(2);
	}

}
