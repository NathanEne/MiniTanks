package com.minitanks.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.minitanks.game.managers.AssetManager;
import com.minitanks.game.managers.InputManager;

public abstract class State {

    public PerspectiveCamera cam;
    public OrthographicCamera camOrth;
    public GameStateManager gsm;
    public AssetManager assets;
    public ModelBatch models;

    protected State(GameStateManager gsm) {
        this.assets = new AssetManager();
        this.gsm = gsm;
    }


    protected abstract void handleInput();

    public abstract void update(float dt);

    public abstract void render(ModelBatch sb);

    public abstract void dispose();

    // Every game state must set their own implementation of InputProcessor.
    public abstract void setInputProcessor();
}
