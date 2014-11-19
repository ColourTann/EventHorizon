package game.ship.mapThings.mapAbility.genAbility;

import util.image.Pic;
import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.ship.mapThings.mapAbility.MapAbility;

public class EmergencyTeleport extends MapAbility{

	public EmergencyTeleport(int cooldown, int fuelCost, float effort) {
		super(Gallery.mapAbilityEmergencyTeleport, cooldown, fuelCost, 7, effort);
	}


	@Override
	public void selectAction() {
		System.out.println("need to code this bit");
	}
	@Override
	public HexChoice getBestTarget() {
		int total=0;
		int hexes=0;
		for(Hex h: mapShip.hex.getHexesWithin(range, false)){
			hexes++;

		}
		return new HexChoice();
	}

	@Override
	public boolean isValidChoice(Hex target) {
		return false;
	}

	@Override
	public void pickHex(Hex hex) {
	}


	@Override
	public String getText() {
		return "blah blah need to code";
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
		Map.player.resetPath();
	}

}
