package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.component.Component;

public class ArcSocket extends Utility{

	public ArcSocket(int tier) {
		super(tier, ModuleType.SHIELD, "Arc socket", "If you're on >2 major damage:|n|-1 shield cost and +1 |shield|", Gallery.blaster, 1, 1);
		
		for(int i=0;i<2;i++) cardPic[i]= Gallery.arcSocket[i];
		
		//double shielding
		//get a shield
		// shield damaged module
		// get a shield
		
		name[0]="Protect";
		cost[0]=1;
		effect[0]=5;
		rules[0]="Shields only modules with major damage";
		code[0].add(Special.AddShieldPoints);
		code[0].add(Special.ShieldOnlyDamaged);
		code[0].add(AI.IncomingOnMajorDamaged, 4);
		code[0].add(AI.RegularShield);
		

		name[1]="Fetch";
		cost[1]=0;
		effect[1]=0;
		rules[1]="Get a shield card";
		code[1].add(Special.GetShieldCard, 1);
		code[1].add(Special.DiscardWhenPlayed);
		
	
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
