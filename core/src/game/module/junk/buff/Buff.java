package game.module.junk.buff;

import util.TextWriter;
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
	public TextWriter tw;
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
			return Gallery.iconMultiShot;
		case Scrambled:
			return Gallery.iconScrambled;
		case ReduceCost:
			return Gallery.iconReducedCost;
		case TakesExtraDamage:
			return Gallery.iconTakesExtraDamage;
		case BonusIncome:
			return Gallery.iconBonusIncome;
		case Disabled:
			return Gallery.iconDisabled;
		default:
			break;
		}
		return null;
	}

	public String getWords(){
		switch(type){
		case BonusEffeect:
			return "+"+value+" strength"+dur();
		case BonusIncome:
			return "+"+value+" energy income"+dur();
		case BonusShot:
			return "+"+value+" shots"+dur();
		case Disabled:
			return "Disabled"+dur();
		case ReduceCost:
			return "-"+value+" energy"+dur();
		case Scrambled:
			return "Scrambled, play any scrambled card to remove";
		case TakesExtraDamage:
			return "Takes +"+value+" extra damage from all shots"+dur();
		default:
			break;
		}
		return "Remind Tann to put something here";
	}

	private String dur(){
		if(duration==-1)return " permanently";
		return " for "+duration +" turn"+(duration>1?"s":"");
	}

	public Buff copy() {
		return new Buff(type, positive, value, duration);
	}
}

