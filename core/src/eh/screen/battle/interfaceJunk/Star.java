package eh.screen.battle.interfaceJunk;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.Main;
import eh.assets.Gallery;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.maths.Pair;

public class Star{
	public static ArrayList<Star> stars = new ArrayList<Star>();
	public Pair location;
	float size;
	public static Texture pixTex;
	static Color purp=Colours.make(60, 56, 85);
	static Color teal=Colours.make(65, 93, 105);
	public Star() {
		size=(float) Math.random();
		location=new Pair((float)Math.random()*Main.width,(float)Math.random()*Main.height);
		stars.add(this);
	}

	public void update(float delta){

	}

	public void render(SpriteBatch batch){
		if(size>.7f)Draw.drawTexture(batch, Gallery.square2.get(), location.x, location.y);
		else Draw.drawTexture(batch, Gallery.square1.get(), location.x, location.y);

	}

	public static void init() {
		for(int i=0;i<100;i++){
			stars.add(new Star());		
		}
		Pixmap pixmap = new Pixmap(960,350,Format.RGB888);
		int width=pixmap.getWidth();
		int height=pixmap.getHeight();
		pixmap.setColor(Color.rgba8888(Colours.dark));
		pixmap.fillRectangle(0,0,width,height);
		for(int i=0;i<2000;i++){

			pixmap.setColor(Color.rgba8888(Math.random()>.5?purp:teal));
			

			int size=Math.random()<.2f?2:1;
			pixmap.drawRectangle((int)(Math.random()*width), (int)(Math.random()*height), size, size);

		}


		pixTex = new Texture( pixmap );
		pixmap.dispose();
	}




}
