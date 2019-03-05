package com.minitanks.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Quaternion;

public class TankBase extends Entity {

    private float lerpRate = 3.141592654f / 36f; // Radians per frame
    // The rotation of the tank in radians
    private float currRotation = 0;

    public TankBase(ModelInstance modelInstance) {
        super(modelInstance);
    }
    public float getCurrRot(){return currRotation;}

    /**
     * Called every single frame for the base to rotate towards
     *
     *
     * @param endRad the desired rotation to move to
     */
    public void rotateTowards(float endRad){
        float modRot = currRotation % 2*(float)Math.PI;
        float ep = 0.01f;

        endRad += Math.PI / 2;

        System.out.println("End radian");
        System.out.println(endRad);
        System.out.println("Curr Rotation");
        System.out.println(modRot);

        if (currRotation > 4*(float)Math.PI)
            currRotation -= 2*(float)Math.PI;

        if (endRad - ep < modRot && endRad + ep > modRot)
            return;

        if (modRot - endRad <= Math.PI && modRot > endRad){
            getModelInstance().transform.rotateRad(Vector3.Y, -lerpRate);
            currRotation = getModelInstance().transform.getRotation(new Quaternion()).getAngleAroundRad(Vector3.Y);

        }

        else{
            getModelInstance().transform.rotateRad(Vector3.Y, lerpRate);
            currRotation = getModelInstance().transform.getRotation(new Quaternion()).getAngleAroundRad(Vector3.Y);
        }

        if (Gdx.input.isButtonPressed(Input.Keys.P)){
            System.out.println(endRad);
            System.out.println(modRot);
        }
    }
}