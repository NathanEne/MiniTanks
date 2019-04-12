package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Wall extends Entity{

    private float angle;

    public Wall(ModelInstance modelInstance, float posX, float posZ, float radRotation){
        super(modelInstance);
        getModelInstance().transform.set(new Vector3(posX, 0, posZ), getModelInstance().transform.getRotation(new Quaternion()));
        getModelInstance().transform.scl(0.5f, 5f, 0.5f);
        getModelInstance().transform.rotateRad(Vector3.Y, radRotation);
        this.id = 5;
        this.angle = radRotation;
    }

    /**
     *
     * @param modelInstance
     * @param radRotation
     * Initializes a wall as a rectangle rotated on the given angle
     *
     */

    public Wall(ModelInstance modelInstance, float radRotation){
        super(modelInstance);
        this.id = 5;
        this.angle = radRotation;
    }
}
