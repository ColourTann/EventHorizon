package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import util.image.Pic;

public class RapidFire extends Utility{

	public RapidFire(int tier) {
		super(tier, "Rapid Fire", Gallery.blaster, 1, 10);
		type=ModuleType.WEAPON;
		
		name[0]="Barrage";
		cost[0]=2;
		cooldown[0]=0;
		effect[0]=1;
		rules[0]="";
		shots[0]=3;
		cardPic[0]=Gallery.armour;
		
		name[1]="Duplicate";
		cost[1]=1;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="Augment weapon card: +1 shot";
		cardPic[1]=Gallery.armour;
		code[1].add(Special.Augment);
		code[1].add(Augment.AugmentWeapon);
		code[1].add(Augment.AugmentAddShot, 1);
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
	public void playCard(Card c) {
	}

	@Override
	public void afterBattle() {
	}

}
