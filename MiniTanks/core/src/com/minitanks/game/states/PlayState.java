package com.minitanks.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.minitanks.world.GameMap;
import com.minitanks.world.TiledGameMap;

public class PlayState extends State {



    private GameMap map;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        Gdx.gl.glClearColor(175f / 255f, 175f / 255f, 175f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.map = new TiledGameMap();
        State.cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        State.cam.position.set(5, 5, 1);
        State.cam.update();

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            State.cam.position.set(State.cam.position.x,State.cam.position.y+5,1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            State.cam.position.set(State.cam.position.x,State.cam.position.y-5,1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            State.cam.position.set(State.cam.position.x-5,State.cam.position.y,1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            State.cam.position.set(State.cam.position.x+5,State.cam.position.y,1);
        }

    }

    @Override
    public void update(float dt) {
    handleInput();
    }

    @Override
    public void render(ModelBatch sb) {

        State.cam.update();
        this.getTiledMap().update(Gdx.graphics.getDeltaTime());
        this.getTiledMap().render(State.cam, sb);
    }

    @Override
    public void dispose() {

    }

    public GameMap getTiledMap() {
        return map;
    }

}
