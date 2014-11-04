package util.update;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import game.Main;
import game.screen.escape.EscapeMenu;
import util.maths.Pair;

public class InputHandler implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		Main.keyPress(keycode);
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		Main.keyUp(keycode);
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		boolean esc=Screen.isActiveType(EscapeMenu.class);
		Mouser.updateClicked(button==0);
		if(!esc)Main.touchDown(new Pair((float)screenX/(float)Gdx.graphics.getWidth()*Main.width, ((float)screenY/(float)Gdx.graphics.getHeight()*Main.height)),button==0);
		
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
		Main.scrolled(amount);
		return false;
	}

	
}
