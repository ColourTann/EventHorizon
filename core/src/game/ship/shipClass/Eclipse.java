package game.ship.shipClass;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;

import game.assets.Gallery;
import game.module.computer.Gamma;
import game.module.generator.Five;
import game.module.shield.Deflector;
import game.module.weapon.Laser;
import game.module.weapon.Tesla;
import game.ship.Ship;
import game.ship.mapThings.mapAbility.MapAbility;
import game.ship.mapThings.mapAbility.genAbility.Teleport;

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

		setWeapon(new Tesla(0), 0);
		setWeapon(new Laser(0), 1);
		setShield(new  Deflector(0));
		setGenerator(new Five());
		setComputer(new Gamma());
	}

	@Override
	public ArrayList<MapAbility> getMapAbilities() {
		ArrayList<MapAbility> result = new ArrayList<MapAbility>();
		result.add(new Teleport());
		return result;
	}
}
