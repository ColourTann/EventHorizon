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
import com.badlogic.gdx.graphics.Color;
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
	public final static int yOffset=2;
	public static ArrayList<TextWriter> staticList= new ArrayList<TextWriter>();
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
	Color col;
	ArrayList<PairPic> pairPics= new ArrayList<PairPic>();

	Texture texture;

	public TextWriter(BitmapFont font, String text){

		//basic stuff for EH//
		int scale=1;
		if(font==Font.medium)scale=1;
		col=font.getColor();

		this.font=font;
		spaceWidth=font.getBounds(" ").width;
		height=font.getBounds("").height;
		bonusHeight=height/7f*2;
		height+=bonusHeight;
		this.text=text;
		staticList.add(this);


	}

	public void setupTexture(){
		if(texture!=null)smallDispose();
		font.setColor(col);
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
				if(specialString.equals(" ")){
					x+=spaceWidth/2f;
					specialMode=false;
					prevIndex=currentIndex;
					continue;
				}
				Texture t= replacers.get(specialString);
				if(x+t.getWidth()>baseWrapWidth){
					x=0;
					y+=height;
				}
				Draw.draw(tempBatch, t, x, y+(int)((height-t.getHeight()-1)/2f)+yOffset);
				x+=t.getWidth();
				specialMode=false;
				prevIndex=currentIndex;
				continue;
			}
			else{
				if(c==' '){
					String word = text.substring(prevIndex, currentIndex-1);
					float wordWidth=font.getBounds(word).width;
					if(x+wordWidth>baseWrapWidth){
						x=0;
						y+=height;
					}
					font.draw(tempBatch, word, x, y+yOffset);
					x+=wordWidth;
					x+=spaceWidth;
					prevIndex=currentIndex;
				}
				if(c=='|'){
					String word = text.substring(prevIndex, currentIndex-1);
					float wordWidth=font.getBounds(word).width;
					if(x+wordWidth>baseWrapWidth){
						x=0;
						y+=height;
					}
					font.draw(tempBatch, word, x, y+yOffset);
					x+=wordWidth;
					specialMode=true;
					prevIndex=currentIndex;
				}
			}
		}
		String word = text.substring(prevIndex, currentIndex);
		float wordWidth=font.getBounds(word).width;
		if(x+wordWidth>baseWrapWidth){
			x=0;
			y+=height;
		}
		font.draw(tempBatch, word, x, y+yOffset);
		maxHeight=y+height;
		tempBatch.end();
		buffer.end();
		texture=buffer.getColorBufferTexture();
		
		tempBatch.dispose();
		
	}
	
	private void smallDispose() {
		if(texture!=null){
		texture.dispose();
		buffer.dispose();
		texture=null;
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
		if(texture==null){
			batch.end();
			setupTexture();
			batch.begin();
		}
		Draw.drawRotatedScaledFlipped(batch, texture, startX, startY-yOffset, 1, 1, 0, false, false);
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
		replace("icondamage", Gallery.damageIcon[1].get());
		replace("iconshield", Gallery.shieldIcon[1].get());
		replace("iconinfinity", Gallery.iconInfinity.get());
		replace("iconshot", Gallery.iconShots.get());
		bonusHeight+=2;
		smallDispose();
	}

	public void setPassiveReplacements() {
		replace("energy", Gallery.iconEnergy.get());
		replace("majordamage", Gallery.redHP[1].get());
		replace("shield", Gallery.shieldIcon[1].getScaled(2).get());
		replace("shot", Gallery.iconShots.getScaled(2).get());
	}
	
	public static void disposeAll() {
		for(TextWriter tw:staticList){
			tw.smallDispose();
		}
		staticList.clear();
	}

	
}

