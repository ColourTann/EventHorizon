package util.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Font {
	public static BitmapFont small;
	public static BitmapFont medium;
	public static BitmapFont big;
	public static BitmapFont test;

	public static void init(){

		
		int size=9;
		FileHandle fontFile = Gdx.files.internal("Font/fontspace.ttf");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		FreeTypeFontParameter param= new FreeTypeFontParameter();
		param.flip=true;
		param.size=size;
		small=generator.generateFont(param);
		small.setUseIntegerPositions(true);

		param.magFilter=TextureFilter.Nearest;
		param.minFilter=TextureFilter.Nearest;
		param.size=size*2;
		medium=generator.generateFont(param);
		medium.setUseIntegerPositions(true);


		param.size=size*3;
		big=generator.generateFont(param);
		big.setUseIntegerPositions(true);
		param.size=(int) (size*1.7f);
		test= generator.generateFont(param);
		test.setUseIntegerPositions(true);
		generator.dispose();


	}
	public static void drawFontCentered(SpriteBatch batch, String s, BitmapFont f, float x, float y){
		f.draw(batch, s, (int)(x-f.getBounds(s).width/2), (int)(y-f.getBounds(s).height/2));
	}
}
