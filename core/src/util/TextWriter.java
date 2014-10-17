package util;

import game.assets.Gallery;

import java.util.ArrayList;
import java.util.HashMap;

import util.assets.Font;
import util.image.Pic;
import util.maths.Pair;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextWriter {
	public enum Alignment{Left, Center}
	ArrayList<Line> lines= new ArrayList<TextWriter.Line>();
	String text;
	int baseWrapWidth=9999;
	float spaceWidth;
	float height;
	float bonusHeight=2;
	public float maxHeight;
	public float maxWidth;
	BitmapFont font;
	Pair obstacle= new Pair();
	HashMap<String, Texture> replacers = new HashMap<String, Texture>();
	public Alignment align= Alignment.Left; 

	public TextWriter(BitmapFont font, String text){
		
		//basic stuff for EH//
		int scale=1;
		if(font==Font.medium)scale=1;
		replace("energy", Gallery.iconEnergy.getScaled(scale).get());
		
		this.font=font;
		spaceWidth=font.getBounds(" ").width;
		height=font.getBounds("").height;
		System.out.println(height);
		bonusHeight=height/7f;
		this.text=text;
		setupLines();
	}
	
	public void setAlignment(Alignment align){
		this.align=align;
		lines.clear();
	}

	public void setWrapWidth(int width){
		baseWrapWidth=width;
		lines.clear();
	}

	public void replace(String string, Texture texture){
		replacers.put(string, texture);
		lines.clear();
	}
	
	public void addObstacleTopLeft(int width, int height){
		obstacle=new Pair(width,height);
		lines.clear();
	}

	public void drawText(SpriteBatch batch, float x, float y){
		if(lines.size()==0)setupLines();
		for(Line l:lines){
			l.render(batch, x, y);
		}
	}
	
	public void setupLines(){
		String[] words = text.split(" ");
		//precheck line width//
		int lineWidth=0;
		int bonusY=0;
		
		int wrapWidth=0;
		ArrayList<String> stringLine=new ArrayList<String>();
		float xOffset=0;
		for(String word:words){
	
			wrapWidth=baseWrapWidth;
			if(bonusY<obstacle.y){
				wrapWidth-=obstacle.x;
				xOffset=obstacle.x;
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
				addLine(xOffset, bonusY, lineWidth, wrapWidth, stringLine);
				bonusY+=height+bonusHeight;
				lineWidth=0;
				stringLine.clear();
				xOffset=0;
				if(bonusY<obstacle.y){
					wrapWidth-=obstacle.x;
					xOffset=obstacle.x;
				}
			}
			
			
			lineWidth+= wordWidth;
			stringLine.add(word);
			
			
			if(lineWidth+spaceWidth>wrapWidth){
				addLine(xOffset, bonusY, lineWidth, wrapWidth, stringLine);
				stringLine.clear();
				bonusY+=height+bonusHeight;
				lineWidth=0;
				xOffset=0;
				if(bonusY<obstacle.y){
					wrapWidth-=obstacle.x;
					xOffset=obstacle.x;
				}
				continue;
			}
			else{
				lineWidth+=spaceWidth;
			}
		}
		addLine(xOffset, bonusY, lineWidth, wrapWidth, stringLine);
		maxHeight=bonusY+height;	
		
	}

	private void addLine(float startX, float startY, float lineWidth, float wrapWidth, ArrayList<String> words) {
		if(lineWidth>maxWidth)maxWidth=lineWidth;
		float actualSpaceWidth=0;
		float xFlib=0;

		switch(align){
		case Center:
			xFlib=(wrapWidth-lineWidth)/2f;
			actualSpaceWidth=spaceWidth;
			break;
		case Left:
			xFlib=0;
			actualSpaceWidth=spaceWidth;
			break;
	
		default:
			break;
		}
	
		lines.add(new Line(startX+xFlib, startY, actualSpaceWidth, (ArrayList<String>) words.clone()));
	}
	
	public int getWrapWidth() {
		return baseWrapWidth;
	}

	public float getHeight() {
		return maxHeight;
	}
	
	public class Line{
		float x, y, spaceWidth;
		ArrayList<String> words;
		Line(float x, float y, float spaceWidth, ArrayList<String> words){
			this.x=x;
			this.y=y;
			this.spaceWidth=spaceWidth;
			this.words=words;
		}
		public void render(SpriteBatch batch, float xDraw, float yDraw){
			float currentX=x;
			for(String s:words){
				Texture t= replacers.get(s);
				if(t!=null){
					Draw.draw(batch, t, currentX+xDraw, y+yDraw+(height-t.getHeight())/2);
					currentX+=t.getWidth();
				}
				else {
					font.draw(batch, s, currentX+xDraw, y+yDraw);
					currentX+=font.getBounds(s).width;
				}
				currentX+= spaceWidth;
				
			}
		}
	}
}

