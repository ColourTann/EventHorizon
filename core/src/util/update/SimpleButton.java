package util.update;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Draw;
import util.assets.Font;
import util.image.Pic;
import util.maths.BoxCollider;
import util.maths.Pair;

public class SimpleButton extends Mouser{

	public Code code;
	
	public Pic pic;
	public String name;
	public int width,height;
	public BitmapFont font=Font.medium;
	float scale=1;
	public SimpleButton(Pair position, String name, Pic pic, Code code){
		this.position=position;
		this.pic=pic;
		this.code=code;
		this.name=name;
		width=pic.get().getWidth();
		height=pic.get().getHeight();
		mousectivate(new BoxCollider(position.x, position.y, width, height));
		moveToTop();
	}
	
	public void setScale(float scale){
		this.scale=scale;
		width=(int) (pic.getWidth()*scale);
		height=(int) (pic.getHeight()*scale);
		mousectivate(new BoxCollider(position.x, position.y, width, height));
	}

	public void setPosition(Pair position){
		this.position=position;
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
		Color base=batch.getColor().cpy();
		batch.setColor(1,1,1,batch.getColor().a*alpha);
		
	
		
		Draw.drawScaled(batch, pic.get(), position.x, position.y, scale, scale);
		if(moused)Draw.drawScaled(batch, pic.getOutline(), position.x, position.y, scale, scale);
		font.setColor(batch.getColor());
		Font.drawFontCentered(batch, name, font, position.x+width/2, position.y+height/2);
		batch.setColor(base);
	}

	


}
