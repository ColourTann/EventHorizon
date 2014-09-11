package game.module.utility.armour;

import game.card.Card;
import game.module.Module;
import game.module.stuff.ShieldPoint;

public class BasicArmour extends Armour{

	public BasicArmour(int tier) {
		super(1+tier*.25f);
	}

	@Override
	public void startBattleEffect() {
		ship.addEnergy(50);
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
