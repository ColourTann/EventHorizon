package game.ship.mapThings.mapAbility.comAbility;

import util.image.Pic;
import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.ship.mapThings.mapAbility.MapAbility;

public class EMP extends MapAbility{

	public EMP(int cooldown, int fuelCost, float effort) {
		super(Gallery.shipAurora, cooldown, fuelCost, 4, effort);
	}

	@Override
	public HexChoice getBestTarget() {
		return null;
	}

	@Override
	public boolean isValidChoice(Hex target) {
		return false;
	}

	@Override
	public void pickHex(Hex hex) {
	}

}
