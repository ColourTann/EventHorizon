package game.ship;

public class ShipStats {
	public float attack;
	public float defence;
	public float energyUsage;
	public float power;
	public ShipStats(float energyUsage, float attack, float defence, float power){
		this.energyUsage=energyUsage;
		this.attack=attack;
		this.defence=defence;
		this.power=power;
	}
	public String toString(){
		return "Strength: "+power+", energy usage: "+energyUsage+", atk: "+attack+", def: "+defence;
	}
}
