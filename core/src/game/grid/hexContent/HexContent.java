package game.grid.hexContent;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.grid.Grid;
import game.grid.hex.Hex;
import game.ship.Ship;
import game.ship.mapThings.MapShip;
import game.ship.mapThings.MapShip.MapShipStrength;

public abstract class HexContent {
	Hex hex;
	public HexContent(Hex hex){
		this.hex=hex;
	}
	public abstract void turn();
	public abstract void render(SpriteBatch batch);
	public abstract String toString();
	
	public void chanceToMakeShip(){
		if(hex.swallowed(0))return;
		for(Hex h:hex.getHexesWithin(2, true)){
			if(h.mapShip!=null){
				//System.out.println("not making because found a ship");
				return;
			}
		}
		int numShips=Grid.getCloseShips().size();
		double firstRandom=Math.random()*2;
		if(firstRandom<1+numShips/(float)Grid.goodNumberOfShips){
//			System.out.println("Not spawning, Current ships:"+numShips+", random: "+firstRandom+", needed: "+(1+numShips/(float)Grid.goodNumberOfShips));
			return;
		}
//		System.out.println("Ok spawning, Current ships:"+numShips+", random: "+firstRandom+", needed: "+(1+numShips/(float)Grid.goodNumberOfShips));
	
		int lowLevels=0;
		int mediumLevels=0;
		int highLevels=0;
		for(MapShip mapShip:Grid.getCloseShips()){
			int level=mapShip.getShip().getSimpleStats();
			if(level<=7){
				lowLevels++;
			}
			else if(level>=10){
				highLevels++;
			}
			else mediumLevels++;
		}
		
//		System.out.println("high: "+highLevels+", medium: "+mediumLevels+", low: "+lowLevels);
		
		ArrayList<MapShipStrength> pool = new ArrayList<MapShip.MapShipStrength>();
		for(int i=lowLevels;i<4;i++){
			pool.add(MapShipStrength.Low);
		}
		for(int i=mediumLevels;i<4;i++){
			pool.add(MapShipStrength.Medium);
		}
		for(int i=highLevels;i<4;i++){
			pool.add(MapShipStrength.High);
		}
//		for(MapShipStrength str:pool)System.out.println(str);
		if(pool.size()==0)return;
		MapShipStrength result =  pool.get((int) (Math.random()*pool.size()));
		new MapShip(Ship.getShip(result, 0), hex);
//		3-4 5-6
//		7 - 11
//		 5/6/7 8/9 10/11
	}
}
