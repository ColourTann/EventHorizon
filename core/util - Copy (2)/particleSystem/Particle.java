package util.particleSystem;

import util.maths.Pair;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Particle {
	
	public float rotation,dr,life,maxLife,ratio,ticks;
	public Pair position=new Pair();
	public Pair vector=new Pair();
	public boolean dead;
	public Color colour;
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);
	public static float random(float amount){
		return (float) (Math.random()*amount-amount/2)*2;
	}
}
