package game.ship.mapThings.mapAbility.comAbility;

import java.util.ArrayList;

import game.assets.Gallery;
import game.assets.Sounds;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.ship.mapThings.mapAbility.MapAbility;



public class ForceField extends MapAbility{

	ArrayList<Hex> forceFields = new ArrayList<Hex>();
	
	public ForceField() {
		super(Gallery.mapAbilityForceField,4, 1, 5, 1);
	}

	@Override
	public boolean isValidChoice(Hex target) {
		Hex origin= mapShip.hex;
		int distance=origin.getDistance(target);
		if(distance>range)return false;
		return !target.isBlocked(true);
	}
	
	@Override
	public void selectAction() {
		forceFields.clear();
		fadeHexesIn();
		Map.using=this;
		Map.setState(MapState.PickHex);
	}

	public void deselect() {
		Map.returnToPlayerTurn();
		for(Hex h:forceFields)h.unForceField();
		forceFields.clear();
	}

	@Override
	public void pickHex(Hex hex) {
		
		if(forceFields.contains(hex)){
			forceFields.remove(hex);
			hex.unForceField();
			Sounds.cardDeselect.play();
			return;
		}
		
		if(!isValidChoice(hex))return;
		
		hex.forceField(4);
		forceFields.add(hex);
		Sounds.cardSelect.play();
		if(forceFields.size()==4){
			for(Hex h:Map.grid.drawableHexes)if(isValidChoice(h))h.mapAbilityChoiceFadeout();
			Map.using=null;
			use();
			endPlayerTurn();
			mapShip.tickMapAbilities();
		}
	}

	@Override
	public HexChoice getBestTarget() {
		return null;
	}

	@Override
	public String getText() {
		return "Block 4 hexes for 4 turns";
	}

	@Override
	public void mouseDownEffect() {
		regularMouseDown();
	}

	@Override
	public void mouseUpEffect() {
		regularMouseUp();
	}

	@Override
	protected void interrupt() {
		Map.using=this;
	}

}
