package game.module.component.computer;

import util.image.Pic;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.module.component.Component;

public abstract class Computer extends Component{
	public int maxCards;
	private int bonusCards;
	public Computer(String modName,Pic p, int maxCards, int[] thresholds) {
		super(-1, modName, p,1,2,thresholds);
		this.maxCards=maxCards;
		type=ModuleType.COMPUTER;
		cardType=type;
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
		return (int) (destroyed?Math.ceil(maxCards/2f):maxCards)+bonusCards;
	}
	public void addBonusCards(int amount){
		bonusCards+=amount;
	}
	public void resetBonusCards(){
		bonusCards=0;
	}
}
