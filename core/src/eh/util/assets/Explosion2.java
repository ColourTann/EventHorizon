package eh.util.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;

import eh.screen.test.Test;
import eh.ship.Debris;
import eh.util.Colours;
import eh.util.maths.Pair;

public class Explosion2 extends Animation{
	public Explosion2(){


		location=new Pair(330,220).add(Pair.randomAnyVector().multiply(80));
		int radius=(int) (6+Math.random()*4);
		int diameter=radius*2;
		Pixmap pixmap=new Pixmap(diameter, diameter, Format.RGBA8888);
		Color col=Colours.weaponCols8[(int) (5+Math.random()*2)];
		Pixmap.setBlending(Blending.None);

		if(Math.random()>.9)Test.debris.add(new Debris(location, Math.random()>.5));
		for(int num=0;num<textures.length;num++){
			for(int x=0;x<diameter;x++){
				for(int y=0;y<diameter;y++){
					int xDist=radius-x;
					int yDist=radius-y;
					float dist=(float) Math.sqrt(xDist*xDist+yDist*yDist);
					float ratio=dist/radius;
					//up to 7 is forming
					//10-30 is deforming
					if(dist>radius)continue;
					if(num<=10){
						if(Math.random()>1f*(ratio*ratio*ratio*ratio*ratio*ratio)){
							pixmap.setColor(col);	
							pixmap.drawPixel(x, y);
						}
					}
					if(num>7){
						pixmap.setColor(0,0,0,0);
						if(Math.random()>1*(ratio*ratio*ratio*ratio*ratio))
						pixmap.drawPixel(x, y);
					}
					



				}	
			}
			textures[num]=new Texture(pixmap);
			
		}
		pixmap.dispose();
		



	}

	@Override
	public void update(float delta) {
	}
}
