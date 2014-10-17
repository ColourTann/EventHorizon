package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.module.Module.ModuleType;
import game.module.component.Component;
import util.image.Pic;

public class Refiller extends Utility{

	public Refiller(int tier) {
		super(tier, ModuleType.WEAPON, "Refiller", "+1 z if you end your turn with 0 z and >1 card", Gallery.blaster, 1, 1);
		
		//discard a card, gain energy equal to half its cost rounded down//
		
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
