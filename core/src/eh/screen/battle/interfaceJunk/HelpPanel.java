package eh.screen.battle.interfaceJunk;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.Main;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.util.Bonkject;
import eh.util.Colours;

public class HelpPanel extends Bonkject{
	String text;
	int y=415;
	public HelpPanel(String s, boolean high) {
		super(null);
		text=s;
		fadeIn(Interp.SQUARE, 2);
		if(high)y=550;
		else y=415;
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
	public void render(SpriteBatch batch) {
		batch.setColor(1,1,1,alpha);
		Texture t=Gallery.helpPanel.get();
		batch.draw(t,Main.width/2-t.getWidth()/2,y);
		Font.medium.setColor(Colours.withAlpha(Colours.dark,alpha));
		Font.medium.draw(batch, text, Main.width/2-Font.medium.getBounds(text).width/2, y+30);
		batch.setColor(1,1,1,1);
	}
	
	public void done(){
		fadeOut(Interp.SQUARE, 1);
	}

	@Override
	public void update(float delta) {
	}

}
