package eh.screen.menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import eh.Main;
import eh.Main.ScreenType;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.maths.BoxCollider;
import eh.util.maths.Collider;

public class GameChoice extends Bonkject{
	static float width=200;
	static float height=40;
	float x;
	float y;
	String str;
	ScreenType type;
	public GameChoice(int x, int y, String title, ScreenType type) {
		super(new BoxCollider(x-width/2, y-height/2, width, height));
		this.x=x-width/2;
		this.y=y+height/2;
		str=title;
		mousectivate();
		this.type=type;
	}

	@Override
	public void mouseDown() {
	}

	@Override
	public void mouseUp() {
	}

	@Override
	public void mouseClicked(boolean left) {
		Main.changeScreen(type);
	}

	@Override
	public void update(float delta) {
	}


	public void render(SpriteBatch batch) {
		Junk.drawTextureScaled(batch,Gallery.tutPanelBorder.get(), x,y, 2f, 2f);
		Junk.drawTextureScaled(batch, Gallery.tutPanelMain.get(), x, y-height, 2f, height);
		Junk.drawTextureScaled(batch,Gallery.tutPanelBorder.get(), x,y-height, 2f, -2f);
		Font.big.setColor(Colours.withAlpha(Colours.light,alpha));
		Font.big.drawWrapped(batch, str, x, y-2, width, HAlignment.CENTER);
	}

}
