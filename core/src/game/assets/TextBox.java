package game.assets;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import util.Colours;
import util.Draw;
import util.TextWriter;
import util.TextWriter.Alignment;
import util.image.Pic;
import util.maths.Pair;
import util.update.Updater;

public class TextBox extends Updater{
	static int cornerSize=Gallery.textBoxTopLeftCorner.getHeight();
	static int sideSize=Gallery.textBoxTop.getHeight();
	public static int gap=4;
	protected Alignment align=Alignment.Left;
	TextWriter tw;
	float width,height;
	public TextBox(Pair position, String text, BitmapFont font, Alignment align){
		this.position=position;
		this.align=align;
		tw=new TextWriter(font, text);
		width=tw.maxWidth;
		height=tw.maxHeight;

	}
	public TextBox(){}
	@Override
	public void update(float delta) {
	}

	protected void renderBox(SpriteBatch batch, float width, float height){
		renderBox(batch, position, width, height, align, false);
	}

	public static void renderBox(SpriteBatch batch, Pair position, float width, float height, Alignment align, boolean moused){
		int x=(int) position.x;
		int y=(int) position.y;
		if(align==Alignment.Center){
			x-=width/2f;
			y-=height/2f;
		}

		Texture mid;
		Texture topLeft;
		Texture top;
		mid = Gallery.textBoxMid.get();
		if(moused){
			topLeft = Gallery.textBoxTopLeftCorner.getMask(Colours.white);
			top = Gallery.textBoxTop.getMask(Colours.white);
		}
		else{
			topLeft = Gallery.textBoxTopLeftCorner.get();
			top = Gallery.textBoxTop.get();
		}

		if(moused){
			batch.setColor(Colours.light);
		}
		Draw.drawScaled(batch, mid, x+sideSize, y+sideSize, width-sideSize*2, height-sideSize*2);

		Draw.drawRotatedScaledFlipped(batch, topLeft, x, y, 1, 1, 0, false, false);
		Draw.drawRotatedScaledFlipped(batch, topLeft, x+width-cornerSize, y, 1, 1, 0, true, false);
		Draw.drawRotatedScaledFlipped(batch, topLeft, x, y+height-cornerSize, 1, 1, 0, false, true);
		Draw.drawRotatedScaledFlipped(batch, topLeft, x+width-cornerSize, y+height-cornerSize, 1, 1, 0, true, true);

		Draw.drawRotatedScaled(batch, top, (int)(x+cornerSize), (int)y, width-cornerSize*2, 1, 0);
		Draw.drawRotatedScaled(batch, top, (int)(x+width), (int)(y+cornerSize), height-cornerSize*2, 1, (float)Math.PI/2);
		Draw.drawRotatedScaled(batch, top, (int)(x+width-cornerSize), (int)(y+height), width-cornerSize*2, 1, (float)Math.PI);
		Draw.drawRotatedScaled(batch, top, (int)x, (int)(y+height-cornerSize), height-cornerSize*2, 1, (float)Math.PI*3f/2f);
	}

	public void render(SpriteBatch batch){
		float x=position.x;
		float y=position.y;
		if(align==Alignment.Center){
			x-=width/2f;
			y-=height/2f;
		}
		tw.render(batch, position.x, position.y);
	}


}
