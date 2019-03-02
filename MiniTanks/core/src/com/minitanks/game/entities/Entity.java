package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public abstract class Entity {

    private Vector3 position;
    private ModelInstance model;

    public ModelInstance getModelInstance() {
        return model;
    }

    public void setModel(ModelInstance model) {
        this.model = model;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }



    public Entity(ModelInstance modelInstance){
        this.model = modelInstance;
    }

    public Entity(){}


    public void update(float delta){

    }

}
