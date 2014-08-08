package eh.screen.battle.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Pic;
import eh.screen.battle.Battle;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.Timer;
import eh.util.Timer.Interp;
import eh.util.maths.Pair;

public class PicLoc {
	
	
	Pic pic;
	Pair location;
	
	Task task;
	public PicLoc(Pic pic, Pair location, Task task){
		this.pic=pic;
		this.location=location;
		this.task=task;
		
	}
	public void render(SpriteBatch batch){
		
		batch.setColor(Colours.withAlpha(Colours.light, (float)Math.sin(Battle.ticks*4)/5+.3f));
		for(int x=-2;x<=2;x++){
			for(int y=-2;y<=2;y++){
				Draw.drawTexture(batch, pic.getOutline(), location.x+x, location.y+y);
			}
		}
		
		
	}
}
