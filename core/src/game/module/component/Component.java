package game.module.component;

import util.image.Pic;
import game.module.Module;

public abstract class Component extends Module{

	public Component(String name, Pic p, int variants, int numCards,
			int[] baseThresholds, int tier) {
		super(name, p, variants, numCards, baseThresholds, tier);
	}

}
