package com.minitanks.game.entities;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.entities.Tank ;
import com.minitanks.game.states.PlayState;

public class Turret extends Entity {

    private PlayState playState;
    private Vector2 currDirection;
    private float modelScale = 0.5f;

    public Turret(ModelInstance modelInstance, PlayState playst) {
        super(modelInstance);
        this.playState = playst;
        currDirection = new Vector2(1, 0);
    }

    public Vector3 getCurrDirection(){
        return new Vector3(this.currDirection.x, 0, this.currDirection.y);
    }

    /**
     * Every frame rotate the turret so it is facing the mouse of the user.
     * @param mouseIn: The point in 3D which the tank's turret should be pointing towards
     */
    public void rotateToMouse(Vector3 mouseIn){
        // Adjust the y value to be in line with this turret y value
        Vector2 spot = new Vector2(mouseIn.x, mouseIn.z);

        // Get normalized vector to rotate towards
        spot = spot.add(new Vector2(-getModelInstance().transform.getTranslation(new Vector3()).x, -getModelInstance().transform.getTranslation(new Vector3()).z)).nor();
        currDirection = currDirection.nor();
        Vector3 spot3 = new Vector3(-spot.x, 0, spot.y);

        getModelInstance().transform.setToRotation(spot3, new Vector3(-1, 0, 0));
        currDirection = spot;
    }
}
