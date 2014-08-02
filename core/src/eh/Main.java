package eh;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

import eh.assets.Font;
import eh.assets.Gallery;
import eh.card.CardGraphic;
import eh.screen.Screen;
import eh.screen.Test;
import eh.screen.battle.Battle;
import eh.screen.cardView.CardViewer;
import eh.screen.map.Map;
import eh.screen.menu.Selector;
import eh.ship.Ship;
import eh.util.Bonkject;
import eh.util.Colours;
import eh.util.Draw;
import eh.util.TextWisp;
import eh.util.Timer;
import eh.util.maths.Pair;
import eh.util.particleSystem.ParticleSystem;

public class Main extends ApplicationAdapter  {

	public static float version=0.15f;
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

	public static OrthographicCamera resetCam;

	public static OrthographicCamera mainCam;
	
	private static Pair cam=new Pair(0,0);

	//ANIMATING//
	float ticks;
	boolean goingUp=true;
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
		resetCam=new OrthographicCamera(Main.width, Main.height);
		resetCam.translate(-Main.width, -Main.height/2);

		mainCam=new OrthographicCamera(Main.width, Main.height);
		mainCam.setToOrtho(true);
		//battle=new Battle();currentScreen=battle;

		select=new Selector();currentScreen=select;

		//viewer=new CardViewer();currentScreen=viewer;

		//map=new Map();currentScreen=map;

		//currentScreen=new Test();
	}	



	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		delta=Math.min(.1f, delta);
		update(delta);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//for some reason I need to reset the projection matrix//
		shape.setProjectionMatrix(new Matrix4());
		shape.getProjectionMatrix().setToOrtho2D((int)(cam.x), (int)(cam.y), Main.width, Main.height);
		currentScreen.shapeRender(shape);
		//batch.getProjectionMatrix().setToOrtho2D((int)(cam.x), (int)(cam.y), Main.width, Main.height);
		batch.setProjectionMatrix(mainCam.combined);
		batch.begin();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.setColor(Colours.white);

		currentScreen.render(batch);
		for(TextWisp t:TextWisp.wisps)t.render(batch);
		ParticleSystem.renderAll(batch);
		
		currentScreen.postRender(batch);
		
		//fading bit//
		batch.setColor(1,1,1,ticks);
		Draw.drawTextureScaled(batch, Gallery.darkDot.get(), 0, 0, width, height);
		
		if(debug){
			batch.end();
			batch.getProjectionMatrix().setToOrtho2D(0,0, Main.width, Main.height);
			batch.begin();
			Font.small.setColor(Colours.white);
			Font.small.draw(batch, "FPS: "+(int)(1/delta), 10, 20);
		}


		batch.end();





	}

	public void update(float delta){
		Timer.updateAll(delta);
		ParticleSystem.updateAll(delta);
		currentScreen.update(delta);
		Bonkject.updateBonkjs(delta);

		if(nextType!=null){
			ticks+=delta*2;
			if(ticks>=1){
				Bonkject.clearAll();
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
				ticks=.9f;
				goingUp=false;
				nextType=null;
			}
			return;
		}
		if(ticks>0){

			ticks-=delta*2;
			ticks=Math.max(0, ticks);
		}
	}

	public enum ScreenType{EasyFight, MediumFight, HardFight, TutorialFight, Menu}
	public static void changeScreen(ScreenType type){
		nextType=type;
	}

	public static Pair getCam(){
		return cam;
	}

	public static void setCam(Pair cam){
		Main.cam=cam.floor();
	}


}
