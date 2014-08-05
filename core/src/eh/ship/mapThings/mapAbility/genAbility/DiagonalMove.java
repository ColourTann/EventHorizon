package eh.ship.mapThings.mapAbility.genAbility;

import eh.assets.Gallery;
import eh.assets.Pic;
import eh.grid.hex.Hex;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.ship.mapThings.mapAbility.MapAbility;

public class DiagonalMove extends MapAbility{

	public DiagonalMove() {
		super(Gallery.mapAbilityDiagonalMove);
	}

	@Override
	public boolean isValidChoice(Hex origin, Hex target) {
		int distance=origin.getDistance(target);
		int xDiff=Math.abs(origin.x-target.x);
		int yDiff=Math.abs(origin.y-target.y);
		return distance==2&&!(xDiff%2==0&&yDiff%2==0);
		
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
