package util.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Clip {
	
	public static Clip error= new Clip("Sound/error.ogg");
	public static Clip shieldUse= new Clip("Sound/shielduse.ogg");
	public static Clip cardFlip= new Clip("Sound/flip.ogg");
	public static Clip cardDeselect= new Clip("Sound/deselectcard.ogg");
	public static Clip cardSelect= new Clip("Sound/selectcard.ogg");
	public static Clip damageMinor=new Clip("Sound/minordamage.ogg");
	public static Clip damageMajor=new Clip("Sound/majordamage.ogg");
	
	public static Clip pulse=new Clip("Sound/pulse.ogg");
	public static Clip ray=new Clip("Sound/ray.ogg");
	public static Clip laser=new Clip("Sound/laser.ogg");
	public static Clip lightning=new Clip("Sound/lightning.ogg");
	
	private Sound sound;
	String path;
	public Clip(String path){
		this.path=path;
	}
	public Sound getClip(){
		if(sound==null)sound=Gdx.audio.newSound(Gdx.files.internal(path));
		return sound;
	}
	public void play(){
		getClip().stop();
		getClip().play();
	}
	public void overlay(){
		getClip().play();
	}
}
