package game;


import util.Colours;
import util.Draw;
import util.TextWriter;
import util.assets.Font;
import util.maths.Pair;
import util.update.InputHandler;
import util.update.Screen;
import util.update.TextWisp;
import util.update.Timer;
import util.update.Timer.Finisher;
import util.update.Timer.Interp;
import util.update.Updater;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import game.assets.Gallery;
import game.assets.Sounds;
import game.card.CardGraphic;
import game.card.CardHover;
import game.screen.battle.Battle;
import game.screen.cardView.CardViewer;
import game.screen.escape.EscapeMenu;
import game.screen.map.Map;
import game.screen.menu.Menu;
import game.screen.preBattle.PreBattle;
import game.screen.test.Test;
import game.ship.Ship;
import game.ship.shipClass.Aurora;
import game.ship.shipClass.Comet;
import game.ship.shipClass.Eclipse;
import game.ship.shipClass.Hornet;
import game.ship.shipClass.Nova;
import game.ship.shipClass.Scout;

public class Main extends ApplicationAdapter  {

	public static float version=0.534f;
	public static boolean debug=!true;
	public static float ticks;
	public static int height=700;
	public static int width=1280;

	public static SpriteBatch bufferBatch;
	
	public static SpriteBatch batch;
	public static SpriteBatch uiBatch;
	public static ShapeRenderer shape;

	//SCREENS//
	public static Screen nextScreen;
	public static Screen currentScreen;

	public static Battle battle;  
	public static CardViewer viewer;
	public static Menu select;
	public static Map map;

	public static OrthographicCamera uiCam;

	public static OrthographicCamera mainCam;

	public static Timer fadeTimer=new Timer();

	static float fadeSpeed=.5f;
	

	//Escape menu//

	@Override
	public void create () {
		init();
	}
	public void init(){
		Gdx.input.setInputProcessor(new InputHandler());
		Sounds.battleMusic.get();
		CardGraphic.init();
		Gallery.init();
		Sounds.init();
		Font.init();
		CardHover.init();
		Ship.init();
		
		batch = new SpriteBatch();
		uiBatch=new SpriteBatch();
		shape=new ShapeRenderer();
		Color c=Colours.dark;
		Gdx.gl.glClearColor(c.r,c.g,c.b,1);
		uiCam=new OrthographicCamera(Main.width, Main.height);
		uiCam.setToOrtho(true);
		uiCam.translate(-Main.width, -Main.height/2);
		uiBatch.setProjectionMatrix(uiCam.combined);
		mainCam=new OrthographicCamera(Main.width, Main.height);
		mainCam.setToOrtho(true);
		bufferBatch = new SpriteBatch();
		OrthographicCamera cam = new OrthographicCamera(Main.width, Main.height);
		//map=new Map();currentScreen=map;currentScreen.init();
		
		//bufferBatch.setProjectionMatrix(Main.mainCam.combined);

		//currentScreen=new Test();currentScreen.init();

		//battle=new Battle(ScreenType.MediumFight);currentScreen=battle;

		select=new Menu();currentScreen=select;select.init();

		//currentScreen=new PreBattle(new Hornet(true, 0), new Hornet(false, 0)); currentScreen.init();

//		viewer=new CardViewer();currentScreen=viewer;



//		currentScreen=new Test();currentScreen.init();


		if(true){
			System.out.println("---------Scout------------");
			System.out.println(new Scout(true, 0).getStats());
			System.out.println("---------Aurora------------");
			System.out.println(new Aurora(true, 0).getStats());
			System.out.println("---------Nova------------");
			System.out.println(new Nova(true, 0).getStats());
			System.out.println("---------Hornet------------");
			System.out.println(new Hornet(true, 0).getStats());
			System.out.println("---------Comet------------");
			System.out.println(new Comet(true, 0).getStats());
			System.out.println("---------Eclipse------------");
			System.out.println(new Eclipse(true, 0).getStats());
		}
	

	}	



	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		delta=Math.min(.1f, delta);
		update(delta);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//for some reason I need to reset the projection matrix//
		shape.setProjectionMatrix(mainCam.combined);
		currentScreen.shapeRender(shape);
		//EscapeMenu.get().shapeRender(shape);
		//batch.getProjectionMatrix().setToOrtho2D((int)(cam.x), (int)(cam.y), Main.width, Main.height);
		batch.setProjectionMatrix(mainCam.combined);
		batch.begin();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.setColor(Colours.white);

		currentScreen.render(batch);

		//ParticleSystem.renderAll(batch);

		currentScreen.postRender(batch);

		for(TextWisp t:TextWisp.wisps)t.render(batch);
		
		//fading bit//
		

		if(debug){
			batch.end();
			batch.setProjectionMatrix(uiCam.combined);
			batch.begin();
			Font.small.setColor(Colours.white);
			Font.small.draw(batch, "FPS: "+(int)(1/delta), 0, 0);
		}

		

		batch.end();
		
		uiBatch.begin();
		
		
		uiBatch.setColor(Colours.withAlpha(Colours.dark, fadeTimer.getFloat()));
		Draw.drawScaled(uiBatch, Gallery.whiteSquare.get(), 0, 0, width, height);
		uiBatch.setColor(1,1,1,1);
		EscapeMenu.get().render(uiBatch);
		EscapeMenu.get().postRender(uiBatch);
		uiBatch.end();
//		batch.begin();
//		//batch.setProjectionMatrix(uiCam.combined);
//		uiCam.update();
//		
//		batch.end();

	}

	public void update(float delta){
		
		ticks+=delta;
		currentScreen.update(delta);
		Updater.updateAll(delta);

		if(nextScreen!=null){
			if(fadeTimer.getFloat()>=1){

				TextWisp.wisps.clear();
				Updater.clearAll();
				fadeTimer=new Timer(1, 0, fadeSpeed, Interp.LINEAR);
				nextScreen.init();
				currentScreen.dispose();
				TextWriter.disposeAll();
				currentScreen=nextScreen;
				nextScreen=null;
				resetMainCam();
			}
			return;
		}

		mainCam.update();
	

	}

	private void resetMainCam() {
		setCam(new Pair(Main.width/2,Main.height/2));
		
	}

	public enum ScreenType{EasyFight, MediumFight, HardFight, TutorialFight, Menu}
	public static void changeScreen(Screen newScreen){
		Gdx.input.setCursorImage(null,0,0);
		fadeSpeed=.5f;
		//if(type==ScreenType.Menu&&currentScreen==select)return;
		TextWisp.wisps.clear();
		nextScreen=newScreen;
		fadeTimer=new Timer(fadeTimer.getFloat(), 1, fadeSpeed, Interp.LINEAR);
	}

	public static void changeScreen(Screen newScreen, float seconds){
		//if(type==ScreenType.Menu&&currentScreen==select)return;
		fadeSpeed=seconds;
		TextWisp.wisps.clear();
		nextScreen=newScreen;
		fadeTimer=new Timer(fadeTimer.getFloat(), 1, fadeSpeed, Interp.LINEAR);

	}

	public static Pair getCam(){
		return new Pair(mainCam.position.x,mainCam.position.y);
	}

	public static void setCam(Pair cam){
		mainCam.position.set(cam.x, cam.y, 0);
	}

	public static Screen getCurrentInputScreen(){
		if(EscapeMenu.get().active)return EscapeMenu.get();
		return currentScreen;
	}

	public static void keyPress(int keycode) {
		if(keycode==Input.Keys.ESCAPE){
			EscapeMenu.get().cycle();
			return;
		}
		getCurrentInputScreen().keyPress(keycode);
	}

	public static void keyUp(int keyCode) {
		getCurrentInputScreen().keyUp(keyCode);
	}
	public static void touchDown(Pair location, boolean left) {
		getCurrentInputScreen().mousePressed(location, left);
	}
	public static void scrolled(int amount){
		getCurrentInputScreen().scroll(amount);
	}


}
