package game.screen.map.stuff;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Draw;
import util.maths.Pair;
import util.update.Timer;
import util.update.Timer.Interp;
import game.assets.Gallery;
import game.card.Card;
import game.module.Module;

public class Item {
	private static final float timerSpeed = .3f;
	Module mod;
	Card card;
	public static float height=Gallery.rewardOutline.getHeight()*4-4;
	public Timer yTimer= new Timer();
	static final Pair size=new Pair(Gallery.rewardOutline.getWidth()*4, Gallery.rewardOutline.getHeight()*4);
	public Item(Module mod){
		this.mod=mod;
	}
	public Item(Card card){
		this.card=card;
	}
	public void setY(float y){
		yTimer=new Timer(y,y,0,Interp.LINEAR);
	}
	public void moveY(float y){
		yTimer=new Timer(yTimer.getFloat(),y,timerSpeed,Interp.SQUARE);
	}
	public void render(SpriteBatch batch, float x, float y) {
		
		Draw.drawScaled(batch, Gallery.rewardOutline.get(), x, y+yTimer.getFloat(), 4, 4);
		if(mod!=null){
			Draw.drawCenteredScaled(batch, mod.getPic(0).get(), x+size.x/2, y+yTimer.getFloat()+size.y/2, 4, 4);
		}
	}
}
