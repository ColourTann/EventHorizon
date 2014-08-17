package eh.util;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import eh.Main;
import eh.util.Timer.Interp;
import eh.util.maths.Collider;
import eh.util.maths.Pair;
import eh.util.particleSystem.ParticleSystem;

public abstract class Bonkject {
	
	public enum Layer{Default, Escape}
	public static Layer currentLayer=Layer.Default;
	
	public boolean dead;
	public Layer layer;
	static boolean debug=false;
	public static ArrayList<Bonkject> mousers = new ArrayList<Bonkject>();
	public static ArrayList<Bonkject> tickers = new ArrayList<Bonkject>();
	

	//Bonkjects are automatically added to update list but not mouse list//
	public Bonkject() {
		layer=currentLayer;
		bonktivate();
	}


	//Must call in your main loop//
	public static void updateBonkjs(float delta) {
		updateActives(delta);
		updateMoused();
		if(debug){
			System.out.println("Mouseable count: "+mousers.size());
			System.out.println("Ticker count: "+tickers.size());
		}
	}

	// UPDATING SHIT //
	
	//Call to re-add to update list//
	public void bonktivate(){
		tickers.remove(this);
		tickers.add(this);
	}
	
	public void debonktivate(){
		tickers.remove(this);
	}

	public static void updateActives(float delta){
		for(Bonkject b:tickers){
			if(b.layer!=currentLayer)continue;
			b.update(delta);
			if(b.fader!=null){
				b.alpha=b.fader.getFloat();
			}

			if(b.slider!=null){

				b.position=b.slider.getPair();
			}
		}
		for(int i=0;i<tickers.size();i++){
			Bonkject b = tickers.get(i);
			if(b.dead||b.alpha<=0){
				b.debonktivate();
				i--;
			}
		}

	}
	
	public abstract void update(float delta);

	public void debugRender(SpriteBatch batch){
		if(collider==null)return;
		batch.end();
		collider.debugDraw();
		batch.begin();
	}

	
	
	//Must call from InputListener as it deals with polled events//
	public static void updateClicked(boolean left){
		updateMousePosition();
		for(int i=0;i<mousers.size();i++){
			Bonkject checkMoused=mousers.get(i);
			if(checkMoused.layer!=currentLayer)continue;
			if(checkMoused.checkClicked(currentMoused, left))return;
		}
	}

	// MOUSING SHIT //
	
	public Collider collider;
	public boolean moused=false;
	public static Pair currentMoused;

	//Call this to add something to the mouse list//
	public void mousectivate(Collider collider){
		if(collider!=null)this.collider=collider;
		mousers.remove(this);
		mousers.add(this);
	}
	
	public void demousectivate(){
		mousers.remove(this);
		moused=false;
	}
	
	public static void updateMoused(){
		updateMousePosition();
		boolean found=false;
		for(int i=0;i<mousers.size();i++){
			
			Bonkject mouseCheck= mousers.get(i);
			if(mouseCheck.layer!=currentLayer)continue;
			
			if(found){
				mouseCheck.deMouse();
				continue;
			}
			found=mouseCheck.checkMoused(currentMoused);
		}
	}	

	private static void updateMousePosition(){
		currentMoused=new Pair(Gdx.input.getX()/(float)Gdx.graphics.getWidth()*(float)Main.width,(Gdx.input.getY()/(float)Gdx.graphics.getHeight()*(float)Main.height));
	}
	
	private boolean checkMoused(Pair s){
		if(collider.collidePoint(s)){
			if(!moused){
				moused=true;
				mouseDown();
			}
			return true;
		}
		if(moused){
			mouseUp();
			moused=false;
		}
		return false;
	}
	
	private void deMouse(){
		if(moused){
			mouseUp();
			moused=false;
		}
	}

	private boolean checkClicked(Pair s, boolean left){
		if(collider.collidePoint(s)){
			mouseClicked(left);
			return true;
		}
		return false;
	}

	public void moveToTop() {
		if(Bonkject.mousers.remove(this))Bonkject.mousers.add(0,this);
	}
	
	public abstract void mouseDown();
	public abstract void mouseUp();
	public abstract void mouseClicked(boolean left);



	// Helper methods //

	//If you want to use the default fading and lerping, you use these inside any bonkject//
	public float alpha=1;
	public Pair position=new Pair(0,0);

	Timer fader;
	Timer slider;

	public void fadeIn(float speed, Interp type){
		bonktivate();
		fader=new Timer(alpha, 1, speed, type);
	}

	public void fadeOut(float speed, Interp type){
		bonktivate();
		demousectivate();
		fader=new Timer(alpha, 0, speed, type);
	}

	public void stopFading(){
		fader=null;
		alpha=1;
	}

	public void slide(Pair target, float speed, Interp type){
		slider=new Timer(position, target, speed, type);
	}
	
	

	public static void clearAllDefaults() {
		for(int i=0;i<mousers.size();i++){
			Bonkject b=mousers.get(i);
			if(b.layer==Layer.Default){
				mousers.remove(b);
				i--;
			}
		}
		for(int i=0;i<tickers.size();i++){
			Bonkject b=tickers.get(i);
			if(b.layer==Layer.Default){
				tickers.remove(b);
				i--;
			}
		}
		ParticleSystem.clearAll();
	}

	public static void setLayer(Layer layer){
		currentLayer=layer;
	}
	
	//debug render is very laggy and will mess up if you're using cameras, might fix if can be bothered//
	public static void debugRenderAll(SpriteBatch batch) {
		batch.end();
		for(Bonkject b:mousers){
			b.collider.debugDraw();
		}
		batch.begin();
	}

}
