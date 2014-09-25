package game.module.utility.armour;

import java.lang.reflect.InvocationTargetException;

import util.Draw;
import util.image.Pic;
import game.assets.Gallery;
import game.card.CardCode.AI;
import game.card.CardCode.Special;
import game.module.component.shield.Deflector;
import game.module.component.shield.Shield;
import game.module.component.weapon.Laser;
import game.module.component.weapon.Pulse;
import game.module.component.weapon.Ray;
import game.module.component.weapon.Tesla;
import game.module.utility.Utility;

public abstract class Armour extends Utility{
	static Class[] classes = new Class[]{BasicArmour.class, OrganicShell.class, GalvanicSkin.class};
	public float multiplier;
	public Armour(float multiplier, int tier, String modName, String passive, Pic modulePic, int variants, int numCards){
		super(tier, ModuleType.UTILITY, modName, passive, modulePic, variants, numCards);
		this.multiplier=multiplier;
		type=ModuleType.ARMOUR;
		cardPic[0]=modulePic;		
	}
	public float getMultuplier(){
		return multiplier;
	}

	public abstract void onTakeMajorDamage();
	
	public static Armour getRandomArmour(int tier) {
		Draw.shuffle(classes);
		try {
			return (Armour) (classes[0].getConstructor(int.class).newInstance(tier));
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
