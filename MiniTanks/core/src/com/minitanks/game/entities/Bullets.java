package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestNotMeRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.minitanks.game.states.PlayState;

public class Bullets extends Entity {

    private float speed;
    private Vector3 direction;
    private PlayState p;
    private short ricoche=2;
    private ClosestNotMeRayResultCallback rays;

    public Bullets(ModelInstance modelInstance, Vector3 dir, float speed, PlayState p){
        super(modelInstance);
        this.speed = speed;
        this.direction = dir;
        this.p = p;
        this.id =2;
        rays = new ClosestNotMeRayResultCallback(this.getBody());
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vector3 getDirection() {
        return new Vector3(direction);
    }


    public void setDirection(Vector3 direction) {
        this.direction = direction.nor();
    }


    public short getRicoche() {
        return this.ricoche;
    }

    public void ricoched() {
        this.ricoche--;
    }

    /**
     *
     *
     */
    public boolean rayTest2Wall(Vector3 test){
        Vector3 rayFrom = this.getModelInstance().transform.getTranslation(new Vector3());
        Vector3 rayTo = (test.scl(this.getSpeed()*2)).sub(rayFrom);

        rays.setCollisionObject(null);
        rays.setClosestHitFraction(1f);
        rays.setRayFromWorld(rayFrom);
        rays.setRayToWorld(rayTo);

        btCollisionWorld collisionWorld = getP().getCollisionWorld();
        collisionWorld.rayTest(rayFrom, new Vector3(rayFrom).add(rayTo), rays);
        if (rays.hasHit()){
            Entity e = getP().getEntities().get(rays.getCollisionObject().getUserValue());
            return e.id == 5;
        }
        return false;
    }

    public PlayState getP() {
        return p;
    }


}
