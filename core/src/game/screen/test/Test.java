package game.screen.test;

import java.util.ArrayList;

import util.Draw;
import util.TextWriter;
import util.TextWriter.Alignment;
import util.assets.Font;
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

	TextWriter tw;
	float ticks=0;


	@Override
	public void init() {
		tw=new TextWriter(Font.small);
		tw.setWrapWidth(600);
		tw.replace("hi", Gallery.iconEnergy.get());
		tw.replace("ho", Gallery.iconCooldown.get());
		tw.replace("he", Gallery.greyHP[2].get());
		tw.addObstacleTopLeft(120, 80);
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
		batch.setColor(.2f,.2f,.2f,1);
		Draw.drawScaled(batch, Gallery.whiteSquare.get(), 200, 200, tw.getWrapWidth(), tw.getHeight());
		batch.setColor(1,1,1,1);
		Font.medium.setColor(1,1,1,1);
		tw.drawText(batch, "1111 222 3333 hi 444444 555 66666666 hi 777777 ho 88888 9 00000000 aaa bbbb c hi hi hi d e f ggggg h 1111 222 3333 hi 444444 555 66666666 hi 777777 ho 88888 9 00000000 aaa bbbb c hi hi hi d e f ggggg h", 200, 200);
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

		case Input.Keys.LEFT:
			tw.setWrapWidth(tw.getWrapWidth()-20);
			break;
		case Input.Keys.RIGHT:
			tw.setWrapWidth(tw.getWrapWidth()+20);
			break;
		case Input.Keys.L:
			tw.setAlignment(Alignment.Left);
			break;
		case Input.Keys.C:
			tw.setAlignment(Alignment.Center);
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
