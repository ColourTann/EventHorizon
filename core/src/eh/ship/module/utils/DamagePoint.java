package eh.ship.module.utils;

import eh.card.Card;

public class DamagePoint {
	public Card card;
	public boolean moused;
	public DamagePoint(Card c){
		card=c;
	}
	public void checkMoused(Card c){
		if(c==card)moused=true;
	}
	public void reset(){
		moused=false;
	}
}
