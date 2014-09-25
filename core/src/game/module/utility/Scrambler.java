package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.module.Module.ModuleType;
import util.image.Pic;

public class Scrambler extends Utility{

	public Scrambler(int tier) {
		super(tier, ModuleType.UTILITY, "Scrambler", "Your ship is immune to scramble", Gallery.blaster, 1, 1);
		
		name[0]="Scramble";
		cost[0]=0;
		cooldown[0]=0;
		effect[0]=0;
		rules[0]="Scramble chosen system";
		cardPic[0]=Gallery.armour;
		code[0].add(Special.ModuleChooser);
		code[0].add(Special.ChooseEnemyModule);
		code[0].add(Special.ScrambleChosenModule, 1);		
		
		name[1]="Muddle";
		cost[1]=2;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="Scramble chosen system twice";
		cardPic[1]=Gallery.armour;
		code[1].add(Special.ModuleChooser);
		code[1].add(Special.ChooseEnemyModule);
		code[1].add(Special.ScrambleChosenModule, 2);	
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
