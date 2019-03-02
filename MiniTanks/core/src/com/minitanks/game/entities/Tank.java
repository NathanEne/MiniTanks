package com.minitanks.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Tank extends Entity {
    private boolean isAI;
    private float movementSpeed = 5;
    private int numOfBullets = 5;
    private int numOfRicochets;
    private double angle=0;
    private Vector3 rotationDir;

    public TankBase getTankBase() {
        return tankBase;
    }

    public Turret getTurret() {
        return turret;
    }

    private TankBase tankBase;

    private Turret turret;

    // The vector to add to the tank base position for the turret position
    private Vector3 turretOffset = new Vector3(7,180,10);

    public Tank(Turret t, TankBase tb) {
        this.turret = t;
        this.tankBase = tb;
        getTankBase().getModelInstance().transform.scl(0.5f);
        getTurret().getModelInstance().transform.scl(0.5f);
        //this.rotationDir = getTankBase().getModelInstance().transform.getRotation(new Quaternion());
    }

    /**
     * Apply the movement for the tank
     *
     * @param keyInputVector A normalized direction for the tank to travel on this frame.
     */
    public void move(Vector3 keyInputVector, Vector3 mouseInput) {
        if (turret == null || tankBase == null)
            return;

        getTankBase().getModelInstance().transform.trn(keyInputVector.scl(movementSpeed));
        Vector3 tankPos = getTankBase().getModelInstance().transform.getTranslation(new Vector3());
        getTurret().getModelInstance().transform.set(tankPos.add(turretOffset), getTurret().getModelInstance().transform.getRotation(new Quaternion()));
        getTurret().getModelInstance().transform.scl(0.5f);

        // Make the turret face mouse position
        turret.rotateToMouse(mouseInput);
    }

    /**
     * @ Param: Mouse position
     * Instantiate a bullet in the tanks barrel and add the respective force on bullet
     */
    public void Shoot(){

    }
}
