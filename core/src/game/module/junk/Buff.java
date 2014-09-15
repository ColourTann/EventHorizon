package game.module.junk;

import util.image.Pic;
import game.assets.Gallery;
import game.card.Card;

public class Buff {
	public enum BuffType{TakesExtraDamage, BonusEffeect, BonusShot, ReduceCost, Scrambled}
	public BuffType type;
	public Card card;
	public boolean permanent;
	public int number;
	public Buff(BuffType type, int amount, Card c,  boolean permanent){
		this.permanent=permanent;
		number=amount;
		this.card=c;
		this.type=type;
	}
	public Pic getPic(){
		switch(type){
		case BonusEffeect:
			return Gallery.iconIncreasedEffect;
		case BonusShot:
			break;
		case Scrambled:
			return Gallery.iconJammed;
		case ReduceCost:
			break;
		case TakesExtraDamage:
			break;
		default:
			break;
		
		}
		return null;
	}
}

