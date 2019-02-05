package com.minitanks.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public abstract class State {

    public static OrthographicCamera cam=new OrthographicCamera();
    public GameStateManager gsm;

    protected State(GameStateManager gsm) {
        this.gsm = gsm;
    }


    protected abstract void handleInput();

    public abstract void update(float dt);

    public abstract void render(ModelBatch sb);

    public abstract void dispose();

}
