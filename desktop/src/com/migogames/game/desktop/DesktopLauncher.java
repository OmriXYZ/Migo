package com.migogames.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = false;
		config.width = 960;
		config.height = 640;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;


		new LwjglApplication(new Boot(), config);
	}
}
