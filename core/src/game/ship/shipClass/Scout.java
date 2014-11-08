package game.ship.shipClass;

import game.assets.Gallery;
import game.module.component.computer.Alpha;
import game.module.component.computer.Gamma;
import game.module.component.generator.Four;
import game.module.component.generator.Two;
import game.module.component.shield.Deflector;
import game.module.component.weapon.Laser;
import game.module.component.weapon.Pulse;
import game.module.component.weapon.Ray;
import game.module.component.weapon.Tesla;
import game.ship.Ship;
import game.ship.mapThings.mapAbility.MapAbility;
import game.ship.mapThings.mapAbility.genAbility.DoubleMove;
import game.ship.mapThings.mapAbility.genAbility.Teleport;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;

public class Scout extends Ship{

	public Scout(boolean player, float tier) {
		super(player, tier, "Scout", Gallery.shipScout, Gallery.scoutGenerator, Gallery.scoutComputer);
	}

	@Override
	public void placeNiches() {
		Polygon weapon1= new Polygon(new float[]{216,83,0,0,0,0});
		Polygon weapon2= new Polygon(new float[]{216,188,0,0,0,0});
		Polygon shield = new Polygon(new float[]{138,99,0,0,0,0});
		Polygon generator = new Polygon(new float[]{
				39,24,
				108,29,
				108,240,
				39,240});
		Polygon computer = new Polygon(new float[]{
				182,138,
				200,138,
				216,160,
				210,195,
				180,195			
				});

		niches[0].setup(weapon1);
		niches[1].setup(weapon2);
		niches[2].setup(shield);
		niches[3].setup(generator);
		niches[4].setup(computer);

		setWeapon(new Laser(0), 0);
		setWeapon(new Pulse(0), 1);
		setShield(new Deflector(0));
		setGenerator(new Two());
		setComputer(new Alpha());
	}

	@Override
	public ArrayList<MapAbility> getMapAbilities() {
		ArrayList<MapAbility> result = new ArrayList<MapAbility>();
		result.add(new Teleport(5,5,.02f));
		result.add(new DoubleMove(3,1,.1f));
		
		return result;
	}
}
