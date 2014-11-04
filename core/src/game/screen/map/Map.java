package game.screen.map;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import game.Main;
import game.assets.Gallery;
import game.grid.Grid;
import game.grid.hex.Hex;
import game.ship.mapThings.MapShip;
import game.ship.mapThings.mapAbility.MapAbility;
import game.ship.shipClass.Nova;

public class Map extends Screen{
	static float scrollSpeed=100;
	public static Grid grid;

	public static MapShip player;
	SpriteBatch batch2=new SpriteBatch();
	public enum MapState{PlayerTurn,PlayerMoving,EnemyMoving,PickHex}
	private static MapState state=MapState.PlayerTurn;
	public static Hex explosion;
	public static float explosionSize=9;
	public static float growthRate=.8f;
	public static float progress=0;
	public static float phaseSpeed=3;
	public static MapAbility using;

	public static ArrayList<Hex> path= new ArrayList<Hex>();

	public Map(){
	
	}

	public void init(){
		Main.setCam(new Pair(0,0));
		Hex.init();
		MapAbility.init();
		grid=Grid.MakeGrid();
		player=new MapShip(new Nova(true, 0), grid.getHex(55, 50));
		Map.explosion=grid.getHex(45, 48);
		
		for(int i=0;i<player.mapAbilities.size();i++){
			MapAbility a=player.mapAbilities.get(i);
			a.showAt(new Pair(5+MapAbility.width/2, 5+MapAbility.height/2+i*MapAbility.gap));
		}
	}


	public static MapState getState(){
		return state;
	}

	public static void returnToPlayerTurn(){
		using=null;
		Map.state=MapState.PlayerTurn;
	}
	
	public static void setState(MapState state){
		progress=0;
		Map.state=state;
		switch (state){
		case EnemyMoving:
			explosionSize+=growthRate/2;
			enemyTurn();
			break;
		case PlayerTurn:
			Hex.mousedHex.moused=false;
			grid.gridTurn();
			player.playerStartTurn();
			break;
		case PlayerMoving:
			break;
		case PickHex:
			break;
		}
	}

	private void updateState(float delta) {

		switch(state){
		case EnemyMoving:
			progress+=delta*phaseSpeed;
			if(progress>1){
				progress=0;
				setState(MapState.PlayerTurn);
			}
			break;
		case PlayerTurn:
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
	public void mousePressed(Pair location, boolean left) {
		Hex h=grid.getHexUnderMouse(location.x-Main.width/2,location.y-Main.height/2);
		if(h!=null){
			if(left)h.click();
			if(!left)h.rightClick();
		}
	}

	public void zoom(int amount) {
		if(Hex.size-amount<1)return;
		Hex.resize(Hex.size-amount);
	}

	public static void updateCamPosition() {
		Pair bonus=new Pair(0,0);
		if(getState()==MapState.PlayerMoving){
			bonus=new Pair().add(player.distance);
		}
		
		Main.setCam(player.hex.getPixel().add(bonus));
	}

	@Override
	public void update(float delta) {

		updateState(delta);		
		grid.update(delta);		
		updateCamPosition();
		Hex h=grid.getHexUnderMouse(Gdx.input.getX()-Main.width/2,Gdx.input.getY()-Main.height/2);
		if(h!=null)h.mouse();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			Main.setCam(Main.getCam().add(1, 0));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
		Main.setCam(Main.getCam().add(-1, 0));
		
		}
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
		batch2.setProjectionMatrix(Main.uiCam.combined);
		batch2.begin();
		batch2.draw(Gallery.mapslice.get(), 0, 0);
		
		Draw.drawScaled(batch2, Gallery.mapsliceRight.get(), Main.width, Main.height	, -1, -1);
		batch2.setColor(Colours.light);
		Font.medium.draw(batch2, ""+getState(), 300, 0);
		for(MapAbility a:player.mapAbilities)a.render(batch2);
		batch2.end();



		batch.begin();

	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void scroll(int amount) {
		zoom(amount);
	}

	@Override
	public void dispose() {
	}







}
