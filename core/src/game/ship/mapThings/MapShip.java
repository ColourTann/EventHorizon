package game.ship.mapThings;

import java.util.ArrayList;

import util.Draw;
import util.maths.Pair;
import util.update.Timer;
import util.update.Timer.Interp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.grid.hex.Hex;
import game.grid.hex.HexChoice;
import game.screen.map.Map;
import game.screen.map.Map.MapState;
import game.ship.Ship;
import game.ship.mapThings.mapAbility.MapAbility;
import game.ship.shipClass.Aurora;
import game.ship.shipClass.Eclipse;
import game.ship.shipClass.Scout;

public class MapShip {

	public Ship ship;
	private Timer timer;


	Pair source = new Pair(0, 0);
	Pair destination = new Pair(0, 0);
	public Pair distance = new Pair(0, 0);
	public Hex hex;
	public ArrayList<Hex> path= new ArrayList<Hex>();
	public float rotation = 0;
	public ArrayList<MapAbility> mapAbilities = new ArrayList<MapAbility>();
	int stunTime=0;
	public Timer stretch=new Timer();
	

	public MapShip(Ship ship, Hex hex) {
		init(ship);
		hex.addShip(this);
	}

	public MapShip(Hex hex) {
		hex.addShip(this);
	}

	public void init() {
		Ship s=null;
		if(Math.random()>.5)s=new Scout(false, 0);
		else s=new Eclipse(false, 0);
		init(s);
	}
	
	public void init(Ship ship){
		setShip(ship);
		ArrayList<MapAbility> abilities=ship.getMapAbilities();
		for(MapAbility a:abilities)a.mapShip=this;
		mapAbilities=abilities;
	}
	
	private void setShip(Ship ship) {
		this.ship = ship;
		ship.setMapShip(this);
	}
	
	public void playerStartTurn() {
		Map.incrementExplosionSize();
		if (path != null && path.size() > 0) {
			Map.setState(MapState.PlayerMoving);
			moveTo(path.remove(path.size()-1));
		}
		else path=null;
		
	}
	
	public void playerAfterMove(){
		tickMapAbilities();
	}

	public void tickMapAbilities(){
		for(MapAbility ma:mapAbilities)ma.turn();
	}
	
	public void takeAITurn() {
		if(stunTime>0){
			stunTime--;
			return;
		}
		if(ship==null)init();
		
		HexChoice best=getBestRegular();
		
		for(MapAbility ma:mapAbilities){
			if(!ma.isUsable())continue;
			HexChoice abilChoice=ma.getBestTarget();
			if(abilChoice.isBetterThan(best))	best=abilChoice;
		}
		
		boolean moved=false;
		
		if(best.source==null){
			moveTo(best.hex);
			moved=true;
		}
		if(!moved){
		best.source.use();
		best.source.pickHex(best.hex);
		}
		
		tickMapAbilities();
	}

	public HexChoice getBestRegular(){
		Hex best=hex;
		float value=hex.howGood(this)-.1f;
		for(Hex h:hex.getHexesWithin(1, false)){
			if(h!=hex&&h.isBlocked(false))continue;
			float hexVal=h.howGood(this);
			if(hexVal>value){
				best=h;
				value=hexVal;
			}
		}
		return new HexChoice(best, value);
	}

	
	public void moveTo(Hex h) {
		if (isMoving()) return;
		if(ship.player)h.highlight=false;
		source = hex.getPixel();
		destination = h.getPixel();
		timer = new Timer(1, 0, 1/Map.phaseSpeed, Interp.SQUARE);
		rotation = source.getAngle(destination);
		if (hex.mapShip == this)hex.mapShip = null;
		h.addShip(this);
	}

	public boolean isMoving() {
		if (timer == null) 	return false;
		if (timer.getFloat() <= .0)	return false;
		return true;
	}

	public void update(float delta) {
		if (ship == null) {
			init();
			source = hex.getPixel();
		}
		distance = new Pair(0, 0);
		if (timer == null)return;
		if (timer.getFloat() <= 0) {
			timer = null;
			source = hex.getPixel();
			destination = null;
		}
		if (destination != null) {
			distance = source.subtract(destination);
			distance = distance.multiply(timer.getFloat());
		}			
	}

	public void render(SpriteBatch batch) {
		if (ship == null)	return;
		
		batch.setColor(1-stretch.getFloat(), 1-stretch.getFloat()/2, 1, 1);
		Draw.drawCenteredRotatedScaled(batch, ship.shipPic.get(),
				(int) (hex.getPixel().x + distance.x),
				(int) (hex.getPixel().y + distance.y), 
				(Hex.size / 300)*(1+stretch.getFloat()/1.3f),
				(Hex.size / 300)*(1-stretch.getFloat()/1.3f), 
				rotation);
	}

	public void setPath(ArrayList<Hex> path) {
		this.path = path;
		if (path.size() > 0) moveTo(path.remove(path.size()-1));
	}
	
	public void resetPath() {
		if(path==null)return;
		for(Hex h:path){
			h.highlight=false;
		}
		path.clear();
	}

	public float getPowerLevel() {
		return ship.getStats().power;
	}

	public void stun(int i) {
		stunTime+=i;
	}
}
