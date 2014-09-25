package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;
import game.module.component.Component;
import game.module.junk.DamagePoint;
import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;
import util.update.TextWisp;
import util.update.TextWisp.WispType;

public class ShockbackHull extends Armour{

	public ShockbackHull(int tier) {
		super(.8f+tier*.2f, tier, "Shockback", "When you take major damage, damages a random enemy module for "+statiCalc(1,0,tier), Gallery.auroraComputer, 0, 0);
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
	public boolean overrideDefeat() {
		return false;
	}

	@Override
	public void afterBattle() {
	}

	@Override
	public void onTakeMajorDamage() {
		Component c= ship.getEnemy().getRandomUndestroyedComponent();
		for(int i=0;i<calc(1);i++) c.damage(new DamagePoint(null));
		new TextWisp("Revenge", Font.medium, c.getCenter().add(new Pair(c.ship.player?0:-500,-60)), WispType.Regular); 
		
	}

}
