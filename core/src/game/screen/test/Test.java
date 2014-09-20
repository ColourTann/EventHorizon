package game.screen.test;

import java.util.ArrayList;

import util.Draw;
import util.image.PicCut;
import util.image.PicCut.Shard;
import util.maths.Pair;
import util.particleSystem.Particle;
import util.particleSystem.ParticleSystem;
import util.update.Animation;
import util.update.Screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import game.assets.Gallery;
import game.assets.particles.SwiftSystem;
import game.ship.Debris;

public class Test extends Screen{

	
	float ticks=0;

	@Override
	public void init() {
		ParticleSystem.systems.add(new SwiftSystem());
	}
	@Override
	public void update(float delta) {
		
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {


	}

	@Override
	public void render(SpriteBatch batch) {
		Draw.drawCentered(batch, Gallery.swift.get(), 180, 200);
	ParticleSystem.renderAll(batch);
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
