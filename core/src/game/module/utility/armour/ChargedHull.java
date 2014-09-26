package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;
import util.image.Pic;

public class ChargedHull extends Armour{

	public ChargedHull(int tier){
		super(.8+tier*.25,tier, "Charged hull", "Start each battle with +3 energy", Gallery.chargedHull, 0, 0);
	}

	@Override
	public void onTakeMajorDamage() {
	}

	@Override
	public void startBattleEffect() {
		System.out.println("adding energy");
		ship.addEnergy(3, true);
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

}
