package game.module.junk.buff;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.TextBox;
import game.module.component.Component;
import util.Colours;
import util.Draw;
import util.TextWriter;
import util.assets.Font;
import util.maths.Pair;

public class BuffList extends TextBox{
	Component component;
	static int yLocation=256;
	static int width= 287;
	static int xOffset=8;
	static int yGap=7;
	static int yWiggle=20;
	static int buffPicWidth=20;
	static int buffPicHeight=10;
	static BitmapFont font = Font.medium;
	public BuffList(Component comp){
		component=comp;
		resetJunk();
		position=new Pair(comp.ship.player?130:Main.width-130-width, yLocation);
		alpha=0;
		
	}

	public void resetJunk(){


	}

	@Override
	public void update(float delta) {
	}

	public void render(SpriteBatch batch){
		batch.setColor(1,1,1,alpha);
		font.setColor(Colours.withAlpha(Colours.light, alpha));
		if(component.buffs.size()==0)return;
		if(alpha==0)return;
		
		
		float height=0;
		
		for(Buff b:component.buffs){
			if(b.tw==null){
				
				b.tw= new TextWriter(font, ": "+b.getWords());
				b.tw.addObstacleTopLeft(buffPicWidth, buffPicHeight);
				b.tw.setWrapWidth(width-xOffset*2);
			}
			height+=b.tw.maxHeight+yGap;
		}
		height-=yGap;
		height+=yWiggle;
		
		int y=0;
		for(Buff b:component.buffs){
			Draw.draw(batch, b.getPic().get(), position.x+xOffset, position.y+y+yWiggle/2-1);
			b.tw.drawText(batch, position.x+xOffset, position.y+y+yWiggle/2);
			y+=b.tw.maxHeight+yGap;
		}
	}
}
