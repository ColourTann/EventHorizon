package game.screen.map.stuff;

import com.badlogic.gdx.graphics.Color;
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
import game.module.component.shield.Shield;
import game.module.component.weapon.Weapon;
import game.module.junk.InfoBox;
import game.module.utility.Utility;
import game.module.utility.armour.Armour;
import game.screen.customise.Reward;
import game.screen.map.Map;

public class Item extends Mouser{
	public Module mod;
	public Card card;
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
		mousectivate(new BoxCollider(0, 0, width, height));
		demousectivate();
		deactivate();
	}
	public void setY(float y){
		yTimer=new Timer(y,y,0,Interp.LINEAR);
	}
	public void moveY(float y){
		yTimer=new Timer(yTimer.getFloat(),y,Scroller.viewSpeed,Interp.SQUARE);
	}

	@Override
	public void mouseDown() {		
		Map.mouseStats(getInfo());
		Map.mouseItem(this);
	}
	private InfoBox getInfo() {
		if(mod!=null)return mod.getInfo();
		if(card!=null)return card.getCardInfo();
		return null;
	}
	@Override
	public void mouseUp() {
		getInfo().fadeAll();
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
		Draw.drawScaled(batch, Gallery.rewardOutline.get(), (int)x, (int)(y+yTimer.getFloat()), 4, 4);
		if(mod!=null){
			Draw.drawCenteredScaled(batch, mod.getPic(0).get(), (int)(x+size.x/2), (int)(y+yTimer.getFloat()+size.y/2), 4, 4);
		}	
		if(mod!=null){
			if(mod.type==ModuleType.WEAPON||mod.type==ModuleType.SHIELD){
				if(mod.type==ModuleType.WEAPON){
					Font.medium.setColor(Colours.withAlpha(Colours.weaponCols8[6], alpha));
				}
				if(mod.type==ModuleType.SHIELD){
					Font.medium.setColor(Colours.withAlpha(Colours.shieldCols6[1], alpha));
				}
				Font.drawFontCentered(batch, (mod.tier+1)+"", Font.medium, (int)(x+20), (int)(collider.position.y+y+26));
			}
		}
		if(card!=null){
			Draw.drawCenteredScaled(batch, card.getImage(0).get(), (int)(x+size.x/2), (int)(y+yTimer.getFloat()+size.y/2), 4, 4);
		}
		
		Color col=null;
		if(mod!=null){
			if(mod instanceof Weapon)col=Colours.weaponCols8[4];
			if(mod instanceof Shield)col=Colours.shieldCols6[4];
			if(mod instanceof Utility)col=Colours.compCols6[2];
			if(mod instanceof Armour)col=Colours.shipHull7[2];
		}
		if(col==null)col=Colours.orangeHPCols[1];
		batch.setColor(Colours.withAlpha(col, alpha));
		
		Draw.drawScaled(batch, Gallery.rewardHighlights.get(), (int)x, (int)(y+yTimer.getFloat()),4,4);

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
