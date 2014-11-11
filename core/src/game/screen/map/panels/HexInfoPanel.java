package game.screen.map.panels;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import game.Main;
import game.grid.hex.Hex;
import game.screen.map.Map;
import game.ship.Ship;
import game.ship.mapThings.MapShip;
import game.ship.mapThings.mapAbility.MapAbility;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HexInfoPanel extends SidePanel{
	public static final int gap=12;
	public static final int width=441;
	Hex moused;
	
	public HexInfoPanel(){
		
	}
	
	public void hexMouse(Hex h){
		
		moused=h;
		MapShip mapShip=moused.mapShip;
		
		if(mapShip!=null&&mapShip!=Map.player){
		for(int i=0;i<mapShip.mapAbilities.size();i++){
			MapAbility ma=mapShip.mapAbilities.get(i);
			ma.showAt(new Pair(Main.width-100,90+gap+100*i));
			ma.demousectivate();
		}
		}
	}
	
	@Override
	public void update(float delta) {
	}
	
	public void render(SpriteBatch batch){
	
		if(moused==null)return;
		Font.medium.setColor(Colours.light);
		MapShip mapShip=moused.mapShip;
		
		if(mapShip!=null&&mapShip!=Map.player){
			
			Ship ship = mapShip.getShip();
			if(ship==null){
				System.out.println("a null ship whuhoh");
			}
			Font.drawFontCentered(batch, ship.shipName, Font.medium, Main.width-width/2, gap);
			Draw.drawCenteredScaled(batch, ship.getPic().get(), Main.width-width/2, 100, .33f, .33f);
			
			int powerDifference=(int)((ship.getStats().power-Map.player.getShip().getStats().power)*10);
			Font.medium.setColor(Colours.genCols5[3]);
			if(powerDifference>MapShip.ignoreRange*10){
				Font.medium.setColor(Colours.enemy2[0]);
			}
			if(powerDifference<-MapShip.ignoreRange*10){
				Font.medium.setColor(Colours.player2[1]);
			}
			Font.drawFontCentered(batch, "Power difference: "+(int)((ship.getStats().power-Map.player.getShip().getStats().power)*10), Font.medium, Main.width-width/2, gap+23);
			Font.medium.setColor(Colours.light);
			for(MapAbility ma:mapShip.mapAbilities)ma.render(batch);
			
		}
		else if(moused.content!=null){
			Font.drawFontCentered(batch, moused.content.toString(), Font.medium, Main.width-width/2, gap);
		}
		
	}

}
