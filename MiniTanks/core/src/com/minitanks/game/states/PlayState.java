package com.minitanks.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class PlayState extends State {


    public PlayState(GameStateManager gsm) {
        super(gsm);
        Gdx.gl.glClearColor(0f / 255f, 0f / 255f, 0f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        State.cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        State.cam.update();

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(ModelBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
