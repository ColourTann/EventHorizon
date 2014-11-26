package game.screen.map.stuff;



import java.util.ArrayList;

import game.Main;
import game.assets.Gallery;
import game.assets.TextBox;
import game.module.component.Component;
import game.module.junk.ModuleInfo;
import game.module.junk.ModuleStats;
import game.screen.map.Map;
import game.ship.Ship;
import game.ship.ShipGraphic;
import util.Draw;
import util.maths.BoxCollider;
import util.maths.Pair;
import util.update.Updater;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Base extends Updater{
	final static Pair size=new Pair(900,600);
	final static Pair location=new Pair(Main.width/2-size.x/2, Main.height/2-size.y/2).floor();
	
//	final static Pair infoLoc=new Pair(location.x+473+infoGap, location.y+size.y-ModuleInfo.staticHeight-TextBox.gap-infoGap);
//	final static Pair shipLoc=new Pair(location.x+300,location.y+size.y-550);
	final static Pair infoLoc=new Pair(location.x+473+5,location.y+TextBox.gap*2);
	final static Pair shipLoc=new Pair(location.x+300, location.y+size.y-ShipGraphic.height-20);
	
	final static Pair scrollerSize=new Pair(Gallery.rewardOutline.getWidth()*4+TextBox.gap*2,Gallery.rewardHighlights.getHeight()*4*5-4+TextBox.gap*2);
	final static Pair scrollerLoc=new Pair(location.x+size.x-scrollerSize.x-TextBox.gap, location.y+TextBox.gap);
	
	ArrayList<ModuleStats> stats= new ArrayList<ModuleStats>();
	ShipGraphic playerGraphic;
	Ship player;
	ModuleInfo oldInfo;
	ModuleInfo info;
	public Scroller inv;
	public Base(){
		player=Map.player.getShip();
		for(Component c:player.components){
			stats.add(c.getStats());
		}
		int x=(int) (location.x+TextBox.gap+ModuleStats.width);
		int y=(int) (location.y+size.y-TextBox.gap-ModuleStats.height*3);
		for(int i=0;i<stats.size();i++){
			ModuleStats ms=stats.get(i);
			ms.mousectivate(new BoxCollider(x, y, ModuleStats.width, ModuleStats.height));
			y+=ModuleStats.height;
			if(i==2){
				y=(int) (location.y+size.y-TextBox.gap-ModuleStats.height*3);
				x-=ModuleStats.width;
			}
		}
		playerGraphic=player.getGraphic();
		playerGraphic.position=shipLoc;
		inv=new Scroller(player.getInv(), scrollerLoc, scrollerSize);
	}
	
	public void render(SpriteBatch batch){
		TextBox.renderBox(batch, location, size.x, size.y, util.TextWriter.Alignment.Left);
		for(ModuleStats ms:stats)ms.render(batch);
		Draw.draw(batch, playerGraphic.picCut.get(), shipLoc.x, shipLoc.y);
		
		if(info!=null)info.render(batch);
		if(oldInfo!=null)oldInfo.render(batch);
		inv.render(batch);
		//Draw.drawScaled(batch, Gallery.whiteSquare.get(), location.x, location.y, size.x, size.y);
	}

	public void mouseStats(ModuleStats moduleStats) {
		oldInfo=info;
		info=moduleStats.info;
		info.setPosition(infoLoc);
		info.alpha=1;
		System.out.println(info.position);
	}
}
