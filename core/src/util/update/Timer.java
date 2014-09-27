package util.update;

import util.maths.Pair;

public class Timer extends Updater{
	public enum Interp{SQUARE, CUBE, INVERSESQUARED, LINEAR, SANTIBOUNCE, SIN}	
	float seconds;
	Interp lerpType;

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

	public void addFinisher(Finisher f){
		this.f=f;
	}

	public void update(float delta){
		ratio+=delta/seconds;
		ratio=Math.min(1, ratio);
		if(ratio>=1){
			dead=true;
			if(f!=null)f.finish();
			ratio=1;
		}
	}

	public Pair getPair(){
		float lerpedRatio=get();
		float x=startPosition.x+(endPosition.x-startPosition.x)*lerpedRatio;
		float y=startPosition.y+(endPosition.y-startPosition.y)*lerpedRatio;
		return new Pair(x,y);
	}

	public float getFloat(){
		if(fromFloat==0&&toFloat==0)return 0;
		
		if(lerpType==null)return ratio;
		return fromFloat+((toFloat-fromFloat)*get());
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
}
