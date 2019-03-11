package com.minitanks.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Quaternion;
import com.minitanks.game.states.PlayState;

public class TankBase extends Entity {

    private float lerpRate = 3.141592654f / 36f; // Radians per frame
    private boolean lerping = false;
    // The rotation of the tank in radians
    private float currRotation = 0;
    private PlayState playState;

    public TankBase(ModelInstance modelInstance, PlayState playst) {
        super(modelInstance);
        this.playState = playst;
    }
    public float getCurrRot(){return currRotation;}

    /**
     * Called every single frame for the base to rotate towards
     *
     *
     * @param endRad the desired rotation to move to
     */
    public void rotateTowards(float endRad){
        if (playState.getMouseInputVector() != Vector3.Zero){
            //Lerp(endRad);
            getModelInstance().transform.rotateRad(Vector3.Y, endRad - currRotation);
            currRotation = endRad;
        }

    }

    private void Lerp(float endRad){
        if (((currRotation + Math.PI) % (2*Math.PI) > endRad) && (currRotation % (Math.PI*2)) < endRad){
            if (currRotation + lerpRate > endRad){
                getModelInstance().transform.rotateRad(Vector3.Y, endRad - currRotation);
                currRotation = endRad;
            }
            else{
                getModelInstance().transform.rotateRad(Vector3.Y, lerpRate);
                currRotation += lerpRate;
            }
        }
        else{
            if (currRotation - lerpRate < endRad){
                getModelInstance().transform.rotateRad(Vector3.Y, currRotation - endRad);
                currRotation = endRad;
            }
            else{
                getModelInstance().transform.rotateRad(Vector3.Y, -lerpRate);
                currRotation -= lerpRate;
            }
        }



    }
}