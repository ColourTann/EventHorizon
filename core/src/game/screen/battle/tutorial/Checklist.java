package game.screen.battle.tutorial;


import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.Mouser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import game.Main;
import game.assets.Gallery;
import game.assets.TextBox;

public class Checklist extends TextBox{
	Task[] tasks;
	float height;
	static float width=350;
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
		position=new Pair(x,y);
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
		renderBox(batch, width, height+offset*2);
//		Draw.drawScaled(batch,Gallery.tutPanelBorder.get(), x,y-6, width/100f, 3);
//		Draw.drawScaled(batch, Gallery.tutPanelMain.get(), x, y, width/100f, height);
//		Draw.drawScaled(batch,Gallery.tutPanelBorder.get(), x,y+height+6, width/100f, -3);
		float heightAdd=0;
		for(Task t:tasks){
			if(t.s.equals(""))continue;
			if(t.isDone())batch.setColor(Colours.withAlpha(Colours.player2[1],alpha));
			else batch.setColor(Colours.withAlpha(Colours.light,alpha));
			t.writer.render(batch, (int)(x+offset), (int)(y+heightAdd+4+offset/2));			
			heightAdd+=t.writer.maxHeight;
			heightAdd+=offset*2;
		}
		height=heightAdd-offset*2;
		
//		if(drawDam){
//			Draw.draw(batch, Gallery.orangeHP[1].get(), 591,148);
//		}
		//599 560

	}
}
