package game.module.component.generator;

import util.image.Pic;
import game.card.CardCode.Special;
import game.module.Module;
import game.module.component.Component;

public abstract class Generator extends Component{
	public int energyIncome;
	public Generator(String modName,Pic p, int energyIncome, int[] thresholds){
		super(0, modName, p, 1, 2, thresholds);
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
