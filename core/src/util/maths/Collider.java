package util.maths;

import com.badlogic.gdx.graphics.Color;


public abstract class Collider {
	public Pair position;
	public abstract boolean collidePoint(Pair s);
	public abstract boolean collideWith(Collider c);
	public abstract void debugDraw();
	public Color override;
	public boolean circleBoxCollide(CircleCollider c, BoxCollider b){
		int misses;
		float cx=c.position.x;
		float cy=c.position.y;
		float cr=c.r;
		float left=b.position.x;
		float right=left+b.w;
		float bot=b.position.y;
		float top=bot+b.h;

		//out of bounds
		if(cx-cr>=right||
			cx+cr<=left||
			cy-cr>=top||
			cy+cr<=bot){

				return false;
		}

		//edge case
		misses = c.position.x>right?1:0;
		misses+=c.position.x<left?1:0;
		misses+=c.position.y>bot?1:0;
		misses+=c.position.y<top?1:0;

		if(misses==2){

			float xDiff=0;
			float yDiff=0;
			float dCom=cr*cr;

			//checking top left
			xDiff=left-cx;
			yDiff=top-cy;
			if(xDiff*xDiff+yDiff*yDiff<=dCom){
				return true;
			}
			//checking top right
			xDiff=cx-right;
			yDiff=top-cy;
			if(xDiff*xDiff+yDiff*yDiff<=dCom){
				return true;
			}
			//checking bot left
			xDiff=left-cx;
			yDiff=cy-bot;
			if(xDiff*xDiff+yDiff*yDiff<=dCom){
				return true;
			}
			//checking bot right
			xDiff=cx-right;
			yDiff=cy-bot;
			if(xDiff*xDiff+yDiff*yDiff<=dCom){
				return true;
			}
			return false;
		}
		
		return true;
	}
}
