package com.minitanks.game.managers;

import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.entities.Tank;

import java.io.*;

public class SavingManager {

    static Vector3 tankVector;
    static long score;

    public SavingManager() throws IOException{
        this.readTankPosition();
    }

    /**
     *
     * @param player
     * @throws IOException
     * Saves the coordinates of the player tank into the save.txt file
     *
     */

    public static void writeTankPosition(Tank player) throws IOException {
        PrintWriter output = new PrintWriter(new File("save.txt"));
        output.print(player.getTankBase().getModelInstance().transform.getTranslation(new Vector3()));
        output.println(","+player.getPlayState().getScore());
        output.close();
    }
    /*
    Resets tank position to (0, 0, 0)
     */

    public static void resetTankPosition() throws IOException {
        PrintWriter output = new PrintWriter(new File("save.txt"));
        output.print(new Vector3(0,0,0));
        output.println(","+0);
        output.close();
    }

    /**
     *
     * @throws IOException
     * BufferedReader reads tank position from save.text
     * Then initializes the tank vector or its starting coordinates from the BufferedReaders input
     *
     */

    public void readTankPosition() throws IOException {

        FileReader freader = new FileReader("save.txt");

        BufferedReader input = new BufferedReader(freader);
        String vector = input.readLine();
        vector = vector.replace("(", "");
        vector = vector.replace(")", "");
        String[] vectorArray = vector.split(",");
        tankVector = new Vector3(Float.parseFloat(vectorArray[0]), Float.parseFloat(vectorArray[1]), Float.parseFloat(vectorArray[2]));
        score = Long.parseLong(vectorArray[3]);
        input.close();
        freader.close();
    }

    public Vector3 getTankVector() {
        return tankVector;
    }

    public long getScore() {
        return score;
    }
}
