package eh.util;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.Main;
import eh.util.maths.Collider;
import eh.util.maths.Sink;
import eh.util.particleSystem.ParticleSystem;

public abstract class Bonkject {
	public boolean dead;
	static boolean debug=false;
	public static ArrayList<Bonkject> mouseables = new ArrayList<Bonkject>();
	public static ArrayList<Bonkject> updateAndRendereables = new ArrayList<Bonkject>();
	public static ArrayList<Timerr> timers = new ArrayList<Timerr>();
	//Updating and rendering//
	
	public static void updateBonkjs(float delta) {
		updateMoused();
		updateLerps(delta);
		updateActives(delta);
		updateTimers(delta);
		if(debug){
			System.out.println("Mouseable count: "+mouseables.size());
			System.out.println("Render count: "+updateAndRendereables.size());
			System.out.println("Lerp count: "+lerpables.size());
		}
	}

	public static void renderBonkjs(SpriteBatch batch){
		for(Bonkject b:updateAndRendereables){
			b.render(batch);
		}
	}
	
	public static void updateTimers(float delta){
		for(int i=0;i<timers.size();i++){
		Timerr tim = timers.get(i);
			tim.timer-=delta;
			if(tim.timer<=0){
				if(tim.f!=null)tim.f.finish();
				timers.remove(tim);
				i--;
			}
		}
	}
	
	
	public static void updateMoused(){
		updateMousePosition();
		boolean found=false;
		for(int i=0;i<mouseables.size();i++){
			if(found){
				mouseables.get(i).deMouse();
				continue;
			}
			found=mouseables.get(i).checkMoused(currentMoused);
		}
	}	

	private static void updateMousePosition(){
		currentMoused=new Sink(Gdx.input.getX()/(float)Gdx.graphics.getWidth()*(float)Main.width,Main.height-(Gdx.input.getY()/(float)Gdx.graphics.getHeight()*(float)Main.height));
	}

	public static void updateClicked(boolean left){
		updateMousePosition();
		for(int i=0;i<mouseables.size();i++){
			if(mouseables.get(i).checkClicked(currentMoused, left))return;
		}
	}

	// MOUSING SHIT //

	public Collider collider;
	public boolean moused=false;
	public static Sink currentMoused;
	

	public Bonkject(Collider col) {
		this.collider=col;
		activate();
	}

	

	public void mousectivate(){
		mouseables.add(this);
	}

	public void activate(){
		updateAndRendereables.add(this);
	}

	public void deactivate(){
		if(updateAndRendereables.contains(this)){
			updateAndRendereables.remove(this);
		}
		else System.out.println(this+" being deactivated and not in update list.");
		lerpables.remove(this);
		mouseables.remove(this);
	}

	public void demousectivate(){
		mouseables.remove(this);
		moused=false;
	}

	public void derendervate(){
		if(updateAndRendereables.contains(this)){
			updateAndRendereables.remove(this);
		}
		else System.out.println(this+" being deactivated and not in update list.");
	}

	private boolean checkMoused(Sink s){
		if(collider.collidePoint(s)){
			if(!moused){
				moused=true;
				mouseDown();
			}
			return true;
		}
		if(moused){
			mouseUp();
			moused=false;
		}
		return false;

	}
	private void deMouse(){
		if(moused){
			mouseUp();
			moused=false;
		}
	}

	private boolean checkClicked(Sink s, boolean left){
		if(collider.collidePoint(s)){
			mouseClicked(left);
			return true;
		}
		return false;
	}
	public abstract void mouseDown();
	public abstract void mouseUp();
	public abstract void mouseClicked(boolean left);
	public void debugRender(SpriteBatch batch){
		
		if(collider==null)return;
		batch.end();
		collider.debugDraw();
		batch.begin();
	}




	// LERPING SHIT //


	public static ArrayList<Bonkject> lerpables = new ArrayList<Bonkject>();
	public enum Interp{SQUARE, CUBE, INVERSESQUARED, LINEAR, SANTIBOUNCE}
	private Interp type;
	public float x;
	public float y;
	float ratio=0;
	public float lerpedRatio=0;
	float speed;
	Sink startPosition;
	Sink targetPosition; 
	Finisher f;
	public float alpha=1;
	public boolean fadingOut;
	private boolean fadingIn;
	public void lerptivate(Interp type, float start, float speed){
		ratio=start;
		this.speed=speed;
		this.type=type;
		startPosition=null;
		targetPosition=null;
		if(!lerpables.contains(this))lerpables.add(this);
	}
	public void lerptivate(Sink target, Interp type, float speed){
		ratio=0;
		this.speed=speed;
		this.targetPosition=target;
		this.type=type;
		startPosition=new Sink(x,y);
		if(!lerpables.contains(this))lerpables.add(this);
	}
	public void lerptivate(Sink target, Interp type, float speed, Finisher finisher){
		lerptivate(target, type, speed);
		f=finisher;
	}
	public void fadeOut(Interp type, float speed){
		fadingOut=true;
		fadingIn=false;
		demousectivate();
		lerptivate(type,0,speed);
	}
	public void fadeIn(Interp type, float speed){
		alpha=0;
		fadingIn=true;
		fadingOut=false;
		demousectivate();
		lerptivate(type,0,speed);
	}
	private static void updateLerps(float delta){
		for(int i=0;i<lerpables.size();i++){
			Bonkject b = lerpables.get(i);
			if(b.ratio>=1){
				if(b.fadingOut){
					b.dead=true;
				}
				if(b.f!=null){
					b.f.finish();
				}
				lerpables.remove(i);
				i--;
			}
		}
		for(Bonkject b:lerpables){
			b.lerp(delta);
		}
	}
	private void lerp(float delta){
		ratio+=delta*speed;
		ratio=Math.min(1, ratio);
		lerpedRatio=get(ratio,type);
		if(startPosition!=null){
			x=startPosition.x+(targetPosition.x-startPosition.x)*lerpedRatio;
			y=startPosition.y+(targetPosition.y-startPosition.y)*lerpedRatio;
		}
		if(fadingOut)alpha=Math.max(0, 1-lerpedRatio);
		if(fadingIn)alpha=Math.min(1, lerpedRatio);
	}

	public static float get(float ratio, Interp type){
		if(ratio>1)ratio=1;
		switch (type){
		case SQUARE: return 1-(1-ratio)*(1-ratio);
		case CUBE: return 1-(1-ratio)*(1-ratio)*(1-ratio);
		case INVERSESQUARED: return ratio*ratio;
		case LINEAR: return ratio;
		case SANTIBOUNCE: return (float) Math.sin(ratio*Math.PI)/((1+ratio)*3)+(1-(ratio-1)*(ratio-1));
		default: return 0;
		}
	}




	// GENERAL SHIT //

	public static void updateActives(float delta){
		for(Bonkject b:updateAndRendereables){
			b.update(delta);
		}
		for(int i=0;i<updateAndRendereables.size();i++){
			Bonkject b = updateAndRendereables.get(i);
			if(b.dead){
				updateAndRendereables.remove(b);
				i--;
			}
		}
		
	}
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);
	public void moveToTop() {
		if(Bonkject.mouseables.remove(this))Bonkject.mouseables.add(0,this);
		if(Bonkject.updateAndRendereables.remove(this))Bonkject.updateAndRendereables.add(this);
	}
	public interface Finisher {
		public void finish();
	}
	
	//Timers//

	
	
	public static Timerr time(float time, Finisher finisher){
		Timerr t=new Timerr(time, finisher);
		timers.add(t);
		return t;
	}

	public static class Timerr{
		public float timer;
		Finisher f;
		Timerr(float timer, Finisher f){
			this.timer=timer;
			this.f=f;
		}
	}
	
	

	public static void clearAll() {
		mouseables.clear();
		updateAndRendereables.clear();
		timers.clear();
		ParticleSystem.clearAll();
	}

}
