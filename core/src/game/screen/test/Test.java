package game.screen.test;

import java.util.ArrayList;

import util.Draw;
import util.Noise;
import util.TextWriter;
import util.TextWriter.Alignment;
import util.assets.Font;
import util.maths.Pair;
import util.particleSystem.ParticleSystem;
import util.update.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import game.Main;
import game.assets.Gallery;
import game.assets.particles.Lightning;

public class Test extends Screen{


	float ticks=0;
	int octaves=1;
	Pixmap map;
	Texture noise;
	double z =1;
	@Override
	public void init() {
		makeNoiseTexture();

	}
	
	private void makeNoiseTexture() {
		if(noise!=null){
			noise.dispose();
			map.dispose();
		}
		
		map = new Pixmap(400,400,Format.RGBA8888);
		Pixmap.setBlending(Blending.None);
		float scale=.011f;
		float offset=400;
		Pair[] foci = new Pair[]{new Pair(100,100), new Pair(250,100), new Pair(175,150)};
		
		float maxDist=(float) Math.sqrt(50*50+50*50);
		maxDist=80;
		for(int x=0;x<map.getWidth();x++){
			for(int y=0;y<map.getHeight();y++){
				float bestCloseness=0;
				for(Pair p:foci){
					float newDist = p.getDistance(new Pair(x,y));
					float closeness=newDist/maxDist;
					closeness=1-closeness;
					if(closeness>0){
						bestCloseness+=Math.pow(closeness, 1.09);
					}
				}
				bestCloseness=Math.min(1, bestCloseness);
				
				
				float noise =(float)Noise.noise(x*scale+offset, y*scale+offset, z+offset, octaves);
				noise++;
				noise/=2d;
				//noise+=dist*2;
				//noise-=2f;
				noise=Math.max(0, noise);
				noise=Math.min(1, noise);
				//noise*=bestCloseness;
				//noise=bestCloseness;
				
				//purple nebula//
				
				
				map.setColor((noise*noise)%.5f, noise*noise*.8f, .5f+noise/4f, bestCloseness*bestCloseness);
				
				
				//map.setColor(noise%.2f, (noise%.5f), noise, 1);
				map.drawPixel(x, y);
			}
		}
		noise=new Texture(map);
	}
	
	@Override
	public void update(float delta) {


	}



	@Override
	public void shapeRender(ShapeRenderer shape) {


	}

	@Override
	public void render(SpriteBatch batch) {
		Draw.draw(batch, noise, 50, 50);

	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public boolean keyPress(int keycode) {

		switch(keycode){

		case Input.Keys.CONTROL_LEFT:
			ParticleSystem.debugDontUse.add(new Lightning(new Pair(10,10), new Pair(700,400), 3, 1));
			ParticleSystem.debugDontUse.add(new Lightning(new Pair(700,400), new Pair(Main.width-10,Main.height-10), 3, 1));
			break;
		case Input.Keys.UP:
			z+=5.03f;
			makeNoiseTexture();
			break;
		case Input.Keys.LEFT:
			octaves--;
			
			break;
		case Input.Keys.RIGHT:
			octaves++;
			makeNoiseTexture();
			break;
		case Input.Keys.L:
			
			break;
		case Input.Keys.C:

			break;
		}
		return false;
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
