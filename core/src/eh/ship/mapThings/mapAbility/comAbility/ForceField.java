package eh.ship.mapThings.mapAbility.comAbility;

import java.util.ArrayList;

import eh.assets.Gallery;
import eh.grid.hex.Hex;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.ship.mapThings.mapAbility.MapAbility;



public class ForceField extends MapAbility{

	ArrayList<Hex> forceFields = new ArrayList<Hex>();
	
	public ForceField() {
		super(Gallery.mapAbilityForceField);
	}

	@Override
	public boolean isValidChoice(Hex origin, Hex target) {
		int distance=origin.getDistance(target);
		if(distance>5)return false;
		return !target.isBlocked(true);
	}

	public void deactivate() {
		Map.returnToPlayerTurn();
		Map.using=null;
		choosing=false;
		for(Hex h:forceFields)h.forceField=0;
		forceFields.clear();
	}

	@Override
	public void pickHex(Hex hex) {
		if(!isValidChoice(Map.player.hex, hex)||forceFields.contains(hex))return;
		
		hex.forceField=3;
		forceFields.add(hex);
		if(forceFields.size()==3){
			for(Hex h:Map.grid.drawableHexes)if(isValidChoice(Map.player.hex, h))h.mapAbilityChoiceFadeout();
			Map.using=null;
			choosing=false;
			Map.setState(MapState.EnemyMoving);
			forceFields.clear();
		}
	}

}
