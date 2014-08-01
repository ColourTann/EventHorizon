package eh.ship.niche;

import java.util.Arrays;

import com.badlogic.gdx.math.Polygon;

import eh.Main;
import eh.ship.Ship;
import eh.ship.ShipGraphic;
import eh.ship.module.Module;
import eh.ship.module.Module.ModuleType;
import eh.ship.module.weapon.Weapon;
import eh.util.maths.PolygonCollider;
import eh.util.maths.Pair;

public class Niche{
	Module mod;
	public Polygon p;
	public ModuleType type;
	Ship ship;
	int index;
	public Pair location;
	public NicheGraphic graphic;
	public float width,height;
	public Niche(Ship ship, ModuleType type) {		
		this.ship=ship;
		this.type=type;
	}

	public void setup(Polygon p){
		//Setting up the polygon for mousing and positioning//
		this.p=p;
		p.setOrigin(195, 1135);
		if(ship.player){
			p.translate(ShipGraphic.offset.x, ShipGraphic.offset.y);
		}
		else{
			p.setScale(-1, 1);
			p.translate(Main.width-ShipGraphic.offset.x-390, ShipGraphic.offset.y);
		}
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
			width=m.modulePic.get().getWidth()*(ship.player?1:-1);
			height=m.modulePic.get().getHeight(); 
			start.y-=height/2;
			if(type==ModuleType.WEAPON){
				start.x+=((Weapon) mod).weaponOffset*(ship.player?1:-1);
			}
			p=new Polygon(new float[]{start.x,start.y,start.x+width,start.y, start.x+width,start.y+height, start.x, start.y+height});
		}
		float x=((PolygonCollider)getGraphic().collider).p.getBoundingRectangle().x;
		float y=((PolygonCollider)getGraphic().collider).p.getBoundingRectangle().y;
		width=p.getBoundingRectangle().width;
		height=p.getBoundingRectangle().height;
		location=new Pair(x,y);
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
