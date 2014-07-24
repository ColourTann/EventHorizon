package eh.ship.mapThings;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eh.grid.hex.Hex;
import eh.screen.map.Map;
import eh.screen.map.Map.MapState;
import eh.ship.Ship;
import eh.ship.shipClass.Aurora;
import eh.util.Bonkject;
import eh.util.Bonkject.Timerr;
import eh.util.Junk;
import eh.util.maths.Sink;

public class MapShip {

	public Ship ship;
	private Timerr timer;


	Sink source = new Sink(0, 0);
	Sink destination = new Sink(0, 0);
	public Sink distance = new Sink(0, 0);
	public Hex hex;
	public ArrayList<Hex> path;

	public float rotation = 0;

	public MapShip(Ship ship, Hex hex) {
		this.ship = ship;
		this.hex = hex;
	}

	public MapShip(Hex hex) {
		this.hex = hex;
		source = hex.getPixel();
	}

	public void moveTo(Hex h) {
		if (isMoving()) return;
		source = hex.getPixel();
		destination = h.getPixel();
		timer = Bonkject.time(1/Map.phaseSpeed, null);
		rotation = source.getAngle(destination);
		if (hex.mapShip == this)hex.mapShip = null;
		h.addShip(this);
	}

	public boolean isMoving() {
		if (timer == null) 	return false;
		if (timer.timer <= .0)	return false;
		return true;
	}

	public void update(float delta) {
		if (ship == null) {
			ship = new Aurora(false);
			ship.hex = hex;
			source = hex.getPixel();
		}
		distance = new Sink(0, 0);
		if (timer == null)return;
		if (timer.timer <= 0) {
			timer = null;
			source = hex.getPixel();
			destination = null;
		}
		if (destination != null) {
			distance = source.subtract(destination);
			distance = distance.multiply((timer.timer/(1/Map.phaseSpeed))*(timer.timer/(1/Map.phaseSpeed)));
		}			
	}

	public void render(SpriteBatch batch) {
		if (ship == null)	return;
		batch.setColor(1, 1, 1, 1);
		Junk.drawTextureRotatedScaledCentered(batch, ship.shipPic.get(),
				(int) (hex.getPixel().x + distance.x),
				(int) (hex.getPixel().y + distance.y), Hex.size / 300,
				Hex.size / 300, rotation);
	}

	public void setPath(ArrayList<Hex> path) {
		this.path = path;
		if (path.size() > 0) moveTo(path.remove(0));
	}

	public void takeTurn() {
		moveTo(decideBest());
	}

	public Hex decideBest(){
		Hex best=hex;
		float value=-1;
		for(Hex h:hex.getHexesWithin(1, false)){
			if(h!=hex&&h.isBlocked())continue;
			float hexVal=h.howGood(this);
			if(hexVal>value){
				best=h;
				value=hexVal;
			}
		}
		return best;
	}
	

	
	public void playerStartTurn() {
		if (path != null && path.size() > 0) {
			Map.setState(MapState.PlayerMoving);
			moveTo(path.remove(0));
		}
	}

	public void resetPath() {
		path.clear();
	}
}
