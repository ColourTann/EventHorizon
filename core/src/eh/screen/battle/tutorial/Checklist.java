package eh.screen.battle.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.Main;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.maths.Collider;

public class Checklist extends Bonkject{
	Task[] tasks;
	 float height;
	static float width=300;
	static float x=Main.width/2-width/2;
	float y=550;
	static float fontHeight=Font.medium.getBounds("a").height;
	static float offset=8;
	boolean drawDam;
	public Checklist(Task[] tasks) {
		super(null);
		demousectivate();
		this.tasks=tasks;
		for(Task t:tasks){
			height+=Font.medium.getWrappedBounds(t.s+"\n\n",width-offset*2).height;
		}
		height-=fontHeight;
		height+=5;
		y+=height/2;
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
	}

	public void render(SpriteBatch batch) {
		batch.setColor(Colours.withAlpha(Colours.white,alpha));
		Junk.drawTextureScaled(batch,Gallery.tutPanelBorder.get(), x,y, 3, 3);
		Junk.drawTextureScaled(batch, Gallery.tutPanelMain.get(), x, y-height, 3, height);
		Junk.drawTextureScaled(batch,Gallery.tutPanelBorder.get(), x,y-height, 3, -3);
		float heightAdd=0;
		for(Task t:tasks){
			if(t.isDone())Font.medium.setColor(Colours.withAlpha(Colours.player2[1],alpha));
			else Font.medium.setColor(Colours.withAlpha(Colours.light,alpha));
			Font.medium.drawWrapped(batch, t.s, x+offset, y+heightAdd, width-offset*2);
			heightAdd-=Font.medium.getWrappedBounds(t.s+"\n\n", width-offset*2).height;
		}
		if(drawDam){
			batch.draw(Gallery.orangeHP[1].get(), 608,559);
		}
		//599 560
		
	}
}
