package game.screen.customise;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Gallery;
import util.Draw;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Mouser;

public class Slot extends Mouser{
	boolean armour;
	static int width=Gallery.rewardOutline.getWidth()*4;
	static int height=Gallery.rewardOutline.getHeight()*4;
	public Slot(Pair position, boolean armourOnly){
		this.position=position;
		this.armour=armourOnly;
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
	}

	@Override
	public void update(float delta) {
	}

	public void render(SpriteBatch batch){
		Draw.drawScaled(batch, Gallery.rewardOutline.get(), position.x, position.y, 4, 4);
	}
	
}
