package eh.module.shield;

import eh.module.Module;
import eh.util.assets.Pic;

public abstract class Shield extends Module{
	public Shield(String name,Pic p, int numCards, int[] thresholds, int tier){
		super(name,p,4, numCards, thresholds, tier);
		type=ModuleType.SHIELD;
	}
}
