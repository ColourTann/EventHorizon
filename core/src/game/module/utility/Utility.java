package game.module.utility;

import game.card.Card;
import game.module.Module;
import game.module.component.shield.Deflector;
import game.module.component.shield.Shield;
import game.module.utility.armour.BasicArmour;
import game.module.utility.armour.OrganicShell;
import game.module.utility.armour.GalvanicSkin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import util.Draw;
import util.image.Pic;

public abstract class Utility extends Module{
	static Class[] classes = new Class[]{AuxiliaryDrive.class, Furnace.class, PhaseArray.class, Repeater.class};
	public String passive;
	public Utility(int tier, ModuleType cardType, String name, String passive, Pic modulePic, int variants, int numCards) {
		super(tier, name, modulePic, variants, numCards);
		this.passive=passive;
		type=ModuleType.UTILITY;
		this.cardType=cardType;
	}

	ArrayList<Card> card=new ArrayList<Card>();

	public abstract void startBattleEffect();

	public abstract void beginTurnEffect();

	public abstract void endTurnEffect();

	public abstract void playCardEffect(Card c);

	public abstract int getBonusEffect(Card c, int baseEffect);

	public abstract int getBonusShots(Card c, int baseShots);

	public abstract int getBonusCost(Card c, int baseCost);
	
	public abstract boolean overrideDefeat();
	
	public abstract void afterBattle();

	public static Utility getRandomUtility(int tier) {

		Draw.shuffle(classes);

		while(tier%2==1&&classes[0]==Deflector.class){
			Draw.shuffle(classes);
		}
		try {
			return (Utility) (classes[0].getConstructor(int.class).newInstance(tier));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;

	}
}
