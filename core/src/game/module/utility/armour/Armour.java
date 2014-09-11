package game.module.utility.armour;

import game.module.utility.Utility;

public abstract class Armour extends Utility{
	private float multiplier;
	public Armour(float multiplier){
		this.multiplier=multiplier;
	}
	public float getMultuplier(){
		return multiplier;
	}

	
}
