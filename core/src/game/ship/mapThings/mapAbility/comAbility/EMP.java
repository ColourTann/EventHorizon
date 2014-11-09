package game.ship.mapThings.mapAbility.comAbility;

import util.image.Pic;
import util.update.Timer;
import util.update.Timer.Finisher;
import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.ship.mapThings.mapAbility.MapAbility;

public class EMP extends MapAbility{

	public EMP(int cooldown, int fuelCost, float effort) {
		super(Gallery.mapAbilityEMP, cooldown, fuelCost, 4, effort);
	}

	@Override
	public void doStuff() {
		
		for(Hex h:getValidHexes()){
			h.mapShip.stun(2);
		}
		
		use();
		
		endPlayerTurn();
		
	}
	
	@Override
	public HexChoice getBestTarget() {
		return null;
	}

	@Override
	public boolean isValidChoice(Hex target) {
		return target.mapShip!=null&&target.mapShip!=mapShip;
	}

	@Override
	public void pickHex(Hex hex) {
	}

	@Override
	public String getText() {
		return "Disables all enemies for 2 turns";
	}

}
