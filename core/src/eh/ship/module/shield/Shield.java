package eh.ship.module.shield;

import eh.assets.Pic;
import eh.ship.module.Module;

public abstract class Shield extends Module{
	public Shield(String name,Pic p, int numCards, int[] thresholds){
		super(name,p,4, numCards, thresholds);
		type=ModuleType.SHIELD;
	}
}
