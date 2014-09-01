package util.update;

import util.Draw;
import util.maths.Pair;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Animation extends Updater{
	private Texture[] textures;
	private float animSpeed=1;
	private float frame=0;
	private boolean oneShot;
	
	public Animation(Texture[] textures, float animSpeed, boolean oneShot, Pair position){
		this.textures=textures;
		this.animSpeed=animSpeed;
		this.oneShot=oneShot;
		this.position=position;
	}
	
	@Override	
	public void update(float delta) {
		frame+=delta*animSpeed;
	}
	
	public void render(SpriteBatch batch){
		Draw.drawTextureCentered(batch, textures[(int) (frame%textures.length)], position.x, position.y);
	}
	
	public boolean isDone(){
		return !oneShot&&frame>=textures.length;
	}
}
