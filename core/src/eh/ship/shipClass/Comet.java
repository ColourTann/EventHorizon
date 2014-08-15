package eh.ship.shipClass;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;

import eh.module.computer.Alpha;
import eh.module.computer.Beta;
import eh.module.computer.Gamma;
import eh.module.generator.Five;
import eh.module.generator.Four;
import eh.module.generator.Three;
import eh.module.shield.Deflector;
import eh.module.shield.Repeller;
import eh.module.shield.Repulsor;
import eh.module.weapon.Laser;
import eh.module.weapon.Pulse;
import eh.module.weapon.Ray;
import eh.module.weapon.Tesla;
import eh.ship.Ship;
import eh.ship.mapThings.mapAbility.MapAbility;
import eh.ship.mapThings.mapAbility.genAbility.Teleport;
import eh.util.assets.Gallery;

public class Comet extends Ship{

	public Comet(boolean player) {
		super(player, Gallery.shipComet, Gallery.cometGenerator, Gallery.cometComputer);
	}

	@Override
	public void placeNiches() {
		Polygon weapon1= new Polygon(new float[]{216,83,0,0,0,0});
		Polygon weapon2= new Polygon(new float[]{216,188,0,0,0,0});
		Polygon shield = new Polygon(new float[]{138,99,0,0,0,0});
		Polygon generator = new Polygon(new float[]{
				39,23,
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

		setWeapon(new Ray(0), 0);
		setWeapon(new Tesla(0), 1);
		setShield(new Deflector(0));
		setGenerator(new Four());
		setComputer(new Gamma());
	}

	@Override
	public ArrayList<MapAbility> getMapAbilities() {
		ArrayList<MapAbility> result = new ArrayList<MapAbility>();
		result.add(new Teleport());
		return result;
	}
}
