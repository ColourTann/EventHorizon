package eh.util.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Font {
	public static BitmapFont small;
	public static BitmapFont medium;
	public static BitmapFont big;
	public static BitmapFont test;

	public static void init(){

		FileHandle fontFile = Gdx.files.internal("Font/fontspace.ttf");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		FreeTypeFontParameter param= new FreeTypeFontParameter();
		param.flip=true;
		param.size=8;
		small=generator.generateFont(param);

		param.magFilter=TextureFilter.Linear;
		param.size=18;
		medium=generator.generateFont(param);

		param.size=26;
		big=generator.generateFont(param);
		
		param.size=14;
		test= generator.generateFont(param);

		generator.dispose();

	}
}
