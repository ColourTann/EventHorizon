package eh.screen.test;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eh.Main;
import eh.screen.Screen;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.PerleyBabes;
import eh.util.Timer;
import eh.util.Timer.Interp;
import eh.util.assets.Gallery;
import eh.util.assets.Pic;
import eh.util.assets.Pic.Shard;
import eh.util.maths.Pair;

public class Test extends Screen{

Pic pic=Gallery.tomato;
ArrayList<Shard> replaced=new ArrayList<Shard>();
boolean exploding;
	@Override
	public void update(float delta) {
		for(Shard s:replaced){
			s.update(delta);
		}
		if(exploding){
			for(int i=0;i<2;i++){
		Shard s=pic.removeCut();
		if(s==null)return;
		s.finalise();
		replaced.add(s);
		}
		}
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {

	
	}

	@Override
	public void render(SpriteBatch batch) {
		Draw.drawTexture(batch, pic.getCut(Colours.black), 0, 0);
		for(Shard s:replaced){
			s.render(batch);
		}
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void keyPress(int keycode) {
		
		switch(keycode){
		case Input.Keys.CONTROL_LEFT:
			pic.addShatter();
			break;
		case Input.Keys.SPACE:
			
			Shard s=pic.removeCut();
			s.finalise();
			replaced.add(s);
			
			break;
		case Input.Keys.SHIFT_LEFT:
			pic.analyseShards();
			break;
		case Input.Keys.ENTER:
			exploding=true;
			break;
		}
	}

	@Override
	public void keyUp(int keyCode) {
	}

	@Override
	public void mousePressed(Pair location, boolean left) {
	}

	@Override
	public void scroll(int amount) {
	}

}
