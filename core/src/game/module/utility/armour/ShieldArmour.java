package game.module.utility.armour;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.module.component.Component;
import game.module.junk.ShieldPoint;

public class ShieldArmour extends Armour{

	public ShieldArmour(int tier) {
		super(.6f+tier*.2f, tier, "Perimeter", "Shield all systems for 1 every turn", Gallery.auroraComputer, 0, 0);
		name[0]="Charge";
		cost[0]=0;
		cooldown[0]=0;
		effect[0]=0;
		rules[0]="+1 energy";
		cardPic[0]=Gallery.armour;
		code[0].add(Special.GainEnergy,1);
		code[0].setPriority(2);
		
		
		name[1]="Repurpose";
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
