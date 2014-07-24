package eh.ship.module.weapon.attack;

import eh.card.Card;
import eh.card.CardCode;
import eh.card.CardCode.Special;
import eh.screen.battle.Battle;
import eh.ship.module.Module;
import eh.ship.module.Module.ModuleType;
import eh.ship.module.utils.Buff;
import eh.ship.module.utils.DamagePoint;
import eh.ship.module.utils.Buff.BuffType;
import eh.ship.module.weapon.Laser;
import eh.ship.module.weapon.Pulse;
import eh.ship.module.weapon.Ray;
import eh.ship.module.weapon.Tesla;

public class Attack {
	public AttackGraphic atkgrphc;
	public Card card;
	public Module mod;
	public Module target;
	boolean finished;
	boolean activated;
	public Attack(Card c){
		this.card=c;
		this.mod=c.mod;
		if(mod instanceof Laser)atkgrphc=new LaserAttack(mod.getBarrel());
		if(mod instanceof Ray)atkgrphc=new RayAttack(mod.getBarrel());
		if(mod instanceof Pulse)atkgrphc=new PulseAttack(mod.getBarrel());
		if(mod instanceof Tesla)atkgrphc=new LightningAttack(mod.getBarrel());
		atkgrphc.atk=this;
	}
	public Attack(Card c, Module target){
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
		int effect=card.getEffect();
		
		//Choosing target if not targeted//
		if(target==null)target = mod.ship.getEnemy().getRandomUndestroyedModule();

		
		effect+=target.getBuffAmount(BuffType.TakesExtraDamage);
		if(target.getDamage()==0)effect+=code.getAmount(Special.BonusVsPristine);
		if(target.type==ModuleType.WEAPON)effect+=code.getAmount(Special.BonusVsWeapon);
		if(target.currentThreshold>0)effect+=code.getAmount(Special.BonusVsMajorDamaged);
		
		for(int i=0;i<effect;i++){
			if(code.contains(Special.Unshieldable)){
				target.addUnshieldable(new DamagePoint(card));
			}
			else{
				target.addIncoming(new DamagePoint(card));
			}
		}

		if(code.contains(Special.MakeVulnerable)){
			target.addBuff(new Buff(BuffType.TakesExtraDamage, 1, card, false));
		}
	}


	public boolean activateDamage(){
		int pre =target.getDamage();
		target.calculateDamage();
		finished=true;
		return pre!=target.getDamage();
	}
}
