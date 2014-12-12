package game.screen.map;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.update.Mouser;
import util.update.Screen;
import util.update.Timer;
import util.update.Timer.Finisher;
import util.update.Updater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.sun.media.jfxmedia.events.PlayerEvent;

import game.Main;
import game.assets.Gallery;
import game.grid.Grid;
import game.grid.hex.Hex;
import game.module.Module;
import game.module.junk.InfoBox;
import game.module.junk.ModuleStats;
import game.screen.escape.EscapeMenu;
import game.screen.map.panels.ActionPanel;
import game.screen.map.panels.HexInfoPanel;
import game.screen.map.panels.PlayerStatsPanel;
import game.screen.map.panels.SidePanel;
import game.screen.map.popup.Popup;
import game.screen.map.popup.PostBattle;
import game.screen.map.stuff.Base;
import game.screen.map.stuff.Equip;
import game.screen.map.stuff.Item;
import game.screen.menu.Menu;
import game.ship.Ship;
import game.ship.mapThings.MapShip;
import game.ship.mapThings.mapAbility.MapAbility;
import game.ship.shipClass.Nova;

public class Map extends Screen{
	static float scrollSpeed=100;
	public static Grid grid;

	public static MapShip player;
	SpriteBatch uiBatch=new SpriteBatch();
	public enum MapState{PlayerTurn,PlayerMoving,EnemyMoving,PickHex,Popup,Equipping}
	private static MapState state;
	public static Hex explosion;
	private static float explosionSize=9;
	public static float growthRate=.7f;
	public static float progress=0;
	public static float phaseSpeed=3f;
	public static MapAbility using;
	public static ArrayList<Hex> path= new ArrayList<Hex>();
	static HexInfoPanel hexInfoPanel;
	static PlayerStatsPanel playerStatsPanel;
	static ActionPanel actionPanel;
	ArrayList<SidePanel> panels = new ArrayList<SidePanel>();
	static Popup currentEvent;
	private Ship ship;
	static Base currentBase;
	public static Map me;
	private boolean initialised;
	private boolean losing;
	public Map(Ship ship){
		this.ship=ship;
	}

	public void init(){
		if(initialised){
			restoreAll();
			return;
		}
		initialised=true;
		resetStatics();
		Main.setCam(new Pair(0,0));
		Hex.init();
		MapAbility.init();
		grid=Grid.MakeGrid();

		for(Hex h:grid.getHex(55, 50).getHexesWithin(5, true)){
			if(!h.isBlocked(true)&&!h.swallowed(3)){
				player=new MapShip(ship, h);
				break;
			}
		}

		//		 grid.getHex(60, 50).startNebula(0);
		//player=new MapShip(new Nova(true, 0), grid.getHex(55, 50));


		for(int i=0;i<player.mapAbilities.size();i++){
			MapAbility a=player.mapAbilities.get(i);
			a.showAt(new Pair(5+MapAbility.width/2, 5+MapAbility.height/2+i*MapAbility.gap));
			a.index=i+1;
		}
		hexInfoPanel=new HexInfoPanel();
		playerStatsPanel=new PlayerStatsPanel();
		actionPanel=new ActionPanel();
		panels.add(hexInfoPanel);
		panels.add(playerStatsPanel);
		panels.add(actionPanel);
		restoreAll();
		me=this;
		grid.setupDrawableHexes();
		grid.startOfPlayerTurn();
	}

	public static void resetStatics(){
		state=MapState.PlayerTurn;
		progress=0;
		explosionSize=9;
		currentEvent=null;
		using=null;
		path=null;
		hexInfoPanel=null;
	}

	public static MapState getState(){
		return state;
	}

	public static void returnToPlayerTurn(){
		System.out.println("returning "+currentEvent);
		using=null;
		if(currentEvent!=null){
			currentEvent.dispose();
			currentEvent=null;	
		}
		Map.state=MapState.PlayerTurn;
	}

	public static void setState(MapState state){

		progress=0;
		Map.state=state;
		switch (state){
		case EnemyMoving:
			explosionSize+=growthRate/2;
			enemyTurn();
			grid.turn();
			break;
		case PlayerTurn:

			Hex.mousedHex.moused=false;
			player.playerStartTurn();
			grid.startOfPlayerTurn();
			if(Map.using!=null){
				MapAbility toUse=Map.using;
				Map.using=null;
				toUse.select();
			}

			if(player.getShip().getFuel()<=0){
				lose();
			}
			
			if(player.hex.swallowed(0)){
				lose();
			}

			break;
		case PlayerMoving:
			player.playerAfterMove();
			break;
		case PickHex:
			break;
		}
	}

	private void updateState(float delta) {
		//System.out.println(state+":"+progress+":"+using);
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
		for(MapShip enemy:grid.getActiveEnemies())enemy.takeAITurn();		
	}



	@Override
	public boolean keyPress(int keycode) {
		switch(keycode){
		case Input.Keys.ESCAPE: 
			if(currentBase!=null)return currentBase.keyPress(keycode);
			break;
		case Input.Keys.SPACE:
			popup(new PostBattle(ship));
			break;
		}
		//hotkeys 8-16
		int hotkey=keycode-8;
		if(hotkey>=0&&hotkey<=9){
			if(player.mapAbilities.size()>hotkey)
				player.mapAbilities.get(hotkey).select();
		}

		return false;
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
		Main.mainCam.zoom+=amount;
		System.out.println(Main.mainCam.zoom);
	}

	public static void updateCamPosition() {
		Main.setCam(player.locationTimer.getPair().round());
	}

	@Override
	public void update(float delta) {
		if(losing)return;
		if(Screen.isActiveType(EscapeMenu.class))return;
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

		batch.end();

		uiBatch.setProjectionMatrix(Main.uiCam.combined);
		uiBatch.begin();
		Parastar.render(uiBatch, Main.getCam(), Hex.size/200f);
		uiBatch.end();


		batch.begin();
		grid.render(batch);
		batch.end();

		//Black Hole//

		Main.shape.begin(ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Main.shape.setColor(0,0,0,1);
		Main.shape.circle(explosion.getPixel().x,explosion.getPixel().y, (explosionSize+progress*(growthRate/2))*Hex.height);
		Main.shape.end();





		uiBatch.setProjectionMatrix(Main.uiCam.combined);
		uiBatch.begin();
		uiBatch.setColor(1,1,1,1);
		uiBatch.draw(Gallery.mapslice.get(), 0, 0);

		Draw.drawScaled(uiBatch, Gallery.mapsliceRight.get(), Main.width, Main.height	, -1, -1);
		uiBatch.setColor(Colours.white);
		Font.medium.setColor(Colours.light);
		Font.medium.draw(uiBatch, ""+getState(), 500, 0);
		for(MapAbility a:player.mapAbilities)a.render(uiBatch);
		batch.setColor(1,1,1,1);
		for(SidePanel p:panels)p.render(uiBatch);

		if(currentEvent!=null)currentEvent.render(uiBatch);
		if(currentBase!=null)currentBase.render(uiBatch);
		
		if(losing){
			Font.big.setColor(Colours.light);
			Font.drawFontCentered(uiBatch, "You lose", Font.big, Main.width/2, Main.height/2);
		}
		uiBatch.end();



		batch.begin();


	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void scroll(int amount) {
		if(currentBase!=null)currentBase.scroll(amount);
	}

	@Override
	public void dispose() {
	}

	public static void mouseOverHex(Hex h){
		hexInfoPanel.hexMouse(h);
	}

	public static void updateActionPanel(Hex h){
		actionPanel.init(h);
	}

	public static void incrementExplosionSize() {
		explosionSize+=Map.growthRate/2;
	}

	public static float getExplosionSize(){
		return explosionSize;
	}

	public static void popup(Popup p){
		setState(MapState.Popup);
		currentEvent=p;
	}

	public static void toggleEquip() {
		if(getState()==MapState.Equipping){
			closeBase();
			return;
		}
		setState(MapState.Equipping);
		currentBase=new Equip();
	}

	public static void mouseStats(InfoBox info) {
		if(currentBase!=null){
			currentBase.mouseInfo(info);
		}
	}
	public static void unMouse() {
		if(currentBase!=null){
			currentBase.unMouse();
		}
	}
	public static void mouseItem(Item i){
		if(currentBase!=null){
			currentBase.mouseItem(i);
		}
	}
	public static void selectItem(Item i){
		if(currentBase!=null){
			currentBase.select(i);
		}

	}

	public static void closeBase() {
		returnToPlayerTurn();
		currentBase.dispose();
		currentBase=null;
	}

	public static void lose(){
		me.losing=true;
		Timer.event(2, new Finisher() {
			
			@Override
			public void finish() {
				Main.changeScreen(new Menu() );
			}
		});
		
		
	}

}
