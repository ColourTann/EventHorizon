package game.ship.mapThings.mapAbility.comAbility;

import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.ship.mapThings.mapAbility.MapAbility;

public class Cloak extends MapAbility{
	int duration=5;
	int durationLeft;
	public Cloak() {
		super(Gallery.mapAbilityCloak,12, 1, 0, 1);
	}
	@Override
	public void selectAction() {
		mapShip.cloak(6);
		use();
		mapShip.tickMapAbilities();
		endPlayerTurn();	
	}

	@Override
	public boolean isValidChoice(Hex target) {
		return false;
	}

	@Override
	public void pickHex(Hex hex) {
	}

	@Override
	public HexChoice getBestTarget() {
		return null;
	}
	@Override
	public String getText() {
		return "Conceals you completely for 6 turns";
	}
	@Override
	public void mouseDownEffect() {
	}
	@Override
	public void mouseUpEffect() {
	}
	@Override
	protected void interrupt() {
		Map.player.resetPath();
	}

	

}
