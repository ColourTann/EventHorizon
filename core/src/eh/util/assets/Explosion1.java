package eh.util.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;

import eh.screen.battle.Battle;
import eh.screen.test.Test;
import eh.ship.Debris;
import eh.util.Colours;
import eh.util.maths.Pair;

public class Explosion1 extends Animation{
	Pair baseLocation;
	public Explosion1(Pair location){
		this.location=location.add(Pair.randomAnyVector().multiply(30));
		baseLocation=location.copy();
		//int radius=(int) (14+Math.random()*12)/3;
		int radius=(int) (3+Math.random()*4);
		int diameter=radius*2;
		Pixmap pixmap=new Pixmap(diameter+1, diameter+1, Format.RGBA8888);
		Color col=Colours.weaponCols8[(int) (5+Math.random()*2)];

		//ParticleSystem.systems.add(new SmokeMachine(location.add(radius,radius).add(Pair.randomAnyVector().multiply(10)), .1f, 20, SmokeType.Damage));
		//if(Math.random()>.9)Test.debris.add(new Debris(location, Math.random()>.5));
		//ParticleSystem.systems.add(new SmokeMachine(location.add(radius,radius).add(Pair.randomAnyVector().multiply(10)), .1f, 20, SmokeType.Laser));

		

		textures[0]=new Texture(pixmap);
		Pair start=new Pair(radius,radius).add(Pair.randomUnitVector().multiply(radius/2));
		Pixmap.setBlending(Blending.None);

		for(int num=0;num<textures.length;num++){
			pixmap.setColor(num<3?Colours.weaponCols8[3]:Colours.weaponCols8[5]);
			pixmap.fillCircle(radius, radius, radius);
			pixmap.setColor(0,0,0,0);
			pixmap.fillCircle((int)start.x, (int)start.y, (int) (((float)diameter/(float)textures.length)*num));
			textures[num]=new Texture(pixmap);
		}
	}
	
	public void update(float delta){
		/*float freq=50;
		float amp=2;
		location=baseLocation.add((int)(Math.sin(Battle.ticks*freq)*amp*2), (int)(Math.sin(Battle.ticks*freq+10)*amp));*/
	}
}
