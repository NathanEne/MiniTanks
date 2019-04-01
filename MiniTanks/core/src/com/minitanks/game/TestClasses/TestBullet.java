package com.minitanks.game.TestClasses;


import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.entities.Bullets;
import com.minitanks.game.entities.Tank;
import com.minitanks.game.entities.TankBase;
import com.minitanks.game.entities.Turret;
import com.minitanks.game.states.GameStateManager;
import com.minitanks.game.states.PlayState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestBullet {

    Bullets bullet;

    @Before
    public void makeBullet() {
        GameStateManager gsm = new GameStateManager();
        PlayState playState = new PlayState(gsm);
        Tank player = new Tank(new Turret(playState.assets.initializeModel("wiiTankTurret.g3db"), playState),
                new TankBase(playState.assets.initializeModel("wiiTankBody.g3db"), playState), playState, Vector3.Zero, false);

        bullet = new Bullets(playState.assets.initializeModel("wiiTankBullet.g3db"), player.getTurret().getCurrDirection(), player.getBulletSpeed());

    }

    @Test
    public void testSpeedSetAndGet() {
        bullet.setSpeed(20);

        assertEquals("Set and get methods", 20, (int) bullet.getSpeed());

    }

    @Test
    public void testDirectionSetAndGet() {
        bullet.setDirection(new Vector3(10, 15, 10));
        assertEquals("Setter and Getter for Direction", 15, (int) bullet.getDirection().y);
    }
}

