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

	
	public float weaponOffset;
	public Weapon(String name, Pic p, int numCards, float xOffset, int[] thresholds, int tier){
		super(tier, name, p,4, numCards, thresholds);
		weaponOffset=xOffset;
		type=ModuleType.WEAPON;
	}


	



	

	
}
