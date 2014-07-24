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
		Polygon weapon1= new Polygon(new float[]{216,187,0,0,0,0});
		Polygon weapon2= new Polygon(new float[]{216,82,0,0,0,0});
		Polygon shield = new Polygon(new float[]{138,171,0,0,0,0});
		Polygon generator = new Polygon(new float[]{
				39,247,
				108,241,
				108,30,
				39,30});
		Polygon computer = new Polygon(new float[]{
				182,132,
				200,132,
				216,110,
				210,75,
				180,75			
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
