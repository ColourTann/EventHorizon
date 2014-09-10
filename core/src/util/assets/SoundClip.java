package util.assets;

import java.util.ArrayList;

import util.Option;
import util.update.Updater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundClip{
	
	//general
	
	public static SoundClip error= new SoundClip("Sound/error.ogg");
	public static SoundClip shieldUse= new SoundClip("Sound/shielduse.ogg");
	public static SoundClip shieldActivate=new SoundClip("Sound/shieldactivate.ogg");
	public static SoundClip cardFlip= new SoundClip("Sound/flip.ogg");
	public static SoundClip cardDeselect= new SoundClip("Sound/deselectcard.ogg");
	public static SoundClip cardSelect= new SoundClip("Sound/selectcard.ogg");
	
	public static SoundClip damageMinor=new SoundClip("Sound/minordamage.ogg");
	public static SoundClip damageMajor=new SoundClip("Sound/majordamage.ogg");
	public static SoundClip shatter=new SoundClip("Sound/shatter.ogg");
	public static SoundClip explode=new SoundClip("Sound/explode.ogg");
	
	public static SoundClip pulse=new SoundClip("Sound/pulse.ogg");
	public static SoundClip ray=new SoundClip("Sound/ray.ogg");
	public static SoundClip laser=new SoundClip("Sound/laser.ogg");
	public static SoundClip lightning=new SoundClip("Sound/lightning.ogg");
	

	
	public static Option soundLevel=new Option(1);
	
	private Sound sound;
	String path;
	float fadeSpeed=0;
	float volumeMultiplier=1;
	float baseVolume=1;
	long currentId=-1;
	public SoundClip(String path){
		this.path=path;
	}
	public SoundClip(String path, float baseVolume){
		this.path=path;
		this.baseVolume=baseVolume;
	}
	public Sound get(){
		if(sound==null)sound=Gdx.audio.newSound(Gdx.files.internal(path));
		return sound;
	}
	public void play(){
		System.out.println(currentId);
		if(currentId>=0){
			new ClipFade(get(), currentId, getVolume(), 100);
		}
		
		currentId=get().play(getVolume());
	}
	public void overlay(){
		currentId=get().play(getVolume());	
	}
	public float getVolume(){
		return soundLevel.getFloat()*volumeMultiplier*baseVolume;
	}
	public void fadeOut(float spd){
		
		
	}
	
	public static class ClipFade extends Updater{
		Sound sound;
		long id;
		float currentVolume;
		float fadeSpeed;
		public ClipFade(Sound sound, long id, float currentVolume, float fadeSpeed){
			this.sound=sound;
			this.id=id;
			this.fadeSpeed=fadeSpeed;
			this.currentVolume=currentVolume;
		}
		@Override
		public void update(float delta) {
			currentVolume-=fadeSpeed*delta;
			if(currentVolume<=0){
				deactivate();
				sound.stop(id);
			}
			sound.setVolume(id, currentVolume);
		}
	}
	
}


