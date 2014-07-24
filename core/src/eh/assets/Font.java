package eh.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Font {
	public static BitmapFont small;
	public static BitmapFont medium;
	public static BitmapFont big;
	public static void init(){
		big=new BitmapFont(Gdx.files.internal("Font/27.fnt"),Gdx.files.internal("Font/27_0.png"), false);
		small=new BitmapFont(Gdx.files.internal("Font/9.fnt"),Gdx.files.internal("Font/9_0.png"), false);
		medium=new BitmapFont(Gdx.files.internal("Font/18.fnt"),Gdx.files.internal("Font/18_0.png"), false);
	}
}
