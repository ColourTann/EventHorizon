package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Augment;
import game.card.CardCode.Special;
import game.module.Module.ModuleType;
import game.module.component.Component;

public class Repeater extends Utility{

	public Repeater(int tier) {
		super(tier, ModuleType.UTILITY, "Repeater", "Weapons cards with 3+ shots get 1 bonus shot", Gallery.blaster, 1, 1);
		
		rocketSize=2;
		
		for(int i=0;i<2;i++) cardPic[i]= Gallery.repeater[i];
		
		name[0]="Duplicate";
		cost[0]=2;
		cooldown[0]=0;
		effect[0]=0;
		rules[0]="Augment weapon card: +1 shot";
		code[0].add(Special.Augment);
		code[0].add(Augment.AugmentWeapon);
		code[0].add(Augment.AugmentAddShot, 1);
		code[0].add(AI.WeaponCards, 1);
		code[0].setPriority(2);
		
		name[1]="Fetch";
		cost[1]=0;
		cooldown[1]=0;
		effect[1]=0;
		rules[1]="Get a card from either weapon module";
		code[1].add(Special.ModuleChooser);
		code[1].add(Special.ChooseWeapon);
		code[1].add(Special.GetCardFromChosenModule, 1);
		code[1].add(Special.DiscardWhenChosen);
		code[1].add(AI.Ignore);
		
		overridePowerLevel=calc(2);
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
	public void afterBattle() {
	}

	@Override
	public int getBonusEffect(Card c, int baseEffect) {
		return 0;
	}

	@Override
	public int getBonusShots(Card c, int baseShots) {
		if(baseShots>=4&&c.type==ModuleType.WEAPON){
			return 1;
		}
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
	public void onScramble(Component c) {
	}

}
