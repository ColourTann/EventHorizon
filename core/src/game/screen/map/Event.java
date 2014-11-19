package game.screen.map;

import util.Colours;
import util.TextWriter;
import util.TextWriter.Alignment;
import util.assets.Font;
import util.maths.Pair;
import util.update.SimpleButton;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.AdvancedButton;
import game.assets.TextBox;

public class Event {
	static final Pair center=new Pair(Main.width/2, Main.height/2);
	AdvancedButton[] buttons;
	static final int width=400;
	static final int gap=10;
	int height;
	String text;
	TextWriter writer;
	public Event(String s, AdvancedButton[] buttons){
		text=s;
		this.buttons=buttons;
		Font.medium.setColor(Colours.light);
		writer=new TextWriter(Font.medium, text);
		writer.setWrapWidth(width-gap*2);
		writer.setMapReplacements();
		writer.setupTexture();
		height=(int) (gap*2+writer.maxHeight);
		int maxButtHeight=0;
		for(int i=0;i<buttons.length;i++){
			AdvancedButton butt=buttons[i];
			if(butt.height>maxButtHeight)maxButtHeight=butt.height;
			butt.setPosition(new Pair(
					center.x-width/2+width/(buttons.length+1)*(i+1),
					center.y+height/2-gap/2), true);
		}
		height+=maxButtHeight;
		//if(maxButtHeight>0)height+=gap;
	}
	
	public void render(SpriteBatch batch){
		TextBox.renderBox(batch, center, width, height, Alignment.Center);
		writer.render(batch, center.x-width/2+gap, center.y-height/2+gap);
		for(AdvancedButton butt:buttons)butt.render(batch);
	}

	public void dispose() {
		writer.smallDispose();
		for(AdvancedButton b:buttons){
			b.deactivate();
			b.demousectivate();
		}
	}
}
