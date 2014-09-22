package game.screen.test;

import game.assets.Gallery;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.maths.Pair;
import util.update.Updater;

public class Fire {
	boolean dead;
	Pair position;
	Pair vector;
	float life, maxLife, ratio, scale;
	
	public Fire(Pair position, float scale){
		this.scale=scale;
		this.position=position;
		maxLife=scale/5f;
		life=maxLife;
		vector=Pair.randomUnitVector().multiply(scale*2);
	}

	public void setLife(float value){
		maxLife=value;
		life=value;
	}
	
	public void update(float delta) {
		if(dead) return;
		ratio=life/maxLife;
		
		life-=delta;
		if(life<=0){
		
			dead=true;
		}
		position=position.add(vector.multiply(delta));
	}
	public void render(SpriteBatch batch){
		if(dead)return;

		batch.setColor(Colours.withAlpha(Colours.shieldCols6[2], ratio));
		Draw.drawCenteredScaled(batch, Gallery.fuzzBall.get(), position.x, position.y,scale/25f*ratio,scale/25f*ratio);
		
	}

}
