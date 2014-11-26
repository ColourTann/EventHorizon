package game.screen.battle.interfaceJunk;

import game.assets.Gallery;
import game.screen.battle.Battle;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.maths.Pair;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Star{
	public static ArrayList<Star> playerStars = new ArrayList<Star>();
	public static ArrayList<Star> enemyStars = new ArrayList<Star>();

	public static Texture pixTex;
	static Color purp=Colours.make(60, 56, 85);
	static Color teal=Colours.make(65, 93, 105);

	public static float playerSpeed=-100;
	public static float enemySpeed=100;

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



	public static void update(float delta){
		if(Battle.getPlayer().dead)playerSpeed-=playerSpeed*delta;
		else playerSpeed+=(-100-playerSpeed)*delta;
		
		if(Battle.getEnemy().dead)enemySpeed-=enemySpeed*delta;
		else enemySpeed+=(100-enemySpeed)*delta;



		for(Star s:playerStars){
			s.location.x+=(s.size)*delta*playerSpeed;
//			
			if(s.location.x<150){
				s.location.x=700;
			}
		}
		for(Star s:enemyStars){
			s.location.x+=(s.size+.1f)*delta*enemySpeed;
			
			if(s.location.x>1700){
				s.location.x=1130;
			}
		}
	}

	public void render(SpriteBatch batch){
		batch.setColor(color);

		if(size>.5f)Draw.drawScaled(batch, Gallery.whiteSquare.get(), (int)location.x, (int)location.y,(int)(size*3), (int)(size*3));
		else Draw.draw(batch, Gallery.whiteSquare.get(), (int)location.x, (int)location.y);

	}

	public static void init() {
		playerStars.clear();
		enemyStars.clear();
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
