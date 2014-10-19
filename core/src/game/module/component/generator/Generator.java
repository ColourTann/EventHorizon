package game.module.component.generator;

import util.image.Pic;
import game.card.CardCode.Special;
import game.module.component.Component;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;

public abstract class Generator extends Component{
	public int energyIncome;
	
	public Generator(String modName,Pic p, int energyIncome, int[] thresholds){
		super(-1, modName, p, 1, 2, thresholds);
		this.energyIncome=energyIncome;
		type=ModuleType.GENERATOR;
		cardType=type;
		name[0]="Recharge";
		cost[0]=0;
		effect[0]=0;
		rules[0]="+1 iconenergy";
		code[0].add(Special.GainEnergy,1);
		code[0].setPriority(2);
	}
	public int getIncome(){
		int result =(int) (destroyed?Math.ceil(energyIncome/2f):energyIncome);
		for(Buff b:buffs){
			if(b.type==BuffType.BonusIncome){
				result += b.value;
			}
		}
		return result;
	}

}
