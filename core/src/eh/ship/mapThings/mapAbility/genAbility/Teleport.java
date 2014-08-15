package eh.ship.mapThings.mapAbility.genAbility;



import eh.grid.hex.Hex;
import eh.grid.hex.HexChoice;
import eh.screen.map.*;
import eh.screen.map.Map.*;
import eh.ship.mapThings.mapAbility.MapAbility;
import eh.util.Timer;
import eh.util.Timer.Interp;
import eh.util.assets.Gallery;
import eh.util.assets.Pic;

public class Teleport extends MapAbility{

	public Teleport() {
		super(Gallery.mapAbilityTeleport,4,.02f);
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
		mapShip.stretch=new Timer(0,1, Map.phaseSpeed, Interp.SIN);
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
