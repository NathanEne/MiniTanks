package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Wall extends Entity{
    public Wall(ModelInstance modelInstance, int posX, int posZ, float sclW, float sclH){
        super(modelInstance);
        getModelInstance().transform.set(new Vector3(posX, 0, posZ), getModelInstance().transform.getRotation(new Quaternion()));
        getModelInstance().transform.scl(sclW, 1, sclH);
        this.id=5;
    }
}
