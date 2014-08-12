package eh.screen.battle;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eh.Main;
import eh.Main.ScreenType;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.card.Card;
import eh.card.CardCode;
import eh.card.CardGraphic;
import eh.card.CardIcon;
import eh.card.CardCode.Special;
import eh.module.Module;
import eh.module.utils.DamagePoint;
import eh.module.utils.ModuleInfo;
import eh.module.weapon.attack.LightningAttack;
import eh.screen.Screen;
import eh.screen.battle.interfaceJunk.CycleButton;
import eh.screen.battle.interfaceJunk.HelpPanel;
import eh.screen.battle.interfaceJunk.PhaseButton;
import eh.screen.battle.interfaceJunk.Star;
import eh.screen.battle.tutorial.Checklist;
import eh.screen.battle.tutorial.Tutorial;
import eh.screen.battle.tutorial.UndoButton;
import eh.screen.battle.tutorial.Tutorial.Trigger;
import eh.ship.Ship;
import eh.ship.niche.Niche;
import eh.ship.shipClass.*;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.TextWisp;
import eh.util.TextWisp.WispType;
import eh.util.maths.Pair;

public class Battle extends Screen{
	public enum Phase{ShieldPhase, EnemyWeaponsFiring, WeaponPhase, EnemyShieldPhase, PlayerWeaponsFiring, EnemyWeaponPhase, End};
	public enum State{Nothing, Augmenting, Targeting, CycleDiscard, CycleGet, ModuleChoose}
	private static Phase currentPhase=Phase.WeaponPhase;
	private static State currentState=State.Nothing;
	public static Card augmentSource;
	public static Card targetSource;
	public static Card moduleChooser;
	public static Ship player;
	public static Ship enemy;

	public static HelpPanel help;
	Texture texture;
	TextureRegion region;
	float debug;
	static Ship victor;
	public static float ticks;
	static float playerShakeIntensity;
	public static float enemyShakeIntensity;
	public static float sinSpeed=60;
	public static float playerScreenShakeDrag=60;
	public static float enemyScreenShakeDrag=15;
	public static boolean tutorial=false;
	private ArrayList<CardGraphic> enemyHandList=new ArrayList<CardGraphic>();
	ArrayList<TextWisp> wisps = new ArrayList<TextWisp>();
	public Battle(Main.ScreenType type){
		init(type);
	}

	public void init(ScreenType type){
		resetStatics();
		Star.init();
		switch(type){
		case EasyFight:
			player = new Eclipse(true);
			enemy=new Aurora(false);
			break;
		case MediumFight:
			player=new Comet(true);
			enemy=new Nova(false);
			break;
		case HardFight:
			player=new Aurora(true);
			enemy=new Nova(false);
			break;
		case TutorialFight:
			player = new Nova(true);
			enemy=new Aurora(false);
			initTutorial();
			break;
		case Menu:
			break;
		}
		player.startFight(true);
		enemy.startFight(false);
		if(type==ScreenType.TutorialFight){
			player.addEnergy(6);
			enemy.addEnergy(2);
		}
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
		tutorial=false;
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
		enemy.checkDefeat();
		player.checkDefeat();
		System.out.println();
		System.out.println("State change to "+s);
		
		switch(s){
		case EnemyShieldPhase:
			player.notifyIncoming();
			enemy.enemyStartPhase();
			break;
		case EnemyWeaponPhase:
			PhaseButton.get().nextPhase();
			enemy.checkDefeat();
			enemy.enemyStartPhase();
			break;
		case EnemyWeaponsFiring:
			enemy.fireAll();
			break;
		case PlayerWeaponsFiring:
			player.fireAll();
			break;
		case ShieldPhase:
			new TextWisp("Shield Phase", Font.big, new Pair(Main.width/2,330), TextWisp.WispType.Regular);
			PhaseButton.get().nextPhase();
			enemy.enemyEndTurn();
			player.playerStartTurn();
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
		if(player.hasSpendableShields())return;
		player.playCards();

		if(getPhase()==Phase.ShieldPhase){
			player.endPhase();
			PhaseButton.button.nextPhase();
		}
		else{
			player.playerEndTurn();
			player.endPhase();
			PhaseButton.button.nextPhase();
		}
	}

	public static void battleWon(Ship ship) {
		setPhase(Phase.End);
		System.out.println("ended");
		victor=ship;
	}


	@Override
	public void update(float delta) {
		//Just used for checking if the phase is finished//
		for(Niche n:player.niches){
			n.graphic.update(delta);
		}
		for(Niche n:enemy.niches){
			n.graphic.update(delta);
		}
		ticks+=delta;
		playerShakeIntensity-=delta*playerScreenShakeDrag;
		playerShakeIntensity=Math.max(0, playerShakeIntensity);
		enemyShakeIntensity-=delta*enemyScreenShakeDrag;
		enemyShakeIntensity=Math.max(0, enemyShakeIntensity);

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
		if(!Battle.tutorial)return;
		Tutorial t= Tutorial.tutorials.get(Tutorial.index);
		if(t.trig==Trigger.PlayerWeaponPhase&&Battle.getPhase()==Phase.WeaponPhase)Tutorial.next();
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
			if(Main.debug){
				debug();
			}
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

				for(Module m:player.getRandomisedModules()){
					//System.out.println(m.getShieldsRequiredToAvoidMajor());
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
			Main.changeScreen(ScreenType.Menu);
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
				cg.debonktivate();
			}

			enemyHandList.clear();

			break;
		
		}
	}

	public static void shake(boolean player, boolean major){
		if(player){
			playerShakeIntensity=Math.max(playerShakeIntensity, major?19:11);
		}
		if(!player){
			enemyShakeIntensity=Math.max(enemyShakeIntensity, major?8:5);
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
		if(Tutorial.undoVisible()&&UndoButton.get().collider.collidePoint(location)){
			Tutorial.goBack();
			return;
		}
		if(tutorial)Tutorial.next();
		else{
			advance();	
		}
		

		System.out.println(location);
	}

	public static void advance() {
		if(getPhase()==Phase.EnemyShieldPhase||getPhase()==Phase.EnemyWeaponPhase) enemy.enemyFadeAll();
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
	}

	@Override
	public void render(SpriteBatch batch) {

		Main.setCam(new Pair((float)Math.sin(ticks*sinSpeed)*playerShakeIntensity+Main.width/2, (float)Math.cos((ticks-2.5f)*sinSpeed)*playerShakeIntensity+Main.height/2));

		Draw.drawTexture(batch, Star.pixTex, 160, 70);
		Draw.drawTexture(batch, Gallery.battleScreen.get(), 128, 0);

		//Rendering ships, statblocks and misc interface//
		player.renderAll(batch);
		enemy.renderAll(batch);
		

	


	

		//debug phase text
		if(Main.debug){
			Font.medium.setColor(Colours.grey);
			Font.medium.draw(batch, "Phase: "+currentPhase+", State: "+currentState, 300, 80);
		}

		if(getPhase()==Phase.End){
			String s=victor.player?"You win!":"You lose.";
			Font.big.setColor(Colours.light);
			Font.big.draw(batch, s, Main.width/2-Font.big.getBounds(s).width/2, 205);
			s="(esc to return)";
			Font.big.draw(batch, s, Main.width/2-Font.big.getBounds(s).width/2, 245);
		}
		
		//	debugRender(batch);

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
	}





}
