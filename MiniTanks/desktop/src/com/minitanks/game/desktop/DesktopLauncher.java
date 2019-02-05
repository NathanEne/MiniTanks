package com.minitanks.game.desktop;

import com.badlogic.gdx.ApplicationListener;

import javafx.scene.PerspectiveCamera;

public class DesktopLauncher implements ApplicationListener {

    public PerspectiveCamera cam;

	@Override
    public void create(){
        cam = new PerspectiveCamera(true);
    }

    @Override
    public void render(){

    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int w, int h){

    }

    @Override
    public void pause(){

    }

    @Override
    public void resume(){

    }
}
