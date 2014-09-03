package game.screen.battle.tutorial;

import game.assets.Gallery;
import util.Draw;
import util.maths.CircleCollider;
import util.maths.Pair;
import util.update.Mouser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UndoButton extends Mouser{
	private static UndoButton me;
	boolean glow;
	public UndoButton(){
		position=new Pair(460,172);
		mousectivate(new CircleCollider(position.x, position.y, 24));
	}
	@Override
	public void mouseDown() {
		glow=true;
	}

	@Override
	public void mouseUp() {
		glow=false;
	}

	@Override
	public void mouseClicked(boolean left) {
		//Tutorial.goBack();
	}

	@Override
	public void update(float delta) {
	}
	public void render(SpriteBatch batch){
		Draw.drawScaledCentered(batch, Gallery.tutUndo.get(), position.x, position.y, 3, 3);
		if(glow)Draw.drawScaledCentered(batch, Gallery.tutUndo.getOutline(), position.x, position.y, 3, 3);
	}
	public static UndoButton get() {
		if(me==null)me=new UndoButton();
		return me;
	}
}
