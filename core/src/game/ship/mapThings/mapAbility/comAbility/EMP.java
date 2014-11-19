package game.ship.mapThings.mapAbility.comAbility;

import util.image.Pic;
import util.update.Timer;
import util.update.Timer.Finisher;
import game.assets.Gallery;
import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.ship.mapThings.mapAbility.MapAbility;

public class EMP extends MapAbility{

	public EMP(int cooldown, float effort) {
		super(Gallery.mapAbilityEMP, cooldown, 2, 4, effort);
	}

	@Override
	public void selectAction() {
		
		for(Hex h:getValidHexes()){
			h.Emp();
		}
		use();
		mapShip.tickMapAbilities();
		endPlayerTurn();		
	}
	
	@Override
	public HexChoice getBestTarget() {
		return null;
	}

	@Override
	public boolean isValidChoice(Hex target) {
		//return target.mapShip!=null&&target.mapShip!=mapShip;
		return true;
	}

	@Override
	public void pickHex(Hex hex) {
	}

	@Override
	public String getText() {
		return "Disables all enemies for 2 turns";
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
	}

}
