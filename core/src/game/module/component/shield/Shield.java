package game.module.component.shield;

import util.image.Pic;
import game.module.component.Component;

public abstract class Shield extends Component{
	public Shield(String name,Pic p, int numCards, int[] thresholds, int tier){
		super(tier, name,p,4, numCards, thresholds);
		type=ModuleType.SHIELD;
	}
}
