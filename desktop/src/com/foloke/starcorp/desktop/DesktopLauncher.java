package com.foloke.starcorp.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.foloke.starcorp.StarCorpGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 400;
		config.foregroundFPS = 60;
		config.forceExit = false;
		new LwjglApplication(new StarCorpGame(), config);
	}
}
