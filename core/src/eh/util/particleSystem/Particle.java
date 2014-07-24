package eh.util.particleSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.util.maths.Sink;

public abstract class Particle {
	
	public float rotation,dr,life,maxLife,ratio,ticks;
	public Sink position=new Sink();
	public Sink vector=new Sink();
	public boolean dead;
	public Color colour;
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);
	public static float random(float amount){
		return (float) (Math.random()*amount-amount/2)*2;
	}
}
