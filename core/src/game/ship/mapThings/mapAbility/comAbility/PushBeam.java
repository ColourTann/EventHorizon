package game.ship.mapThings.mapAbility.comAbility;

import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.ship.mapThings.MapShip;
import game.ship.mapThings.mapAbility.MapAbility;

public class PushBeam extends MapAbility{

	public PushBeam() {
		super(Gallery.mapAbilityPush,6,1);
	}

	@Override
	public boolean isValidChoice(Hex target) {
		if(target.isBlocked(false)||target==mapShip.hex) return false;

		return(mapShip.hex.getDistance(target)<=range&&target.mapShip!=null);
		}

	public void deselect() {
		Map.returnToPlayerTurn();
		Map.using=null;
	}

	@Override
	public void pickHex(Hex targetHex) {
		if(!isValidChoice(targetHex))return;
		afterPlayerUse();
		MapShip target=targetHex.mapShip;
		Hex best=targetHex;
		float bestDist=targetHex.getLineDistance(mapShip.hex);
		for(Hex h:targetHex.getHexesWithin(2, false)){
			if(h.isBlocked(false))continue;
			float dist=h.getLineDistance(mapShip.hex);
			if(dist>bestDist){
				bestDist=dist;
				best=h;
			}
		}
		target.moveTo(best);
		}

	@Override
	public HexChoice getBestTarget() {
		return null;
	}
}
