package eh.screen.battle.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.Main;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.assets.Font;
import eh.util.assets.Gallery;

public class Checklist extends Bonkject{
	Task[] tasks;
	float height;
	static float width=300;
	static float x=Main.width/2-width/2;
	float y=173;
	static float fontHeight=Font.medium.getBounds("a").height;
	static float offset=8;
	boolean drawDam;
	Tutorial tutorial;
	public Checklist(Tutorial tutorial, Task[] tasks) {
		this.tutorial=tutorial;
		this.tasks=tasks;
		for(Task t:tasks){
			if(t.s.equals(""))continue;
			height+=Font.medium.getWrappedBounds(t.s+"\n\n",width-offset*2).height;
		}
		height-=fontHeight;
		height+=5;
		y-=height/2;
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

	@Override
	public void update(float delta) {
		if(tasks[tasks.length-1].isDone()){
			Tutorial.glows.clear();
			return;
		}
		
		for(Task t:tasks){
			if(!t.isDone()){
				if(Tutorial.glows.size()>0){
		
					PicLoc pl= Tutorial.glows.get(0);
					if(pl!=null){
						if(pl.task==t){
							return;
						}
					}
				}

				Tutorial.glows.clear();
				PicLoc pl=t.getPicLoc();
				if(pl!=null)	Tutorial.glows.add(pl);
				
				return;

			}
		}
	}

	public boolean isCurrent(){
		return Tutorial.getCurrentTutorial()==tutorial;
	}
	
	public void render(SpriteBatch batch) {
		if(!isCurrent())return;
		batch.setColor(Colours.withAlpha(Colours.white,alpha));
		Draw.drawTextureScaled(batch,Gallery.tutPanelBorder.get(), x,y-6, 3, 3);
		Draw.drawTextureScaled(batch, Gallery.tutPanelMain.get(), x, y, 3, height);
		Draw.drawTextureScaled(batch,Gallery.tutPanelBorder.get(), x,y+height+6, 3, -3);
		float heightAdd=0;
		for(Task t:tasks){
			if(t.isDone())Font.medium.setColor(Colours.withAlpha(Colours.player2[1],alpha));
			else Font.medium.setColor(Colours.withAlpha(Colours.light,alpha));
			Font.medium.drawWrapped(batch, t.s, x+offset, y+heightAdd+5, width-offset*2);
			heightAdd+=Font.medium.getWrappedBounds(t.s+"\n\n", width-offset*2).height;
		}
		if(drawDam){
			Draw.drawTexture(batch, Gallery.orangeHP[1].get(), 614,148);
		}
		//599 560

	}
}
