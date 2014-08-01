package eh.ship;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import eh.Main;
import eh.screen.battle.Battle;
import eh.ship.module.Module.ModuleType;
import eh.ship.niche.Niche;
import eh.util.maths.Pair;

public class ShipGraphic {
	Ship ship;
	public static Pair offset=new Pair(165, 340);
	public ShipGraphic(Ship s){
		ship=s;
	}
	
	
	
	public void render(SpriteBatch batch){
		

		
			
	
		
		for(Niche n:ship.niches){
			if(n.type==ModuleType.WEAPON) n.getGraphic().render(batch);
		}

		if(ship.player){
			batch.draw(ship.getPic().get(), offset.x, offset.y);
		}
		else{
			Texture t=ship.getPic().get();
			batch.draw(t, Main.width-offset.x-t.getWidth()+(float)Math.sin(Battle.ticks*Battle.sinSpeed)*Battle.enemyShakeIntensity, offset.y+(float)Math.cos((Battle.ticks-2.5f)*Battle.sinSpeed)*Battle.enemyShakeIntensity, t.getWidth(), t.getHeight(), 0, 0, t.getWidth(), t.getHeight(), true, false);
		}
		for(Niche n:ship.niches){
			if(n.type!=ModuleType.WEAPON){
				n.getGraphic().render(batch);
			}
		}

	}
}
