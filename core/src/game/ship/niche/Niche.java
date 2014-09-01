package game.ship.niche;

import java.util.Arrays;

import util.maths.Pair;
import util.maths.PolygonCollider;

import com.badlogic.gdx.math.Polygon;

import game.Main;
import game.module.Module;
import game.module.Module.ModuleType;
import game.module.weapon.Weapon;
import game.ship.Ship;
import game.ship.ShipGraphic;

public class Niche{
	public Module mod;
	public Polygon p;

	public ModuleType type;
	Ship ship;
	int index;
	public Pair location;
	public NicheGraphic graphic;
	public float width,height;
	public Pair relativeTopLeft;
	
	public Niche(Ship ship, ModuleType type) {		
		this.ship=ship;
		this.type=type;
	}

	public void setup(Polygon p){
		relativeTopLeft=new Pair(p.getVertices()[0],p.getVertices()[1]);
	
		//Setting up the polygon for mousing and positioning//
		this.p=p;
		/*p.setOrigin(195, 1135);
		if(ship.player){
			p.translate(ShipGraphic.offset.x, ShipGraphic.offset.y);
		}
		else{
			p.setScale(-1, 1);
			p.translate(Main.width-ShipGraphic.offset.x-390, ShipGraphic.offset.y);
		}*/
	}

	public void install(Module m){
		m.niche=this;
		index=Arrays.asList(ship.niches).indexOf(this);
		ship.modules[index]=m;
		m.index=index;
		this.mod=m;
		m.ship=ship;
		if(type==ModuleType.WEAPON||type==ModuleType.SHIELD){
			Pair start=new Pair(p.getTransformedVertices()[0], p.getTransformedVertices()[1]);
			float tWidth=m.modulePic.get().getWidth()*(ship.player?1:-1);
			float tHeight=m.modulePic.get().getHeight(); 
			start.y-=tHeight/2;
			if(type==ModuleType.WEAPON){
				start.x+=((Weapon) mod).weaponOffset*(ship.player?1:-1);
			}
			p=new Polygon(new float[]{start.x,start.y,start.x+tWidth,start.y, start.x+tWidth,start.y+tHeight, start.x, start.y+tHeight});
		}
		getGraphic();
		float x=p.getBoundingRectangle().x;
		float y=p.getBoundingRectangle().y;
		width=p.getBoundingRectangle().width;
		height=p.getBoundingRectangle().height;
		location=new Pair(x,y);
		location=location.floor();
	}

	public NicheGraphic getGraphic(){
		if(mod==null){
			System.out.println("niche not installed with module yet");
			return null;
		}
		if(graphic==null)graphic=new NicheGraphic(this);
		return graphic;
	}

	public String toString(){
		return ship+" niche "+ index+ ": "+type+" containting "+mod;
	}


}
