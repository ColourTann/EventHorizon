package util.update;

import util.Draw;
import util.maths.Pair;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Animation extends Updater{
	private Texture[] textures;
	private float animSpeed=1;
	private float frame=0;
	public boolean oneShot;
	private boolean disposed;
	public void setup(Texture[] textures, float animSpeed, boolean oneShot, Pair position){
		this.textures=textures;
		this.animSpeed=animSpeed;
		this.oneShot=oneShot;
		this.position=position;
	}
	

	
	@Override	
	public void update(float delta) {
		if(frame>=textures.length-1&&oneShot)return;
		frame+=delta*animSpeed;
		
	}
	
	public void render(SpriteBatch batch){
		if(disposed)return;
		Draw.drawCentered(batch, textures[(int) (frame%textures.length)], position.x, position.y);
	}
	
	public boolean isDone(){
		return !oneShot&&frame>=textures.length;
	}
	
	public void dispose(){
		for(Texture t:textures){
			t.dispose();
		}
		disposed=true;
	}
}
