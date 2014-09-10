package game.screen.battle;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.Noise;
import util.assets.SoundClip;
import util.assets.Font;
import util.maths.Pair;
import util.particleSystem.ParticleSystem;
import util.update.Animation;
import util.update.Mouser;
import util.update.Screen;
import util.update.TextWisp;
import util.update.Timer;
import util.update.Timer.Interp;
import util.update.Updater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import game.Main;
import game.Main.ScreenType;
import game.assets.Gallery;
import game.card.Card;
import game.card.CardCode;
import game.card.CardGraphic;
import game.card.CardIcon;
import game.card.CardCode.Special;
import game.module.Module;
import game.module.utils.DamagePoint;
import game.module.utils.ModuleInfo;
import game.screen.battle.interfaceJunk.CycleButton;
import game.screen.battle.interfaceJunk.HelpPanel;
import game.screen.battle.interfaceJunk.PhaseButton;
import game.screen.battle.interfaceJunk.Star;
import game.screen.battle.tutorial.Tutorial;
import game.screen.battle.tutorial.UndoButton;
import game.screen.battle.tutorial.Tutorial.Trigger;
import game.screen.menu.Selector;
import game.ship.Ship;
import game.ship.niche.Niche;
import game.ship.shipClass.Aurora;
import game.ship.shipClass.Comet;
import game.ship.shipClass.Eclipse;
import game.ship.shipClass.Nova;
import game.utilitySystem.armour.BasicArmour;

public class Battle extends Screen{
	public enum Phase{ShieldPhase, EnemyWeaponsFiring, WeaponPhase, EnemyShieldPhase, PlayerWeaponsFiring, EnemyWeaponPhase, End};
	public enum State{Nothing, Augmenting, Targeting, CycleDiscard, CycleGet, ModuleChoose}
	private static Phase currentPhase=Phase.WeaponPhase;
	private static State currentState=State.Nothing;
	public static Card augmentSource;
	public static Card targetSource;
	public static Card moduleChooser;
	public Ship player;
	public Ship enemy;

	public static HelpPanel help;
	float debug;
	static Ship victor;
	public static float ticks;
	static Pair playerKnockBack=new Pair();
	static Pair playerKnockBackTarget=new Pair();

	static Pair enemyKnockBack=new Pair();
	static Pair enemyKnockBackTarget=new Pair();

	public boolean tutorial=false;
	private ArrayList<CardGraphic> enemyHandList=new ArrayList<CardGraphic>();
	ArrayList<TextWisp> wisps = new ArrayList<TextWisp>();

	public static int dividerWidth=12;
	public static Pair viewport=new Pair(480-dividerWidth/2, 340);

	public static OrthographicCamera playerCam=new OrthographicCamera();
	public static OrthographicCamera enemyCam=new OrthographicCamera();

	public static Pair basePlayerCamPosition= new Pair(Main.width/2-480+viewport.x/2, 80+viewport.y/2);
	public static Pair baseEnemyCamPosition= new Pair(Main.width/2+500+dividerWidth/2+viewport.x/2, 80+viewport.y/2);

	public static Pair playerBonus= new Pair();
	public static Pair enemyBonus= new Pair();

	public static float enemyShakeIntensity=0;
	public static float playerShakeIntensity=0;
	float shakeDrag=.005f;
	private static Battle me;
	ArrayList<Animation> animations=new ArrayList<Animation>();
	float animTicker=0;
	private ScreenType type;
	
	static Timer victoryFadeInTimer=new Timer();
	
	public Battle(Ship player, Ship enemy, boolean tutorial){
		this.player=player;
		this.enemy=enemy;
		this.tutorial=tutorial;

	}

	@Override
	public void init() {
		me=this;
		playerCam.setToOrtho(true, viewport.x, viewport.y);
		enemyCam.setToOrtho(true, viewport.x, viewport.y);
		resetStatics();
		Star.init();
		if(tutorial)initTutorial();
		player.startFight(true);
		enemy.startFight(false);
		if(tutorial){
			player.addEnergy(6);
			enemy.addEnergy(2);
		}
		player.setArmour(new BasicArmour(1.9f));
	}

	public static Ship getPlayer(){
		return me.player;
	}

	public static Ship getEnemy(){
		return me.enemy;
	}

	public static boolean isTutorial(){
		return me.tutorial;
	}

	private void resetStatics() {
		TextWisp.wisps.clear();
		currentState=State.Nothing;
		currentPhase=Phase.WeaponPhase;
		augmentSource=null;
		targetSource=null;
		moduleChooser=null;
		ticks=0;
		victor=null;
		Tutorial.overrideStopClick=false;
		Tutorial.overrideStopCycle=false;
		Tutorial.overrideStopFlip=false;
		Tutorial.overrideStopEnd=false;
		Tutorial.player=null;
		Tutorial.enemy=null;
		Tutorial.three.clear();
		Tutorial.tutorials.clear();
		Tutorial.currentList=null;
		Tutorial.glows.clear();
		Tutorial.index=0;
		CycleButton.button=null;
		CycleButton.choices.clear();;
		PhaseButton.button=null;
		Card.extraCardsToRender.clear();
		CardIcon.icons.clear();
		CardGraphic.augmentPicker=null;
		help=null;
		victoryFadeInTimer=new Timer();
	}

	private void initTutorial() {
		CardCode code = new CardCode();
		code.add(Special.AddShieldPoints);
		enemy.drawCard(enemy.getModule(0).getCard(5));
		tutorial=true;
		Tutorial.init();
		currentPhase=Phase.EnemyWeaponPhase;
	}

	public static void debug(){



		/*player.analyseDeck();
		enemy.analyseDeck();*/
		/*	player.discardHand();
		player.drawToMaximum();*/

		/*for(Module m:enemy.modules){
		System.out.println(m.getDamageUntilMajor());
	}*/


	}
	public static void zSet(Phase s){
		currentPhase=s;
	}

	public static void setPhase(Phase s){
		if(getPhase()==Phase.End)return;
		currentPhase=s;
		getEnemy().checkDefeat();
		getPlayer().checkDefeat();
		
		

		switch(s){
		case EnemyShieldPhase:
			getPlayer().notifyIncoming();
			getEnemy().enemyStartPhase();
			break;
		case EnemyWeaponPhase:
			PhaseButton.get().nextPhase();
			getEnemy().checkDefeat();
			getEnemy().enemyStartPhase();
			break;
		case EnemyWeaponsFiring:
			getEnemy().fireAll();
			break;
		case PlayerWeaponsFiring:
			getPlayer().fireAll();
			break;
		case ShieldPhase:
			new TextWisp("Shield Phase", Font.big, new Pair(Main.width/2,330), TextWisp.WispType.Regular);
			PhaseButton.get().nextPhase();
			getEnemy().enemyEndTurn();
			getPlayer().playerStartTurn();
			break;
		case WeaponPhase:
			new TextWisp("Weapon Phase", Font.big, new Pair(Main.width/2,330), TextWisp.WispType.Regular);
			break;
		default:
			break;

		}
	}

	public static void turnButtonClicked(){
		if(Tutorial.stopEnd())return;
		if(!isPlayerTurn())return;
		if(getState()!=State.Nothing)return;
		if(getPlayer().hasSpendableShields())return;
		getPlayer().playCards();

		if(getPhase()==Phase.ShieldPhase){
			getPlayer().endPhase();
			PhaseButton.button.nextPhase();
		}
		else{
			getPlayer().playerEndTurn();
			getPlayer().endPhase();
			PhaseButton.button.nextPhase();
		}
	}

	public static void battleWon(Ship ship) {
		ship.getEnemy().dead=true;
		ship.getEnemy().getGraphic().destroy();
		setPhase(Phase.End);
		System.out.println("ended");
		victor=ship;
		victoryFadeInTimer=new Timer(0,1,5,Interp.LINEAR);
	}





	public void debugRender(SpriteBatch batch){
	}


	private void drawInterfaceOverlay(SpriteBatch batch){
		PhaseButton.get().render(batch);
		CycleButton.get().render(batch);
	}

	@Override
	public void keyPress(int keyCode) {
		switch (keyCode){

		case Input.Keys.SPACE:
			if(Main.debug){player.drawCard(1); player.addEnergy(1);
			}
			break;

			//case Input.Keys.F:player=Ship.getRandomShip(true);enemy=Ship.getRandomShip(false);break;
		case Input.Keys.D:
			

			break;
		case Input.Keys.Q:

			//show enemy cards//
			if(Main.debug){
				for(int i=0;i<enemy.hand.size();i++){
					CardGraphic cg=new CardGraphic(enemy.hand.get(i));
					cg.override=true;
					enemyHandList.add(cg);
					cg.setPosition(new Pair(i*CardGraphic.width,Main.height-CardGraphic.height));
				}
			}
			break;


		case Input.Keys.CONTROL_LEFT:
			if(Main.debug){
				Card c=player.pickCard(getPhase());
				if(c!=null)c.playerSelect();
			}
			break;


		case Input.Keys.S:


			break;
		case Input.Keys.A:
			battleWon(getPlayer());
			break;



		case Input.Keys.TAB:

			if(tutorial){
				Tutorial.next();
			}
			if(!tutorial){
				if (Tutorial.three.size()>0){
					Tutorial.next();
				}
				else Tutorial.makeThree();
			}

			break;

		case Input.Keys.ESCAPE:
			Main.changeScreen(new Selector());
			break;

		}



	}

	@Override
	public void keyUp(int keyCode) {
		switch (keyCode){
		case Input.Keys.Q:

			//hide enemy cards//
			if(enemyHandList.isEmpty())return;
			for(CardGraphic cg:enemyHandList){
				cg.deactivate();
			}

			enemyHandList.clear();

			break;

		}
	}

	public static void shake(boolean player, float amount){

		
		//amount is energy cost of card
		Ship s=null;
		if(player)s=getPlayer();
		else s=getEnemy();
		//if(!s.dead)Star.shake(player, amount);
		
		Pair shakeAdd=new Pair(amount*4, (float)(Math.random()-.5)*amount);
		if(player){
			
			playerKnockBackTarget=playerKnockBackTarget.add(shakeAdd);
			playerShakeIntensity+=amount;
		}
		if(!player){
			enemyKnockBackTarget=enemyKnockBackTarget.add(shakeAdd.multiply(new Pair(-1,1)));
			enemyShakeIntensity+=amount;
		}
	}

	public static Phase getPhase(){return currentPhase;}

	public static void setState(State s){
		currentState=s;
		switch(currentState){
		case Augmenting:
			help=new HelpPanel("Pick a card to augment",false);
			break;
		case CycleDiscard:
			help=new HelpPanel("Discard a card",false);
			break;
		case CycleGet:
			help=new HelpPanel("Pick a card to get!",true);
			CycleButton.get().setupChoices();
			break;
		case Nothing:
			if(help!=null)help.done();
			moduleChooser=null;
			targetSource=null;
			augmentSource=null;
			break;
		case Targeting:
			help=new HelpPanel("Pick a target module",false);
			break;
		case ModuleChoose:
			help=new HelpPanel("Choose a module",false);
			break;
		default:
			break;

		}
	}

	public static State getState(){return currentState;}

	public static boolean isPlayerTurn(){return(getPhase()!=Phase.End&&getPhase()==Phase.ShieldPhase||getPhase()==Phase.WeaponPhase);}

	@Override
	public void mousePressed(Pair location, boolean left) {

		if(tutorial)Tutorial.next();
		else{
			advance();	
		}

		if(Tutorial.undoVisible()&&UndoButton.get().collider.collidePoint(location)){
			Tutorial.goBack();
			return;
		}
		
	}

	public static void advance() {
		if(getPhase()==Phase.EnemyShieldPhase||getPhase()==Phase.EnemyWeaponPhase) getEnemy().enemyFadeAll();
	}

	@Override
	public void update(float delta) {
		

		playerShakeIntensity*=Math.pow(shakeDrag, delta);
		enemyShakeIntensity*=Math.pow(shakeDrag, delta);

		float freq=20;
		float amp=5;
		float jitter=3;
		float bonusX=(float) ((Math.sin(ticks*freq)*amp)+(Math.random()-.5)*jitter);
		float bonusY=(float) ((Math.sin(100+ticks*(freq*1.1f))*amp)+(Math.random()-.5)*jitter);
		playerBonus=new Pair();
		if(!player.exploded){
			playerBonus=new Pair(Noise.noise(Battle.ticks/4, 100), 
					Noise.noise(Battle.ticks/4, 300)).multiply(new Pair(15,3)).add(playerKnockBack);
					
			playerBonus=playerBonus.floor();
		}
		playerBonus=playerBonus.add(new Pair(bonusX*playerShakeIntensity, bonusY*playerShakeIntensity));
		playerKnockBackTarget=playerKnockBackTarget.multiply((float) Math.pow(.2f, delta));
		playerKnockBack=playerKnockBack.add(playerKnockBackTarget.subtract(playerKnockBack).multiply(delta*50));


		bonusX=(float) ((Math.sin(100+ticks*freq)*amp)+(Math.random()-.5)*jitter);
		bonusY=(float) ((Math.sin(ticks*(freq*1.1f))*amp)+(Math.random()-.5)*jitter);

		playerCam.position.set(basePlayerCamPosition.x ,basePlayerCamPosition.y, 0);
		enemyBonus=new Pair();
		if(!enemy.exploded){
		enemyBonus=new Pair(Noise.noise(Battle.ticks/4, 1100), Noise.noise(Battle.ticks/4, 1300)).multiply(new Pair(15,3).add(enemyKnockBack));
		enemyBonus=enemyBonus.floor();
		}
		enemyBonus=enemyBonus
				.add(new Pair(bonusX*enemyShakeIntensity, bonusY*enemyShakeIntensity));
		enemyKnockBackTarget=enemyKnockBackTarget.multiply((float) Math.pow(.2f, delta));
		enemyKnockBack=enemyKnockBack.add(enemyKnockBackTarget.subtract(enemyKnockBack).multiply(delta*50));

		enemyCam.position.set(baseEnemyCamPosition.x ,baseEnemyCamPosition.y, 0);

		//Just used for checking if the phase is finished//
		for(Niche n:player.niches){
			n.graphic.update(delta);
		}
		for(Niche n:enemy.niches){
			n.graphic.update(delta);
		}
		Star.update(delta);
		ticks+=delta;


		switch(currentPhase){
		case WeaponPhase: break;
		case ShieldPhase: break;
		case EnemyShieldPhase:break;
		case EnemyWeaponPhase:	break;
		case EnemyWeaponsFiring:

			if(enemy.finishedAttacking())enemy.endPhase();
			break;
		case PlayerWeaponsFiring:

			if(player.finishedAttacking())player.endPhase();
			break;
		case End:
			break;
		}






		//tutorishit//
		if(!isTutorial())return;
		Tutorial t= Tutorial.tutorials.get(Tutorial.index);
		if(t.trig==Trigger.PlayerWeaponPhase&&Battle.getPhase()==Phase.WeaponPhase)Tutorial.next();
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.end();


		Gdx.gl.glViewport(Main.width/2-480, 282, (int)viewport.x, (int)viewport.y);
		playerCam.update();
		batch.setProjectionMatrix(playerCam.combined);
		batch.begin();
		batch.setColor(1,1,1,1);
		Star.renderStars(batch, true);

		batch.end();

		playerCam.translate(playerBonus.x, playerBonus.y);
		playerCam.update();
		batch.setProjectionMatrix(playerCam.combined);
		batch.begin();
		player.renderShip(batch);
		ParticleSystem.renderAll(batch);
		batch.end();


		Gdx.gl.glViewport(Main.width/2+dividerWidth/2, 282, (int)viewport.x, (int)viewport.y);
		enemyCam.update();
		batch.setProjectionMatrix(enemyCam.combined);
		batch.begin();
		batch.setColor(1,1,1,1);
		Star.renderStars(batch, false);//Draw.drawTexture(batch, Star.pixTex, 500-pixTexTimer.getPair().x, pixTexTimer.getPair().y);
		batch.end();

		enemyCam.translate(enemyBonus.x, enemyBonus.y);
		enemyCam.update();
		batch.setProjectionMatrix(enemyCam.combined);
		batch.begin();
		enemy.renderShip(batch);
		ParticleSystem.renderAll(batch);
		batch.end();

		Gdx.gl.glViewport(0, 0, Main.width, Main.height);
		batch.setProjectionMatrix(Main.mainCam.combined);
		batch.begin();
		batch.setColor(1,1,1,1);
		player.renderFightStats(batch);
		enemy.renderFightStats(batch);
		Draw.draw(batch, Gallery.battleScreen.get(), 128, 0);
		//debug phase text
		if(Main.debug){
			Font.medium.setColor(Colours.grey);
			Font.medium.draw(batch, "Phase: "+currentPhase+", State: "+currentState, 300, 80);
		}

		if(getPhase()==Phase.End){
			String s=victor.player?"You win!":"You lose.";
			Font.big.setColor(Colours.withAlpha(Colours.light,victoryFadeInTimer.getFloat()));
			Font.big.draw(batch, s, Main.width/2-Font.big.getBounds(s).width/2, 205);
			s="(esc to return)";
			Font.big.draw(batch, s, Main.width/2-Font.big.getBounds(s).width/2, 245);
		}
		//	debugRender(batch);


		//Draw.drawScaledCentered(batch, Gallery.whiteSquare.get(), Mouser.getMousePosition().x, Mouser.getMousePosition().y, 200, 200);
	}

	@Override
	public void postRender(SpriteBatch batch) {

		drawInterfaceOverlay(batch);
		for(CardIcon icon:CardIcon.icons){
			icon.render(batch);
		}
		for(Module m:player.modules){
			m.getStats().render(batch);
		}
		for(Module m:enemy.modules){
			m.getStats().render(batch);
		}
		for(CardIcon icon:CardIcon.icons)icon.mousedGraphic.render(batch);
		for(CardGraphic cg:Card.extraCardsToRender)cg.render(batch);

		if(ModuleInfo.top!=null)ModuleInfo.top.render(batch);

		for(Card c:player.hand){
			c.getGraphic().render(batch);
		}
		CardGraphic.renderOffCuts(batch);
		CycleButton.get().render(batch);

		if(help!=null)help.render(batch);



		Tutorial.renderAll(batch);
		for(Animation a:animations){
			a.render(batch);
		}

	}

	@Override
	public void scroll(int amount) {
	}

	public static void setTutorial(boolean tutorial) {
		me.tutorial=tutorial;
	}

	@Override
	public void dispose() {
	//Updater.clearAll();
	player.getGraphic().dispose();
	enemy.getGraphic().dispose();
	}







}
