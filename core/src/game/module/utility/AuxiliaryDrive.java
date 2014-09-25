package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;

public class AuxiliaryDrive extends Utility{

	public AuxiliaryDrive(int tier) {
		super(tier, ModuleType.UTILITY,"Auxiliary Drive", "-1 hand size, +2 cards next turn if you use all cards", Gallery.blaster, 1, 1);
		
		name[0]="Crunch";
		cost[0]=0;
		cooldown[0]=0;
		effect[0]=0;
		rules[0]="Draw 2 cards";
		cardPic[0]=Gallery.armour;
		code[0].add(Special.DrawCard,2);
		code[0].add(Special.DiscardWhenPlayed);
		code[0].setPriority(2);
		
		
		name[1]="Store";
		cost[1]=0;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="Discard a card, +2 hand size next turn";
		cardPic[1]=Gallery.armour;
		code[1].add(Augment.AugmentAny);
		code[1].add(Augment.AugmentDiscard);
		code[1].add(Special.DiscardOthers,1);
		code[1].add(Augment.AugmentAddBonusHandSize, 2);
		code[1].add(AI.Ignore);
		
		cardType=ModuleType.UTILITY;
		
	}

	@Override
	public void startBattleEffect() {
		ship.getComputer().addBonusCards(-1);
	}

	@Override
	public void beginTurnEffect() {
	}

	@Override
	public void endTurnEffect() {
		ship.getComputer().addBonusCards(-1);
		if(ship.hand.size()==0)ship.getComputer().addBonusCards(2);
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
