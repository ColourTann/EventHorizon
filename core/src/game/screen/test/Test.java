package game.screen.test;

import java.util.ArrayList;

import util.Draw;
import util.Draw.BlendType;
import util.image.PicCut;
import util.image.PicCut.Shard;
import util.maths.Pair;
import util.update.Animation;
import util.update.Screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import game.assets.Gallery;
import game.ship.Debris;

public class Test extends Screen{

	PicCut cut=Gallery.shipComet.getCut();
	ArrayList<Shard> replaced=new ArrayList<Shard>();
	boolean exploding;
	public static ArrayList<Debris> debris= new ArrayList<Debris>(); 
	ArrayList<Animation> animations= new ArrayList<Animation>();


	float ticks=0;

	@Override
	public void init() {
	}
	@Override
	public void update(float delta) {
		for(Shard s:replaced){
			s.update(delta);
		}
		if(exploding){
			for(int i=0;i<2;i++){
				Shard s=cut.removeCut();
				if(s==null)return;
				s.finalise();
				replaced.add(s);
			}
		}
		ticks+=delta;
		if(ticks>.1f){
			//animations.add(new Explosion1());
			ticks-=.1f;
		}
		for(int i=0;i<animations.size();i++){
			Animation a= animations.get(i);
			if(a.isDone()){
				animations.remove(a);
				i--;
			}
		}
		/*	debris.add(new Debris(Math.random()>.7));
		}

		for(Debris d:debris){
			d.update(delta);
		}*/
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {


	}

	@Override
	public void render(SpriteBatch batch) {
		Draw.draw(batch, cut.get(), 0, 0);
		for(Shard s:replaced){
			s.render(batch);
		}
		for(Debris d:debris){
			d.render(batch);
		}
		for(Animation a:animations){
			a.render(batch);
		}
	
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void keyPress(int keycode) {

		switch(keycode){
		case Input.Keys.CONTROL_LEFT:
			cut.addShatter();

			break;
		case Input.Keys.SPACE:

			Shard s=cut.removeCut();
			s.finalise();
			replaced.add(s);

			break;
		case Input.Keys.SHIFT_LEFT:

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
