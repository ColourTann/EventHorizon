package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.module.Module.ModuleType;
import util.image.Pic;

public class Furnace extends Utility{

	public Furnace(int tier) {
		super(tier, ModuleType.UTILITY,"Fusion Furnace", "+1 energy if you end your turn with 7+ energy", Gallery.blaster, 1, 10);
		
		name[0]="Divert";
		cost[0]=0;
		cooldown[0]=0;
		effect[0]=0;
		rules[0]="+1 energy";
		cardPic[0]=Gallery.armour;
		code[0].add(Special.GainEnergy,1);
		code[0].setPriority(2);
		
		
		name[1]="Burn";
		cost[1]=0;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="Discard a card, +2 energy";
		cardPic[1]=Gallery.armour;
		code[1].add(Special.DiscardOthers,1);
		code[1].add(Augment.AugmentAny);
		code[1].add(Augment.AugmentDiscard);
		code[1].add(Augment.AugmentGainEnergy, 2);
		code[1].add(AI.Ignore);
	
		cardType=ModuleType.UTILITY;
	}

	@Override
	public void startBattleEffect() {
	}

	@Override
	public void beginTurnEffect() {
	}

	@Override
	public void endTurnEffect() {
		if(ship.getEnergy()>=7)ship.addEnergy(1, true);
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

}
