package eh.util;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.assets.Font;
import eh.util.maths.Pair;

public class TextWisp extends Bonkject{
	String text;    
	float holdTime=.3f;
	float speed=30;
	float fadeTime=.3f;
	float fadeSpeed=1;
	Pair position;
	BitmapFont f=Font.medium;
	Color c=Colours.light;
	public static ArrayList<TextWisp> wisps= new ArrayList<TextWisp>();
	public TextWisp(String text, Pair startPosition) {
		super(null);
		this.text=text;
		this.position=startPosition;
		wisps.add(this);
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


	public void render(SpriteBatch batch) {
		
		f.setColor(Colours.withAlpha(Colours.dark, alpha));
		f.draw(batch, text, position.x-f.getBounds(text).width/2, position.y-f.getBounds(text).height/2);
		f.setColor(Colours.withAlpha(c, alpha));
		f.draw(batch, text, position.x-f.getBounds(text).width/2+2, position.y-f.getBounds(text).height/2+2);
	}

	@Override
	public void update(float delta) {
		if(holdTime>0){
			holdTime-=delta;
			return;
		}
		if(alpha>0){
			position.y+=delta*speed;
			alpha-=delta*fadeSpeed;
		}
		else{
			dead=true;
		}
	}


}
