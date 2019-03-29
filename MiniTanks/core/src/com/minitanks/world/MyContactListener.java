package com.minitanks.world;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.minitanks.game.entities.Entity;

public class MyContactListener extends ContactListener {
    private GameMap map;
    public MyContactListener(GameMap map){
        this.map = map;

    }
    @Override
    public boolean onContactAdded(int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
    Entity one = map.getEntities().get(0);
        //Entity two = map.getEntities().get(userValue1-1);
//        if (one instanceof Bullets && two instanceof Wall){
//            ((Bullets) one).setDirection(((Bullets) one).getDirection().setToRandomDirection());
//
//        }else  if(one instanceof Bullets && two instanceof Tank){





        System.out.println("hi");
        return true;
    }
}
