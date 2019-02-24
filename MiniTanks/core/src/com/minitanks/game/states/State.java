package com.minitanks.game.states;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.minitanks.game.managers.AssetManager;

public abstract class State {

    public PerspectiveCamera cam;
    public OrthographicCamera camOrth;
    public GameStateManager gsm;
    public AssetManager assets;
    protected State(GameStateManager gsm) {
        this.assets = new AssetManager();
        this.gsm = gsm;
    }


    protected abstract void handleInput();

    public abstract void update(float dt);

    public abstract void render(ModelBatch sb);

    public abstract void dispose();

}
