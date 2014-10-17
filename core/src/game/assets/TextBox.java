package game.assets;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Draw;
import util.TextWriter;
import util.TextWriter.Alignment;
import util.maths.Pair;
import util.update.Updater;

public class TextBox extends Updater{

	
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
		int cornerSize=Gallery.textBoxTopLeftCorner.getHeight();
		int sideSize=Gallery.textBoxTop.getHeight();
		
		int x=(int) position.x;
		int y=(int) position.y;
		if(align==Alignment.Center){
			x-=width/2f;
			y-=height/2f;
		}
		
		Draw.drawScaled(batch, Gallery.textBoxMid.get(), x+sideSize, y+sideSize, width-sideSize*2, height-sideSize*2);
		
		Draw.drawRotatedScaledFlipped(batch, Gallery.textBoxTopLeftCorner.get(), x, y, 1, 1, 0, false, false);
		Draw.drawRotatedScaledFlipped(batch, Gallery.textBoxTopLeftCorner.get(), x+width-cornerSize, y, 1, 1, 0, true, false);
		Draw.drawRotatedScaledFlipped(batch, Gallery.textBoxTopLeftCorner.get(), x, y+height-cornerSize, 1, 1, 0, false, true);
		Draw.drawRotatedScaledFlipped(batch, Gallery.textBoxTopLeftCorner.get(), x+width-cornerSize, y+height-cornerSize, 1, 1, 0, true, true);
				
		Draw.drawRotatedScaled(batch, Gallery.textBoxTop.get(), (int)(x+cornerSize), (int)y, width-cornerSize*2, 1, 0);
		Draw.drawRotatedScaled(batch, Gallery.textBoxTop.get(), (int)(x+width), (int)(y+cornerSize), height-cornerSize*2, 1, (float)Math.PI/2);
		Draw.drawRotatedScaled(batch, Gallery.textBoxTop.get(), (int)(x+width-cornerSize), (int)(y+height), width-cornerSize*2, 1, (float)Math.PI);
		Draw.drawRotatedScaled(batch, Gallery.textBoxTop.get(), (int)x, (int)(y+height-cornerSize), height-cornerSize*2, 1, (float)Math.PI*3f/2f);
	}
	
	public void render(SpriteBatch batch){
		float x=position.x;
		float y=position.y;
		if(align==Alignment.Center){
			x-=width/2f;
			y-=height/2f;
		}
		tw.drawText(batch, position.x, position.y);
	}
	
	
}
