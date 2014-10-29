package game.screen.battle.tutorial;

import game.assets.Gallery;
import game.screen.battle.Battle;
import util.Draw;
import util.maths.CircleCollider;
import util.maths.Pair;
import util.update.Mouser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UndoButton extends Mouser{
	private static UndoButton me;
	boolean glow;
	public UndoButton(){
		position=new Pair(420,172);
		mousectivate(new CircleCollider(position.x, position.y, 24));
	}
	public static void setPosition(Pair position){
		get().position=position;
		me.collider.position=position;
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
	//	if(Tutorial.undoVisible()){
			Tutorial.goBack();
		//}
	}

	@Override
	public void update(float delta) {
	}
	public void render(SpriteBatch batch){
		batch.setColor(1,1,1,1);
		Draw.drawCenteredScaled(batch, Gallery.tutUndo.get(), position.x, position.y, 3, 3);
		if(glow)Draw.drawCenteredScaled(batch, Gallery.tutUndo.getOutline(), position.x, position.y, 3, 3);
	}
	public static UndoButton get() {
		if(me==null)me=new UndoButton();
		return me;
	}
}
