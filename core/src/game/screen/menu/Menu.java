package game.screen.menu;

import java.util.ArrayList;
import java.util.HashMap;

import util.assets.Font;
import util.maths.Pair;
import util.update.Screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import game.Main;
import game.assets.Sounds;
import game.screen.battle.Battle;
import game.screen.battle.Battle.BattleType;
import game.screen.customise.Customise;
import game.screen.map.Map;
import game.screen.preBattle.PreBattle;
import game.ship.shipClass.Aurora;
import game.ship.shipClass.Comet;
import game.ship.shipClass.Eclipse;
import game.ship.shipClass.Hornet;
import game.ship.shipClass.Nova;

public class Menu extends Screen{
	public static float power=0;
	public Menu me;
	ArrayList<GameChoice> choices= new ArrayList<GameChoice>();
	public Menu(){

	}

	@Override
	public void init() {
		Customise.power=0;
		choices.clear();
		choices.add(new GameChoice(Main.width/2, 155, "tutorial", 
				new Battle(new Nova(true, 0), new Aurora(false, 0), BattleType.Tutorial)));
		choices.add(new GameChoice(Main.width/2, 255, "easy", 
				new Battle(new Hornet(true, 0), new Aurora(false, 0), BattleType.Basic)));
		choices.add(new GameChoice(Main.width/2, 355, "medium", 	
				new Battle(new Comet(true, 0), new Eclipse(false, 0), BattleType.Basic)));
		choices.add(new GameChoice(Main.width/2, 455, "arena", 	
				new Customise(new Aurora(true, 0), true, false)));
		choices.add(new GameChoice(Main.width/2, 555, "map", 	
				new Customise(new Aurora(true, 0), true, true)));

	
	

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
	public boolean keyPress(int keycode) {
		switch(keycode){
		case Input.Keys.NUM_1:
			
			
			break;
		case Input.Keys.NUM_2:
	
			break;
		case Input.Keys.NUM_3:

			break;
		}
		return false;


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
