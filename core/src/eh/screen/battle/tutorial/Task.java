package eh.screen.battle.tutorial;



import eh.card.Card;
import eh.card.CardCode.Special;
import eh.screen.battle.Battle;
import eh.screen.battle.Battle.Phase;
import eh.screen.battle.Battle.State;
import eh.ship.Ship;
import eh.ship.module.Module;
import eh.ship.module.Module.ModuleType;
import eh.ship.module.utils.Buff;
import eh.ship.module.utils.Buff.BuffType;

public class Task {
	String s;
	TaskType t;
	Ship player = Battle.player;
	public enum TaskType{PlayShield, ShieldGen, EndShieldPhase, PlayWeapon, EndWeaponPhase, PreventAllMajor, TargetGenerator, PlayerAlternateSide, NoneScrambled, WeaponPlayed, WeirdPrevent}
	public Task(String s, TaskType t){
		this.s=s;
		this.t=t;
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
			
		
		}
		return false;

	}
}
