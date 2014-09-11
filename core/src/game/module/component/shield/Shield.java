package game.module.component.shield;

import util.image.Pic;
import game.module.Module;

public abstract class Shield extends Module{
	public Shield(String name,Pic p, int numCards, int[] thresholds, int tier){
		super(name,p,4, numCards, thresholds, tier);
		type=ModuleType.SHIELD;
	}
}
