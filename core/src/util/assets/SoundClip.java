package util.assets;

import util.Option;
import util.update.Updater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundClip{
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
		
		get().stop();
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


