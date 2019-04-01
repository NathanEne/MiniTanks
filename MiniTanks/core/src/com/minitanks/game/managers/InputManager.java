package com.minitanks.game.managers;

import com.badlogic.gdx.InputProcessor;
import com.minitanks.game.states.PlayState;


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
        curr_playState.getPlayer().Shoot();
        System.out.println(curr_playState.getMouseInputVector());
        return false;
    }

    public boolean touchUp(int k, int k1, int k2, int k3){
        return false;
    }

    public boolean touchDragged(int k, int k1, int k2){
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY){
        curr_playState.setMouseInputVect(screenX, screenY);
        return false;
    }

    public boolean scrolled (int amount){
        return false;
    }
}
