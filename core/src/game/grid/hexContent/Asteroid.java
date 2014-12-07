package game.grid.hexContent;

import util.Draw;
import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;
import util.update.SimpleButton.Code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.AdvancedButton;
import game.assets.Gallery;
import game.grid.hex.Hex;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.screen.map.popup.Event;
import game.screen.map.popup.Popup;

public class Asteroid extends HexContent{
	float rotation;
	boolean mined;
	int baseFuel=7;
	int randomFuel=5;
	public Asteroid(Hex hex) {
		super(hex);
		rotation=(float) (Math.random()*10000);
	}

	@Override
	public void turn() {
		if(Math.random()>.9)chanceToMakeShip();
	}

	@Override
	public void render(SpriteBatch batch) {
		Pair loc = hex.getPixel();
		Draw.drawCenteredRotated(batch, Gallery.asteroid.get(), loc.x, loc.y, rotation);
	}

	@Override
	public String toString() {
		return "Asteroid";
	}

	@Override
	public void action() {
		mined=true;
		Map.updateActionPanel(hex);
		final int fuel =(int) (baseFuel+Math.random()*randomFuel);
		AdvancedButton[] butts = new AdvancedButton[1];
		butts[0]= new AdvancedButton(new Pair(), true, null, "Great!", Font.medium, new Code() {	
			@Override
			public void onPress() {
				Map.returnToPlayerTurn();
				Map.player.getShip().addFuel(fuel);
				Map.setState(MapState.EnemyMoving);
			}
		});
		Map.popup(new Event("You mined "+fuel+" |fuel| from the asteroid.", butts));
	}

	@Override
	public String getFlavour() {
		return mined?"Depleted":"This asteroid can be mined for |fuel|.|n|It will take one turn to mine.";
	}

	@Override
	public String getActionName() {
		return mined?null:"Mine Asteroid";
	}

	@Override
	public Pic getPic() {
		return Gallery.asteroid;
	}

}
