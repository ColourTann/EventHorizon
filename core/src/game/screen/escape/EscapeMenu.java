package game.screen.escape;



import java.util.ArrayList;







import util.Draw;
import util.maths.Pair;
import util.update.Screen;
import util.update.SimpleButton;
import util.update.SimpleButton.Code;
import util.update.Timer;
import util.update.Timer.Interp;
import util.update.Updater;
import util.update.Updater.Layer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import game.Main;
import game.Main.ScreenType;
import game.assets.Gallery;

public class EscapeMenu extends Screen{
	
	public static EscapeMenu me;
	
	public Timer alphaTimer= new Timer();
	public boolean active;
	public ArrayList<SimpleButton> buttons= new ArrayList<SimpleButton>();
	public EscapeMenu(){
		
		buttons.add(new SimpleButton(new Pair(Main.width/2-120-Gallery.shitButton.get().getWidth()/2,400), "Quit", Gallery.shitButton, new Code() {
			@Override
			public void onPress() {
				deactivate();
				Main.changeScreen(ScreenType.Menu);
			}
		}));
		
		buttons.add(new SimpleButton(new Pair(Main.width/2+120-Gallery.shitButton.get().getWidth()/2,400), "OK!", Gallery.shitButton, new Code() {
			@Override
			public void onPress() {
				deactivate();
			}
		}));
		
		for(SimpleButton b:buttons){
			b.layer=Layer.Escape;
		}
	}
	
	@Override
	public void update(float delta) {
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
		if(alphaTimer.getFloat()==0)return;
		shape.setColor(1, 1, 1, alphaTimer.getFloat());
		shape.begin(ShapeType.Filled);
		shape.rect(50, 50, 50, 50);
		shape.end();
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(1, 1, 1, alphaTimer.getFloat());
		Draw.drawTextureScaledCentered(batch, Gallery.pauseBase.get(), Main.width/2, Main.height/2, 1, 1);
		for(SimpleButton sb:buttons){
			sb.render(batch);
		}
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
		
	public void cycle() {
		if(active)deactivate();
		else activate();
	}
	
	public void activate(){
		Updater.setLayer(Layer.Escape);
		fadeIn();
		active=true;
	}
	
	public void deactivate(){
		Updater.setLayer(Layer.Default);
		fadeOut();
		active=false;
	}
	
	public void fadeIn(){
		Main.fadeTimer=new Timer(Main.fadeTimer.getFloat(), .8f, 4, Interp.LINEAR);
		alphaTimer=new Timer(alphaTimer.getFloat(), 1, 4, Interp.SQUARE);
	}
	public void fadeOut(){
		Main.fadeTimer=new Timer(Main.fadeTimer.getFloat(), 0, 4, Interp.LINEAR);
		alphaTimer=new Timer(alphaTimer.getFloat(), 0, 4, Interp.SQUARE);
	}
	
	public static EscapeMenu get(){
		if(me==null)me=new EscapeMenu();
		return me;
	}

	@Override
	public void scroll(int amount) {
	}

	@Override
	public void init() {
	}

	

}
