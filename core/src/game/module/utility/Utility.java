package game.module.utility;

import game.card.Card;
import game.module.Module;
import game.ship.Ship;

import java.util.ArrayList;

import util.image.Pic;

public abstract class Utility extends Module{
	public Utility(int tier, String name, Pic modulePic, int variants, int numCards) {
		super(tier, name, modulePic, variants, numCards);
	}

	
	
	
	
	ArrayList<Card> card=new ArrayList<Card>();
	
	public abstract void startBattleEffect();
	
	public abstract void beginTurnEffect();
	
	public abstract void endTurnEffect();
	
	public abstract void playCard(Card c);
	
	public abstract void afterBattle();
}
