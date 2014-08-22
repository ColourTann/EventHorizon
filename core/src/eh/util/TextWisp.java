package eh.util;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.util.assets.Font;
import eh.util.maths.Pair;

public class TextWisp extends Bonkject{
	
	public enum WispType{Regular, HoldUntilFade}
	
	String text;    
	float holdTime=.3f;
	float speed=30;
	float fadeTime=.3f;
	float fadeSpeed=1;
	Pair position;
	BitmapFont f=Font.medium;
	Color c=Colours.light;
	public WispType type;
	public static ArrayList<TextWisp> wisps= new ArrayList<TextWisp>();
	
	public TextWisp(String text, BitmapFont font, Pair startPosition, WispType type) {
		this.text=text;
		this.position=startPosition;
		this.type=type;
		f=font;
		switch (type){
		case HoldUntilFade:
			holdTime=-1;
			speed=0;
			
			break;
		case Regular:
			break;
		}
		
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
	
	public void release(){
		holdTime=0;
	}

	public void render(SpriteBatch batch) {
		f.setColor(Colours.withAlpha(Colours.dark, alpha));
		f.draw(batch, text, position.x-f.getBounds(text).width/2, position.y-f.getBounds(text).height/2);
		f.setColor(Colours.withAlpha(c, alpha));
		f.draw(batch, text, position.x-f.getBounds(text).width/2+2, position.y-f.getBounds(text).height/2+2);
	}

	@Override
	public void update(float delta) {
		if(holdTime==-1)return;
		
		if(holdTime>0){
			holdTime-=delta;
			return;
		}
		if(alpha>0){
			position.y-=delta*speed;
			alpha-=delta*fadeSpeed;
			if(alpha<=0){
				wisps.remove(this);
				dead=true;
			}
		}
	}


}
