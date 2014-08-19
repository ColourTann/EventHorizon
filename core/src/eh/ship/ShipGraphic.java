package eh.ship;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.Main;
import eh.module.Module.ModuleType;
import eh.ship.niche.Niche;
import eh.ship.niche.NicheGraphic;
import eh.util.Bonkject;
import eh.util.Draw;
import eh.util.maths.Pair;

public class ShipGraphic extends Bonkject{
	Ship ship;
	public static Pair offset=new Pair(175, 90);
	public static int height=270;
	Texture composite;
	public ShipGraphic(Ship s){
		ship=s;
		Pixmap shipMap=new Pixmap(390, 270, Format.RGBA8888);
		shipMap.drawPixmap(ship.shipPic.getPixMap(), 0, 0, 0, 0, 390, 270);
		
		for(Niche n:ship.niches){
			
			shipMap.drawPixmap(n.mod.modulePic.getPixMap(),(int)(n.originalPolygon.getVertices()[0]), (int)(n.originalPolygon.getVertices()[1]), 0, 0,  n.mod.modulePic.get().getWidth(), n.mod.modulePic.get().getHeight());
		}

		composite=new Texture(shipMap);
		shipMap.dispose();
	}



	public void render(SpriteBatch batch){



		batch.setColor(1, 1, 1, 1);
		if(ship.player)Draw.drawTexture(batch, composite, 200, 90);
		if(!ship.player)	Draw.drawTextureScaledFlipped(batch, composite, 
				500+Main.width-offset.x-composite.getWidth(), 
				offset.y,
				1,1,true, false);



		/*for(Niche n:ship.niches){
			if(n.type==ModuleType.WEAPON) n.getGraphic().render(batch);
		}

		if(ship.player){
			Draw.drawTexture(batch, ship.getPic().get(), offset.x, offset.y);
		}
		else{
			Texture t=ship.getPic().get();

			Draw.drawTextureScaledFlipped(batch, t, 
					500+Main.width-offset.x-t.getWidth(), 
					offset.y,
					1,1,true, false);
		}
		for(Niche n:ship.niches){
			if(n.type!=ModuleType.WEAPON){
				n.getGraphic().render(batch);
			}
		}*/

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



	@Override
	public void update(float delta) {

	}
}
