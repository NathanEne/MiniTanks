package com.minitanks.game.managers;

import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.entities.Tank;

import java.io.*;

public class SavingManager {

    static Vector3 tankVector;

    public SavingManager() throws IOException{
        this.readTankPosition();
    }

    public static void writeTankPosition(Tank player) throws IOException {
        PrintWriter output = new PrintWriter(new File("save.txt"));
        output.println(player.getTankBase().getModelInstance().transform.getTranslation(new Vector3()));
        output.close();
    }

    public void readTankPosition() throws IOException {
        FileReader freader = new FileReader("save.txt");
        BufferedReader input = new BufferedReader(freader);
        String vector = input.readLine();
        vector = vector.replace("(", "");
        vector = vector.replace(")", "");
        String[] vectorArray = vector.split(",");
        this.tankVector = new Vector3(Float.parseFloat(vectorArray[0]), Float.parseFloat(vectorArray[1]), Float.parseFloat(vectorArray[2]));

    }

    public Vector3 getTankVector() {
        return tankVector;
    }
}
