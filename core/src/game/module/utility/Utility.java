package game.module.utility;

import game.card.Card;
import game.module.Module;
import java.util.ArrayList;

import util.image.Pic;

public abstract class Utility extends Module{
	public String passive;
	public Utility(int tier, String name, String passive, Pic modulePic, int variants, int numCards) {
		super(tier, name, modulePic, variants, numCards);
		this.passive=passive;
	}

	
	
	
	
	ArrayList<Card> card=new ArrayList<Card>();
	
	public abstract void startBattleEffect();
	
	public abstract void beginTurnEffect();
	
	public abstract void endTurnEffect();
	
	public abstract void playCard(Card c);
	
	public abstract int getBonusEffect(Card c, int baseEffect);
	
	public abstract int getBonusShots(Card c, int baseShots);
	
	public abstract void afterBattle();
}
