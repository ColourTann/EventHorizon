package eh.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import game.Main;
import game.assets.Gallery;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=Main.width;
		config.height=Main.height;
		config.foregroundFPS=0;
		config.vSyncEnabled=false;
		config.title="Event Horizon";
		config.addIcon("Ship/Eclipse/computer.png", FileType.Internal);
		config.resizable=false;
		new LwjglApplication(new Main(), config);
	}
}
