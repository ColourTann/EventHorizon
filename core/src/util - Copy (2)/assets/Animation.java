package util.assets;

import util.Colours;
import util.Draw;
import util.maths.Pair;
import util.particleSystem.ParticleSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.module.weapon.attack.particle.SmokeMachine;
import eh.module.weapon.attack.particle.Smoke.SmokeType;
import eh.screen.test.Test;
import eh.ship.Debris;

public abstract class Animation {
	Texture[] textures = new Texture[30];
	float ticks=0;
	float speed=30;
	Pair location;
	public Animation (){
		
	}
	
	
	
	public Texture get() {
		
		
		ticks+=Gdx.graphics.getDeltaTime()*speed;
		
		return textures[(int)ticks%textures.length];
		
	}
	public abstract void update(float delta);
	public void render(SpriteBatch batch){
		update(Gdx.graphics.getDeltaTime());
		Draw.drawTextureScaledCentered(batch, get(), location.x, location.y,3,3);
	}

	public void dispose(){
		for(Texture t:textures){
			t.dispose();
		}
	}
	public boolean isDone() {
		return ticks>textures.length-1;
	}
}
