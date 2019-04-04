package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Wall extends Entity{
    public Wall(ModelInstance modelInstance, float posX, float posZ, float radRotation){
        super(modelInstance);
        getModelInstance().transform.set(new Vector3(posX, 0, posZ), getModelInstance().transform.getRotation(new Quaternion()));
        getModelInstance().transform.scl(0.5f, 5f, 0.5f);
        getModelInstance().transform.rotateRad(Vector3.Y, radRotation);
        this.id = 5;
    }

    public Wall(ModelInstance modelInstance){
        super(modelInstance);
        this.id = 5;
    }
}
