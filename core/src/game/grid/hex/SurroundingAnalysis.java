package game.grid.hex;

import game.ship.mapThings.MapShip;

import java.util.ArrayList;

public class SurroundingAnalysis {
	ArrayList<ShipDist> ships= new ArrayList<SurroundingAnalysis.ShipDist>();
	float furthestDistance=-1;
	public void setDistance(float distanceFromExplosion) {
		if(distanceFromExplosion>furthestDistance)furthestDistance=distanceFromExplosion;
	}		
	public void addShip(MapShip ship, int dist){
		ships.add(new ShipDist(ship, dist));
	}
	public String toString(){
		for(ShipDist sd:ships){
			System.out.println(sd);
		}
		return "Best distance: "+furthestDistance+", Ships: "+ships.size();
		
	}
	static class ShipDist{
		MapShip ship;
		int dist;
		public ShipDist(MapShip ship, int dist){
			this.ship=ship;
			this.dist=dist;
		}
		public String toString(){
			return ship+", dist: "+dist;
		}
	}

}

