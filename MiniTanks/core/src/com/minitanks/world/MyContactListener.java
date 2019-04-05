package com.minitanks.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.minitanks.game.entities.Bullets;
import com.minitanks.game.entities.Entity;
import com.minitanks.game.states.PlayState;


public class MyContactListener extends ContactListener {
    private PlayState playState;
    //private CollisionManager manager;
    public MyContactListener(PlayState playst){
        this.playState = playst;
      //  manager = new CollisionManager(map);


    }
    @Override
    public boolean onContactAdded(int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
        Entity one = playState.getEntities().get(userValue0);
        Entity two = playState.getEntities().get(userValue1);

        if (two.getId()==2&&one.getId() ==5){
            BoundingBox aabb1 = new BoundingBox();
            one.getModelInstance().calculateBoundingBox(aabb1);
            BoundingBox aabb2 = new BoundingBox();
            two.getModelInstance().calculateBoundingBox(aabb2);
            aabb1.mul(one.getModelInstance().transform);
            aabb2.mul(two.getModelInstance().transform);

            if(aabb2.min.x<=aabb1.min.x){
                if( ((Bullets) two).getDirection().x>0){
                    ((Bullets) two).setDirection(((Bullets) two).getDirection().scl(new Vector3(-1,1,1)));
                }

            }else if(aabb2.max.x>=aabb1.max.x){
                if( ((Bullets) two).getDirection().x<0){
                    ((Bullets) two).setDirection(((Bullets) two).getDirection().scl(new Vector3(-1,1,1)));
                }

            }else if(aabb2.min.z<=aabb1.min.z){
                if( ((Bullets) two).getDirection().z>0){
                    ((Bullets) two).setDirection(((Bullets) two).getDirection().scl(new Vector3(1,1,-1)));
                }

            }else if(aabb2.max.z>=aabb1.max.z){
                if( ((Bullets) two).getDirection().z<0){
                    ((Bullets) two).setDirection(((Bullets) two).getDirection().scl(new Vector3(1,1,-1)));
                }

            }

        }else  if (two.getId()==1&&one.getId() ==5){
            BoundingBox aabb1 = new BoundingBox();
            one.getModelInstance().calculateBoundingBox(aabb1);
            BoundingBox aabb2 = new BoundingBox();
            two.getModelInstance().calculateBoundingBox(aabb2);
            aabb1.mul(one.getModelInstance().transform);
            aabb2.mul(two.getModelInstance().transform);


            //todo add restrictions on this condition to remove bugs
            if(aabb2.min.z<=aabb1.min.z&&Gdx.input.isKeyPressed(Input.Keys.D)){
                //left
                float delta = aabb2.max.z - aabb1.min.z;
                two.getModelInstance().transform.trn(0,0,-delta);


            }else if(aabb2.max.z>=aabb1.max.z&&Gdx.input.isKeyPressed(Input.Keys.A)){
                //right
                float delta = aabb2.min.z - aabb1.max.z;
                two.getModelInstance().transform.trn(0,0,-delta);

            }
            else if(aabb2.min.x<=aabb1.min.x && Gdx.input.isKeyPressed(Input.Keys.W)){
                //bottom
                float delta = aabb2.max.x - aabb1.min.x;
                two.getModelInstance().transform.trn(-delta,0,0);
                aabb1.mul(two.getModelInstance().transform);
                //todo add restrictions on this condition
            }else if(aabb2.max.x>=aabb1.max.x&&Gdx.input.isKeyPressed(Input.Keys.S)){
                //top
                float delta = aabb2.min.x - aabb1.max.x;
                two.getModelInstance().transform.trn(-delta,0,0);

            }

        } else if (two.getId()==2&&one.getId() ==1){
           one.getModelInstance().transform.trn(0,10000,0);
            two.getModelInstance().transform.trn(0,10000,0);
        } else if (two.getId()==2&&one.getId() ==2){
            one.getModelInstance().transform.trn(0,10000,0);
            two.getModelInstance().transform.trn(0,10000,0);
        }
        else if (two.getId()==1&&one.getId() ==1){
            one.getModelInstance().transform.trn(0,10000,0);
            two.getModelInstance().transform.trn(0,10000,0);
        }
        return true;
    }
}
