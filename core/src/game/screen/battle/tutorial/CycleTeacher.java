package game.screen.battle.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.TextWriter;
import util.assets.Font;
import util.maths.Pair;
import game.Main;
import game.assets.Gallery;
import game.assets.TextBox;

public class CycleTeacher extends TextBox{
	private static CycleTeacher teacher;
	TextWriter writer;
	float width,height;
	static float offset=10;
	Pair midPoint;
	Pair target;
	float distance;
	float rotation;
	public CycleTeacher(){
		
		Font.medium.setColor(Colours.light);
		writer=new TextWriter(Font.medium, "You can use the cycle button to get the card you need but it costs more each time you use it");
		writer.setWrapWidth(300);
		writer.setupTexture();
		width=writer.maxWidth+offset*2;
		height=writer.maxHeight+offset*2;
		position=new Pair(Main.width/2-width/2,200);
		target=new Pair(200,360);
		midPoint=position.add(new Pair(width/2,height/2));
		distance=midPoint.getDistance(target);
		rotation=midPoint.getAngle(target);
	}
	public void render(SpriteBatch batch){
		batch.setColor(1,1,1,1);
		Draw.drawRotatedScaled(batch, Gallery.tutPoint.get(), midPoint.x, midPoint.y, distance, 1, rotation);
		renderBox(batch, width, height);
		writer.render(batch, position.x+offset, position.y+offset);
	}
	public static CycleTeacher get(){
		if(teacher==null) teacher=new CycleTeacher();
		return teacher;
	}

}
