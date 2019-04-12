package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class TankBase extends Entity {
    private Vector3 direction;
    private Tank owner;


    public TankBase(ModelInstance modelInstance,Tank t) {
        super(modelInstance);
        this.owner = t;
        this.id =1;
    }

    public Vector3 getDirection() {
        if (direction != null) {
            return new Vector3(direction);
        }else{
            return null;
        }
    }

    public void setDirection(Vector3 direction) {
        this.direction = direction.nor();
    }

    public Tank getOwner() {
        return owner;
    }

}