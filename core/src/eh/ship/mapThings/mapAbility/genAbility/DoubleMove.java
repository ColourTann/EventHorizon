package eh.ship.mapThings.mapAbility.genAbility;

import eh.assets.Gallery;
import eh.assets.Pic;
import eh.grid.hex.Hex;
import eh.grid.hex.HexChoice;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.ship.mapThings.mapAbility.MapAbility;

public class DoubleMove extends MapAbility{

	public DoubleMove() {
		super(Gallery.mapAbilityDoubleMove,2,1);
	}



	@Override
	public boolean isValidChoice(Hex target) {
		Hex origin=mapShip.hex;
		int distance=origin.getDistance(target);
		if(target.isBlocked(false))return false;
		return distance==2;
	}

	@Override
	public void pickHex(Hex hex) {
		if(!isValidChoice(hex))return;
		afterPlayerUse();
		mapShip.moveTo(hex);
	}



	@Override
	public HexChoice getBestTarget() {
		HexChoice result = new HexChoice();
		for (Hex h:mapShip.hex.getHexesWithin(range, false)){
			if(!isValidChoice(h))continue;
			HexChoice current=new HexChoice(h, h.howGood(mapShip));
			if(current.isBetterThan(result))result=current;
		}
		return result;
	}

}
