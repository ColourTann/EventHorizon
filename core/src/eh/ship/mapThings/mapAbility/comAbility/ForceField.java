package eh.ship.mapThings.mapAbility.comAbility;

import java.util.ArrayList;

import eh.assets.Gallery;
import eh.grid.hex.Hex;
import eh.grid.hex.HexChoice;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.ship.mapThings.mapAbility.MapAbility;



public class ForceField extends MapAbility{

	ArrayList<Hex> forceFields = new ArrayList<Hex>();
	
	public ForceField() {
		super(Gallery.mapAbilityForceField, 5, 1);
	}

	@Override
	public boolean isValidChoice(Hex target) {
		Hex origin= mapShip.hex;
		int distance=origin.getDistance(target);
		if(distance<=range)return false;
		return !target.isBlocked(true);
	}

	public void deselect() {
		Map.returnToPlayerTurn();
		Map.using=null;
		for(Hex h:forceFields)h.forceField=0;
		forceFields.clear();
	}

	@Override
	public void pickHex(Hex hex) {
		if(!isValidChoice(hex)||forceFields.contains(hex))return;
		
		hex.forceField=3;
		//hex.highlight=false;
		forceFields.add(hex);
		if(forceFields.size()==3){
			for(Hex h:Map.grid.drawableHexes)if(isValidChoice(h))h.mapAbilityChoiceFadeout();
			Map.using=null;
			Map.setState(MapState.EnemyMoving);
			forceFields.clear();
		}
	}

	@Override
	public HexChoice getBestTarget() {
		return null;
	}

}
