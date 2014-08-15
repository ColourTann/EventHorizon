package eh.screen.battle.tutorial;



import eh.card.Card;
import eh.card.CardCode.Special;
import eh.module.Module;
import eh.module.Module.ModuleType;
import eh.module.utils.Buff.BuffType;
import eh.screen.battle.Battle;
import eh.screen.battle.Battle.Phase;
import eh.screen.battle.Battle.State;
import eh.ship.Ship;
import eh.util.assets.Pic;
import eh.util.maths.Pair;

public class Task {
	String s;
	TaskType t;
	Ship player = Battle.player;
	public Pic pic;
	public Pair location;
	public Card card;
	public int side;
	public boolean phaseButton;
	public enum TaskType{PlayShield, ShieldGen, EndShieldPhase, PlayWeapon, EndWeaponPhase, PreventAllMajor, TargetGenerator, PlayerAlternateSide, NoneScrambled, WeaponPlayed, WeirdPrevent, FlipCard}
	public Task(String s, TaskType t){
		this.s=s;
		this.t=t;
	}
	public Task(String s, TaskType t, Pic pic, Pair location){
		this.s=s;
		this.t=t;
		this.pic=pic;
		this.location=location;
	}

	public Task(String s, TaskType t, Card card, int side){
		this.s=s;
		this.t=t;
		this.card=card;
		this.side=side;
	}

	public Task(String s, TaskType t, boolean phaseButton){
		this.s=s;
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
			for(Card c:Battle.player.playList){
				if(c.type==ModuleType.SHIELD)return true;
			}
			break;
		case PlayWeapon:
			for(Card c:Battle.player.playList){
				if(c.type==ModuleType.WEAPON)return true;
			}
			break;
		case ShieldGen:
			return Battle.player.getGenerator().getShield()==2;
		case PreventAllMajor:
			for(Module m:Battle.player.getRandomisedModules()){
				if(m.getShieldsRequiredToAvoidMajor()!=999)return false;
			}
			return true;
		case EndShieldPhase:
			return Battle.getPhase()!=Phase.ShieldPhase;
		case EndWeaponPhase:
			return Battle.getPhase()!=Phase.WeaponPhase;
		case PlayerAlternateSide:

			if(Battle.getState()==State.Targeting)return true;
			for(Card c:Battle.player.playList){
				if(c.getCode().contains(Special.Targeted))return true;
			}

		case TargetGenerator:
			return Battle.enemy.getGenerator().targeteds>0;
		case NoneScrambled:
			for(Module m:Battle.player.getRandomisedModules()){
				if(m.getBuffAmount(BuffType.Scrambled)>0){
					return false;
				}
			}
			return true;
		case WeaponPlayed:
			for(Card c:Battle.player.playList){
				if(!c.wasScrambled&&c.mod.type==ModuleType.WEAPON)return true;
			}
			break;
		case WeirdPrevent:
			if(player.getModule(0).getShieldableIncoming()<=player.getModule(0).thresholds[0]&&player.getModule(1).getShieldableIncoming()<=player.getModule(1).thresholds[1])return true;
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
