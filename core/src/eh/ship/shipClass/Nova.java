package eh.ship.shipClass;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;

import eh.module.computer.Beta;
import eh.module.generator.Three;
import eh.module.shield.Repeller;
import eh.module.weapon.Pulse;
import eh.module.weapon.Tesla;
import eh.ship.Ship;
import eh.ship.mapThings.mapAbility.MapAbility;
import eh.ship.mapThings.mapAbility.genAbility.Teleport;
import eh.util.assets.Gallery;

public class Nova extends Ship{

	public Nova(boolean player) {
		super(player, Gallery.shipNova, Gallery.novaGenerator, Gallery.novaComputer);
	}

	@Override
	public void placeNiches() {
		
		Polygon weapon1= new Polygon(new float[]{330,196,0,0,0,0});
		Polygon weapon2= new Polygon(new float[]{330,76,0,0,0,0});
		Polygon shield = new Polygon(new float[]{175,45,0,0,0,0});
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

		setWeapon(new Pulse(0), 0);
		setWeapon(new Tesla(0), 1);
		setShield(new Repeller(0));
		setGenerator(new Three());
		setComputer(new Beta());
	}

	@Override
	public ArrayList<MapAbility> getMapAbilities() {
		ArrayList<MapAbility> result = new ArrayList<MapAbility>();
		result.add(new Teleport());
		return result;
	}
}
