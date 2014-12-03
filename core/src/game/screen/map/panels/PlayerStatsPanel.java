package game.screen.map.panels;

import game.Main;
import game.assets.AdvancedButton;
import game.assets.Gallery;
import game.screen.map.Map;
import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.Mouser;
import util.update.SimpleButton.Code;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerStatsPanel extends SidePanel{
	static final Pair fuelLoc=new Pair(50,Main.height/2+80);
	static final Pair equipLoc=new Pair(50,Main.height/2+120);
	public static final Color fuelCol= Colours.weaponCols8[3];
	AdvancedButton equip;
	public PlayerStatsPanel(){
		Font.medium.setColor(Colours.white);
		equip=new AdvancedButton(equipLoc, false, null, "My Ship", Font.medium, new Code(){

			@Override
			public void onPress() {
				Map.showEquip();
			}
			
		});
	}
	
	@Override
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch) {
		Draw.drawCenteredScaled(batch, Gallery.fuel.get(), fuelLoc.x, fuelLoc.y, 2, 2);
		Font.big.setColor(fuelCol);
		Font.drawFontCentered(batch, ":"+Map.player.getShip().getFuel(), Font.big, fuelLoc.x+58, fuelLoc.y);
		equip.render(batch);
	}

}
