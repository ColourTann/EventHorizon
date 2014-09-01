package util;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Draw {
	//Drawing Junk//
	public static void drawTexture(SpriteBatch batch, Texture t, float x, float y){
		drawTextureRotatedScaled(batch, t, x, y, 1, 1, 0);
	}
	
	public static void drawTextureScaled(SpriteBatch batch, Texture t, float x, float y, float scaleX, float scaleY){
		drawTextureRotatedScaled(batch, t, x, y, scaleX, scaleY, 0);
	}
	
	public static void drawTextureScaledFlipped(SpriteBatch batch, Texture t, float x, float y, float scaleX, float scaleY, boolean xFlip, boolean yFlip){
		batch.draw(t, x, y, 0, 0, t.getWidth(), t.getHeight(), scaleX, scaleY, 0,0,0,t.getWidth(),t.getHeight(),xFlip,!yFlip);	
	}
	
	public static void drawTextureRotatedScaled(SpriteBatch batch, Texture t, float x, float y, float scaleX, float scaleY, float radianRotation){
		batch.draw(t, x, y, 0, 0, t.getWidth(), t.getHeight(), scaleX, scaleY, rad2deg(radianRotation),0,0,t.getWidth(),t.getHeight(),false,true);
	}
	
	public static void drawTextureCentered(SpriteBatch batch, Texture t, float x, float y){
		drawTextureRotatedScaledCentered(batch, t, x, y, 1, 1, 0);
	}
	
	public static void drawTextureScaledCentered(SpriteBatch batch, Texture t, float x, float y, float scaleX, float scaleY){
		drawTextureRotatedScaledCentered(batch, t, x, y, scaleX, scaleY, 0);
	}
	public static void drawTextureRotatedCentered(SpriteBatch batch, Texture t, float x, float y, float radianRotation){
		drawTextureRotatedScaledCentered(batch, t, x, y, 1, 1, radianRotation);
	}
	public static void drawTextureRotatedScaledCentered(SpriteBatch batch, Texture t, float x, float y, float xScale, float yScale, float radianRotation){
		batch.draw(t, x-(t.getWidth()/2f*xScale), y-(t.getHeight()/2f*yScale), t.getWidth()/2*xScale, t.getHeight()/2*yScale, t.getWidth(), t.getHeight(), xScale, yScale, rad2deg(radianRotation),0,0,t.getWidth(),t.getHeight(),false,true);
	}
	

	//Blending Junk
	public enum BlendType{Normal, Additive, MaxBuggy}
	public static void setBlend(SpriteBatch batch, BlendType type){
		switch(type){
		case Additive:
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			break;
		case Normal:
			
			Gdx.gl20.glBlendEquation(GL20.GL_FUNC_ADD);
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			break;
		case MaxBuggy:
			Gdx.gl20.glBlendEquation(0x8008);
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			break;
		}	
	}

	//Random Junk//
	@SuppressWarnings("unchecked")
	public static <T> void shuffle(ArrayList<T> list){
		ArrayList<T> copiedList= (ArrayList<T>) list.clone();
		list.clear();
		for(T o:copiedList){
			list.add((int)(Math.random()*(list.size()+1)),o);
		}
	}
	public static <T> T getRandom(ArrayList<T> possibles){
		return possibles.get((int)(Math.random()*possibles.size()));
	}
	
	public static <T> void shuffle(T[] list){
		for(int i=0;i<list.length;i++){
			int index=(int) (Math.random()*list.length);
			T temp =list[index];
			list[index]=list[i];
			list[i]=temp;
		}
	}
	

	public static float rad2deg(float rad){
		return (float) (rad*180/Math.PI);
	}
}
