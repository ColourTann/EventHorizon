package game.utilitySystem.armour;

public abstract class Armour {
	private float multiplier;
	public Armour(float multiplier){
		this.multiplier=multiplier;
	}
	public float getMultuplier(){
		return multiplier;
	}
}
