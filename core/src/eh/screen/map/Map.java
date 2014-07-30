package eh.screen.map;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eh.Main;
import eh.assets.Font;
import eh.assets.Gallery;
import eh.grid.Grid;
import eh.grid.hex.Hex;
import eh.screen.Screen;
import eh.ship.mapThings.MapAbility;
import eh.ship.mapThings.MapShip;
import eh.ship.shipClass.*;
import eh.util.Colours;
import eh.util.Junk;
import eh.util.maths.Sink;

public class Map extends Screen{
	static float scrollSpeed=100;
	public static Grid grid;

	public static MapShip player;
	SpriteBatch batch2=new SpriteBatch();
	public enum MapState{PlayerChoosing,PlayerMoving,EnemyMoving,PickHex}
	private static MapState state=MapState.PlayerChoosing;
	public static Hex explosion;
	public static float explosionSize=9;
	public static float growthRate=.8f;
	public static float progress=0;
	public static float phaseSpeed=3;
	public static MapAbility using;
	static MapAbility abil;
	
	public static ArrayList<Hex> path= new ArrayList<Hex>();
	
	public Map(){
		init();
	}

	public static void init(){
		grid=Grid.MakeGrid();
		Hex.init();
		//player=new MapShip(new Nova(true), grid.getHex(50, 50));
		player=new MapShip(new Nova(true), grid.getHex(60, 60));

		Map.explosion=grid.getHex(45, 48);
		MapAbility.init();

		abil=new MapAbility(new Sink(5+MapAbility.width/2,Main.height-MapAbility.height/2-5));
	}


	public static MapState getState(){
		return state;
	}

	public static void setState(MapState state){
		progress=0;
		Map.state=state;
		switch (state){
		case EnemyMoving:
			explosionSize+=growthRate/2;
			enemyTurn();
			break;
		case PlayerChoosing:
			Hex.mousedHex.moused=false;
			explosionSize+=growthRate/2;
			player.playerStartTurn();
			break;
		case PlayerMoving:
			break;
		default:
			break;
		}
	}

	private void updateState(float delta) {

		switch(state){
		case EnemyMoving:
			progress+=delta*phaseSpeed;
			if(progress>1){
				progress=0;
				setState(MapState.PlayerChoosing);
			}
			break;
		case PlayerChoosing:
			break;
		case PlayerMoving:
			progress+=delta*phaseSpeed;
			if(progress>1){
				progress=0;
				setState(MapState.EnemyMoving);
			}
			break;
		default:
			break;
		}
	}

	public static void enemyTurn(){
		for(MapShip enemy:grid.getActiveEnemies())enemy.takeTurn();
	}

	@Override
	public void update(float delta) {
		updateState(delta);		
		grid.update(delta);		
		moveCam();
		Hex h=grid.getHexUnderMouse(Gdx.input.getX(),Main.height-Gdx.input.getY());
		if(h!=null)h.mouse();
	}

	@Override
	public void keyPress(int keycode) {
		switch(keycode){
		case (Input.Keys.COMMA):
			zoom(-1);
		break;
		case (Input.Keys.PERIOD):
			zoom(1);
		case Input.Keys.SPACE:
			
		break;
		}
	}

	@Override
	public void keyUp(int keyCode) {
	}

	@Override
	public void mousePressed(Sink location, boolean left) {
		Hex h=grid.getHexUnderMouse(location.x,location.y);
		if(h!=null){
			if(left)h.click();
			if(!left)h.rightClick();
		}
	}

	public void zoom(int amount) {
		if(Hex.size-amount<1)return;
		Hex.resize(Hex.size-amount);
	}

	public static void moveCam() {
		Sink bonus=new Sink(0,0);
		if(getState()==MapState.PlayerMoving){
			bonus=player.distance;
		}
		Main.setCam(player.hex.getPixel().add(bonus).subtract(new Sink(Main.width/2,Main.height/2)));
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		shape.setColor(1, 1, 1, 1f);
		grid.shapeRender(shape);
	}

	@Override
	public void render(SpriteBatch batch) {
		grid.render(batch);
		batch.end();

		//Black Hole//

		Main.shape.begin(ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Main.shape.setColor(0,0,0,1);
		Main.shape.circle(explosion.getPixel().x,explosion.getPixel().y, (explosionSize+progress*(growthRate/2))*Hex.size);
		Main.shape.end();

		//batch2 is for interface stuff//
		batch2.begin();
		batch2.draw(Gallery.mapslice.get(), 0, 0);
		Junk.drawTextureScaled(batch2, Gallery.mapsliceRight.get(), Main.width, Main.height	, -1, -1);
		batch2.setColor(Colours.light);
		Font.medium.draw(batch2, ""+getState(), Main.width-300, Main.height);
		abil.render(batch2);
		batch2.end();



		batch.begin();

	}







}
