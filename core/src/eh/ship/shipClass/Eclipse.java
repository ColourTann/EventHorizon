package eh.ship.shipClass;

import com.badlogic.gdx.math.Polygon;

import eh.assets.Gallery;
import eh.ship.Ship;
import eh.ship.module.computer.Alpha;
import eh.ship.module.computer.Gamma;
import eh.ship.module.generator.Five;
import eh.ship.module.generator.Three;
import eh.ship.module.shield.Deflector;
import eh.ship.module.shield.Repeller;
import eh.ship.module.shield.Repulsor;
import eh.ship.module.weapon.Laser;
import eh.ship.module.weapon.Pulse;
import eh.ship.module.weapon.Ray;
import eh.ship.module.weapon.Tesla;

public class Eclipse extends Ship{

	public Eclipse(boolean player) {
		super(player,Gallery.shipEclipse, Gallery.eclipseGenerator, Gallery.eclipseComputer);
	}

	@Override
	public void placeNiches() {
		Polygon weapon1= new Polygon(new float[]{280,245,0,0,0,0});
		Polygon weapon2= new Polygon(new float[]{335,40,0,0,0,0});
		Polygon shield = new Polygon(new float[]{122,161,0,0,0,0});
		Polygon generator = new Polygon(new float[]{
				120,117,
				183,100,
				176,65,
				105,85});
		Polygon computer = new Polygon(new float[]{
				192,132,
				220,150,
				243,130,
				228,95});

		niches[0].setup(weapon1);
		niches[1].setup(weapon2);
		niches[2].setup(shield);
		niches[3].setup(generator);
		niches[4].setup(computer);

		setWeapon(new Tesla(), 0);
		setWeapon(new Laser(), 1);
		setShield(new  Deflector());
		setGenerator(new Five());
		setComputer(new Gamma());
	}
}
