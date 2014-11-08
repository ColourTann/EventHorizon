package game.ship.shipClass;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;

import game.assets.Gallery;
import game.module.component.computer.Alpha;
import game.module.component.generator.Three;
import game.module.component.shield.Blocker;
import game.module.component.shield.Deflector;
import game.module.component.shield.Repeller;
import game.module.component.shield.Repulsor;
import game.module.component.weapon.Laser;
import game.module.component.weapon.Ray;
import game.ship.Ship;
import game.ship.mapThings.mapAbility.MapAbility;
import game.ship.mapThings.mapAbility.comAbility.Cloak;
import game.ship.mapThings.mapAbility.genAbility.DiagonalMove;
import game.ship.mapThings.mapAbility.genAbility.Teleport;

public class Aurora extends Ship{

	public Aurora(boolean player, float tier) {
		super(player, tier, "Aurora", Gallery.shipAurora, Gallery.auroraGenerator, Gallery.auroraComputer);
	}

	@Override
	public void placeNiches() {
		Polygon weapon1= new Polygon(new float[]{306,125,0,0,0,0});
		Polygon weapon2= new Polygon(new float[]{260,69,0,0,0,0});
		Polygon shield = new Polygon(new float[]{280,194,0,0,0,0});
		Polygon generator = new Polygon(new float[]{
				187,129,
				227,135,
				237,150,
				205,150,
				183,135,
				});
		Polygon computer = new Polygon(new float[]{
				8,30,
				69,68,
				68,78,
				32,70,
				0,32
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
		result.add(new Teleport(5,5,.02f));
		result.add(new DiagonalMove(3, 2, .01f));
		if(player){
			result.add(new Cloak());
		}
		return result;
	}
}
