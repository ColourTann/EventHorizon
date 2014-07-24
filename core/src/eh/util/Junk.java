package eh.util;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Junk {
	//Drawing Junk//
	public static void drawTextureScaled(SpriteBatch batch, Texture t, float x, float y, float scaleX, float scaleY){
		batch.draw(t, x, y, 0, 0, t.getWidth(), t.getHeight(), scaleX, scaleY, 0, 0,0,t.getWidth(),t.getHeight(),false,false);
	}
	public static void drawTextureScaledCentered(SpriteBatch batch, Texture t, float x, float y, float xScale, float yScale){
		batch.draw(t, x-t.getWidth()/2*xScale, y-t.getHeight()/2*yScale, 0, 0, t.getWidth(), t.getHeight(), xScale, yScale, 0, 0,0,t.getWidth(),t.getHeight(),false,false);
	}
	public static void drawTextureRotatedCentered(SpriteBatch batch, Texture t, float x, float y, float radianRotation){
		batch.draw(t, x-t.getWidth()/2, y-t.getHeight()/2, t.getWidth()/2, t.getHeight()/2, t.getWidth(), t.getHeight(), 1, 1, rad2deg(radianRotation),0,0,t.getWidth(),t.getHeight(),false,false);
	}
	public static void drawTextureRotatedScaledCentered(SpriteBatch batch, Texture t, float x, float y, float xScale, float yScale, float radianRotation){
		batch.draw(t, x-t.getWidth()/2, y-t.getHeight()/2, t.getWidth()/2, t.getHeight()/2, t.getWidth(), t.getHeight(), xScale, yScale, rad2deg(radianRotation),0,0,t.getWidth(),t.getHeight(),false,false);
	}
	public static void drawTextureRotatedScaled(SpriteBatch batch, Texture t, float x, float y, float xScale, float yScale, float radianRotation){
		batch.draw(t, x, y, 0, 0, t.getWidth(), t.getHeight(), xScale, yScale, rad2deg(radianRotation),0,0,t.getWidth(),t.getHeight(),false,false);
	}

	//Blending Junk
	public enum BlendType{Normal, Additive, Max}
	public static void setBlend(SpriteBatch batch, BlendType type){
		switch(type){
		case Additive:
			
			
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			break;
		case Normal:
			
			Gdx.gl20.glBlendEquation(GL20.GL_FUNC_ADD);
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			break;
		case Max:
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

	public static float rad2deg(float rad){
		return (float) (rad*180/Math.PI);
	}
}
