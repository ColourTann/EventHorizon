package game.screen.battle.interfaceJunk;


import util.Colours;
import util.Draw;
import util.maths.Pair;
import util.update.Timer.Interp;
import util.TextWriter.Alignment;
import util.assets.Font;
import util.update.Mouser;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.assets.TextBox;

public class HelpPanel extends TextBox{
	String text;
	int y=415;
	int x=Main.width/2;
	public HelpPanel(String s, boolean high) {
		text=s;
		fadeIn(.5f, Interp.SQUARE);
		if(high)y=170;
		else y=272;
		position=new Pair(x,y);
		align=Alignment.Center;
	}
	
	public HelpPanel(String s, int x, int y) {
		text=s;
		this.x=x;
		fadeIn(.5f, Interp.SQUARE);
		this.y=y;
		position=new Pair(x,y);
		align=Alignment.Center;
	}

	public void render(SpriteBatch batch) {
		batch.setColor(1,1,1,alpha);
		int width=(int) Font.medium.getBounds(text).width+16;
		renderBox(batch, width, 35);
	
		Font.medium.setColor(Colours.withAlpha(Colours.light,alpha));
		Font.medium.draw(batch, text, x-Font.medium.getBounds(text).width/2, y-Font.medium.getWrappedBounds(" ", 50).height/2f);
		batch.setColor(1,1,1,1);
	}
	
	public void done(){
		fadeOut(.5f, Interp.SQUARE);
	}

	@Override
	public void update(float delta) {
	}

}
