package game.module.component.weapon;

import game.assets.Gallery;
import util.image.Pic;

public class Scatter extends Weapon{

	public Scatter(int tier){
		super("Scatter",Gallery.pulse,8,0, new int[]{7,10,12}, tier);
		
		// 3 shots 1 damage tier 1 //
		
	}

}
