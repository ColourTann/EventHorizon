package util.particleSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.maths.Pair;

public abstract class Particle {
	
	public float rotation,dr,life,maxLife,ratio,ticks;
	public float alpha=1;
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
