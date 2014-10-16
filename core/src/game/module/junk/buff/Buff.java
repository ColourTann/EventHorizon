package game.module.junk.buff;

import util.image.Pic;
import game.assets.Gallery;
import game.card.Card;

public class Buff {
	public enum BuffType{TakesExtraDamage, BonusEffeect, BonusShot, ReduceCost, Scrambled, BonusIncome, Disabled}
	public BuffType type;
	public Card card;
	public int duration;
	public int value;
	boolean positive;
	
	public Buff(BuffType type, boolean positive, int value, int duration){
		this.type=type;
		this.positive=positive;
		this.value=value;
		this.duration=duration;
	}
	
	public Buff(BuffType type, boolean positive, int value, int duration, Card card){
		this.type=type;
		this.positive=positive;
		this.value=value;
		this.duration=duration;
		this.card=card;
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
			return Gallery.iconIncreasedEffect;
		case TakesExtraDamage:
			return Gallery.iconIncreasedEffect;
		case BonusIncome:
			return Gallery.iconIncreasedEffect;
		case Disabled:
			return Gallery.iconIncreasedEffect;
		default:
			break;
		}
		return null;
	}
	
	public String getWords(){
		switch(type){
		case BonusEffeect:
			return "+"+value+" strength for "+duration+" turns";
		case BonusIncome:
			return "+"+value+" income for "+duration+" turns";
		case BonusShot:
			return "+"+value+" shots for "+duration+" turns";
		case Disabled:
			return "Disabled for "+duration+" turns";
		case ReduceCost:
			return "-"+value+" cost for "+duration+" turns";
		case Scrambled:
			return "Scrambled, play any scrambeld card to unscramble";
		case TakesExtraDamage:
			return "Takes +"+value+" extra damage from all shots for "+duration+" turns";
		default:
			break;
		}
		return "Remind Tann to put something here";
	}
	
	public Buff copy() {
		return new Buff(type, positive, value, duration);
	}
}

