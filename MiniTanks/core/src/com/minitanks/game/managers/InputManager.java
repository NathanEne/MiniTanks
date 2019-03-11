package com.minitanks.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.minitanks.game.states.PlayState;
import javafx.print.PageLayout;

public class InputManager implements InputProcessor {

    private PlayState curr_playState;
    public InputManager(PlayState state){
        this.curr_playState = state;
    }

    public boolean keyDown(int k){
        return false;
    }
    public boolean keyUp(int k){
        return false;
    }

    public boolean keyTyped(char k){
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        curr_playState.getPlayer().Shoot(screenX, screenY);
        return false;
    }

    public boolean touchUp(int k, int k1, int k2, int k3){
        return false;
    }

    public boolean touchDragged(int k, int k1, int k2){
        return false;
    }

    public boolean mouseMoved(int k, int k2){
        return false;
    }

    public boolean scrolled (int amount){
        return false;
    }
}
