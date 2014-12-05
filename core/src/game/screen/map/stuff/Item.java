package game.screen.map.stuff;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Mouser;
import util.update.Timer;
import util.update.Timer.Interp;
import game.assets.Gallery;
import game.assets.TextBox;
import game.card.Card;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.junk.ModuleInfo;
import game.screen.customise.Reward;
import game.screen.map.Map;

public class Item extends Mouser{
	Module mod;
	Card card;
	public static float height=Gallery.rewardOutline.getHeight()*4-4;
	public static float width=Gallery.rewardOutline.getWidth()*4;
	public Timer yTimer= new Timer();
	private boolean selected;
	static final Pair size=new Pair(Gallery.rewardOutline.getWidth()*4, Gallery.rewardOutline.getHeight()*4);
	public Item(Module mod){
		this.mod=mod;
		mousectivate(new BoxCollider(0, 0, width, height));
		demousectivate();
		deactivate();
	}
	public Item(Card card){
		this.card=card;
	}
	public void setY(float y){
		yTimer=new Timer(y,y,0,Interp.LINEAR);
	}
	public void moveY(float y){
		yTimer=new Timer(yTimer.getFloat(),y,Scroller.viewSpeed,Interp.SQUARE);
	}

	@Override
	public void mouseDown() {
		System.out.println("mousing"+ mod);
		Map.mouseStats(mod.getInfo());
		Map.mouseItem(this);
	}
	@Override
	public void mouseUp() {
		mod.getInfo().fadeAll();
		Map.unMouse();
	}
	@Override
	public void mouseClicked(boolean left) {
		Map.selectItem(this);
	}
	@Override
	public void update(float delta) {


	}
	public void updateCollider(Pair scrollPo, float view) {
		collider.position.x=scrollPo.x;
		collider.position.y=scrollPo.y+Scroller.buttonHeight+view+yTimer.getFloat();

	}
	public void render(SpriteBatch batch, float x, float y) {
		batch.setColor(1,1,1,1);
		Draw.drawScaled(batch, Gallery.rewardOutline.get(), x, y+yTimer.getFloat(), 4, 4);
		if(mod!=null){
			Draw.drawCenteredScaled(batch, mod.getPic(0).get(), x+size.x/2, y+yTimer.getFloat()+size.y/2, 4, 4);
		}	
		if(mod!=null){
			if(mod.type==ModuleType.WEAPON||mod.type==ModuleType.SHIELD){
				if(mod.type==ModuleType.WEAPON){
					Font.medium.setColor(Colours.withAlpha(Colours.weaponCols8[6], alpha));
				}
				if(mod.type==ModuleType.SHIELD){
					Font.medium.setColor(Colours.withAlpha(Colours.shieldCols6[1], alpha));
				}
				Font.drawFontCentered(batch, mod.tier+"", Font.medium, x+20, collider.position.y+20);
			}
		}

	}
	public void postRender(SpriteBatch batch, float x, float y){
		if(moused){
			batch.setColor(Colours.light);
			Draw.drawScaled(batch, Gallery.rewardOutline.getOutline(), x, y+yTimer.getFloat(), 4, 4);
		}
		if(selected){
			batch.setColor(Reward.selectedColor);
			Draw.drawScaled(batch, Gallery.rewardOutline.getOutline(), x, y+yTimer.getFloat(), 4, 4);
		}
	}
	public void unSelect() {
		selected=false;
	}
	public void select() {
		selected=true;
	}
	public int getBinValue() {
		return 10;
	}

}
