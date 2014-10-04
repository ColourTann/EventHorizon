package game.module.utility;

import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.Module;
import game.module.component.Component;

public class PhaseArray extends Utility{

	public PhaseArray(int tier) {
		super(tier, ModuleType.WEAPON, "Phase Array", "If you play 3+ cards from one weapon: +1 energy", Gallery.blaster, 1, 1);
		
		for(int i=0;i<2;i++) cardPic[i]= Gallery.phaseArray[i];
		
		name[0]="Surge";
		cost[0]=1;
		cooldown[0]=0;
		effect[0]=1;
		shots[0]=1;
		rules[0]="+1 damage for each other weapon card played this turn";
		code[0].add(Special.BonusEffectPerOtherWeapon, 1);
		
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
		rocketSize=2;
	}

	@Override
	public void startBattleEffect() {
	}

	@Override
	public void beginTurnEffect() {
	}

	@Override
	public void endTurnEffect() {
		for(Card c: ship.playList){
			if(c.mod.type==ModuleType.WEAPON){
				int total=1;
				for(Card check: ship.playList){
					if(c!=check&&c.mod.getClass()==check.mod.getClass()){
						total++;
						if(total==3){
							ship.addEnergy(1, true);
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public void playCardEffect(Card c) {
	}

	@Override
	public void afterBattle() {
	}

	@Override
	public int getBonusEffect(Card c, int baseEffect) {
//		if(baseEffect>=1&&c.type==ModuleType.WEAPON){
//			Module m=c.mod;
//			int cardsFromModule=0;
//			for(Card card:ship.hand){
//				if(card.selected&&!card.wasScrambled&&m.getClass()==card.mod.getClass()){
//					cardsFromModule++;
//				}
//			}
//			if(cardsFromModule>=3)return calc(0);
//			
//		}
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

	@Override
	public void onScramble(Component c) {
	}
	
	
}