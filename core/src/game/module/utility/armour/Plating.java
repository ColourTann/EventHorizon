package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;

public class Plating extends Armour{

	public Plating(int tier) {
		super(1+tier*.5,tier, "Plating", "Basic armour", Gallery.basicArmour, 0, 0);
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
