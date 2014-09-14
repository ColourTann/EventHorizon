package game.module.component.weapon;

import java.util.ArrayList;

import util.image.Pic;
import util.particleSystem.ParticleSystem;
import game.attack.Attack;
import game.card.Card;
import game.module.Module;
import game.module.component.Component;
import game.module.stuff.Buff.BuffType;

public abstract class Weapon extends Component{

	int[] shots = new int[7];
	
	public int getShots(int i){	


		return shots[i]==0?0:shots[i]+getBuffAmount(BuffType.BonusShot);

	}
	public float weaponOffset;
	public Weapon(String name, Pic p, int numCards, float xOffset, int[] thresholds, int tier){
		super(tier, name, p,4, numCards, thresholds);
		weaponOffset=xOffset;
		type=ModuleType.WEAPON;
	}


	

	public void updateIntensity(){
		int count=0;
		for(Attack atk:ship.getAttacks()){
			if(atk.mod==this)count++;
		}
		for(Attack atk:ship.getAttacks()){
			atk.atkgrphc.intensity=count;
		}
	
	}

	

	
}
