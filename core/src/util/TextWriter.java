package util;

import java.util.ArrayList;
import java.util.HashMap;

import util.image.Pic;
import util.maths.Pair;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextWriter {
	public enum Alignment{Left, Center}
	int baseWrapWidth=9999;
	float spaceWidth;
	float height;
	float bonusHeight=2;
	float maxHeight;
	BitmapFont font;
	Pair obstacle= new Pair();
	int[] offsets;
	HashMap<String, Texture> replacers = new HashMap<String, Texture>();
	public Alignment align= Alignment.Left; 

	public TextWriter(BitmapFont font){
		this.font=font;
		spaceWidth=font.getBounds(" ").width;
		height=font.getBounds("").height;
	}

	public void setOffsets(int[] offets){
		this.offsets=offets;
	}

	public void setAlignment(Alignment align){
		this.align=align;
	}

	public void setWrapWidth(int width){
		baseWrapWidth=width;
	}

	public void replace(String string, Texture texture){
		replacers.put(string, texture);
	}
	
	public void addObstacleTopLeft(int width, int height){
		obstacle=new Pair(width,height);
	}

	public void drawText(SpriteBatch batch, String text, float startX, float startY){

		//gotta do offsets//


		String[] words = text.split(" ");


		//precheck line width//
		int lineWidth=0;
		int bonusY=0;
		float baseStartX=startX;
		int wrapWidth=0;
		ArrayList<String> stringLine=new ArrayList<String>();
		for(String word:words){
			startX=baseStartX;
			wrapWidth=baseWrapWidth;
			if(bonusY<obstacle.y){
				wrapWidth-=obstacle.x;
				startX+=obstacle.x;
			}
			float wordWidth=0;
			Texture t= replacers.get(word);
			if(t!=null){
				wordWidth=t.getWidth();
			}
			else{
				wordWidth=font.getBounds(word).width;
			}
			if(lineWidth+wordWidth>wrapWidth&&lineWidth>0){
				write(batch, startX, startY+bonusY, lineWidth, wrapWidth, stringLine);
				bonusY+=height+bonusHeight;
				lineWidth=0;
				stringLine.clear();

			}
			
			
			lineWidth+= wordWidth;
			stringLine.add(word);
			
			
			if(lineWidth+spaceWidth>wrapWidth){
				write(batch, startX, startY+bonusY, lineWidth, wrapWidth, stringLine);
				stringLine.clear();
				bonusY+=height+bonusHeight;
				lineWidth=0;
				continue;
			}
			else{
				lineWidth+=spaceWidth;
			}
		}
		write(batch, startX, startY+bonusY, lineWidth, wrapWidth, stringLine);
		maxHeight=bonusY+height;

	}

	private void write(SpriteBatch batch, float startX, float startY, float lineWidth, float wrapWidth, ArrayList<String> words) {
		float actualSpaceWidth=0;
		float xOffset=0;

		switch(align){
		case Center:
			xOffset=(wrapWidth-lineWidth)/2f;
			actualSpaceWidth=spaceWidth;
			break;
		case Left:
			xOffset=0;
			actualSpaceWidth=spaceWidth;
			break;
	
		default:
			break;
		}

		int currentX=0;
		for(String word:words){
			Texture t = replacers.get(word);
			if(t != null){
				int picWidth=t.getWidth();
				float yOffset =(height-t.getHeight())/2f;
				Draw.draw(batch, t, (int)(startX+currentX+xOffset), (int)(startY+yOffset));
				currentX += picWidth;
			}
			else{
				TextBounds bound=font.getBounds(word);
				font.draw(batch, word, startX+currentX+xOffset, startY);
				currentX+=bound.width;
			}
			currentX+=actualSpaceWidth;
		}
	}

	public int getWrapWidth() {
		return baseWrapWidth;
	}

	public float getHeight() {
		return maxHeight;
	}
}
