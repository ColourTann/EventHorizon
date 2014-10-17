package game.module.component.weapon;

import game.assets.Gallery;

public class OtherWeapon extends Weapon{

	public OtherWeapon(int tier){
		super("Pulse",Gallery.pulse,8,0, new int[]{7,10,12}, tier);
	}

}
