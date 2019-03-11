package com.minitanks.game.entities;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.entities.Tank ;
import com.minitanks.game.states.PlayState;

public class Turret extends Entity {

    private PlayState playState;

    public Turret(ModelInstance modelInstance, PlayState playst) {
        super(modelInstance);
        this.playState = playst;
    }


    /**
     * Every frame rotate the turret so it is facing the mouse of the user.
     * @param spot: The point in 3D which the tank's turret should be pointing towards
     */
    public void rotateToMouse(Vector3 spot){

    }
}
