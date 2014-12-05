package util.update;

import java.util.ArrayList;

import util.maths.Pair;
import util.update.Updater.Layer;

public class Timer extends Updater{
	public enum Interp{SQUARE, CUBE, INVERSESQUARED, LINEAR, SANTIBOUNCE, SIN}	
	float seconds;
	Interp lerpType=Interp.LINEAR;

	Pair startPosition;
	Pair endPosition;
	Pair currentPosition;
	float fromFloat;
	float toFloat;
	public float ratio;

	public interface Finisher {
		public void finish();
	}
	Finisher f;

	
	public Timer() {
		ratio=0;
		fromFloat=0;
		toFloat=0;
		dead=true;
	}
	
	public Timer(float from, float to, float seconds, Interp type){
		fromFloat=from;
		toFloat=to;
		this.seconds=seconds;
		lerpType=type;
		
	}

	public Timer(Pair from, Pair to, float seconds, Interp type){
		startPosition=from;
		endPosition=to;
		this.seconds=seconds;
		lerpType=type;
		fromFloat=0;
		toFloat=1;
		ratio=0;
	}
	
	public static Timer event(float seconds, Finisher finisher){
		Timer t= new Timer(0,1,seconds, Interp.LINEAR);
		t.addFinisher(finisher);
		return t;
	}

	public void addFinisher(Finisher f){
		this.f=f;
	}

	public void update(float delta){
		if(seconds==-1)return;
		ratio+=delta/seconds;
		ratio=Math.min(1, ratio);
		if(ratio>=1){
			dead=true;
			if(f!=null)f.finish();
			ratio=1;
		}
	}

	public Pair getPair(){
		if(startPosition==null)return new Pair(-1,-1);
		float lerpedRatio=get();
		float x=startPosition.x+(endPosition.x-startPosition.x)*lerpedRatio;
		float y=startPosition.y+(endPosition.y-startPosition.y)*lerpedRatio;
		return new Pair(x,y);
	}

	public float getFloat(){
		if(lerpType==null)return ratio;
		return fromFloat+((toFloat-fromFloat)*get());
	}

	public float getTarget(){
		if(lerpType==null)return 1;
		return toFloat;
	}
	
	private float get(){
		if(ratio>1)ratio=1;
		switch (lerpType){
		case SQUARE: return 1-(1-ratio)*(1-ratio);
		case CUBE: return 1-(1-ratio)*(1-ratio)*(1-ratio);
		case INVERSESQUARED: return ratio*ratio;
		case LINEAR: return ratio;
		case SANTIBOUNCE: return (float) Math.sin(ratio*Math.PI)/((1+ratio)*3)+(1-(ratio-1)*(ratio-1));
		case SIN: return (float)Math.sin(ratio*Math.PI);
		default: return 0;
		}
	}

	public String toString(){
		return "Timer, from "+fromFloat+" to "+toFloat+". ratio: "+ratio;
	}

	public void removeFinisher() {
		this.f=null;
	}

	public void stop() {
		seconds=-1;
	}

	
}
