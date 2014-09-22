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

	ArrayList<Rocket>rockets=new ArrayList<Rocket>();

	@Override
	public void init() {
		//ParticleSystem.systems.add(new SwiftSystem());
		
	}
	@Override
	public void update(float delta) {
		for(Rocket rocket:rockets){
			if(Gdx.input.isKeyPressed(Input.Keys.UP))rocket.move(delta, 0);
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN))rocket.move(-delta, 0);
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT))rocket.move(0, -delta*3);
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))rocket.move(0, delta*3);
			rocket.update(delta);
		}
		
		//System.out.println(rockets.size());
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {


	}

	@Override
	public void render(SpriteBatch batch) {

		for(Rocket rocket:rockets)rocket.render(batch);
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void keyPress(int keycode) {

		/*switch(keycode){
case Input.Keys.UP:

			break;
case Input.Keys.DOWN:
	rocket.move(0.7f, 0);
	break;
case Input.Keys.LEFT:
	rocket.move(1f, 1);
	break;
case Input.Keys.RIGHT:
	rocket.move(1f, 1);
	break;
		case Input.Keys.CONTROL_LEFT:

			break;
		}
		 */
	}

	@Override
	public void keyUp(int keyCode) {
	}

	@Override
	public void mousePressed(Pair location, boolean left) {
		for(int i=0;i<10;i++) rockets.add(new Rocket(new Pair(50,300), location.add(Pair.randomAnyVector().multiply(40)), 3));
		
	}

	@Override
	public void scroll(int amount) {
	}
	@Override
	public void dispose() {
	}


}
