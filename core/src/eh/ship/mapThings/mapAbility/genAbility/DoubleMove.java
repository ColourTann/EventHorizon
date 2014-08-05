package eh.ship.mapThings.mapAbility.genAbility;

import eh.assets.Gallery;
import eh.assets.Pic;
import eh.grid.hex.Hex;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.ship.mapThings.mapAbility.MapAbility;

public class DoubleMove extends MapAbility{

	public DoubleMove() {
		super(Gallery.mapAbilityDoubleMove);
	}



	@Override
	public boolean isValidChoice(Hex origin, Hex target) {
		int distance=origin.getDistance(target);
		return distance==2;
	}

	@Override
	public void pickHex(Hex hex) {
		if(!isValidChoice(Map.player.hex, hex))return;
		fadeHexesOut();
		Map.setState(MapState.PlayerMoving);
		Map.player.moveTo(hex);
		Map.using=null;
		choosing=false;
	}

}
