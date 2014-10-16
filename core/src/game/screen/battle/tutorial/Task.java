package game.screen.battle.tutorial;



import util.image.Pic;
import util.maths.Pair;
import game.card.Card;
import game.card.CardCode.Special;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.component.Component;
import game.module.junk.buff.Buff.BuffType;
import game.screen.battle.Battle;
import game.screen.battle.Battle.Phase;
import game.screen.battle.Battle.State;
import game.ship.Ship;

public class Task {
	String s;
	TaskType t;
	Ship player = Battle.getPlayer();
	public Pic pic;
	public Pair location;
	public Card card;
	public int side;
	public boolean phaseButton;
	public enum TaskType{PlayShield, ShieldGen, EndShieldPhase, PlayWeapon, EndWeaponPhase, PreventAllMajor, TargetGenerator, PlayerAlternateSide, NoneScrambled, WeaponPlayed, WeirdPrevent, FlipCard}
	public Task(String s, TaskType t){
		this.s="- "+s;
		this.t=t;
	}
	public Task(String s, TaskType t, Pic pic, Pair location){
		this.s="- "+s;
		this.t=t;
		this.pic=pic;
		this.location=location;
	}

	public Task(String s, TaskType t, Card card, int side){
		this.s="- "+s;
		this.t=t;
		this.card=card;
		this.side=side;
	}

	public Task(String s, TaskType t, boolean phaseButton){
		this.s="- "+s;
		this.t=t;
		this.phaseButton=true;
	}
	public Task(TaskType phaseTrigger) {
		s="";
		t=phaseTrigger;
		this.phaseButton=true;
	}
	public boolean isDone(){
		switch(t){
		case PlayShield:
			for(Card c:Battle.getPlayer().playList){
				if(c.type==ModuleType.SHIELD)return true;
			}
			break;
		case PlayWeapon:
			for(Card c:Battle.getPlayer().playList){
				if(c.type==ModuleType.WEAPON)return true;
			}
			break;
		case ShieldGen:
			return Battle.getPlayer().getGenerator().getShield()==2;
		case PreventAllMajor:
			for(Component c:Battle.getPlayer().getRandomisedModules()){
				if(c.getShieldsRequiredToAvoidMajor()!=999)return false;
			}
			return true;
		case EndShieldPhase:
			return Battle.getPhase()!=Phase.ShieldPhase;
		case EndWeaponPhase:
			return Battle.getPhase()!=Phase.WeaponPhase;
		case PlayerAlternateSide:

			if(Battle.getState()==State.Targeting)return true;
			for(Card c:Battle.getPlayer().playList){
				if(c.getCode().contains(Special.Targeted))return true;
			}

		case TargetGenerator:
			return Battle.getEnemy().getGenerator().targeteds>0;
		case NoneScrambled:
			for(Module m:Battle.getPlayer().getRandomisedModules()){
				if(m.getBuffAmount(BuffType.Scrambled)>0){
					return false;
				}
			}
			return true;
		case WeaponPlayed:
			for(Card c:Battle.getPlayer().playList){
				if(!c.wasScrambled&&c.mod.type==ModuleType.WEAPON)return true;
			}
			break;
		case WeirdPrevent:
			if(player.getComponent(0).getShieldableIncoming()<=player.getComponent(0).thresholds[0]&&player.getComponent(1).getShieldableIncoming()<=player.getComponent(1).thresholds[1])return true;
			break;
		case FlipCard:
			return Tutorial.targetedWeaponCard.side==1;
		}

		return false;

	}
	public PicLoc getPicLoc() {
		if(Tutorial.currentList!=null){
			if(!Tutorial.currentList.isCurrent()){
				return null;
			}
		}
		if(location!=null)	return new PicLoc(pic, location, this);
		if(card!=null)		return new PicLoc(card, side, this);
		if(phaseButton)		return new PicLoc(this);
		return null;
	}
}
