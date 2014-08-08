package eh.screen.battle.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.Main;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.maths.Collider;

public class Checklist extends Bonkject{
	Task[] tasks;
	float height;
	static float width=300;
	static float x=Main.width/2-width/2;
	float y=173;
	static float fontHeight=Font.medium.getBounds("a").height;
	static float offset=8;
	boolean drawDam;
	public Checklist(Task[] tasks) {
		this.tasks=tasks;
		for(Task t:tasks){
			height+=Font.medium.getWrappedBounds(t.s+"\n\n",width-offset*2).height;
		}
		height-=fontHeight;
		height+=5;
		y-=height/2;
	}
	
	public Checklist(Task[] tasks, int bonusY) {
		this.tasks=tasks;
		for(Task t:tasks){
			height+=Font.medium.getWrappedBounds(t.s+"\n\n",width-offset*2).height;
		}
		height-=fontHeight;
		height+=5;
		y-=height/2;
		y+=bonusY;
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
				if(t.pic!=null){
					
					Tutorial.glows.add(new PicLoc(t.pic, t.location, t));
				}
				return;

			}
		}
	}

	public void render(SpriteBatch batch) {
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
			Draw.drawTexture(batch, Gallery.orangeHP[1].get(), 613,148);
		}
		//599 560

	}
}
