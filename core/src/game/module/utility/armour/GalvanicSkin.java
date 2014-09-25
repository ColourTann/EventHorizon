package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.module.component.Component;
import game.module.junk.ShieldPoint;

public class GalvanicSkin extends Armour{

	public GalvanicSkin(int tier) {
		super(.7f+tier*.15f, tier, "Galvanic Skin", "Shield all systems for "+statiCalc(0,0,tier) +" every turn", Gallery.auroraComputer, 0, 0);
	
	}

	@Override
	public void startBattleEffect() {
	}

	@Override
	public void beginTurnEffect() {
		for(Component c:ship.components){
			for(int i=0;i<calc(0);i++)c.shield(new ShieldPoint(null, false), false);
		}
	}

	@Override
	public void endTurnEffect() {
	}

	@Override
	public void playCardEffect(Card c) {
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

	@Override
	public int getBonusCost(Card c, int baseCost) {
		return 0;
	}

	@Override
	public boolean overrideDefeat() {
		return false;
	}

	@Override
	public void onTakeMajorDamage() {
	}

}
