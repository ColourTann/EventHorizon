package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.Special;

public class OrganicShell extends Armour{

	public OrganicShell(int tier) {
		super(.95+tier*.25, tier, "Organic Shell", "Repair 2 extra damage between each fight", Gallery.organicShell, 0, 0);
		
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

	@Override
	public int getBonusCost(Card c, int baseCost) {
		return 0;
	}

	@Override
	public boolean overrideDefeat() {
		return false;
	}

	@Override
	public void onTakeMajorDamage() {
	}

}
