package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.module.Module.ModuleType;
import game.module.component.Component;
import util.image.Pic;

public class Focuser extends Utility{

	public Focuser(int tier) {
		super(tier, ModuleType.UTILITY, "Focuser", "Targeted single-shot attacks cost 1 less", Gallery.blaster, 1, 1);
		//Cards are: targeted glitch 3: takes extra damage and... another augment targeted + draw?
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
	public void playCardEffect(Card c) {
	}

	@Override
	public int getBonusEffect(Card c, int baseEffect) {
		return 0;
	}

	@Override
	public int getBonusShots(Card c, int baseShots) {
		return 0;
	}

	@Override
	public int getBonusCost(Card c, int baseCost) {
		return 0;
	}

	@Override
	public boolean overrideDefeat() {
		return false;
	}

	@Override
	public void afterBattle() {
	}

	@Override
	public void onScramble(Component c) {
	}

}
