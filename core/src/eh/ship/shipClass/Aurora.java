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
		Polygon weapon1= new Polygon(new float[]{306,113,0,0,0,0});
		Polygon weapon2= new Polygon(new float[]{260,56,0,0,0,0});
		Polygon shield = new Polygon(new float[]{280,190,0,0,0,0});
		Polygon generator = new Polygon(new float[]{
				187,118,
				227,131,
				237,153,
				205,153,
				183,131,
				});
		Polygon computer = new Polygon(new float[]{
				8,18,
				69,56,
				68,66,
				32,58,
				0,20
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
