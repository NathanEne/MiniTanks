package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class TankBase extends Entity {
    private Vector3 direction;
    private Tank owner;
   // private Vector3 previousPosition;
    public TankBase(ModelInstance modelInstance,Tank t) {
        super(modelInstance);
       // previousPosition = new Vector3();
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

//    public Vector3 getPreviousPosition() {
//        return new Vector3(previousPosition);
//    }
//
//    public void setPreviousPosition(Vector3 previousPosition) {
//        this.previousPosition = new Vector3(previousPosition);
//    }

    public Tank getOwner() {
        return owner;
    }

}