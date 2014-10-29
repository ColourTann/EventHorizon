package game.screen.escape;



import java.util.ArrayList;



















import javax.crypto.spec.PSource;

import util.Colours;
import util.Draw;
import util.TextWriter;
import util.TextWriter.Alignment;
import util.assets.Font;
import util.assets.MusicClip;
import util.assets.SoundClip;
import util.maths.Pair;
import util.update.Screen;
import util.update.SimpleButton;
import util.update.SimpleButton.Code;
import util.update.SimpleSlider;
import util.update.Timer;
import util.update.Timer.Interp;
import util.update.Updater;
import util.update.Updater.Layer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import game.Main;
import game.assets.AdvancedButton;
import game.assets.Gallery;
import game.assets.Sounds;
import game.assets.TextBox;
import game.screen.menu.Menu;

public class EscapeMenu extends Screen{
	static final float width=370, height=300;
	static final int buttonWidth=130, buttonY=448;
	static final int buttonSeparation=70;
	
	public static EscapeMenu me;
	Pair position;
	public Timer alphaTimer= new Timer();
	public boolean active;
	public ArrayList<AdvancedButton> buttons= new ArrayList<AdvancedButton>();
	public static final float sliderGap=70;
	public static final float sliderWidth=280, sliderHeight=30;
	public static final Pair sliderPosition= new Pair(Main.width/2-sliderWidth/2, 400);
	TextWriter creditsWriter;
	SimpleSlider sfx = new SimpleSlider(sliderPosition, (int)(sliderWidth), (int)(sliderHeight), Colours.grey, Colours.backgrounds1[0], 3, SoundClip.soundLevel);
	SimpleSlider music = new SimpleSlider(sliderPosition.add(0,-sliderGap), (int)(sliderWidth), (int)sliderHeight, Colours.grey, Colours.backgrounds1[0], 3, MusicClip.musicLevel);
	public EscapeMenu(){
		position=new Pair(Main.width/2-width/2, Main.height/2-height/2);
		Font.medium.setColor(Colours.light);
		buttons.add(new AdvancedButton(new Pair(Main.width/2-buttonSeparation-buttonWidth/2,buttonY), new Pair(buttonWidth,0), "Quit", Font.medium, new Code() {
			@Override
			public void onPress() {
				deactivate();
				Sounds.battleMusic.fadeOut(.3f);
				Main.changeScreen(new Menu());
			}
		}));
		
		buttons.add(new AdvancedButton(new Pair(Main.width/2+buttonSeparation-buttonWidth/2,buttonY), new Pair(buttonWidth,0), "Resume", Font.medium, new Code() {
			@Override
			public void onPress() {
				deactivate();
			}
		}));
		
		for(AdvancedButton b:buttons){
			b.layer=Layer.Escape;
		}
		music.layer=Layer.Escape;
		sfx.layer=Layer.Escape;
		Font.medium.setColor(Colours.light);
		creditsWriter=new TextWriter(Font.medium, "Art: @scutanddestroy|n|Music: @thewillformusic|n|Code & bad art: @3CGames");
		creditsWriter.setWrapWidth((int)width-10);
		creditsWriter.height+=8;
		creditsWriter.setAlignment(Alignment.Center);
		creditsWriter.setupTexture();
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
		TextBox.renderBox(batch, position, width, height, Alignment.Left);
		//Draw.drawCenteredScaled(batch, Gallery.pauseBase.get(), Main.width/2, Main.height/2, 1, 1);
		for(AdvancedButton ab:buttons){
			ab.render(batch);
		}
		music.render(batch);
		sfx.render(batch);
		Font.medium.setColor(Colours.withAlpha(Colours.light, batch.getColor().a));
		
		Font.drawFontCentered(batch, "Sound Effects", Font.medium, Main.width/2, sliderPosition.y-15);
		Font.drawFontCentered(batch, "Music", Font.medium, Main.width/2, sliderPosition.y-15-sliderGap);
		batch.setColor(1, 1, 1, alphaTimer.getFloat());
		creditsWriter.render(batch, (int)(position.x+5), (int)(position.y+15));
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
		System.out.println("cycling");
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
		Main.fadeTimer=new Timer(Main.fadeTimer.getFloat(), .8f, 1/4f, Interp.LINEAR);
		alphaTimer=new Timer(alphaTimer.getFloat(), 1, 1/4f, Interp.SQUARE);
		alphaTimer.layer=Layer.ALL;
		Main.fadeTimer.layer=Layer.ALL;
	}
	public void fadeOut(){
		Main.fadeTimer=new Timer(Main.fadeTimer.getFloat(), 0, 1/4f, Interp.LINEAR);
		alphaTimer=new Timer(alphaTimer.getFloat(), 0, 1/4f, Interp.SQUARE);
		alphaTimer.layer=Layer.ALL;
		Main.fadeTimer.layer=Layer.ALL;
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

	@Override
	public void dispose() {
	}

	

}
