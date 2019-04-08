package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.states.PlayState;

public class TankBase extends Entity {


    public TankBase(ModelInstance modelInstance) {
        super(modelInstance);
        this.id =1;
    }

}