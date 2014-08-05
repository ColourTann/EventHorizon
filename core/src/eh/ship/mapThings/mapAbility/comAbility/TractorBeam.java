package eh.ship.mapThings.mapAbility.comAbility;

import eh.assets.Gallery;
import eh.grid.hex.Hex;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.ship.mapThings.MapShip;
import eh.ship.mapThings.mapAbility.MapAbility;

public class TractorBeam extends MapAbility{
	private MapShip chosenShip;
	public TractorBeam() {
		super(Gallery.mapAbilityTractorBeam);
	}

	@Override
	public boolean isValidChoice(Hex origin, Hex target) {
		if(target.isBlocked(true));
		if(chosenShip==null){
			if(origin.getDistance(target)<=4&&target.mapShip!=null)return true;
		}
		else{
			if(target.getDistance(chosenShip.hex)<=2)return true;
		}
		return false;

	}

	public void deactivate() {
		Map.returnToPlayerTurn();
		Map.using=null;
		choosing=false;
		chosenShip=null;
	}

	@Override
	public void pickHex(Hex hex) {
		if(!isValidChoice(Map.player.hex, hex))return;
		fadeHexesOut();
		if(chosenShip==null){
			chosenShip=hex.mapShip;
			fadeHexesIn();
		}
		else {
			fadeHexesOut();
			chosenShip.moveTo(hex);
			Map.using=null;
			choosing=false;
			chosenShip=null;
			Map.setState(MapState.PlayerMoving);
		}

	}

}
