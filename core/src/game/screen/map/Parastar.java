package game.screen.map;

import game.Main;
import game.assets.Gallery;
import game.grid.hex.Hex;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.maths.Pair;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.org.glassfish.external.statistics.impl.StatsImpl;

public class Parastar {
	private static ArrayList<Parastar> stars=new ArrayList<Parastar>();
	private static final int numStars=600;
	private static final int width=Main.width*2;
	private static final int height=Main.height*2;
	private static final Color[] cols = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.WHITE, Color.BLUE};
	float starSize=2;
	float starDistance=2;
	Pair location;
	Color col;
	float sizeToDraw;
	public Parastar(Pair location){
		this.location=location;
		starSize+=Math.random()*5f;
		starDistance+=Math.random()*3f;
		starSize/=2;
//		if(starSize>6){
//			col=Color.RED;
//		}
//		else
//		col=Colours.shiftedTowards(cols[(int)starSize-2],cols[(int)starSize-1], starSize%1);
//		
		col=Colours.white;
	
		
		
		sizeToDraw=starSize/starDistance/10f;
		sizeToDraw=Math.max(1/25f, sizeToDraw);
		//small is red//
		//big is blue//
		//white
		//yellow
		//orange	
		//red
	}

	public static void init(){
		for(int i=0;i<numStars;i++){
			stars.add(new Parastar(new Pair(Math.random(), Math.random()).multiply(new Pair(width,height))));
		}
	}

	public static void render(SpriteBatch batch, Pair camPosition, float scale){
		if(stars.size()==0)init();
		for(Parastar p:stars){
			batch.setColor(p.col);
			Draw.drawCenteredScaled(batch, Gallery.circle32.get(), ((p.location.x-camPosition.x/p.starDistance+1000000)%width), ((p.location.y-camPosition.y/p.starDistance+1000000)%height), p.sizeToDraw, p.sizeToDraw);
		}
		
	}
}
