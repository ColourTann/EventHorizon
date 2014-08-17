package eh;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eh.card.CardGraphic;
import eh.screen.Screen;
import eh.screen.battle.Battle;
import eh.screen.cardView.CardViewer;
import eh.screen.escape.EscapeMenu;
import eh.screen.map.Map;
import eh.screen.menu.Selector;
import eh.screen.test.Test;
import eh.ship.Ship;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.TextWisp;
import eh.util.Timer;
import eh.util.Timer.Interp;
import eh.util.assets.Font;
import eh.util.assets.Gallery;
import eh.util.maths.Pair;
import eh.util.particleSystem.ParticleSystem;

public class Main extends ApplicationAdapter  {

	public static float version=0.221f;
	public static boolean debug=true;

	public static int height=700;
	public static int width=1280;

	public static SpriteBatch batch;
	public static ShapeRenderer shape;

	//SCREENS//
	public static ScreenType nextType;
	public static Screen currentScreen;

	public static Battle battle;  
	public static CardViewer viewer;
	public static Selector select;
	public static Map map;

	public static OrthographicCamera uiCam;

	public static OrthographicCamera mainCam;
	
	public static Timer fadeTimer=new Timer();
	
	
	

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
		
		
		
		//battle=new Battle(ScreenType.MediumFight);currentScreen=battle;

		//select=new Selector();currentScreen=select;

		//viewer=new CardViewer();currentScreen=viewer;

		//map=new Map();currentScreen=map;

		currentScreen=new Test();
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
		batch.setColor(Colours.withAlpha(Colours.dark, fadeTimer.getFloat()
				));
		Draw.drawTextureScaled(batch, Gallery.square1.get(), 0, 0, width, height);
		
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
		
		Timer.updateAll(delta);
		ParticleSystem.updateAll(delta);
		currentScreen.update(delta);
		Bonkject.updateBonkjs(delta);

		if(nextType!=null){
			if(fadeTimer.getFloat()==1){
				TextWisp.wisps.clear();
				Bonkject.clearAllDefaults();
				fadeTimer=new Timer(1, 0, 2, Interp.LINEAR);
				
				switch(nextType){
				case EasyFight:
					battle=new Battle(ScreenType.EasyFight);currentScreen=battle;
					break;
				case HardFight:
					battle=new Battle(ScreenType.HardFight);currentScreen=battle;
					break;
				case MediumFight:
					battle=new Battle(ScreenType.MediumFight);currentScreen=battle;
					break;
				case Menu:
					select=new Selector();currentScreen=select;
					break;
				case TutorialFight:
					battle=new Battle(ScreenType.TutorialFight);currentScreen=battle;
					break;			
				}
			
				
				nextType=null;
				
			}
			return;
		}
		
		mainCam.update();
		
		
	}

	public enum ScreenType{EasyFight, MediumFight, HardFight, TutorialFight, Menu}
	public static void changeScreen(ScreenType type){
		if(type==ScreenType.Menu&&currentScreen==select)return;
		TextWisp.wisps.clear();
		nextType=type;
		fadeTimer=new Timer(fadeTimer.getFloat(), 1, 2, Interp.LINEAR);
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
