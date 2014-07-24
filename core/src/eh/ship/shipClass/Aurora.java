package eh.ship.shipClass;

import com.badlogic.gdx.math.Polygon;

import eh.assets.Gallery;
import eh.ship.Ship;
import eh.ship.module.computer.Alpha;
import eh.ship.module.generator.Three;
import eh.ship.module.shield.Deflector;
import eh.ship.module.shield.Repeller;
import eh.ship.module.weapon.Laser;
import eh.ship.module.weapon.Pulse;
import eh.ship.module.weapon.Ray;
import eh.ship.module.weapon.Tesla;

public class Aurora extends Ship{

	public Aurora(boolean player) {
		super(player, Gallery.shipAurora, Gallery.auroraGenerator, Gallery.auroraComputer);
	}

	@Override
	public void placeNiches() {
		Polygon weapon1= new Polygon(new float[]{306,157,0,0,0,0});
		Polygon weapon2= new Polygon(new float[]{260,214,0,0,0,0});
		Polygon shield = new Polygon(new float[]{280,80,0,0,0,0});
		Polygon generator = new Polygon(new float[]{
				187,152,
				227,139,
				237,117,
				205,117,
				183,139,
				});
		Polygon computer = new Polygon(new float[]{
				8,255,
				69,214,
				68,204,
				32,212,
				0,240
				});
		
		niches[0].setup(weapon1);
		niches[1].setup(weapon2);
		niches[2].setup(shield);
		niches[3].setup(generator);
		niches[4].setup(computer);

		setWeapon(new Ray(), 0);
		setWeapon(new Laser(), 1);
		setShield(new Repeller());
		setGenerator(new Three());
		setComputer(new Alpha());
	}

}
