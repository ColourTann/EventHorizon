package game.attack;

import game.screen.test.Rocket;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.maths.Pair;

public class RocketAttack extends AttackGraphic{
	Rocket rocket;
	public RocketAttack(Pair origin) {
		super(origin);
	}

	@Override
	public void fire(Pair target) {
		//rocket=new Rocket(origin.copy(), 5);
	}

	@Override
	public void impact() {
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch) {
	}

	@Override
	public boolean finishedAttacking() {
		return false;
	}

}
