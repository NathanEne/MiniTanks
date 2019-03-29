package com.minitanks.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.states.PlayState;
import com.minitanks.world.MapGenerator;

public class Bot extends Tank {

    private int AIType;
    private float counter = 0;
    private Tank player;
    private int[] mapHeightBounds = {1000, -1000};
    private int[] mapWidthBounds = {-3000, 4200};

    private boolean pendingTarget = false;
    private Vector3 moveDirection = new Vector3();
    private Vector3 gotoLoc;


    public Bot(Turret t, TankBase tb, PlayState plst, Vector3 startingPos, boolean isAI, int aiType, Tank player){
        super(t, tb, plst, startingPos, true);
        this.AIType = aiType;
        this.player = player;
    }


    // Each tank will only display one type of behaviour
    public void playBehaviour(){
        switch (AIType){
            case 1:
                behaviour1();
                break;
            case 2:
                behaviour2();
                break;
        }
    }

    public void behaviour1(){
        /*
        Choose random points on the board and move to there.
        Will shoot at player in three sucessive shots randomly, increase probability of shooting as time continues.
        One reaches its desired point, choose a new point within the map.
         */
        super.move(new Vector3(0, 0, 0), new Vector3(player.getTankBase().getModelInstance().transform.getTranslation(new Vector3())));
        counter += 0.8f;
        if (MapGenerator.randomNumber(0, 0.015f) > 1/counter){
            super.Shoot();
            counter = 0;
        }

    }

    public void behaviour2(){
        /*
            A static tank that only rotates its turret and its shooting is modelled through an exponential distribution.
           (Increased probability of shooting as time without shooting continues).
         */
        Vector3 thisPos = super.getTankBase().getModelInstance().transform.getTranslation(new Vector3());


        if (!pendingTarget){
            // Pick a random location
            do{
                gotoLoc = new Vector3(MapGenerator.randomNumber(this.mapHeightBounds[0], this.mapHeightBounds[1]), 0, MapGenerator.randomNumber(this.mapWidthBounds[0], this.mapWidthBounds[1]));

            } while (Vector2.dst(gotoLoc.x, gotoLoc.z, thisPos.x, thisPos.z) < 1000);
            pendingTarget = true;
        }
        else {
            // Traverse to point
            moveDirection = new Vector3(gotoLoc).add(new Vector3(-thisPos.x, thisPos.y, -thisPos.z));
            moveDirection = moveDirection.nor();
            if (Vector2.dst2(gotoLoc.x, gotoLoc.z, thisPos.x, thisPos.z) < 100){
                pendingTarget = false;
            }
        }

        super.move(moveDirection, new Vector3(player.getTankBase().getModelInstance().transform.getTranslation(new Vector3())));
        counter += 0.8f;
        if (MapGenerator.randomNumber(0, 0.015f) > 1/counter){
            super.Shoot();
            counter = 0;
        }
    }
}
