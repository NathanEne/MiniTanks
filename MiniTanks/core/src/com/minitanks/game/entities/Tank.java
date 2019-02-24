package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Tank extends Entity {
    private boolean isAI;
    private float movementSpeed;
    private int numOfBullets = 5;
    private int numOfRicochets;

    public TankBase getTankBase() {
        return tankBase;
    }

    public Turret getTurret() {
        return turret;
    }

    private TankBase tankBase;

    private Turret turret;


    public Tank(Turret t, TankBase tb) {
        this.turret = t;
        this.tankBase = tb;
    }

    @Override
    public void move() {

        this.turret.getModelInstance().transform.trn(this.getTankBase().getModelInstance().transform.getTranslation(new Vector3()));


    }

    /**
     * @ Param: Mouse position
     * Instantiate a bullet in the tanks barrel and add the respective force on bullet
     */
    public void Shoot(){

    }
}
