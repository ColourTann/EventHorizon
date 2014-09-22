package game.screen.preBattle;

import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.card.CardGraphic;
import game.module.Module;
import game.module.component.Component;
import game.screen.battle.Battle;
import game.ship.Ship;
import game.ship.ShipGraphic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.Screen;
import util.update.Timer;
import util.update.Timer.Finisher;
import util.update.Timer.Interp;

public class PreBattle extends Screen{
	float shakeAmplitude;
	float shakeFrequency=70;
	private double shakeDrag=.01f;
	float ticks;
	Ship player;
	Ship enemy;

	Timer playerLocation=new Timer();
	Timer enemyLocation=new Timer();
	Timer flashTimer=new Timer();
	Timer dialTimer=new Timer();
	boolean flashed;
	float powerRatio;
	Pair difficultyStart=new Pair(Main.width/2-Gallery.difficultyDial.getWidth()*3/2, 10);

	public PreBattle(Ship player, Ship enemy){
		this.player=player;
		this.enemy=enemy;
	}

	@Override
	public void init() {
		playerLocation=new Timer(
				new Pair(-ShipGraphic.width, ShipGraphic.offset.y),
				new Pair(ShipGraphic.offset.x, ShipGraphic.offset.y),
				.5f,
				Interp.LINEAR);
		enemyLocation=new Timer(
				new Pair(Main.width, ShipGraphic.offset.y),
				new Pair(Main.width-Battle.viewport.x-ShipGraphic.offset.x, ShipGraphic.offset.y),
				.5f,
				Interp.LINEAR);
		enemyLocation.addFinisher(new Finisher() {

			@Override
			public void finish() {
				flashTimer=new Timer(1,0,.8f, Interp.LINEAR);
				flashed=true;

				float totalPower=player.getStats().power+enemy.getStats().power;
				powerRatio=1-(player.getStats().power/totalPower);
				System.out.println(powerRatio);
				powerRatio-=.5f;
				powerRatio*=3f;
				dialTimer=new Timer(0, powerRatio, .8f, Interp.SQUARE);
				shake(15);
				Sounds.preBattleImpact.play();
			}
		});


	}

	public void shake(float amount){
		shakeAmplitude=amount;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void update(float delta) {
		ticks+=delta;
		shakeAmplitude*=Math.pow(shakeDrag, delta);
		Main.setCam(new Pair((int)Main.width/2+Math.sin(ticks*shakeFrequency)*shakeAmplitude,(int)Main.height/2));
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(1,1,1,1);
		if(flashed){

			for(Component  c:player.components){
				c.getStats().render(batch);
			}
			for(Component  c:enemy.components){
				c.getStats().render(batch);
			}

			Font.drawFontCentered(batch, "FIGHT!", Font.big, Main.width/2, 500);
			Font.drawFontCentered(batch, "Easy", Font.big, 270, 35);
			Font.drawFontCentered(batch, "Hard", Font.big, Main.width-270, 35);

			Draw.drawScaled(batch, Gallery.difficultyDial.get(), difficultyStart.x, difficultyStart.y, 3, 3);
			Draw.drawScaled(batch, Gallery.difficultyMeter.get(), 
					(int)(Main.width/2-Gallery.difficultyMeter.getWidth()*3/2+
							dialTimer.getFloat()*Gallery.difficultyDial.getWidth()*3) ,
							difficultyStart.y, 3, 3);
		}
		Draw.draw(batch, player.getGraphic().composite.get(), playerLocation.getPair().x, playerLocation.getPair().y);
		Draw.drawRotatedScaledFlipped(batch, enemy.getGraphic().composite.get(), enemyLocation.getPair().x, enemyLocation.getPair().y, 1, 1, 0, true, false);
		batch.setColor(Colours.withAlpha(Colours.enemy2[1], flashTimer.getFloat()));
		Draw.drawScaled(batch, Gallery.whiteSquare.get(), 0, 0, Main.width, Main.height);

	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void keyPress(int keycode) {
		Main.changeScreen(new Battle(player, enemy, false));
	}

	@Override
	public void keyUp(int keyCode) {
	}

	@Override
	public void mousePressed(Pair location, boolean left) {
	}

	@Override
	public void scroll(int amount) {
	}

}
