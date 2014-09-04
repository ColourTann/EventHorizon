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
		fadeIn(2, Interp.SQUARE);
		if(high)y=150;
		else y=250;
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
		Texture t=Gallery.helpPanel.get();
		Draw.draw(batch, t,Main.width/2-t.getWidth()/2,y);
		Font.medium.setColor(Colours.withAlpha(Colours.dark,alpha));
		Font.medium.draw(batch, text, Main.width/2-Font.medium.getBounds(text).width/2, y+14);
		batch.setColor(1,1,1,1);
	}
	
	public void done(){
		fadeOut(2, Interp.SQUARE);
	}

	@Override
	public void update(float delta) {
	}

}
