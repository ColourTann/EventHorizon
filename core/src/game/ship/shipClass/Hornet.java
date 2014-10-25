package game.ship.shipClass;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;

import game.assets.Gallery;
import game.module.component.computer.Alpha;
import game.module.component.computer.Gamma;
import game.module.component.generator.Five;
import game.module.component.generator.Four;
import game.module.component.shield.Blocker;
import game.module.component.shield.Deflector;
import game.module.component.shield.Repulsor;
import game.module.component.weapon.Ray;
import game.module.component.weapon.Swift;
import game.module.component.weapon.Tesla;
import game.ship.Ship;
import game.ship.mapThings.mapAbility.MapAbility;

public class Hornet extends Ship{

	public Hornet(boolean player, float tier) {
		super(player, tier, "Hornet", Gallery.shipHornet, Gallery.hornetGenerator, Gallery.hornetComputer);
	}

	@Override
	public void placeNiches() {
		//Polygon weapon1= new Polygon(new float[]{58*3,15*3,0,0,0,0});
		Polygon weapon1= new Polygon(new float[]{174,15*3,0,0,0,0});
		Polygon weapon2= new Polygon(new float[]{58*3,75*3,0,0,0,0});
		Polygon shield = new Polygon(new float[]{84*3,46*3,0,0,0,0});
		Polygon generator = new Polygon(new float[]{
				59*3,29*3,
				77*3,29*3,
				99*3,61*3,
				59*3,61*3});
		Polygon computer = new Polygon(new float[]{
				30*3,36*3,
				60*3,36*3,
				60*3,62*3,
				30*3,62*3			
				});

		niches[0].setup(weapon1);
		niches[1].setup(weapon2);
		niches[2].setup(shield);
		niches[3].setup(generator);
		niches[4].setup(computer);

		setWeapon(new Swift(0), 0);
		setWeapon(new Ray(0), 1);
		setShield(new Repulsor(0));

		setGenerator(new Five());
		setComputer(new Alpha());
	}

	@Override
	public ArrayList<MapAbility> getMapAbilities() {
		return null;
	}

}
