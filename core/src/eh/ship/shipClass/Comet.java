package eh.ship.shipClass;

import com.badlogic.gdx.math.Polygon;

import eh.assets.Gallery;
import eh.ship.Ship;
import eh.ship.module.computer.Alpha;
import eh.ship.module.computer.Beta;
import eh.ship.module.computer.Gamma;
import eh.ship.module.generator.Five;
import eh.ship.module.generator.Four;
import eh.ship.module.generator.Three;
import eh.ship.module.shield.Deflector;
import eh.ship.module.shield.Repeller;
import eh.ship.module.shield.Repulsor;
import eh.ship.module.weapon.Laser;
import eh.ship.module.weapon.Pulse;
import eh.ship.module.weapon.Ray;
import eh.ship.module.weapon.Tesla;

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

		setWeapon(new Ray(), 0);
		setWeapon(new Tesla(), 1);
		setShield(new Deflector());
		setGenerator(new Four());
		setComputer(new Gamma());
	}
}
