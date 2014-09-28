package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;
import game.module.component.Component;
import game.module.component.weapon.Weapon;
import game.module.junk.DamagePoint;
import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;
import util.update.TextWisp;
import util.update.TextWisp.WispType;

public class VoltaicCarapace extends Armour{

	public VoltaicCarapace(int tier) {
		super(.85+tier*.25, tier, "Voltaic Carapace", "On taking major damage: Scramble an enemy weapon", Gallery.voltaicCarapce, 0, 0);
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
		int random=(int)(Math.random()*2);
		Weapon w=ship.getEnemy().getWeapons()[random];
		if(!w.destroyed)w.scramble(null);
		else ship.getEnemy().getWeapons()[1-random].scramble(null);
		
	}

}
