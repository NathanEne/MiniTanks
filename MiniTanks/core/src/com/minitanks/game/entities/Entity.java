package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class Entity {

    private Vector3 position;
    private Model model;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }



    public Entity(){

    }

    public abstract void render(ModelBatch batch);

    public abstract void move();

    public void update(float delta){

    }

}
