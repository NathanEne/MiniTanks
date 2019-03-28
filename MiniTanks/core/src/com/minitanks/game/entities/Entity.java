package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;


public abstract class Entity {

    private btCollisionObject body;
    private Vector3 position;
    private ModelInstance model;

    public btCollisionObject getBody() {
        return body;
    }

    public void setBody(btCollisionObject body) {
        this.body = body;
    }

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
        body = new btCollisionObject();
        body.setCollisionShape(new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f)));
    }

    public Entity(){}


    public void update(float delta){

    }

}
