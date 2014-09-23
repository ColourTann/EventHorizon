package game.module.component.weapon;

import java.lang.reflect.InvocationTargetException;



import util.Draw;
import util.image.Pic;
import game.module.component.Component;

public abstract class Weapon extends Component{

	static Class[] classes = new Class[]{Laser.class, Pulse.class, Ray.class, Tesla.class, Swift.class};
	public float weaponOffset;
	public Weapon(String name, Pic p, int numCards, float xOffset, int[] thresholds, int tier){
		super(tier, name, p,4, numCards, thresholds);
		weaponOffset=xOffset;
		type=ModuleType.WEAPON;
		cardType=type;
	}
	public static Weapon getRandomWeapon(int tier) {
		Draw.shuffle(classes);
	
		while(tier%2==1&&classes[0]==Pulse.class){
			Draw.shuffle(classes);
		}
			try {
				return (Weapon) (classes[0].getConstructor(int.class).newInstance(tier));
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
