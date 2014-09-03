package util.update;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Draw;
import util.image.Pic;
import util.maths.Pair;
import util.update.Body.BodyAnim.Change;
import util.update.Timer.Interp;

public class Body extends Mouser{
	ArrayList<BodyAnim> animations= new ArrayList<BodyAnim>();
	ArrayList<Change> anyStateChanges= new ArrayList<Change>();
	Timer moveTimer= new Timer();
	public boolean flipped=false;
	public BodyAnim currentAnimation;
	
	public Body(Pair position){
		this.position=position;
		moveTimer=new Timer(position, position,0,Interp.LINEAR);
	}
	
	public void addChange(Change c){
		anyStateChanges.add(c);
	}
	
	public void addAnimation(BodyAnim a){
		animations.add(a);
	}
	
	public void update(float delta){
		for(Change c:currentAnimation.changes){
			if(c.trigger.trigger()){
				setAnimation(c.animation);
				break;
			}
		}
		for(Change c:anyStateChanges){
			if(c.trigger.trigger()){
				setAnimation(c.animation);
				break;
			}
		}
		currentAnimation.update(delta);
		position=moveTimer.getPair();
	}
	public void setAnimation(BodyAnim a){
		currentAnimation=a;
		a.ticks=0;
	}
	public void render(SpriteBatch batch){
		currentAnimation.render(batch, position, 0);
	}
	@Override
	public void mouseDown() {
	}
	@Override
	public void mouseUp() {
	}
	@Override
	public void mouseClicked(boolean left) {
	}

	public void startAnimation(BodyAnim anim) {
		this.currentAnimation=anim;
	}
	public void moveTo(Pair location){
		moveTimer=new Timer(moveTimer.getPair(), location, 1, Interp.LINEAR);
	}
	
	static class BodyAnim{
		public Texture[] textures;
		float animationSpeed;
		public float ticks;
		public float[] timing;
		public BodyAnim(Texture[] textures, float animationSpeed){
			this.textures=textures;
			this.animationSpeed=animationSpeed;
			timing=new float[textures.length];
			for(int i=0;i<timing.length;i++){
				timing[i]=1;
			}
		}
		
		public BodyAnim(Pic[] pics, float animationSpeed){
			Texture[] t=new Texture[pics.length];
			for(int i=0;i<pics.length;i++){
				t[i]=pics[i].get();
			}
			textures=t;
			this.animationSpeed=animationSpeed;
			timing=new float[textures.length];
			for(int i=0;i<timing.length;i++){
				timing[i]=1;
			}
		}
		
		public void setTimings(float[] timings){
			this.timing=timings;
		}

		
		public void addChange(Change c){
			changes.add(c);
		}
		
		public void update(float delta){
			ticks+=delta*animationSpeed*timing[(int) (ticks%textures.length)];
		}
		public void render(SpriteBatch batch, Pair location, float rotation){
			if(ticks>textures.length)return;
			Draw.drawRotatedCentered(batch, textures[(int) (ticks%textures.length)], location.x, location.y, rotation);
		}
		
		public ArrayList<Change> changes=new ArrayList<Change>();
		public static class Change{
			public BodyAnim animation;
			public Trigger trigger;	
			public Change(BodyAnim animation, Trigger trigger){
				this.animation=animation;
				this.trigger=trigger;
				
			}
		}
		public interface Trigger{
			public boolean trigger();
		}
		
	}
}
