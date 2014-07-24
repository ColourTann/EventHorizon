package eh.screen.menu;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eh.Main;
import eh.Main.ScreenType;
import eh.assets.Font;
import eh.screen.Screen;
import eh.util.maths.Sink;

public class Selector extends Screen{
	public static Selector me;
	ArrayList<GameChoice> choices= new ArrayList<GameChoice>();
	public Selector(){
		choices.add(new GameChoice(Main.width/2, 505, "tutorial", ScreenType.TutorialFight));
		choices.add(new GameChoice(Main.width/2, 405, "easy", ScreenType.EasyFight));
		choices.add(new GameChoice(Main.width/2, 305, "medium", ScreenType.MediumFight));
		choices.add(new GameChoice(Main.width/2, 205, "hard", ScreenType.HardFight));
	}
	@Override
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch) {
		Font.small.setColor(1,1,1,1);
		Font.small.draw(batch, ""+Main.version, 3, Main.height);
	}

	@Override
	public void keyPress(int keycode) {
	}

	@Override
	public void keyUp(int keyCode) {
	}

	@Override
	public void mousePressed(Sink location, boolean left) {
	}
	@Override
	public void shapeRender(ShapeRenderer shape) {
	}
	
}
