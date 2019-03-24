package com.minitanks.TestClasses;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.entities.Bullet;
import com.minitanks.game.managers.AssetManager;



public class TestBullet{
    Bullet bullet;

    @Before
    public void makeBullet(){
        AssetManager aM = new AssetManager();
        //Default speed = 10
        float speed = 10;
        bullet = new Bullet(aM.initializeModel("wiiTankBullet.g3db"), new Vector3(), speed);
    }

    @Test
    public void testSpeedSetAndGet(){
        bullet.setSpeed(20);

        assertEquals("Set and get methods", 20, (int)bullet.getSpeed());

    }

    @Test
    public void testDirectionSetAndGet(){
        bullet.setDirection(new Vector3(10, 15, 10));
        assertEquals("Setter and Getter for Direction", 15, (int)bullet.getDirection().y);
    }

}
