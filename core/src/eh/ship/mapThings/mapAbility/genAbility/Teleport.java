package eh.ship.mapThings.mapAbility.genAbility;

import eh.assets.Gallery;
import eh.assets.Pic;
import eh.grid.hex.Hex;
import eh.screen.map.*;
import eh.screen.map.Map.*;
import eh.ship.mapThings.mapAbility.MapAbility;

public class Teleport extends MapAbility{

	public Teleport() {
		super(Gallery.mapAbilityTeleport);
	}
	
	
	
	
	@Override
	public boolean isValidChoice(Hex origin, Hex target) {
		int dist=origin.getDistance(target);
		if(target.mapShip!=null)return false;
		return dist>1&&dist<=4;
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
