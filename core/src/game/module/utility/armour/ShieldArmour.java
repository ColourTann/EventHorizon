package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;
import game.module.component.Component;
import game.module.junk.ShieldPoint;

public class ShieldArmour extends Armour{

	public ShieldArmour(int tier) {
		super(.6f+tier*.2f, tier, "Plating", "Shield all systems for 1 every turn", Gallery.auroraComputer, 0, 0);
	}

	@Override
	public void startBattleEffect() {
	}

	@Override
	public void beginTurnEffect() {
		for(Component c:ship.components)c.shield(new ShieldPoint(null, false), false);
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
