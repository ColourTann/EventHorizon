package eh.util;

import java.util.ArrayList;

import eh.util.maths.Pair;

public class Timer {
	public enum Interp{SQUARE, CUBE, INVERSESQUARED, LINEAR, SANTIBOUNCE, SIN}

	public static ArrayList<Timer> lerpables = new ArrayList<Timer>();

	float speed;
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
	}
	
	public Timer(float from, float to, float speed, Interp type){
		fromFloat=from;
		toFloat=to;
		this.speed=speed;
		lerpType=type;
		lerpables.add(this);
	}

	public Timer(Pair from, Pair to, float speed, Interp type){
		startPosition=from;
		endPosition=to;
		this.speed=speed;
		lerpType=type;
		fromFloat=0;
		toFloat=1;
		ratio=0;
		lerpables.add(this);
	}

	

	public void reTime(float from, float to, float speed, Interp type){
		fromFloat=from;
		toFloat=to;
		this.speed=speed;
		lerpType=type;
		startPosition=null;
		endPosition=null;
		lerpables.remove(this);
		lerpables.add(this);
	}

	public void reTime(Pair from, Pair to, float speed, Interp type){
		startPosition=from;
		endPosition=to;
		this.speed=speed;
		lerpType=type;
		fromFloat=0;
		toFloat=1;
		ratio=0;
		lerpables.remove(this);
		lerpables.add(this);
	}

	public void addFinisher(Finisher f){
		this.f=f;
	}

	public static void updateAll(float delta){

		for(Timer t:lerpables){
			t.update(delta);
		}

		//culling//
		for(int i=0;i<lerpables.size();i++){
			Timer b = lerpables.get(i);
			if(b.ratio>=1){
				lerpables.remove(i);
				if(b.f!=null)b.f.finish();
				i--;
			}
		}
	}

	private void update(float delta){
		ratio+=delta*speed;
		ratio=Math.min(1, ratio);
	}


	public Pair getPair(){
		float lerpedRatio=get(ratio,lerpType);
		float x=startPosition.x+(endPosition.x-startPosition.x)*lerpedRatio;
		float y=startPosition.y+(endPosition.y-startPosition.y)*lerpedRatio;
		return new Pair(x,y);
	}

	public float getFloat(){
		if(lerpType==null)return 0;
		return fromFloat+((toFloat-fromFloat)*get(ratio,lerpType));
	}

	public static float get(float ratio, Interp type){
		if(ratio>1)ratio=1;
		switch (type){
		case SQUARE: return 1-(1-ratio)*(1-ratio);
		case CUBE: return 1-(1-ratio)*(1-ratio)*(1-ratio);
		case INVERSESQUARED: return ratio*ratio;
		case LINEAR: return ratio;
		case SANTIBOUNCE: return (float) Math.sin(ratio*Math.PI)/((1+ratio)*3)+(1-(ratio-1)*(ratio-1));
		case SIN: return (float)Math.sin(ratio*Math.PI);
		default: return 0;
		}
	}
}
