package game.module.component.weapon;

import util.image.Pic;
import game.module.component.Component;

public abstract class Weapon extends Component{

	
	public float weaponOffset;
	public Weapon(String name, Pic p, int numCards, float xOffset, int[] thresholds, int tier){
		super(tier, name, p,4, numCards, thresholds);
		weaponOffset=xOffset;
		type=ModuleType.WEAPON;
	}


	



	

	
}
