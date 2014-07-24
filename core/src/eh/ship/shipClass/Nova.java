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

public class Nova extends Ship{

	public Nova(boolean player) {
		super(player, Gallery.shipNova, Gallery.novaGenerator, Gallery.novaComputer);
	}

	@Override
	public void placeNiches() {
		
		Polygon weapon1= new Polygon(new float[]{330,196,0,0,0,0});
		Polygon weapon2= new Polygon(new float[]{330,76,0,0,0,0});
		Polygon shield = new Polygon(new float[]{175,225,0,0,0,0});
		Polygon generator = new Polygon(new float[]{
				78,182,
				108,182,
				108,90,
				78,90});
		Polygon computer = new Polygon(new float[]{
				201,148,
				215,170,
				240,170,
				258,148,
				258,120,
				240,102,
				215,102,
				201,120,				
				});

		niches[0].setup(weapon1);
		niches[1].setup(weapon2);
		niches[2].setup(shield);
		niches[3].setup(generator);
		niches[4].setup(computer);

		setWeapon(new Pulse(), 0);
		setWeapon(new Tesla(), 1);
		setShield(new Repeller());
		setGenerator(new Three());
		setComputer(new Beta());
	}
}
