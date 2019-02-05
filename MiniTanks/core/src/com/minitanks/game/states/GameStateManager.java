package com.minitanks.game.states;

import com.badlogic.gdx.graphics.g3d.ModelBatch;

import java.util.Stack;

public class GameStateManager {
    private Stack<State> states;

    public GameStateManager() {
        states = new Stack<State>();
    }

    public void push(State state) {
        states.push(state);
    }

    public void pop() {
        states.pop().dispose();

    }

    public void set(State state) {
        states.pop();
        states.push(state);

    }

    public void update(float dt) {
        states.peek().update(dt);
    }

    public void render(ModelBatch mb) {
        states.peek().render(mb);
    }
    public State getInstaceOfState(){
        return states.lastElement();
    }
}
