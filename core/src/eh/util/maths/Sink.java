package eh.util.maths;

import eh.util.particleSystem.Particle;

public class Sink {
	public float x,y;
	public Sink(float x, float y){
		this.x=x;
		this.y=y;
	}
	public Sink(){}	
	public static Sink getVector(Sink from, Sink to){
		return new Sink(to.x-from.x,to.y-from.y);
	}
	public float getDistance(){
		return (float) Math.sqrt(x*x+y*y);
	}
	public Sink normalise(){
		float dist = getDistance();
		return new Sink(x=x/dist,y=y/dist);
	}
	public void rotate(){
		float tempy=y;
		y=-x;
		x=tempy;
	}
	public Sink multiply(float amount){
		return new Sink(x*amount,y*amount);
	}
	public Sink multiply(Sink s){
		return new Sink(x*s.x,y*s.y);
	}
	public Sink subtract(Sink s){
		return new Sink(x-s.x,y-s.y);
	}
	public Sink add(Sink s){
		return new Sink(x+s.x,y+s.y);
	}
	public String toString(){
		return x+":"+y;
	}
	public static Sink randomUnitVector(){
		float dx=Particle.random(1);
		float dy=(float) Math.sqrt(1-dx*dx);
		if(Math.random()>.5){
			dy=-dy;
		}
		return new Sink(dx,dy);
	}
	public static Sink randomAnyVector(){
		float dx=Particle.random(1);
		float dy=Particle.random(1);
		return new Sink(dx,dy);
	}
	public static Sink randomLocation(float startX, float startY, float width, float height){
		return new Sink(startX+(float)Math.random()*width,startY+(float)Math.random()*height);
	}
	public Sink copy() {
		return new Sink(x,y);
	}
	public float getAngle(Sink to){
		return (float) Math.atan2(to.y-y, to.x-x);
	}
	public Sink floor() {
		return new Sink((int)x,(int)y);
	}
	public Sink squared() {
		return new Sink(x*x,y*y);
	}
}