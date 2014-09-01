package eh.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import game.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=Main.width;
		config.height=Main.height;
		config.foregroundFPS=0;
		config.vSyncEnabled=false;
		new LwjglApplication(new Main(), config);
	}
}
