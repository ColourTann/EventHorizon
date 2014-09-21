package game.module.component.shield;

import java.lang.reflect.InvocationTargetException;

import util.Draw;
import util.image.Pic;
import game.card.Card;
import game.module.Module;
import game.module.component.Component;
import game.module.component.weapon.Laser;
import game.module.component.weapon.Pulse;
import game.module.component.weapon.Ray;
import game.module.component.weapon.Tesla;
import game.module.component.weapon.Weapon;

public abstract class Shield extends Component{
	static Class[] classes = new Class[]{Deflector.class, Repeller.class, Repulsor.class};
	public Shield(String name,Pic p, int numCards, int[] thresholds, int tier){
		super(tier, name,p,4, numCards, thresholds);
		type=ModuleType.SHIELD;
	}

	public static Shield getRandomShield(int tier) {
		
			Draw.shuffle(classes);
		
			while(tier%2==1&&classes[0]==Deflector.class){
				Draw.shuffle(classes);
			}
				try {
					return (Shield) (classes[0].getConstructor(int.class).newInstance(tier));
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
