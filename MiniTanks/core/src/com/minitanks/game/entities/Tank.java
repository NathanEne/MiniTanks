package com.minitanks.game.entities;

import com.badlogic.gdx.math.Vector3;

public class Tank extends Entity {
    private boolean isAI;
    private float movementSpeed;
    private int numOfBullets = 5;
    private int numOfRicochets;


    private Turret turret;

    public Tank(){

    }

    /**
     * @ Param: Mouse position
     * Instantiate a bullet in the tanks barrel and add the respective force on bullet
     */
    public void Shoot(){

    }
}
