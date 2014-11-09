package game.ship.mapThings.mapAbility.comAbility;

import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.ship.mapThings.mapAbility.MapAbility;

public class Cloak extends MapAbility{
	int duration=5;
	int durationLeft;
	public Cloak() {
		super(Gallery.mapAbilityCloak,4, 1, 0, 1);
	}
	@Override
	public void doStuff() {
		
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
		return "cloak need to code";
	}

	

}
