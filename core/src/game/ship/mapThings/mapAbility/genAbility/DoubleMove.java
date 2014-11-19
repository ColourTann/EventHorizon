package game.ship.mapThings.mapAbility.genAbility;

import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.module.utility.Furnace;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.ship.mapThings.mapAbility.MapAbility;

public class DoubleMove extends MapAbility{

	public DoubleMove(int cooldown, int fuelCost, float effort) {
		super(Gallery.mapAbilityDoubleMove ,cooldown, fuelCost, 2, effort);
	}


	@Override
	public void selectAction() {
		fadeHexesIn();
		Map.setState(MapState.PickHex);
	}

	@Override
	public boolean isValidChoice(Hex target) {
		if(target.isBlocked(false))return false;
		Hex origin=mapShip.hex;
		int distance=origin.pathFind(target).size();
		return distance==2;
	}

	@Override
	public void pickHex(Hex hex) {
		if(!isValidChoice(hex))return;
		use();
		if(mapShip.getShip().player) afterPlayerUse();
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


	@Override
	public String getText() {
		return "Move two spaces";
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
		Map.using=this;
		fadeHexesIn();
	}


}
