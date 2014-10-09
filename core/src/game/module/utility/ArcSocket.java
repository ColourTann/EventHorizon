package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.component.Component;

public class ArcSocket extends Utility{

	public ArcSocket(int tier) {
		super(tier, ModuleType.SHIELD, "Arc socket", "If you've taken 3+ major damage: -1 shield cost", Gallery.blaster, 1, 1);
		
		for(int i=0;i<2;i++) cardPic[i]= Gallery.arcSocket[i];
		
		//double shielding
		//get a shield
		
		name[0]="Protect";
		cost[0]=1;
		effect[0]=2;
		rules[0]="+1 effect per major damage taken";
		code[0].add(Special.BonusPerMajorDamage, calc(0));
		code[0].add(Special.AddShieldPoints);
		code[0].add(AI.RegularShield);
		
		name[1]="Swell";
		cost[1]=0;
		effect[1]=0;
		rules[1]="+1 shield effect this turn";
		code[1].add(Special.BonusEffectToShield, calc(0,1));
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
//		if(ship.getMajorDamage()>=3 && c.type==ModuleType.SHIELD){
//			return calc(0);
//		}
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
		if(ship.getMajorDamage()>=3 && c.type==ModuleType.SHIELD){
		return -1;
		}
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
