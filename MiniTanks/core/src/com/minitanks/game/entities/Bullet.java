package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Bullet extends Entity {

    private float speed;
    private Vector3 direction;

    public Bullet(ModelInstance modelInstance, Vector3 dir, float speed){
        super(modelInstance);
        this.speed = speed;
        this.direction = dir;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vector3 getDirection() {
        return new Vector3(direction);
    }


    public void setDirection(Vector3 direction) {
        this.direction = direction.nor();
    }


}
