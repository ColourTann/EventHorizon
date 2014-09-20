package game.ship;

import game.assets.Gallery;
import util.Draw;
import util.maths.Pair;
import util.update.Updater;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Debris extends Updater{
	Texture t;
	Pair vector;
	float dr;
	float rotation;
	public Debris(Pair location, boolean big){
		t=Gallery.debris[big?1:0][(int) (Math.random()*5)].get();
		vector=Pair.randomUnitVector().multiply(250);
		dr=(float) (.5+Math.random())*5;
		position=location;
	}
	
	@Override
	public void update(float delta) {
		position=position.add(vector.multiply(delta));
		rotation+=dr*delta;
		vector=vector.multiply((float) Math.pow(.8, delta));
		dr=(float) (dr*Math.pow(.8, delta));
	}
	

	
	public void render(SpriteBatch batch){
		Draw.drawCenteredRotatedScaled(batch, t, position.x, position.y, 3, 3, rotation);
	}

	
	
}
