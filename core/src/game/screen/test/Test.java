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

import game.Main;
import game.assets.Gallery;
import game.assets.particles.Lightning;

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

		ParticleSystem.renderAll(batch);
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void keyPress(int keycode) {

		switch(keycode){

		case Input.Keys.CONTROL_LEFT:
			ParticleSystem.debugDontUse.add(new Lightning(new Pair(10,10), new Pair(700,400), 3, 1));
			ParticleSystem.debugDontUse.add(new Lightning(new Pair(700,400), new Pair(Main.width-10,Main.height-10), 3, 1));
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
