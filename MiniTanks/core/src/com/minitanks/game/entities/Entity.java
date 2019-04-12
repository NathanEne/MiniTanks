package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;


public abstract class Entity {
    public int getId() {
        return id;
    }

    protected int id;
    private btCollisionObject body;
    private Vector3 position;
    private ModelInstance model;
    private boolean hasBody = false;
    private boolean isAI = false;


    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }


    private MyMotionState motionState;

    public btCollisionObject getBody() {
        return body;
    }

    public void setBody(btCollisionObject body) {
        this.body = body;
        hasBody =true;
    }

    public ModelInstance getModelInstance() {
        return model;
    }

    public void setModel(ModelInstance model) {
        this.model = model;
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

    public boolean hasBody() {
        return hasBody;
    }
}
