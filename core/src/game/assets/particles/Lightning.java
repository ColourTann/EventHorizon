package game.assets.particles;

import game.Main;
import game.assets.Gallery;
import util.Colours;
import util.Draw;
import util.Draw.BlendType;
import util.maths.Pair;
import util.particleSystem.Particle;
import util.update.Mouser;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Lightning extends Particle{
	Pair start;
	Pair finish;
	float alpha;
	public Lightning(Pair start, Pair finish, int position, float alpha){
		this.start=start;
		this.finish=finish;
		this.alpha=alpha;
		
	}
	@Override
	public void update(float delta) {
		alpha-=delta*1.4f;
		alpha=Math.max(0, alpha);
		
	}

	@Override
	public void render(SpriteBatch batch) {
		
		Draw.setBlend(batch, BlendType.Additive);
		
		batch.setColor(Colours.withAlpha(Colours.blueWeaponCols4[0],alpha));//Colours.make(99,187,213),alpha));
		Texture t=Gallery.lightningEnd.get();
		Pair distance=finish.subtract(start);
		Pair vector = finish.subtract(start);
		double angle=Math.atan2(vector.y, vector.x);
		Pair normal=vector.normalise();
		float scale=.5f;
		normal=normal.multiply((t.getWidth()/2+1)*scale);

		//batch.setColor(1,1,1,.4f);
		Draw.drawCenteredRotatedScaled(batch, Gallery.lightning.get(), start.x+distance.x/2f, start.y+distance.y/2f+scale/2f, distance.getDistance(), scale, (float)angle);
		//batch.setColor(1,1,1,1);
		Draw.drawCenteredRotatedScaled(batch, t, start.x-normal.x, start.y-normal.y, scale,scale, (float)angle);
		
		Draw.drawCenteredRotatedScaled(batch, t, finish.x+normal.x, finish.y+normal.y, -scale,scale, (float)angle);
		
		
	
		
		Draw.setBlend(batch, BlendType.Normal);
		
		
	}

}
