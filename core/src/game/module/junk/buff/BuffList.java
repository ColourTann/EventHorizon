package game.module.junk.buff;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.module.component.Component;
import util.TextWriter;
import util.assets.Font;
import util.update.Updater;

public class BuffList extends Updater{
	Component component;
	ArrayList<Buff> buffs;
	static int width= 280;
	static int xOffset=8;
	static int yGap=5;
	static BitmapFont font = Font.small;
	static TextWriter textWriter= new TextWriter(font);
	int height;
	public BuffList(Component comp){
		component=comp;
		buffs.addAll(comp.buffs);
		for(Buff b:buffs){
			height+=font.getWrappedBounds(b.toString(), width-xOffset*2).height;
			height+=yGap;
		}
		alpha=0;
		
	}
	
	@Override
	public void update(float delta) {
	}
	
	public void render(SpriteBatch batch){
		if(buffs.size()==0)return;
		int currentY=0;
		for(Buff b:buffs){
			
		}
		//batch
	}

}
