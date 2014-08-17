package eh.ship;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.util.Bonkject;
import eh.util.Draw;
import eh.util.assets.Gallery;
import eh.util.maths.Pair;

public class Debris extends Bonkject{
	Texture t;
	Pair vector;
	float dr;
	float rotation;
	public Debris(boolean big){
		t=Gallery.debris[big?1:0][(int) (Math.random()*5)].get();
		vector=Pair.randomUnitVector().multiply(50);
		dr=(float) (.5+Math.random());
		position=new Pair(50+Math.random()*200, 50+Math.random()*200);
	}
	
	@Override
	public void update(float delta) {
		position=position.add(vector.multiply(delta));
		rotation+=dr*delta;
		vector=vector.multiply((float) Math.pow(.8, delta));
		dr=(float) (dr*Math.pow(.8, delta));
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
	
	public void render(SpriteBatch batch){
		Draw.drawTextureRotatedScaledCentered(batch, t, position.x, position.y, 3, 3, rotation);
	}

	
	
}
