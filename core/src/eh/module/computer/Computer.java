package eh.module.computer;

import eh.assets.Pic;
import eh.card.CardCode.AI;
import eh.card.CardCode.Augment;
import eh.card.CardCode.Special;
import eh.module.Module;

public abstract class Computer extends Module{
	public int maxCards;
	public Computer(String modName,Pic p, int maxCards, int[] thresholds) {
		super(modName, p,1,2,thresholds, 0);
		this.maxCards=maxCards;
		type=ModuleType.COMPUTER;
		name[0]="Lock-on";
		cost[0]=0;
		cooldown[0]=0;
		effect[0]=0;
		rules[0]="Augment weapon card: Targeted.\nThen draw a card";
		code[0].add(Special.Augment);
		code[0].add(Augment.AugmentWeapon);
		code[0].add(Augment.AugmentTargeted);
		code[0].add(Augment.AugmentDrawCard, 1);
		code[0].setPriority(2);
		code[0].add(AI.OverrideIfOtherSideIgnore);
	}
	public int getMaximumHandSize() {
		return (int) (destroyed?Math.ceil(maxCards/2f):maxCards);
	}
}
