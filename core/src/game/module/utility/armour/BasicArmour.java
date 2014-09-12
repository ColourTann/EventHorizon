package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Special;

public class BasicArmour extends Armour{

	public BasicArmour(int tier) {
		super(1+tier*.25f,tier, "Plating", Gallery.auroraComputer, 0, 0);
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
