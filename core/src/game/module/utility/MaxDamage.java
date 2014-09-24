package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.module.Module.ModuleType;

public class MaxDamage extends Utility{

	public MaxDamage(int tier) {
		super(tier, ModuleType.WEAPON, "Enhancer", "Cards that do 5 or more damage get +1 damage", Gallery.blaster, 1, 1);
		
		name[0]="Magnify";
		cost[0]=1;
		cooldown[0]=0;
		effect[0]=0;
		rules[0]="Augment weapon card: +"+calc(1,1)+" damage";
		cardPic[0]=Gallery.armour;
		code[0].add(Special.Augment);
		code[0].add(Augment.AugmentWeapon);
		code[0].add(Augment.AugmentDamage, calc(1,1));
		
		name[1]="Fetch";
		cost[1]=0;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="Get a card from either weapon module";
		cardPic[1]=Gallery.armour;
		code[1].add(Special.ModuleChooser);
		code[1].add(Special.ChooseWeapon);
		code[1].add(Special.GetCardFromChosenModule);
		code[1].add(Special.DiscardWhenChosen);
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
		if(baseEffect>=2&&c.type==ModuleType.WEAPON){
			return 1;
		}
		return 0;
	}

	@Override
	public int getBonusShots(Card c, int baseShots) {
		return 0;
	}

}