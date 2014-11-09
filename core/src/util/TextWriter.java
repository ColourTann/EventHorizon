package util;

import game.Main;
import game.assets.Gallery;
import game.screen.battle.tutorial.PicLoc;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.LineListener;

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
	public float height;
	float bonusHeight=3;
	public float maxHeight;
	public float maxWidth;
	float wigglyTextureHeight;
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
		col=font.getColor().cpy();

		this.font=font;
		spaceWidth=font.getBounds(" ").width;
		height=font.getBounds("l").height;
		bonusHeight=height/7f*4;
		height+=bonusHeight;
		this.text=text;
		staticList.add(this);
		wigglyTextureHeight=height*.5f;
		setCardGraphicReplacements();

	}

	public static int getClosestPower(int i){
		//if(true)return 1024; mac stuff ughhhh
		int result=1;
		while(result<i)result<<=1;
		return result;
	}
	
	public void setupTexture(){
		if(texture!=null)smallDispose();
		font.setColor(col);
		int buffWidth=getClosestPower(baseWrapWidth);
		int buffHeight=getClosestPower(300);
		buffer = new FrameBuffer(Format.RGBA4444, buffWidth, buffHeight ,false);
		SpriteBatch tempBatch = new SpriteBatch();
		buffer.begin();
		OrthographicCamera tempCam = new OrthographicCamera(buffWidth, buffHeight);

		tempCam.translate(buffWidth/2, buffHeight/2);
		tempCam.update();
		tempBatch.setProjectionMatrix(tempCam.combined);
		tempBatch.begin();

		boolean specialMode=false;
		int prevIndex=0;
		int currentIndex=0;
		float x=0;
		float y=0;
		int lineStart=0;
		float wrapWidth;
		float xOffset;
		
		xOffset=0;
		wrapWidth=baseWrapWidth;
		if(y<obstacle.y){
			wrapWidth-=obstacle.x;
			xOffset=obstacle.x;
		}
		
		for(char c:text.toCharArray()){
			currentIndex++;
			if(specialMode){
				if(c!='|')continue;
				String specialString=text.substring(prevIndex, currentIndex-1);	
				if(specialString.equals("n")){
					drawLine(tempBatch, text.substring(lineStart, prevIndex-1), y, x, xOffset);
					xOffset=0;
					wrapWidth=baseWrapWidth;
					lineStart=currentIndex;
					x=0;
					y+=height;
					specialMode=false;
					prevIndex=currentIndex;
					if(y<obstacle.y){
						wrapWidth-=obstacle.x;
						xOffset=obstacle.x;
					}
					continue;
				}
				if(specialString.equals(" ")){
					x+=spaceWidth/2f;
					specialMode=false;
					prevIndex=currentIndex;
					continue;
				}
				Texture t= replacers.get(specialString);
				if(x+t.getWidth()>wrapWidth&&lineStart!=prevIndex){
					drawLine(tempBatch, text.substring(lineStart, prevIndex-1), y, x, xOffset);
					xOffset=0;
					wrapWidth=baseWrapWidth;
					lineStart=prevIndex-1;
					x=0;
					y+=height;
					if(y<obstacle.y){
						wrapWidth-=obstacle.x;
						xOffset=obstacle.x;
					}
				}

				x+=t.getWidth();
				specialMode=false;
				prevIndex=currentIndex;
				continue;
			}
			else{
				if(c==' '){
					
					String word = text.substring(prevIndex, currentIndex-1);
				
					float wordWidth=font.getBounds(word).width;
					if(x+wordWidth>wrapWidth&&lineStart!=prevIndex){
						
						drawLine(tempBatch, text.substring(lineStart, prevIndex-1), y, x-spaceWidth, xOffset);
						xOffset=0;
						wrapWidth=baseWrapWidth;
						lineStart=prevIndex;
						x=0;
						y+=height;
						if(y<obstacle.y){
							wrapWidth-=obstacle.x;
							xOffset=obstacle.x;
						}
					}
						x+=wordWidth;
						x+=spaceWidth;
					prevIndex=currentIndex;
				}
				if(c=='|'){
					String word = text.substring(prevIndex, currentIndex-1);
					float wordWidth=font.getBounds(word).width;
					if(x+wordWidth>wrapWidth&&lineStart!=prevIndex){
						drawLine(tempBatch, text.substring(lineStart, prevIndex-1), y, x, xOffset);
						xOffset=0;
						wrapWidth=baseWrapWidth;
						lineStart=prevIndex;
						x=0;
						y+=height;
						if(y<obstacle.y){
							wrapWidth-=obstacle.x;
							xOffset=obstacle.x;
						}
					}

					x+=wordWidth;
					specialMode=true;
					prevIndex=currentIndex;
				}
			}
		}
		String word = text.substring(prevIndex, currentIndex);
		float wordWidth=font.getBounds(word).width;
		if(x+wordWidth>wrapWidth){
			drawLine(tempBatch, text.substring(lineStart, prevIndex), y, x, xOffset);
			xOffset=0;
			wrapWidth=baseWrapWidth;
			lineStart=prevIndex;
			x=wordWidth;
			y+=height;
			if(y<obstacle.y){
				wrapWidth-=obstacle.x;
				xOffset=obstacle.x;
			}
		}
		else x+=wordWidth;
		drawLine(tempBatch, text.substring(lineStart, currentIndex), y, x, xOffset);
		maxHeight=y+height-bonusHeight/2;
		tempBatch.setColor(1,1,1,1);
		
		for(PairPic pp:pairPics){
			Draw.draw(tempBatch, pp.pic.get(), pp.position.x, pp.position.y+yOffset);
		}
		//tempBatch.setColor(1,1,1,.8f);	Draw.drawScaled(tempBatch, Gallery.whiteSquare.get(), 0, yOffset, baseWrapWidth, maxHeight);
		tempBatch.end();
		buffer.end();
		texture=buffer.getColorBufferTexture();
//		Pixmap map = new Pixmap(baseWrapWidth, (int) maxHeight, Format.RGBA8888);
//		Pixmap old = Pic.getPixMap(texture);
//		map.drawPixmap(old, 0, 0);
//		texture=new Texture(map);
		tempBatch.dispose();
		

	}

	private void drawLine(SpriteBatch batch, String s, float y, float lineLength, float xOffset){
		int prevIndex=0;
		int currentIndex=0;
		float x=0;
		if(align==Alignment.Center){
			x=(baseWrapWidth/2f-lineLength/2f);
		}
		boolean specialMode=false;
		for(char c:s.toCharArray()){
			currentIndex++;
			if(specialMode){

				if(c!='|')continue;
				String specialString=s.substring(prevIndex, currentIndex-1);	
				if(specialString.equals(" ")){
					x+=spaceWidth/2f;
					prevIndex=currentIndex;
					specialMode=false;
					continue;
				}
				Texture t= replacers.get(specialString);
				Draw.draw(batch, t, (int)(x+xOffset), (int)(y+((float)height-(wigglyTextureHeight/2f))/2f-t.getHeight()/2f+yOffset));
				x+=t.getWidth();
				prevIndex=currentIndex;
				specialMode=false;

			}
			else{


				if(c=='|'){
					String word=s.substring(prevIndex, currentIndex-1);
					font.draw(batch, word, (int)(x+xOffset), (int)(y+yOffset));
					x+=font.getBounds(word).width;
					specialMode=true;
					prevIndex=currentIndex;
				}
				if(c==' '){
					String word=s.substring(prevIndex, currentIndex-1);
					font.draw(batch, word, (int)(x+xOffset), (int)(y+yOffset));

					x+=font.getBounds(word).width;
					x+=spaceWidth;
					prevIndex=currentIndex;
				}
			}

		}
		font.draw(batch, s.substring(prevIndex, currentIndex), (int)(x+xOffset), (int)(y+yOffset));
		x+=font.getBounds(s.substring(prevIndex, currentIndex)).width;
		if(x>maxWidth){
			maxWidth=x;
		}
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

	public void render(SpriteBatch batch, float startX, float startY){
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

