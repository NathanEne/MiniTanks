package com.minitanks.game.entities;

import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.states.PlayState;

public class Bot extends Tank {

    private int AIType;

    public Bot(Turret t, TankBase tb, PlayState plst, Vector3 startingPos, boolean isAI, int aiType){
        super(t, tb, plst, startingPos, true);
        this.AIType = aiType;
    }



    public void playBehaviour(){
        switch (AIType){
            case 1:
                behaviour1();
                break;
        }
    }

    public void behaviour1(){
        super.move(new Vector3(0, 0, 1), Vector3.Zero);
    }

    public void behaviour2(){

    }



}
