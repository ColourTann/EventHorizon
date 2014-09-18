package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.Special;

public class RegenArmour extends Armour{

	public RegenArmour(int tier) {
		super(.9f+tier*.2f, tier, "Plating", "Repair 1 extra damage between each fight", Gallery.auroraComputer, 1, 1);
		name[0]="Repair";
		cost[0]=0;
		cooldown[0]=0;
		effect[0]=0;
		rules[0]="Repair "+calc(1)+" minor damage.";
		cardPic[0]=Gallery.armour;
		code[0].add(Special.ModuleChooser);
		code[0].add(Special.RepairChosenModule, calc(1));
		//code[0].add(AI.Ignore);
	}

	@Override
	public void startBattleEffect() {
	}

	@Override
	public void beginTurnEffect() {
	}

	@Override
	public void endTurnEffect() {
	}

	@Override
	public void playCard(Card c) {
	}

	@Override
	public void afterBattle() {
	}

	@Override
	public int getBonusEffect(Card c, int baseEffect) {
		return 0;
	}

	@Override
	public int getBonusShots(Card c, int baseShots) {
		return 0;
	}

}
