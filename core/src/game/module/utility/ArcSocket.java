package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Special;

public class ArcSocket extends Utility{

	public ArcSocket(int tier) {
		super(tier, ModuleType.SHIELD, "Arc socket", "If you've taken 3+ major damage: shields -1 cost, +1 effect", Gallery.blaster, 1, 10);
		
		name[0]="weirdname";
		cost[0]=1;
		cooldown[0]=0;
		effect[0]=calc(1);
		rules[0]="+1 effect per major damage taken";
		cardPic[0]=Gallery.armour;
		code[0].add(Special.BonusPerMajorDamage, 1);
		code[0].add(Special.AddShieldPoints);
		code[0].add(AI.RegularShield);
		
		name[1]="boossterer";
		cost[1]=0;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="+1 shield effect this turn";
		cardPic[1]=Gallery.armour;
		code[1].add(Special.BonusEffectToShield, 1);
		code[1].add(AI.Ignore);
	
		cardType=ModuleType.SHIELD;
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
	public void afterBattle() {
	}

	@Override
	public int getBonusCost(Card c, int baseCost) {
		return 0;
	}

	@Override
	public boolean overrideDefeat() {
		return false;
	}

}
