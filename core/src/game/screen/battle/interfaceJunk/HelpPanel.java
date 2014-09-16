package game.screen.battle.interfaceJunk;


import util.Colours;
import util.Draw;
import util.update.Timer.Interp;
import util.assets.Font;
import util.update.Mouser;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;

public class HelpPanel extends Mouser{
	String text;
	int y=415;
	
	public HelpPanel(String s, boolean high) {
		text=s;
		fadeIn(.5f, Interp.SQUARE);
		if(high)y=150;
		else y=252;
	}
	
	public HelpPanel(String s, int y) {
		text=s;
		fadeIn(.5f, Interp.SQUARE);
		this.y=y;
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

	public void render(SpriteBatch batch) {
		batch.setColor(1,1,1,alpha);
		int width=(int) Font.medium.getBounds(text).width+10;
		Draw.drawScaled(batch, Gallery.helpPanelMid.get(), Main.width/2-width/2, y, width, 1);
		Draw.draw(batch, Gallery.helpPanelEdge.get(), Main.width/2-width/2-Gallery.helpPanelEdge.getWidth(), y);
		Draw.drawRotatedScaledFlipped(batch, Gallery.helpPanelEdge.get(), Main.width/2+width/2, y, 1,1,0, true, false);
	
		Font.medium.setColor(Colours.withAlpha(Colours.dark,alpha));
		Font.medium.draw(batch, text, Main.width/2-Font.medium.getBounds(text).width/2, y+13);
		batch.setColor(1,1,1,1);
	}
	
	public void done(){
		fadeOut(.5f, Interp.SQUARE);
	}

	@Override
	public void update(float delta) {
	}

}
