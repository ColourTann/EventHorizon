package game.module.utility;

import game.card.Card;
import game.ship.Ship;

import java.util.ArrayList;

public abstract class Utility {
	public Ship ship;
	
	
	
	ArrayList<Card> card=new ArrayList<Card>();
	
	public abstract void startBattleEffect();
	
	public abstract void beginTurnEffect();
	
	public abstract void endTurnEffect();
	
	public abstract void playCard(Card c);
	
	public abstract void afterBattle();
}
