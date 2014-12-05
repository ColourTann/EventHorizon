package game.screen.preBattle;

import java.util.ArrayList;

import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.card.Card;
import game.card.CardGraphic;
import game.module.component.Component;
import game.module.junk.ModuleInfo;
import game.module.junk.ModuleStats;
import game.screen.battle.Battle;
import game.screen.battle.Battle.BattleType;
import game.screen.customise.Customise;
import game.ship.Ship;
import game.ship.ShipGraphic;
import util.update.SimpleButton.Code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.Screen;
import util.update.SimpleButton;
import util.update.Timer;
import util.update.Timer.Finisher;
import util.update.Timer.Interp;

public class PreBattle extends Screen{
	public static CardGraphic mousedGraphic;
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
	SimpleButton fightButton;
	ArrayList<Card> consumables;
	
	ModuleInfo slotInfo;
	static PreBattle me;
	
	BattleType type;
	public PreBattle(Ship player, Ship enemy, BattleType type){
		this.player=player;
		this.enemy=enemy;
		this.type=type;
	}

	@Override
	public void init() {
	player.resetGraphics();
	enemy.resetGraphics();
	for(Component  c:player.components){
		c.getStats().moveToDefaultPosition();
	}
		flashTimer=new Timer();
		
		me=this;
		mousedGraphic=null;
		consumables=player.getConsumables();

		playerLocation=new Timer(
				new Pair(-ShipGraphic.width, ShipGraphic.offset.y),
				new Pair(ShipGraphic.offset.x, ShipGraphic.offset.y),
				.5f,
				Interp.LINEAR);
		enemyLocation=new Timer(
				new Pair(Main.width, ShipGraphic.offset.y),
				new Pair(Main.width-enemy.getGraphic().composite.getWidth()-ShipGraphic.offset.x, ShipGraphic.offset.y),
				.5f,
				Interp.LINEAR);
		enemyLocation.addFinisher(new Finisher() {

			@Override
			public void finish() {
				flashTimer=new Timer(1,0,.8f, Interp.LINEAR);
				flashed=true;

				float totalPower=player.getStats().power+enemy.getStats().power;
				System.out.println(player.getStats().power/totalPower);
				powerRatio=1-(player.getStats().power/totalPower);
				
				powerRatio-=.5f;
				powerRatio*=3f;
				dialTimer=new Timer(0, powerRatio, .8f, Interp.SQUARE);
				shake(15);
				Sounds.preBattleImpact.play();




				float start=300;
				float width=Main.width-start*2;

				float y=Main.height-CardGraphic.height;


				for(Card c:player.getConsumables()){
					c.getGraphic().activate();
					c.getGraphic().mousectivate(null);
					c.getGraphic().setPosition(new Pair(Main.width/2, Main.height));
				}


				if(width>CardGraphic.width*player.getConsumables().size()){

					float gap=(width-player.getConsumables().size()*CardGraphic.width)/(player.getConsumables().size()+1);
					for(int i=0;i<player.getConsumables().size();i++){
						CardGraphic c=player.getConsumables().get(i).getGraphic();
						c.override=true;
						c.slide(new Pair(start+gap*(i+1)+CardGraphic.width*i, y), .5f, Interp.SQUARE);
						c.finishFlipping();
					}
				}
				else{
					float funnyWidth=width-CardGraphic.width;
					float gap=funnyWidth/(player.getConsumables().size()-1);
					for(int i=0;i<player.getConsumables().size();i++){
						CardGraphic c=player.getConsumables().get(i).getGraphic();
						
						c.slide(new Pair(start+gap*i, y), .5f, Interp.SQUARE);
						c.finishFlipping();
					}
				}
			}
		});


		fightButton=new SimpleButton(new Pair(Main.width/2-Gallery.fightButton.getWidth()/2, 200), "Fight!", Gallery.fightButton, new Code() {

			@Override
			public void onPress() {
				Sounds.bigAccept.overlay();
				Main.changeScreen(new Battle(player, enemy, type));
			}
		});
		fightButton.font=Font.big;

	}

	private void resetJunk() {

	}

	private void deactivateJunk(){
	enemy.resetGraphics();
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
		Main.setCam(new Pair(Math.round(Main.width/2+Math.sin(ticks*shakeFrequency)*shakeAmplitude),Math.round(Main.height/2)));
	
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(1,1,1,1);


		Draw.draw(batch, player.getGraphic().composite.get(), playerLocation.getPair().x, playerLocation.getPair().y);
		Draw.drawRotatedScaledFlipped(batch, enemy.getGraphic().composite.get(), enemyLocation.getPair().x, enemyLocation.getPair().y, 1, 1, 0, true, false);


		if(flashed){
			batch.setColor(1,1,1,1);

			Font.drawFontCentered(batch, "FIGHT!", Font.big, Main.width/2, 222);


			Font.drawFontCentered(batch, "Easy", Font.big, 270, 35);
			Font.drawFontCentered(batch, "Hard", Font.big, Main.width-270, 35);

			Draw.drawScaled(batch, Gallery.difficultyDial.get(), difficultyStart.x, difficultyStart.y, 3, 3);
		
			Draw.drawScaled(batch, Gallery.difficultyMeter.get(), 
					(int)(Main.width/2-Gallery.difficultyMeter.getWidth()*3/2+
							dialTimer.getFloat()*Gallery.difficultyDial.getWidth()*3) ,
							difficultyStart.y, 3, 3);

			
			if(ModuleInfo.top!=null)ModuleInfo.top.render(batch);
			for(Component  c:player.components){
				c.getStats().render(batch);
			}
			for(Component  c:enemy.components){
				c.getStats().render(batch);
			}
			for(ModuleStats ms:player.getUtilityStats())ms.render(batch);
			for(ModuleStats ms:enemy.getUtilityStats())ms.render(batch);

			Font.medium.setColor(Colours.light);
			Font.drawFontCentered(batch, (player.getConsumables().size()>0?"Select consumable cards to use for this fight":"No consumable cards available"), Font.medium, Main.width/2, 400);
			for(Card c: consumables){
				c.getGraphic().render(batch);
			}

			fightButton.render(batch);
			if(mousedGraphic!=null)mousedGraphic.render(batch);
			
			//if(slotInfo!=null)slotInfo.render(batch);
		}
		batch.setColor(Colours.withAlpha(Colours.enemy2[1], flashTimer.getFloat()));
		Draw.drawScaled(batch, Gallery.whiteSquare.get(), 0, 0, Main.width, Main.height);
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public boolean keyPress(int keycode) {
		if(Main.debug){
			deactivateJunk();
			enemy=Customise.makeEnemyShip();
			resetJunk();
		}
		return false;
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
