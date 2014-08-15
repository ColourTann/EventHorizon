package eh.ship.mapThings.mapAbility.comAbility;

import eh.grid.hex.Hex;
import eh.grid.hex.HexChoice;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.ship.mapThings.MapShip;
import eh.ship.mapThings.mapAbility.MapAbility;
import eh.util.assets.Gallery;

public class PullBeam extends MapAbility{

	public PullBeam() {
		super(Gallery.mapAbilityPull,6,1);
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
			if(dist<bestDist){
				bestDist=dist;
				best=h;
			}
		}
	}

	@Override
	public HexChoice getBestTarget() {
		return null;
	}
}
