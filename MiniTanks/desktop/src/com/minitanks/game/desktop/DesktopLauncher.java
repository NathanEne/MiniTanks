package com.minitanks.game.desktop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.minitanks.game.MiniTanksGame;



public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width=1920;
        config.height=1080;

        new LwjglApplication(new MiniTanksGame(), config);
    }
}