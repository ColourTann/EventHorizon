package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;
import util.image.Pic;

public class CrystalLattice extends Armour{

	public CrystalLattice(int tier){
		super(.95+tier*.25,tier, "Crystal Lattice", "6 major damage required to destroy", Gallery.crystalLattice, 0, 0);
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
	public void afterBattle() {
	}

	@Override
	public boolean overrideDefeat() {
		return ship.getMajorDamage()<6;
	}

	@Override
	public void onTakeMajorDamage() {
	}

}
