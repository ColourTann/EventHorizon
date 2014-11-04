package game.ship.mapThings.mapAbility.genAbility;





import util.update.Timer;
import util.update.Timer.Interp;
import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.ship.mapThings.mapAbility.MapAbility;

public class Teleport extends MapAbility{

	public Teleport() {
		super(Gallery.mapAbilityTeleport,6,4,.02f);
	}

	@Override
	public boolean isValidChoice(Hex target) {
		Hex origin=mapShip.hex;
		int dist=origin.getDistance(target);
		if(target.isBlocked(true))return false;
		return dist>1&&dist<=range;
	}
	@Override
	public void pickHex(Hex hex) {
		if(!isValidChoice(hex))return;
		if(mapShip.ship.player) afterPlayerUse();
		mapShip.stretch=new Timer(0,1, 1/Map.phaseSpeed, Interp.SIN);
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
		result.value-=effort;
		result.source=this;
		return result;
	}
	
}
