package com.minitanks.world;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.minitanks.game.entities.Bullets;
import com.minitanks.game.entities.Entity;
import com.minitanks.game.managers.CollisionManager;


public class MyContactListener extends ContactListener {
    private GameMap map;
    private CollisionManager manager;
    public MyContactListener(GameMap map){
        this.map = map;
        manager = new CollisionManager(map);


    }
    @Override
    public boolean onContactAdded(int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
        Entity one = map.getEntities().get(userValue0);
        Entity two = map.getEntities().get(userValue1);

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

        }
        if (two.getId()==2&&one.getId() ==1){
           one.getModelInstance().transform.trn(0,10000,0);

        }
        return true;
    }
}
