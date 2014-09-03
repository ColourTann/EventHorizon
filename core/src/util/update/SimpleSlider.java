package util.update;

import game.assets.Gallery;
import util.Colours;
import util.Draw;
import util.Option;
import util.assets.MusicClip;
import util.maths.BoxCollider;
import util.maths.Pair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class SimpleSlider extends Mouser{
	Color outside, inside;
	int offset, width, height;
	Option option;
	public SimpleSlider(Pair position, int width, int height, Color outside, Color inside, int offset, Option option){
		this.position=position;
		this.width=width;
		this.height=height;
		this.offset=offset;
		this.option=option;
		this.offset=offset;
		this.outside=outside;
		this.inside=inside;
		collider=new BoxCollider(position.x, position.y, width, height);
		mousectivate(collider);
	}
	@Override
	public void update(float delta) {
	
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			Pair mouse=Mouser.getMousePosition();
			if(collider.collidePoint(mouse)){
				float value=(mouse.x-position.x-offset)/(width-offset*2);
				option.setFloat(value);
				MusicClip.refresh();
			}
		}
	}

	@Override
	public void mouseDown() {
	}

	@Override
	public void mouseUp() {
	}

	@Override
	public void mouseClicked(boolean left) {
	}

	public void render(SpriteBatch batch){


		batch.setColor(Colours.withAlpha(outside, batch.getColor().a));
		Draw.drawScaled(batch, Gallery.whiteSquare.get(), position.x, position.y, width, height);
		batch.setColor(Colours.withAlpha(inside, batch.getColor().a));
		Draw.drawScaled(batch, Gallery.whiteSquare.get(), position.x+offset, position.y+offset, (width-offset*2)*option.getFloat(), height-offset*2);

	}

}
