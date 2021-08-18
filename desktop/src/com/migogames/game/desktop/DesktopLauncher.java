package com.migogames.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = false;
		config.width = (int)(960*1.5f);
		config.height = (int)(640*1.5f);
		config.foregroundFPS = 80;
		config.backgroundFPS = 80;
		config.resizable = false;


		new LwjglApplication(new Boot(), config);
	}
}
