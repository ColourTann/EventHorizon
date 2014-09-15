package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;

public class MaxDamage extends Utility{

	public MaxDamage(int tier) {
		super(tier, "Flux Alternator", "Weapons that do 5 or more damage get +1 bonus damage", Gallery.blaster, 1, 10);
		type=ModuleType.WEAPON;
		name[0]="Strike";
		cost[0]=3;
		cooldown[0]=0;
		effect[0]=5;
		rules[0]="";
		shots[0]=1;
		cardPic[0]=Gallery.armour;

		name[1]="Magnify";
		cost[1]=1;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="Augment weapon card: +2 damage";
		cardPic[1]=Gallery.armour;
		code[1].add(Special.Augment);
		code[1].add(Augment.AugmentWeapon);
		code[1].add(Augment.AugmentDamage, 2);

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

	@Override
	public int getBonusEffect(Card c, int baseEffect) {
		if(baseEffect>=5&&c.type==ModuleType.WEAPON){
			return 1;
		}
		return 0;
	}

	@Override
	public int getBonusShots(Card c, int baseShots) {
		return 0;
	}

}