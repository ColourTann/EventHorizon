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
	public ArrayList<Attack> attacks=new ArrayList<Attack>();
	public int getShots(int i){	


		return shots[i]==0?0:shots[i]+getBuffAmount(BuffType.BonusShot);

	}
	public float weaponOffset;
	public Weapon(String name, Pic p, int numCards, float xOffset, int[] thresholds, int tier){
		super(tier, name, p,4, numCards, thresholds);
		weaponOffset=xOffset;
		type=ModuleType.WEAPON;
	}

	public void addAttack(Card card){addAttack(new Attack(card));}
	public void addAttack(Card card, Component target){addAttack(new Attack(card,target));}

	private void addAttack(Attack a){
		a.atkgrphc.order=attacks.size();
		
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
