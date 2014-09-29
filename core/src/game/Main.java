package game;


import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.InputHandler;
import util.update.Screen;
import util.update.TextWisp;
import util.update.Timer;
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
import game.card.CardGraphic;
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

public class Main extends ApplicationAdapter  {

	public static float version=0.38f;
	public static boolean debug=false;
	public static float ticks;
	public static int height=700;
	public static int width=1280;

	public static SpriteBatch batch;
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

		CardGraphic.init();
		Gallery.init();
		Font.init();
		Ship.init();
		batch = new SpriteBatch();
		shape=new ShapeRenderer();
		Color c=Colours.dark;
		Gdx.gl.glClearColor(c.r,c.g,c.b,1);
		uiCam=new OrthographicCamera(Main.width, Main.height);
		uiCam.setToOrtho(true);
		uiCam.translate(-Main.width, -Main.height/2);

		mainCam=new OrthographicCamera(Main.width, Main.height);
		mainCam.setToOrtho(true);


		//currentScreen=new Test();currentScreen.init();

		//battle=new Battle(ScreenType.MediumFight);currentScreen=battle;

		select=new Menu();currentScreen=select;select.init();

		//currentScreen=new PreBattle(new Hornet(true, 0), new Hornet(false, 0)); currentScreen.init();

		//viewer=new CardViewer();currentScreen=viewer;

		//map=new Map();currentScreen=map;

		//currentScreen=new Test();


		if(true){
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
		batch.setColor(Colours.withAlpha(Colours.dark, fadeTimer.getFloat()));
		Draw.drawScaled(batch, Gallery.whiteSquare.get(), 0, 0, width, height);

		if(debug){
			batch.end();
			batch.setProjectionMatrix(uiCam.combined);
			batch.begin();
			Font.small.setColor(Colours.white);
			Font.small.draw(batch, "FPS: "+(int)(1/delta), 0, 0);
		}

		EscapeMenu.get().render(batch);
		EscapeMenu.get().postRender(batch);


		batch.end();




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

				currentScreen=nextScreen;
				nextScreen=null;
			}
			return;
		}

		mainCam.update();


	}

	public enum ScreenType{EasyFight, MediumFight, HardFight, TutorialFight, Menu}
	public static void changeScreen(Screen newScreen){
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
