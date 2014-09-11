package game.module.utility.armour;

import game.card.Card;

public class RegenArmour extends Armour{

	public RegenArmour(int tier) {
		super(.9f+tier*.2f);
		
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

}
