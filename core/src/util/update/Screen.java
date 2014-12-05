package util.update;

import java.util.ArrayList;

import game.Main;
import game.screen.escape.EscapeMenu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import util.maths.Pair;

public abstract class Screen {
	private ArrayList<Updater> savedUpdaters= new ArrayList<Updater>();
	private ArrayList<Mouser> savedMousers= new ArrayList<Mouser>();
	
	public void saveAll(){
		savedUpdaters=Updater.getAllUpdaters();
		savedMousers=Mouser.getAllMousers();
		for(Updater u:savedUpdaters)System.out.println(u);
		for(Updater u:savedMousers)System.out.println(u);
	}
	public void restoreAll(){
		Updater.addList(savedUpdaters);
		Mouser.addList(savedMousers);
		savedUpdaters.clear();
		savedMousers.clear();
	}
	public abstract void init();
	public abstract void dispose();
	public abstract void update(float delta);
	public abstract void shapeRender(ShapeRenderer shape);
	public abstract void render(SpriteBatch batch);
	public abstract void postRender(SpriteBatch batch);
	public abstract boolean keyPress(int keycode);
	public abstract void keyUp(int keyCode);
	public abstract void mousePressed(Pair location, boolean left);
	public abstract void scroll(int amount);
	public static boolean isActiveType(Class<?> cl){
		if(EscapeMenu.get().active)return cl==EscapeMenu.class;
		if(Main.currentScreen==null)return false;
		return Main.currentScreen.getClass()==cl;
	}
}
