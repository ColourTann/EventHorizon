package game.module.component.generator;

import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;

public class Five extends Generator{
	public Five(){
		super("Negatron",null,5, new int[]{5,9,15});
		for(int i=0;i<=variants;i++){
			cardPic[i]=Gallery.cardGenerator[i*3];
		}

		name[1]="Power up";
		cost[1]=4;
		effect[1]=0;
		rules[1]="Self boost 10: +1 |iconenergy| income";
		code[1].add(Special.BuffSelf);
		code[1].setBuff(new Buff(BuffType.BonusIncome, true, 1, 10));
		code[1].add(AI.BeforeTurn, 3);
		code[1].add(AI.EvenChance);
	}

}
