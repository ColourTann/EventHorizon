package game.screen.customise;

import java.util.ArrayList;

import game.Main;
import game.module.Module;
import game.module.component.Component;
import game.module.component.shield.Deflector;
import game.module.component.weapon.Ray;
import game.module.junk.ModuleInfo;
import game.module.junk.ModuleStats;
import game.module.utility.FluxAlternator;
import game.ship.Ship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import util.Draw;
import util.maths.Pair;
import util.update.Screen;
import util.update.Timer.Interp;

public class Customise extends Screen{
	static float fadeInSpeed=0f;
	static float fadeOutSpeed=.3f;
	Ship ship;
	public ArrayList<ModuleStats> stats=new ArrayList<ModuleStats>();
	public ArrayList<Reward> rewards=new ArrayList<Reward>();
	public static Reward selectedReward;
	ModuleInfo infoBox;
	ModuleInfo oldInfoBox;
	private static Customise me;
	public Customise(Ship s){
		me=this;
		this.ship=s;
		
	}
	
	@Override
	public void init() {
		for(Component c:ship.components){
			stats.add(new ModuleStats(c));
		}
		rewards.add(new Reward(new Deflector(1), 0));
		rewards.add(new Reward(new Ray(1), 1));
		rewards.add(new Reward(new FluxAlternator(1), 2));
	}

	public static void changeStats(ModuleInfo info){
		if(info==null){
			
			me.infoBox.fadeOut(fadeOutSpeed, Interp.LINEAR);
			return;
		}
		
		/*if(info==me.infoBox){
			me.infoBox.fadeIn(.5f, Interp.LINEAR);
			return;
		}*/
		if(me.infoBox!=null){
		me.oldInfoBox=me.infoBox;
		me.oldInfoBox.fadeOut(fadeOutSpeed, Interp.LINEAR);
		}
		info.fadeIn(fadeInSpeed, Interp.LINEAR);
		info.setPosition(new Pair(Main.width/2,280));
		me.infoBox=info;
	}
	
	public void mouseOver(Component component) {
		changeStats(new ModuleInfo(component));
	}
	
	public static void deselect() {
		me.selectedReward=null;
	}
	
	public static void unMouse(Module module){
			if(me.infoBox.mod==module){
				me.oldInfoBox=me.infoBox;
				me.oldInfoBox.fadeOut(fadeOutSpeed, Interp.LINEAR);
				
			}
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
	}

	@Override
	public void render(SpriteBatch batch) {
		for(ModuleStats ms:stats) ms.render(batch);
		
		for(Reward r:rewards) r.render(batch);
		batch.setColor(1, 1, 1, 1);
		Draw.drawCentered(batch, ship.getGraphic().composite.get(), Main.width/2, 140);
		
		if(infoBox!=null)infoBox.render(batch);
		if(oldInfoBox!=null)oldInfoBox.render(batch);
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void keyPress(int keycode) {
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

	public static void select(Reward reward) {
		if(me.selectedReward!=null)me.selectedReward.deselect();
		me.selectedReward=reward;
	}

	

	

}
