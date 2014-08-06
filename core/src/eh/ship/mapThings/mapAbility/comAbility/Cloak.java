package eh.ship.mapThings.mapAbility.comAbility;

import eh.assets.Gallery;
import eh.assets.Pic;
import eh.grid.hex.Hex;
import eh.grid.hex.HexChoice;
import eh.ship.mapThings.mapAbility.MapAbility;

public class Cloak extends MapAbility{
	int duration=5;
	int durationLeft;
	public Cloak() {
		super(Gallery.mapAbilityCloak, 0, 1);
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

}
