package game.assets;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType.Bitmap;

import util.TextWriter;
import util.TextWriter.Alignment;
import util.assets.Font;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Mouser;
import util.update.SimpleButton.Code;

public class AdvancedButton extends Mouser{
	static int xOffset=10,yOffset=10;
	BitmapFont font=Font.medium;
	TextWriter tw;
	Code code;
	int width, height;
	public AdvancedButton(Pair position, Pair overrideSize, String name, BitmapFont font, Code code) {
		this.font=font;
		tw=new TextWriter(font, name);
		tw.setupTexture();
		this.code=code;
		
		width=(int) (tw.maxWidth+xOffset*2);
		height=(int) (tw.maxHeight+yOffset*2);
		if(overrideSize!=null){
			if(overrideSize.x>0){
				width=(int) overrideSize.x;
			}
			if(overrideSize.y>0){
				height=(int) overrideSize.y;
			}
		}
		this.position=position;
		mousectivate(new BoxCollider(position.x, position.y, width, height));
	}

	@Override
	public void mouseDown() {
		
	}

	@Override
	public void mouseUp() {
	}

	@Override
	public void mouseClicked(boolean left) {
		code.onPress();
	}

	@Override
	public void update(float delta) {
	}

	public void render(SpriteBatch batch){
		TextBox.renderBox(batch, position, width, height, Alignment.Left);
		tw.render(batch, position.x+width/2-tw.maxWidth/2, position.y+yOffset);
	}
}
