package eh.module.generator;

import eh.assets.Pic;
import eh.card.CardCode.Special;
import eh.module.Module;

public abstract class Generator extends Module{
	public int energyIncome;
	public Generator(String modName,Pic p, int energyIncome, int[] thresholds){
		super(modName,p,1,2, thresholds, 0);
		this.energyIncome=energyIncome;
		type=ModuleType.GENERATOR;
		name[0]="Recharge";
		cost[0]=0;
		cooldown[0]=0;
		effect[0]=0;
		rules[0]="+1 energy";
		code[0].add(Special.GainEnergy,1);
		code[0].setPriority(2);
	}
	public int getIncome(){
		return (int) (destroyed?Math.ceil(energyIncome/2f):energyIncome);
	}
	public void addIncome(int amount){
		energyIncome+=amount;
	}
}
