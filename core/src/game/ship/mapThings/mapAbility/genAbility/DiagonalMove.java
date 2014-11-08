package game.ship.mapThings.mapAbility.genAbility;

import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.ship.mapThings.mapAbility.MapAbility;

public class DiagonalMove extends MapAbility{

	public DiagonalMove(int cooldown, int fuelCost, float effort) {
		super(Gallery.mapAbilityDiagonalMove, cooldown, fuelCost, 2, effort);
	}

	@Override
	public boolean isValidChoice(Hex target) {
		Hex origin=mapShip.hex;
		int distance=origin.getDistance(target);
		int xDiff=Math.abs(origin.x-target.x);
		int yDiff=Math.abs(origin.y-target.y);
		if(target.isBlocked(false))return false;
		return distance==2&&!(xDiff%2==0&&yDiff%2==0);

	}

	@Override
	public void pickHex(Hex hex) {
		if(!isValidChoice(hex))return;
		use();
		if(mapShip.ship.player) afterPlayerUse();
		mapShip.moveTo(hex);
	}

	@Override
	public HexChoice getBestTarget() {
		HexChoice result = new HexChoice();
		for (Hex h:mapShip.hex.getHexesWithin(range, false)){
			if(!isValidChoice(h))continue;
			HexChoice current=new HexChoice(h, h.howGood(mapShip));
			if(current.isBetterThan(result))result=current;
		}
		result.source=this;
		return result;
	}

}
