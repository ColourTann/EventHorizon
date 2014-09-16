package game.screen.customise;


import util.Colours;
import util.Draw;
import util.image.Pic;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Mouser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.module.Module;
import game.module.junk.ModuleInfo;

public class Reward extends Mouser{
	Module module;
	static int width=136;
	static int height=100;
	static int offset=15;
	static int y=600;
	private ModuleInfo info;
	public boolean selected;
	public static Color selectedColor=Colours.genCols5[3];
	public Reward (Module m, int index){
		this.module=m;
		
		
		
		this.position=new Pair(Main.width/2-width-offset+width*index+offset*index-width/2, y-height/2);
		mousectivate(new BoxCollider(position.x, position.y, width, height));
	}
	
	public ModuleInfo getInfo(){
		if(info==null)info=new ModuleInfo(module);
		return info;
	}
	
	public void render(SpriteBatch batch){
		batch.setColor(Colours.withAlpha(Colours.white, alpha));
		Pic p=module.getPic(0);
		Draw.drawScaled(batch, Gallery.rewardOutline.get(), position.x, position.y,4,4);
		Draw.drawScaledCentered(batch, p.get(), position.x+width/2, position.y+height/2, 4, 4);
		if(moused){
			batch.setColor(Colours.withAlpha(Colours.light, alpha));
			Draw.drawScaled(batch, Gallery.rewardOutline.getOutline(), position.x, position.y,4,4);
		}
		if(selected){
			batch.setColor(Colours.withAlpha(selectedColor, alpha));
			Draw.drawScaled(batch, Gallery.rewardOutline.getOutline(), position.x, position.y,4,4);
		}
	}


	@Override
	public void mouseDown() {
		Customise.changeStats(getInfo());
		
	}

	

	@Override
	public void mouseUp() {
		Customise.unMouse(module);
	}


	@Override
	public void mouseClicked(boolean left) {
		if(!selected)select();
		else deselect();
	}


	@Override
	public void update(float delta) {
	}

	public void select(){
		selected=true;
		Customise.select(this);
	}
	
	public void deselect() {
		moused=false;
		selected=false;
		Customise.deselect();
	}

	public Module getModule() {
		return module;
	}
}
