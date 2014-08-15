package eh.screen.menu;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eh.Main;
import eh.Main.ScreenType;
import eh.screen.Screen;
import eh.util.assets.Font;
import eh.util.maths.Pair;

public class Selector extends Screen{
	public static Selector me;
	ArrayList<GameChoice> choices= new ArrayList<GameChoice>();
	public Selector(){
		choices.add(new GameChoice(Main.width/2, 205, "tutorial", ScreenType.TutorialFight));
		choices.add(new GameChoice(Main.width/2, 305, "easy", ScreenType.EasyFight));
		choices.add(new GameChoice(Main.width/2, 405, "medium", ScreenType.MediumFight));
		choices.add(new GameChoice(Main.width/2, 505, "hard", ScreenType.HardFight));
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
	
}
