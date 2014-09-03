package util;

import util.assets.Font;
import util.assets.Pic;
import util.maths.BoxCollider;
import util.maths.Pair;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SimpleButton extends Bonkject{

	Code code;
	Pic pic;
	String name;
	int width,height;
	public SimpleButton(Pair position, String name, Pic pic, Code code){
		this.position=position;
		this.pic=pic;
		this.code=code;
		this.name=name;
		width=pic.get().getWidth();
		height=pic.get().getHeight();
		mousectivate(new BoxCollider(position.x, position.y, width, height));
	}
	
	public interface Code{
		public void onPress();
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
		Draw.drawTexture(batch, pic.get(), position.x, position.y);
		if(moused)Draw.drawTexture(batch, pic.getOutline(), position.x, position.y);
		Font.medium.setColor(batch.getColor());
		Font.medium.draw(batch, name, position.x+width/2-Font.medium.getBounds(name).width/2, position.y+height/2-Font.medium.getBounds(name).height/2);
	}
	
		
}
