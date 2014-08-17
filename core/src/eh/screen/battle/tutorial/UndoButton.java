package eh.screen.battle.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.util.Bonkject;
import eh.util.Draw;
import eh.util.assets.Gallery;
import eh.util.maths.CircleCollider;
import eh.util.maths.Pair;

public class UndoButton extends Bonkject{
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
		Draw.drawTextureScaledCentered(batch, Gallery.tutUndo.get(), position.x, position.y, 3, 3);
		if(glow)Draw.drawTextureScaledCentered(batch, Gallery.tutUndo.getOutline(), position.x, position.y, 3, 3);
	}
	public static UndoButton get() {
		if(me==null)me=new UndoButton();
		return me;
	}
}
