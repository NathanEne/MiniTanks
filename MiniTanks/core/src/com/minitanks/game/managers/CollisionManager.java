package com.minitanks.game.managers;

import com.minitanks.world.GameMap;

public class CollisionManager {
    private GameMap map;

    public CollisionManager(GameMap map) {
        this.map = map;
    }

    public void resolveCollision(int val1, int val2) {
        System.out.println(map);
        map.getEntities();
//        Entity two = map.getEntities().get(1);
//        if (one instanceof Bullets && two instanceof Wall) {
//            ((Bullets) one).setDirection(((Bullets) one).getDirection().setToRandomDirection());
//
//        } else if (one instanceof Bullets && two instanceof Tank) {
//
//        }
    }
}
