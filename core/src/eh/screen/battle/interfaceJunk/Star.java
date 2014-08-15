package eh.screen.battle.interfaceJunk;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.Main;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.assets.Gallery;
import eh.util.maths.Pair;

public class Star{
	public static ArrayList<Star> playerStars = new ArrayList<Star>();
	public static ArrayList<Star> enemyStars = new ArrayList<Star>();

	public static Texture pixTex;
	static Color purp=Colours.make(60, 56, 85);
	static Color teal=Colours.make(65, 93, 105);

	static float playerSpeed=100;
	static float enemySpeed=100;

	Pair location;
	float size;
	Color color;

	public Star(boolean player) {
		size=(float) ( Math.random()*Math.random());

		location=new Pair((float)Math.random()*480,(float)Math.random()*339);
		if(player){
			location=location.add(160,78);
			playerStars.add(this);
		}
		else{
			location=location.add(1140,78);
			enemyStars.add(this);
		}

		color=Math.random()>.5?purp:teal;
	}

	public static void shake(boolean player, float amount){
		if(player)playerSpeed-=amount*20;
		if(!player)enemySpeed-=amount*20;
	}
	
	public static void update(float delta){
		playerSpeed+=(100-playerSpeed)*delta;
		enemySpeed+=(100-enemySpeed)*delta;

		for(Star s:playerStars){

			s.location=s.location.add((-s.size-.1f)*delta*playerSpeed,0);
			s.location.x+=delta;
			if(s.location.x<150){
				s.location.x=700;
			}
		}
		for(Star s:enemyStars){
			s.location=s.location.add((s.size+.1f)*delta*enemySpeed,0);
			if(s.location.x>1700){
				s.location.x=1130;
			}
		}
	}

	public void render(SpriteBatch batch){
		batch.setColor(color);
	
		if(size>.5f)Draw.drawTextureScaled(batch, Gallery.square1.get(), location.x, location.y,size*3, size*3);
		else Draw.drawTexture(batch, Gallery.square1.get(), location.x, location.y);

	}

	public static void init() {
		for(int i=0;i<600;i++){
			new Star(true);
			new Star(false);
		}
	}

	public static void renderStars(SpriteBatch batch, boolean player){
		if(player){
			for(Star s:playerStars)s.render(batch);
		}
		else{
			for(Star s:enemyStars)s.render(batch);
		}
	}




}
