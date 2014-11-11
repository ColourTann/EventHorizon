package game.screen.map.panels;

import game.Main;
import game.assets.Gallery;

import java.util.ArrayList;

import util.Draw;
import util.maths.Pair;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Parastar {
	private static ArrayList<Parastar> stars=new ArrayList<Parastar>();
	private static final int numStars=10;
	private static final int width=Main.width*2;
	private static final int height=Main.height;
	Pair location;

	public Parastar(Pair location){
		this.location=location;
	}

	public static void init(){
		for(int i=0;i<numStars;i++){
			stars.add(new Parastar(Pair.randomAnyVector().multiply(new Pair(Math.random()*width, Math.random()*height))));
		}
	}

	public static void render(SpriteBatch batch, Pair camPosition, float scale){
		if(stars.size()==0)init();
		for(Parastar p:stars){
			Draw.drawCenteredScaled(batch, Gallery.circle32.get(), (p.location.x-Math.abs(camPosition.x%Main.width)), Math.abs((p.location.y-camPosition.y/10f))%height, scale, scale);
			System.out.println(p.location.x-Math.abs(camPosition.x%Main.width));
		}

	}
}
