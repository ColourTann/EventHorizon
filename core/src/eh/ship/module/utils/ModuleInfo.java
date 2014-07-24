package eh.ship.module.utils;

import java.util.ArrayList;

import javax.swing.GroupLayout.Alignment;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import eh.Main;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.card.CardGraphic;
import eh.ship.module.Module;
import eh.ship.module.Module.ModuleType;
import eh.ship.module.computer.Computer;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.maths.Sink;

public class ModuleInfo extends Bonkject{
	//279373
	static float width=279;
	float height=373;
	public Module mod;
	public ArrayList<CardGraphic> graphics = new ArrayList<CardGraphic>();
	public ModuleInfo(Module m) {
		super(null);
		mod=m;
		float baseY=Main.height-(Main.height/5*m.index)-height;
		y=Main.height-height-4;
	
		
		x=mod.ship.player?132:Main.width-132-width;



	


		if(mod.type==ModuleType.WEAPON||mod.type==ModuleType.SHIELD){
			for(int i=0;i<=3;i++){
				CardGraphic cg=mod.getCard(i+1).getHalfGraphic(true);
				cg.setPosition(new Sink(x+(i/2)*139, y+(i%2)*124));
				graphics.add(cg);
			}
			CardGraphic cg=mod.getCard(0).getHalfGraphic(false);
			cg.setPosition(new Sink(x+139,y+124));
			graphics.add(cg);
		}
		else{
			height-=124;
			y+=124;

			CardGraphic cg=mod.getCard(1).getHalfGraphic(true);
			cg.setPosition(new Sink(x+139, y));
			graphics.add(cg);

			CardGraphic cg1=mod.getCard(0).getHalfGraphic(false);
			cg1.setPosition(new Sink(x+139,y));
			graphics.add(cg1);
		}


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

	public void fadeAll(){
		for(CardGraphic cg: graphics){
			cg.fadeOut(CardGraphic.fadeType, CardGraphic.fadeSpeed);
		}
		fadeOut(CardGraphic.fadeType, CardGraphic.fadeSpeed);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(1,1,1,alpha);

		Junk.drawTextureScaled(batch, Gallery.darkDot.get(), x, y, width, height);

		Font.medium.setColor(Colours.withAlpha(Colours.light,alpha));
		String s=mod.moduleName;
		Font.medium.draw(batch, s, x+width/4-Font.medium.getBounds(s).width/2, y+height-12);
		s="Cards:";
		Font.medium.draw(batch, s, x+width/4-Font.medium.getBounds(s).width/2, y+height-58);
		s=""+mod.numCards+"/"+mod.ship.getTotalDeckSize();
		Font.medium.draw(batch, s, x+width/4-Font.medium.getBounds(s).width/2, y+height-83);
		
		if(mod.type==ModuleType.GENERATOR){
			s="Energy";
			Font.medium.draw(batch, s, x+width/4-Font.medium.getBounds(s).width/2, y+height-150);
			s="Income:";
			Font.medium.draw(batch, s, x+width/4-Font.medium.getBounds(s).width/2, y+height-175);
			s=""+mod.ship.getIncome();
			Font.medium.draw(batch, s, x+width/4-Font.medium.getBounds(s).width/2, y+height-200);
		}
		if(mod.type==ModuleType.COMPUTER){
			s="Hand";
			Font.medium.draw(batch, s, x+width/4-Font.medium.getBounds(s).width/2, y+height-150);
			s="Size:";
			Font.medium.draw(batch, s, x+width/4-Font.medium.getBounds(s).width/2, y+height-175);
			s=""+((Computer)mod).maxCards;
			Font.medium.draw(batch, s, x+width/4-Font.medium.getBounds(s).width/2, y+height-200);
		}
		//Font.medium.drawWrapped(batch, s, 0, y+height, 500, HAlignment.CENTER);
		//Font.medium.drawWrapped(batch, s, width/4, 20, 500, HAlignment.CENTER);
	
		
		
		batch.setColor(1,1,1,1);
	}

}
