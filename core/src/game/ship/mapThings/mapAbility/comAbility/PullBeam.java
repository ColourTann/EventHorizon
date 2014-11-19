package game.ship.mapThings.mapAbility.comAbility;

import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.ship.mapThings.MapShip;
import game.ship.mapThings.mapAbility.MapAbility;

public class PullBeam extends MapAbility{

	public PullBeam() {
		super(Gallery.mapAbilityPull,4,1,6,1);
	}

	@Override
	public boolean isValidChoice(Hex target) {
		if(target.isBlocked(false)||target==mapShip.hex) return false;

		return(mapShip.hex.getDistance(target)<=range&&target.mapShip!=null);

		

	}

	@Override
	public void selectAction() {
		fadeHexesIn();
		Map.using=this;
		Map.setState(MapState.PickHex);
	}
	
	public void deselect() {
		Map.returnToPlayerTurn();
		Map.using=null;
	}

	@Override
	public void pickHex(Hex targetHex) {
		if(!isValidChoice(targetHex))return;
		
		
		MapShip target=targetHex.mapShip;
		Hex best=targetHex;
		float bestDist=targetHex.getLineDistance(mapShip.hex);
		for(Hex h:targetHex.getHexesWithin(2, false)){
			if(h.isBlocked(false))continue;
			float dist=h.getLineDistance(mapShip.hex);
			if(dist<bestDist){
				bestDist=dist;
				best=h;
			}
		}
		target.moveTo(best);
		target.tractor();
		use();
		endPlayerTurn();
		mapShip.tickMapAbilities();
		fadeOutHighlights();
	}

	@Override
	public HexChoice getBestTarget() {
		return null;
	}

	@Override
	public String getText() {
		return "Drag an enemy ship 2 spaces towards you";
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
	}
}
