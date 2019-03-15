package com.minitanks.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.states.PlayState;

import java.lang.Math.*;

public class Tank extends Entity {
    private boolean isAI;
    private float movementSpeed = 10;
    private float bulletSpeed = 15f;
    private int numOfBullets = 5;
    private int numOfRicochets;
    private PlayState playState;

    // The vector to add to the tank base position for the turret position
    private Vector3 turretOffset = new Vector3(7,240,10);

    private TankBase tankBase;

    private Turret turret;

    private Vector3 turretDirection;

    public Vector3 getTurretDirection() { return turretDirection;    }

    public void setTurretDirection(Vector3 turretDirection) { this.turretDirection = turretDirection;    }

    public TankBase getTankBase() {
        return tankBase;
    }

    public Turret getTurret() {
        return turret;
    }

    public PlayState getPlayState() {
        return playState;
    }

    public Tank(Turret t, TankBase tb, PlayState plst) {
        this.turret = t;
        this.tankBase = tb;
        this.playState = plst;
        getTurret().getModelInstance().transform.scl(0.5f);
    }


    /**
     * Apply the movement for the tank
     *
     * @param keyInputVector A normalized direction for the tank to travel on this frame.
     */
    public void move(Vector3 keyInputVector, Vector3 mouseInput) {
        if (turret == null || tankBase == null)
            return;

        // Make the turret face mouse position
        turret.rotateToMouse(mouseInput);


        if (!keyInputVector.isZero()){
            // Linearly interpolate the rotation of the base
            float endRad = getEndRad(keyInputVector);
            getTankBase().rotateTowards(getEndRad(keyInputVector));
        }


        getTankBase().getModelInstance().transform.trn(keyInputVector.scl(movementSpeed));
        Vector3 tankPos = getTankBase().getModelInstance().transform.getTranslation(new Vector3());
        getTurret().getModelInstance().transform.set(tankPos.add(turretOffset), getTurret().getModelInstance().transform.getRotation(new Quaternion()));

    }



    /**
     *
     * @param input the vector 3 of either mouse or keyboard input.
     * @return the angle the 3D model should attempt to rotate towards.
     */
    private float getEndRad(Vector3 input) {
        // Input vector will never be zero vector

        if (input.z == 0){
            if (input.x < 0)
                return (float)Math.PI;
            return (float)Math.PI * 2;
        }
        if (input.x == 0){
            if (input.z < 0)
                return (float)Math.PI/2;
            return (float)Math.PI*3/2;
        }

        if (input.z < 0){
            if (input.x < 0)
                return (float)Math.PI*3 / 4;
            return (float)Math.PI / 4;
        }
        if (input.z > 0){
            if (input.x > 0)
                return (float)Math.PI + (float)Math.PI*3 / 4;
            return (float)Math.PI + (float)Math.PI / 4;
        }
        return 0;
    }



    /**
     * @ Param: Mouse position
     * Instantiate a bullet in the tanks barrel and add the respective force on bullet
     */
    public void Shoot(int screenX, int screenY){
        // Instantiate a bullet at tip of turret
        Vector3 turretPos = getTurret().getModelInstance().transform.getTranslation(new Vector3());
        Vector3 bulletStart = turretPos.add(new Vector3(this.turretDirection).scl(800));
        Bullet newBullet = new Bullet(playState.assets.initializeModel("wiiTankBullet.g3db"), turretDirection, bulletSpeed);
        newBullet.getModelInstance().transform.set(bulletStart, getTurret().getModelInstance().transform.getRotation(new Quaternion()));

        playState.addEntity(newBullet);

    }

}
