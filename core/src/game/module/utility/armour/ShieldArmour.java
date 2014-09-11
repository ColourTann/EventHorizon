package game.module.utility.armour;

import game.card.Card;
import game.module.Module;
import game.module.stuff.ShieldPoint;

public class ShieldArmour extends Armour{

	public ShieldArmour(int tier) {
		super(.6f+tier*.2f);
	}

	@Override
	public void startBattleEffect() {
	}

	@Override
	public void beginTurnEffect() {
		for(Module m:ship.modules)m.shield(new ShieldPoint(null, false), false);
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
