package game.ship.mapThings.mapAbility.genAbility;

import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.ship.mapThings.mapAbility.MapAbility;

public class DiagonalMove extends MapAbility{

	public DiagonalMove(int cooldown, int fuelCost, float effort) {
		super(Gallery.mapAbilityDiagonalMove, cooldown, fuelCost, 2, effort);
	}

	@Override
	public boolean isValidChoice(Hex target) {
		if(target.isBlocked(false))return false;
		Hex origin=mapShip.hex;
		int distance=origin.getDistance(target);
		int xDiff=Math.abs(origin.x-target.x);
		int yDiff=Math.abs(origin.y-target.y);
		
		int actualDistace=origin.pathFind(target).size();
		if(actualDistace!=2)return false;
		return distance==2&&!(xDiff%2==0&&yDiff%2==0);
	}

	@Override
	public void selectAction() {
		fadeHexesIn();
		Map.setState(MapState.PickHex);
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
		return "Move like a knight!";
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
