package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;

public class BasicArmour extends Armour{

	public BasicArmour(int tier) {
		super(1+tier*.25f,tier, "Plating", "Basic armour", Gallery.auroraComputer, 0, 0);
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
