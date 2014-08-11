package eh.module.utils;

import eh.card.Card;

public class ShieldPoint {
	public Card card;
	public boolean firstAdded;
	public ShieldPoint(Card c, boolean firstAdded){
		card=c;
		this.firstAdded=firstAdded;
	}
}
