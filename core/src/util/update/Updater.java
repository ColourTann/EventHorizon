package util.update;

import java.util.ArrayList;

import util.maths.Pair;
import util.particleSystem.ParticleSystem;
import util.update.Timer.Interp;

public abstract class Updater {
	private static ArrayList<Updater> tickers = new ArrayList<Updater>();
	
	static boolean debug=false;
	public enum Layer{Default, Escape, ALL}
	private static Layer currentLayer=Layer.Default;
	

	public Layer layer=Layer.Default;
	Timer fader;
	public float alpha=1;
	Timer slider;
	public Pair position=new Pair(0,0);
	public boolean dead;
	
	
	public Updater(){
		activate();
	}
	
	public abstract void update(float delta);
	
	public void activate(){
		dead=false;
		stopFading();
		alpha=1;
		tickers.remove(this);
		tickers.add(this);
	}

	public void deactivate(){
		dead=true;
		tickers.remove(this);
	}
	
	public void fadeIn(float seconds, Interp type){
		activate();
		fader=new Timer(alpha, 1, seconds, type);
	}

	public void fadeOut(float seconds, Interp type){
		activate();
		Mouser.mousers.remove(this); //Just in case//
		fader=new Timer(alpha, 0, seconds, type);
	}

	public void stopFading(){
		fader=null;
		alpha=1;
	}

	public void slide(Pair target, float seconds, Interp type){
		slider=new Timer(position, target, seconds, type);
	}
	
	public static void updateAll(float delta) {
		
		updateActives(delta);
		Mouser.updateMoused();
		ParticleSystem.updateAllSystems(delta);
		if(debug){
			System.out.println("Mouseable count: "+Mouser.mousers.size());
			System.out.println("Ticker count: "+tickers.size());
			System.out.println("end of list");
			System.out.println();
		}
	}

	private static void updateActives(float delta){
		
		for(int i=0;i<tickers.size();i++){
			Updater u = tickers.get(i);
			if(u.dead){
				tickers.remove(u);
				TextWisp.wisps.remove(u);
				i--;
			}
		}
		
		for(int i=0;i<tickers.size();i++){
			Updater u = tickers.get(i);
			if(u.layer!=currentLayer&&u.layer!=Layer.ALL)continue;
			
			
			
			u.update(delta);
			if(u.fader!=null){
				u.alpha=u.fader.getFloat();
				if(u.alpha<0){
					u.alpha=0;
					u.dead=true;
				}
			}
			if(u.slider!=null){
				u.position=u.slider.getPair();
			}
		}
		

	}

	public static void clearAll(){
		clearAllDefaults();
		Mouser.clearAllDefaultMousers();
		TextWisp.wisps.clear();
		ParticleSystem.clearAll();
	}
	
	private static void clearAllDefaults() {
		for(int i=0;i<tickers.size();i++){
			Updater u=tickers.get(i);
			if(u.layer==Layer.Default){
				tickers.remove(u);
				i--;
			}
		}
	}
	
	public static void setLayer(Layer l){
		currentLayer=l;
	}
	
	public static Layer getLayer(){
		return currentLayer;
	}
	
	
}
