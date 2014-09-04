package game.screen.menu;

import java.util.ArrayList;

import util.assets.Font;
import util.maths.Pair;
import util.update.Screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import game.Main;
import game.Main.ScreenType;
import game.screen.battle.Battle;
import game.screen.cardView.CardViewer;
import game.ship.shipClass.Aurora;
import game.ship.shipClass.Comet;
import game.ship.shipClass.Eclipse;
import game.ship.shipClass.Nova;

public class Selector extends Screen{
	public static Selector me;
	ArrayList<GameChoice> choices= new ArrayList<GameChoice>();
	public Selector(){
		
	}
	
	@Override
	public void init() {
		choices.clear();
		choices.add(new GameChoice(Main.width/2, 205, "tutorial", 
				new Battle(new Nova(true), new Aurora(false), true)));
		/*choices.add(new GameChoice(Main.width/2, 305, "easy", 
				new Battle(new Eclipse(true), new Aurora(false), false)));*/
		choices.add(new GameChoice(Main.width/2, 305, "easy", 
				new CardViewer()));
		choices.add(new GameChoice(Main.width/2, 405, "medium", 	
				new Battle(new Comet(true), new Nova(false), false)));
		choices.add(new GameChoice(Main.width/2, 505, "hard", 	
				new Battle(new Aurora(true), new Nova(false), false)));
	}
	
	@Override
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch) {
		Font.small.setColor(1,1,1,1);
		Font.small.draw(batch, ""+Main.version, Main.width-Font.small.getBounds(""+Main.version).width, 0);
		for(GameChoice c:choices){
			c.render(batch);
		}
	}

	@Override
	public void keyPress(int keycode) {
	}

	@Override
	public void keyUp(int keyCode) {
	}

	@Override
	public void mousePressed(Pair location, boolean left) {
	}
	@Override
	public void shapeRender(ShapeRenderer shape) {
	}
	@Override
	public void postRender(SpriteBatch batch) {
	}
	@Override
	public void scroll(int amount) {
	}

	@Override
	public void dispose() {
	}

	
}
