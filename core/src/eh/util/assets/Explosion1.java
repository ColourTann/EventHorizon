package eh.util.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;

import eh.screen.test.Test;
import eh.ship.Debris;
import eh.util.Colours;
import eh.util.maths.Pair;

public class Explosion1 extends Animation{
	public Explosion1(){
		location=new Pair(130,100).add(Pair.randomAnyVector().multiply(80));
		//int radius=(int) (14+Math.random()*12)/3;
		int radius=(int) (6+Math.random()*4);
		int diameter=radius*2;
		Pixmap pixmap=new Pixmap(diameter+1, diameter+1, Format.RGBA8888);
		Color col=Colours.weaponCols8[(int) (5+Math.random()*2)];

		//ParticleSystem.systems.add(new SmokeMachine(location.add(radius,radius).add(Pair.randomAnyVector().multiply(10)), .1f, 20, SmokeType.Damage));
		if(Math.random()>.9)Test.debris.add(new Debris(location, Math.random()>.5));
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
}
