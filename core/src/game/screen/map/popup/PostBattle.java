package game.screen.map.popup;

import util.Colours;
import util.Draw;
import util.TextWriter;
import util.TextWriter.Alignment;
import util.assets.Font;
import util.maths.Pair;
import util.update.SimpleButton.Code;
import game.Main;
import game.assets.AdvancedButton;
import game.assets.Gallery;
import game.assets.TextBox;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.screen.map.panels.PlayerStatsPanel;
import game.screen.map.stuff.Item;
import game.ship.Ship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PostBattle extends Popup{

	String text= "In the wreckage of the defeated ship you find-";
	Item loot;
	int fuelReward;
	static final int width=320;
	static final int height=240;
	static final int gap=10;
	static final Pair position=new Pair(Main.width/2-width/2, Main.height/2-height/2);
	static final Pair lootPosition=new Pair(20,85);
	static final Pair textPosition=new Pair(gap+TextBox.gap, gap+TextBox.gap);
	static final Pair buttPosition=new Pair(width/2, 210);
	Pair fuelPosition;
	TextWriter tw;
	AdvancedButton button;
	public PostBattle(Ship defeated){
		loot=defeated.getLoot();
		
		if(loot!=null){
			loot.deactivate();
			loot.demousectivate();
			fuelPosition=new Pair(175,100);
		}
		else{
			fuelPosition=new Pair(width/5+10,100);
			
		}
		Font.medium.setColor(Colours.light);
		tw=new TextWriter(Font.medium, text);
		tw.setWrapWidth(width-gap*2);
		
		button=new AdvancedButton(buttPosition.add(position), true, null, "Ok!", Font.medium, new Code() {
			@Override
			public void onPress() {
				Map.returnToPlayerTurn();
				Map.player.getShip().addFuel(fuelReward);
				Map.player.getShip().addInv(loot);
			}
		});
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(1, 1, 1, 1);
		TextBox.renderBox(batch, position, width, height, Alignment.Left, false);
		Draw.drawScaled(batch, Gallery.fuel.get(), position.x+fuelPosition.x, position.y+fuelPosition.y, 3, 3);
		Font.big.setColor(PlayerStatsPanel.fuelCol);
		Font.big.draw(batch, ":"+fuelReward, position.x+fuelPosition.x+89, position.y+fuelPosition.y+24);
		tw.render(batch, position.x+textPosition.x, position.y+textPosition.y);
		if(loot!=null){
			loot.render(batch, position.x+lootPosition.x, position.y+lootPosition.y);
		}
		batch.setColor(1,1,1,1);
		button.render(batch);
	}

	@Override
	public void dispose() {
		tw.smallDispose();
	}

	@Override
	public void update(float delta) {
	}

}
