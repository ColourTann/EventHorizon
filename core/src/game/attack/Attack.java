package game.attack;

import game.card.Card;
import game.card.CardCode;
import game.card.CardCode.Special;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.Component;
import game.module.component.SpecialComponent;
import game.module.component.weapon.Laser;
import game.module.component.weapon.Pulse;
import game.module.component.weapon.Ray;
import game.module.component.weapon.Swift;
import game.module.component.weapon.Tesla;
import game.module.junk.DamagePoint;
import game.module.junk.buff.Buff;
import game.module.junk.buff.Buff.BuffType;

public class Attack {
	public AttackGraphic atkgrphc;
	public Card card;
	public Module mod;
	public Component target;
	boolean finished;
	boolean activated;
	public int order;
	public int damage;
	public boolean unshieldable;
	int lockedEffect;
	public Attack(Card c){
		this.card=c;
		this.mod=c.mod;
		if(mod instanceof Laser)atkgrphc=new LaserAttack(mod.getBarrel());
		if(mod instanceof Ray)atkgrphc=new RayAttack(mod.getBarrel());
		if(mod instanceof Pulse)atkgrphc=new PulseAttack(mod.getBarrel());
		if(mod instanceof Tesla)atkgrphc=new LightningAttack(mod.getBarrel());
		if(mod instanceof Swift)atkgrphc=new SwiftAttack(mod.getBarrel());
		//if(mod instanceof SpecialComponent)atkgrphc=new LaserAttack(mod.getBarrel());
		if(atkgrphc==null)atkgrphc=new RocketAttack(mod.ship.getShield().getCenter(), Math.max(card.rocketSize, card.mod.rocketSize));
		
		atkgrphc.atk=this;
	}
	
	public void lockEffect(){
		lockedEffect=card.getEffect();
	}
	
	public Attack(Card c, Component target){
		this(c);
		this.target=target;
	}
	public void disable(){
		atkgrphc.disable();
	}

	public void fire(){
		if(finished)return;

		atkgrphc.fire(target.getHitLocation());
	}
	public void activateIncoming(){
		//Checking if already done//
		if(activated)return;
		activated=true;
		
		CardCode code=card.getCode();
		int effect=lockedEffect;
		
		//Choosing target if not targeted//
		if(target==null)target = mod.ship.getEnemy().getRandomUndestroyedComponent();

		
		effect+=target.getBuffAmount(BuffType.TakesExtraDamage);
		if(target.getDamage()+target.getTotalIncoming()==0)effect+=code.getAmount(Special.BonusVsPristine);
		if(target.type==ModuleType.WEAPON)effect+=code.getAmount(Special.BonusVsWeapon);
		if(target.currentThreshold>0)effect+=code.getAmount(Special.BonusVsMajorDamaged);
		
		for(int i=0;i<effect;i++){
			if(code.contains(Special.Unshieldable)){
				target.addUnshieldable(new DamagePoint(card));
				unshieldable=true;
			}
			else{
				target.addIncoming(new DamagePoint(card));
			}
			damage++;
		}
	}


	public boolean activateDamage(){
		int pre =target.getDamage();
		target.calculateDamage(damage, unshieldable);
		finished=true;
		return pre!=target.getDamage();
	}
}
