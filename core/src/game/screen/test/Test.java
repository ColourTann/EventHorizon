package game.screen.test;

import java.util.ArrayList;

import util.Draw;
import util.maths.Pair;
import util.particleSystem.ParticleSystem;
import util.update.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import game.assets.Gallery;

public class Test extends Screen{


	float ticks=0;


	@Override
	public void init() {
		//ParticleSystem.systems.add(new SwiftSystem());
		
	}
	@Override
	public void update(float delta) {
		
		//System.out.println(rockets.size());
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {


	}

	@Override
	public void render(SpriteBatch batch) {

	
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void keyPress(int keycode) {

		switch(keycode){

		case Input.Keys.CONTROL_LEFT:

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
	@Override
	public void dispose() {
	}


}
