package game.ship.shipClass;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;

import game.assets.Gallery;
import game.module.component.computer.Alpha;
import game.module.component.generator.Three;
import game.module.component.shield.Repeller;
import game.module.component.weapon.Laser;
import game.module.component.weapon.Pulse;
import game.module.component.weapon.Ray;
import game.ship.Ship;
import game.ship.mapThings.mapAbility.MapAbility;
import game.ship.mapThings.mapAbility.genAbility.Teleport;

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

		setWeapon(new Ray(0), 0);
		setWeapon(new Laser(0), 1);
		setShield(new Repeller(0));
		setGenerator(new Three());
		setComputer(new Alpha());
	}

	@Override
	public ArrayList<MapAbility> getMapAbilities() {
		ArrayList<MapAbility> result = new ArrayList<MapAbility>();
		result.add(new Teleport());
		return result;
	}
}
