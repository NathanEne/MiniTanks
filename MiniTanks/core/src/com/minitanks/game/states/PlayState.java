package com.minitanks.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.minitanks.world.GameMap;
import com.minitanks.world.TiledGameMap;

public class PlayState extends State {



    private GameMap map;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        Gdx.gl.glClearColor(0f / 255f, 0f / 255f, 0f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.map = new TiledGameMap();
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

    public GameMap getMap() {
        return map;
    }

}
