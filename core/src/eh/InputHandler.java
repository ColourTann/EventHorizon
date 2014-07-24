package eh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import eh.util.Bonkject;
import eh.util.maths.Sink;

public class InputHandler implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		Main.currentScreen.keyPress(keycode);
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		Main.currentScreen.keyUp(keycode);
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Bonkject.updateClicked(button==0);
		Main.currentScreen.mousePressed(new Sink((float)screenX/(float)Gdx.graphics.getWidth()*Main.width, Main.height-((float)screenY/(float)Gdx.graphics.getHeight()*Main.height)),button==0);
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		Main.map.zoom(amount);
		return false;
	}

	
}
