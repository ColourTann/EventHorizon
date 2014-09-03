package util.assets;

import util.Option;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Clip{
	
	//general
	
	public static Clip error= new Clip("Sound/error.ogg");
	public static Clip shieldUse= new Clip("Sound/shielduse.ogg");
	public static Clip shieldActivate=new Clip("Sound/shieldactivate.wav");
	public static Clip cardFlip= new Clip("Sound/flip.ogg");
	public static Clip cardDeselect= new Clip("Sound/deselectcard.ogg");
	public static Clip cardSelect= new Clip("Sound/selectcard.ogg");
	public static Clip damageMinor=new Clip("Sound/minordamage.ogg");
	public static Clip damageMajor=new Clip("Sound/majordamage.ogg");
	
	public static Clip pulse=new Clip("Sound/pulse.ogg");
	public static Clip ray=new Clip("Sound/ray.ogg");
	public static Clip laser=new Clip("Sound/laser.ogg");
	public static Clip lightning=new Clip("Sound/lightning.ogg");
	
	
	
	public static Option soundLevel=new Option(1);
	
	private Sound sound;
	String path;
	float fadeSpeed=0;
	float volumeMultiplier=1;
	float baseVolume=1;
	public Clip(String path){
		this.path=path;
	}
	public Clip(String path, float baseVolume){
		this.path=path;
		this.baseVolume=baseVolume;
	}
	public Sound get(){
		if(sound==null)sound=Gdx.audio.newSound(Gdx.files.internal(path));
		return sound;
	}
	public void play(){
		get().stop();
		get().play(getVolume());
	}
	public void overlay(){
		get().play(getVolume());	
	}
	public float getVolume(){
		return soundLevel.getFloat()*volumeMultiplier*baseVolume;
	}
	
}
