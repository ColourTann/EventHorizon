package util;

import game.Main;
import game.assets.Gallery;
import game.screen.battle.tutorial.PicLoc;

import java.util.ArrayList;
import java.util.HashMap;

import util.assets.Font;
import util.image.PairPic;
import util.image.Pic;
import util.maths.Pair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;

public class TextWriter {
	public enum Alignment{Left, Center}
	String text;
	int baseWrapWidth=9999;
	float spaceWidth;
	float height;
	float bonusHeight=3;
	public float maxHeight;
	public float maxWidth;
	BitmapFont font;
	Pair obstacle= new Pair();
	HashMap<String, Texture> replacers = new HashMap<String, Texture>();
	public Alignment align= Alignment.Left; 
	FrameBuffer buffer;
	
	ArrayList<PairPic> pairPics= new ArrayList<PairPic>();

	Texture testTexture;

	public TextWriter(BitmapFont font, String text){

		//basic stuff for EH//
		int scale=1;
		if(font==Font.medium)scale=1;


		this.font=font;
		spaceWidth=font.getBounds(" ").width;
		height=font.getBounds("").height;
		bonusHeight=height/7f*2;
		height+=bonusHeight;
		this.text=text;



	}

	public void setupTexture(){
		if(testTexture!=null)smallDispose();
		
		buffer = new FrameBuffer(Format.RGBA8888, baseWrapWidth,300, false);
		SpriteBatch tempBatch = new SpriteBatch();
		buffer.begin();
		OrthographicCamera tempCam = new OrthographicCamera(baseWrapWidth, 300);
		
		tempCam.translate(baseWrapWidth/2, 300/2);
		tempCam.update();
		tempBatch.setProjectionMatrix(tempCam.combined);
		tempBatch.begin();
		
		boolean specialMode=false;
		int prevIndex=0;
		int currentIndex=0;
		int x=0;
		int y=0;
		for(char c:text.toCharArray()){
			currentIndex++;
			if(specialMode){
				if(c!='|')continue;
				String specialString=text.substring(prevIndex, currentIndex-1);	
				if(specialString.equals("n")){
					x=0;
					y+=height;
					specialMode=false;
					prevIndex=currentIndex;
					continue;
				}
				Texture t= replacers.get(specialString);
				if(x+t.getWidth()>baseWrapWidth){
					x=0;
					y+=height;
				}
				Draw.draw(tempBatch, t, +x, +y+(int)((height-t.getHeight())/2f));
				x+=t.getWidth();
				specialMode=false;
				prevIndex=currentIndex;
				continue;
			}
			else{
				if(c==' '){
					String word = text.substring(prevIndex, currentIndex);
					float wordWidth=font.getBounds(word).width;
					if(x+wordWidth>baseWrapWidth){
						x=0;
						y+=height;
					}
					font.draw(tempBatch, word, x, y);
					x+=wordWidth;
					x+=spaceWidth;
					prevIndex=currentIndex;
				}
				if(c=='|'){
					String word = text.substring(prevIndex, currentIndex);
					float wordWidth=font.getBounds(word).width;
					if(x+wordWidth>baseWrapWidth){
						x=0;
						y+=height;
					}
					font.draw(tempBatch, word, x, y);
					x+=wordWidth;
					specialMode=true;
					prevIndex=currentIndex;
				}
			}
		}
		maxHeight=y+height;
		tempBatch.end();
		buffer.end();
		testTexture=buffer.getColorBufferTexture();
		
		tempBatch.dispose();
		
	}
	
	private void smallDispose() {
		if(testTexture!=null){
		testTexture.dispose();
		buffer.dispose();
		testTexture=null;
		}
	}

	public void setText(String text){
		this.text=text;
		smallDispose();
	}

	public void setAlignment(Alignment align){
		this.align=align;
		smallDispose();
	}

	public void setWrapWidth(int width){
		baseWrapWidth=width;
		smallDispose();
	}

	public void replace(String string, Texture texture){
		replacers.put(string, texture);
		smallDispose();
	}

	public void addObstacleTopLeft(int width, int height){
		obstacle=new Pair(width,height);
		smallDispose();
	}

	public void drawText(SpriteBatch batch, float startX, float startY){
		if(testTexture==null){
			batch.end();
			setupTexture();
			batch.begin();
		}
		Draw.drawRotatedScaledFlipped(batch, testTexture, startX, startY, 1, 1, 0, false, false);
		
		

	}

	

	//	public int n(int index){
	//		int finalIndex=index;
	//		String s=text.substring(index);
	//		for(char c:s.toCharArray()){
	//			finalIndex++;
	//			if(c==' '){
	//
	//			}
	//		}
	//	}

	//	public void setupLines(){
	//		String[] words = text.split(" ");
	//		//precheck line width//
	//		int lineWidth=0;
	//		int bonusY=0;
	//
	//		int wrapWidth=0;
	//		ArrayList<String> stringLine=new ArrayList<String>();
	//		float xOffset=0;
	//		for(String word:words){
	//
	//			wrapWidth=baseWrapWidth;
	//			if(bonusY<obstacle.y){
	//				wrapWidth-=obstacle.x;
	//				xOffset=obstacle.x;
	//			}
	//			float wordWidth=0;
	//			Texture t= replacers.get(word);
	//			if(t!=null){
	//				wordWidth=t.getWidth();
	//			}
	//			else{
	//				wordWidth=font.getBounds(word).width;
	//			}
	//			if(lineWidth+wordWidth>wrapWidth&&lineWidth>0){
	//				addLine(xOffset, bonusY, lineWidth, wrapWidth, stringLine);
	//				bonusY+=height+bonusHeight;
	//				lineWidth=0;
	//				stringLine.clear();
	//				xOffset=0;
	//				if(bonusY<obstacle.y){
	//					wrapWidth-=obstacle.x;
	//					xOffset=obstacle.x;
	//				}
	//			}
	//
	//
	//			if(word.equals("\n")){
	//				if(lineWidth==0)continue;
	//				else{
	//					addLine(xOffset, bonusY, lineWidth, wrapWidth, stringLine);
	//					bonusY+=height+bonusHeight;
	//					lineWidth=0;
	//					stringLine.clear();
	//					xOffset=0;
	//					continue;
	//				}
	//			}
	//
	//			lineWidth+= wordWidth;
	//			stringLine.add(word);
	//
	//
	//			if(lineWidth+spaceWidth>wrapWidth){
	//				addLine(xOffset, bonusY, lineWidth, wrapWidth, stringLine);
	//				stringLine.clear();
	//				bonusY+=height+bonusHeight;
	//				lineWidth=0;
	//				xOffset=0;
	//				if(bonusY<obstacle.y){
	//					wrapWidth-=obstacle.x;
	//					xOffset=obstacle.x;
	//				}
	//				continue;
	//			}
	//			else{
	//				lineWidth+=spaceWidth;
	//			}
	//		}
	//		addLine(xOffset, bonusY, lineWidth, wrapWidth, stringLine);
	//		maxHeight=bonusY+height;	
	//
	//	}

	//	private void addLine(float startX, float startY, float lineWidth, float wrapWidth, ArrayList<String> words) {
	//		if(lineWidth>maxWidth)maxWidth=lineWidth;
	//		float actualSpaceWidth=0;
	//		float xFlib=0;
	//
	//		switch(align){
	//		case Center:
	//			xFlib=(wrapWidth-lineWidth)/2f;
	//			actualSpaceWidth=spaceWidth;
	//			break;
	//		case Left:
	//			xFlib=0;
	//			actualSpaceWidth=spaceWidth;
	//			break;
	//
	//		default:
	//			break;
	//		}
	//
	//		lines.add(new Line(startX+xFlib, startY, actualSpaceWidth, (ArrayList<String>) words.clone()));
	//	}

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
					Draw.draw(batch, t, (currentX+xDraw), (y+yDraw+(int)((height)/2f-((t.getHeight())/2f))));
					currentX+=t.getWidth();
				}
				else {
					font.draw(batch, s, (currentX+xDraw), (y+yDraw));
					currentX+=font.getBounds(s).width;
				}
				currentX+= spaceWidth;

			}
		}
	}

	public void addPairPic(PairPic pairPic) {
		pairPics.add(pairPic);
		smallDispose();
	}

	public void setCardGraphicReplacements() {
		replace("icontarget", Gallery.iconTargetedMini.get());
		replace("iconenergy", Gallery.iconEnergyMini.get());
		replace("icondamage", Gallery.iconDamage.get());
		replace("iconshield", Gallery.shieldIcon[1].get());
		replace("iconinfinity", Gallery.iconInfinity.get());
		replace("iconmajordamage", Gallery.iconMajorDamage.get());
		bonusHeight+=2;
		smallDispose();
	}
}

