package game.module.component.computer;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;

public class Beta extends Computer{

	public Beta() {
		super("Beta",null,5, new int[]{5,10,13});
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.cardComputer[i*2];
		}
		
		name[1]="Search";
		cost[1]=0;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="Get a card from any module";
		code[1].add(Special.ModuleChooser);
		code[1].add(Special.GetCardFromChosenModule, 1);
		code[1].add(Special.DiscardWhenChosen);
		code[1].add(AI.Ignore);
		
		
	}

}
