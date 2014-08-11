package eh.module.weapon;

import java.util.ArrayList;

import eh.assets.Pic;
import eh.card.Card;
import eh.module.Module;
import eh.module.utils.Buff.BuffType;
import eh.module.weapon.attack.Attack;
import eh.util.particleSystem.ParticleSystem;

public abstract class Weapon extends Module{

	int[] shots = new int[7];
	public ArrayList<Attack> attacks=new ArrayList<Attack>();
	public int getShots(int i){	


		return shots[i]==0?0:shots[i]+getBuffAmount(BuffType.BonusShot);

	}
	public float weaponOffset;
	public Weapon(String name, Pic p, int numCards, float xOffset, int[] thresholds, int tier){
		super(name, p,4, numCards, thresholds, tier);
		weaponOffset=xOffset;
		type=ModuleType.WEAPON;
	}

	public void addAttack(Card card){addAttack(new Attack(card));}
	public void addAttack(Card card, Module target){addAttack(new Attack(card,target));}

	private void addAttack(Attack a){
		attacks.add(a);
		ParticleSystem.systems.add(a.atkgrphc);
		updateIntensity();
	}
	public void removeAttack(Card card){
		for(int i=0;i<attacks.size();i++){
			Attack a=attacks.get(i);
			if(a.card==card){
				if(a.target!=null)a.target.targeteds--;
				attacks.remove(i);
				a.disable();
				i--;
			}
		}
		updateIntensity();
	}

	public void updateIntensity(){
		for(Attack atk:attacks){
			atk.atkgrphc.intensity=attacks.size();
		}
	}

	public boolean checkFinished(){
		for (int i=0;i<attacks.size();i++){
			Attack a=attacks.get(i);
			if(a.atkgrphc.particles.size()==0){
				attacks.remove(i);
				i--;
			}
		}
		return attacks.size()==0;
	}

	public void notifyIncoming() {
		for(Attack a:attacks){
			a.activateIncoming();
		}
	}
}
