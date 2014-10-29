package game.screen.menu;


import util.Colours;
import util.Draw;
import util.TextWriter.Alignment;
import util.assets.Font;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Mouser;
import util.update.Screen;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.assets.TextBox;

public class GameChoice extends Mouser{
	static float width=200;
	static float height=40;
	float x;
	float y;
	String str;
	Screen screen;
	public GameChoice(int x, int y, String title, Screen screen) {
		this.x=x-width/2;
		this.y=y+height/2;
		str=title;
		mousectivate(new BoxCollider(x-width/2, y-height/2, width, height));
		this.screen=screen;
		position=new Pair(x-width/2,y-height/2);
	}

	@Override
	public void mouseDown() {
	}

	@Override
	public void mouseUp() {
	}

	@Override
	public void mouseClicked(boolean left) {
		Main.changeScreen(screen);
	}

	@Override
	public void update(float delta) {
	}


	public void render(SpriteBatch batch) {
		TextBox.renderBox(batch, position, width, height, Alignment.Left);
//		Draw.drawScaled(batch,Gallery.tutPanelBorder.get(), x,y, 2f, 2f);
//		Draw.drawScaled(batch, Gallery.tutPanelMain.get(), x, y-height, 2f, height);
//		Draw.drawScaled(batch,Gallery.tutPanelBorder.get(), x,y-height, 2f, -2f);
		Font.big.setColor(Colours.withAlpha(Colours.light,alpha));
		Font.big.drawWrapped(batch, str, x, y-31, width, HAlignment.CENTER);
		
	}

}
