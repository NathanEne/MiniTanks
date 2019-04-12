package com.minitanks.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.minitanks.game.entities.Bullets;
import com.minitanks.game.entities.Entity;
import com.minitanks.game.entities.TankBase;
import com.minitanks.game.states.PlayState;


public class MyContactListener extends ContactListener {
    private PlayState playState;
    public MyContactListener(PlayState playst){
        this.playState = playst;


    }
    @Override
    public boolean onContactAdded(int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
        Entity one = playState.getEntities().get(userValue0);
        Entity two = playState.getEntities().get(userValue1);


        if (two.getId()==2&&one.getId() ==5){
            if(((Bullets) two).getRicoche()>0) {
                BoundingBox aabb1 = new BoundingBox();
                one.getModelInstance().calculateBoundingBox(aabb1);
                BoundingBox aabb2 = new BoundingBox();
                two.getModelInstance().calculateBoundingBox(aabb2);
                aabb1.mul(one.getModelInstance().transform);
                aabb2.mul(two.getModelInstance().transform);
                Vector3 a = ((Bullets) two).getDirection();
                a.nor();
                Vector3 b = new Vector3(a);


                b.rotate(new Vector3(0, 1, 0), 90);
                b.sub(0, b.y, 0);
                two.getModelInstance().transform.trn(((Bullets) two).getDirection().scl(((Bullets) two).getSpeed() * -1.0f));
                if(((Bullets) two).rayTest2Wall(b)) {
                    ((Bullets) two).setDirection(a.rotate(new Vector3(0, 1, 0), -90));
                }else{
                    a.rotate(new Vector3(0, 1, 0), 90);
                    a.sub(0, a.y, 0);
                    ((Bullets) two).setDirection(a);
                }
                ((Bullets) two).ricoched();
            }else{
                two.getModelInstance().transform.trn(0,1000000,0);
            }
        } else if(one.getId()==1&&two.getId() ==5){
           // System.out.println( one.getModelInstance().transform.getTranslation(new Vector3()));

            if(((TankBase) one).getDirection()!= null) {
                one.getModelInstance().transform.trn(((TankBase) one).getDirection().scl(-1));
                //one.getModelInstance().transform.trn(0,1000000,0);
            }else{
                one.getModelInstance().transform.trn(100,0,100);
            }

        } else if(two.getId()==1&&one.getId() ==5){
            if(((TankBase) two).getDirection()!= null) {
                two.getModelInstance().transform.trn(((TankBase) two).getDirection().scl(-1));

            }else{
                two.getModelInstance().transform.trn(100,0,100);
            }




            } else if (two.getId()==2&&one.getId() ==1){

            one.getModelInstance().transform.trn(-1000000,10000,-1000000);
            two.getModelInstance().transform.trn(0,2000000,0);
            playState.scored();
        } else if (two.getId()==2&&one.getId() ==2){
            one.getModelInstance().transform.trn(0,1000500,0);
            two.getModelInstance().transform.trn(0,1000000,0);
        }
        return true;
    }
}
