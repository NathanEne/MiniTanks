import java.util.ArrayList;
import java.util.Random;


/**
 *class map is designed to create a 2D arraylist of chars that are representative of the game board
 *updates this 2d arraylist and evaluates when the game is over and whether you've won
 */

public class Map{

    /**
     * Instance variables for class map
     * ArrayLists for map creation and updates
     *
     * Booleans: wonGame and gameOver decide if the game is won or if it is over respectively
     * Boolean: ricochet is a boolean that determines when the bullet has hit a wall
     *
     * int: width and length are the dimensions for the board
     * int: targets (X) is the number of targets remaining to be shot down
     */

    private ArrayList<ArrayList<Character>> mapArray;
    private ArrayList<Character> arrayColumn;

    private boolean wonGame;
    private boolean gameOver;
    private boolean ricochet;

    private int width;
    private int length;
    private int targets;

    /**
     *
     * @param width
     * @param length
     * constructer which creates 2D arrayList map
     * initiates all aforementioned instance variables
     */

    protected Map(int width, int length){
        this.width = width;
        this.length = length;
        this.gameOver = false;
        this.wonGame = true;
        this.targets = 0;
        this.ricochet = false;

        mapArray = new ArrayList<>();
        arrayColumn = new ArrayList<>();

        for (int i = 0; i< this.length; i++){
            for (int j = 0; j < this.width; j++){
                    arrayColumn.add(newCharGenerator());
                    // column of chars is made
            }
            // column of chars is added to mapArray and reset to account for the length of the map
            ArrayList<Character> arrayColumn1 = (ArrayList<Character>) arrayColumn.clone();
            mapArray.add(arrayColumn1);
            arrayColumn.clear();

        }
        // Starting position of player/tank is always (0,0)
        if (mapArray.get(0).get(0).equals('X'))
            targets--;
        mapArray.get(0).set(0, 'T');
    }

    /**
     *
     * @param min
     * @param max
     * random float generator which takes in a min and max
     * used to randomize map generation of walls, mines and targets
     */
    private static float randomNumber(float min, float max) {
        Random rn = new Random();
        float number = min + rn.nextFloat() * (max - min);
        return number;
    }

    /**
     * char generator which uses specific odds to decide whether to return a Mine(M), Target(T) or Wall(W)
     */

    private char newCharGenerator(){
        float number = randomNumber(0, 1);
        if (number > 0.9)
            return 'M';

        if (number > 0.8) {
            targets++;
            return 'X';
        }

        if (number > 0.6)
            return 'W';

        else{
            return ' ';
        }
    }

    /**
     * necessary getter methods used by Tank and Main class
     *
     */
    protected int getLength() {
        return length;
    }

    protected int getWidth() {
        return width;
    }

    public boolean getGameOver(){
        return gameOver;
    }
    public boolean getWonGame(){
        return wonGame;
    }
    public int getTargets() {
        return targets;
    }

    protected boolean getRicochet() {
        return ricochet;
    }

    /**
     *
     * @param pos_x
     * @param pos_y
     * @param pos1_x
     * @param pos1_y
     * void method which updates the mapArray to account for movement of the tank
     * if player has lost game tank is dead (D)
     */

    protected void changeTankPosition(int pos_x, int pos_y, int pos1_x, int pos1_y) {
        mapArray.get(pos_y).set(pos_x, ' ');

        if (!wonGame){
            mapArray.get(pos1_y).set(pos1_x, 'D');
        }
        else {
            mapArray.get(pos1_y).set(pos1_x, 'T');
        }
    }

    /**
     *
     * @param pos_x
     * @param pos_y
     * boolean method which ensures that tanks is not moving over a wall or target.
     * also, if tank has moved on a mine, game is lost.
     */

    protected boolean ifValidMove(int pos_x, int pos_y){

        if (mapArray.get(pos_y).get(pos_x) == 'M') {

            System.out.println("No! You stepped on a mine!");

            gameOver = true;
            wonGame = false;
            return true;
        }

        return mapArray.get(pos_y).get(pos_x) == ' ';

    }
     // Just prints the mapArray
    protected void printMapArray(){
        for (int i = 0; i < this.length; i ++) {
            System.out.println(mapArray.get(i));
        }
        System.out.println("\n");
    }

    /**
     *
     * @param pos_x
     * @param pos_y
     * @param pos1_x
     * @param pos1_y
     * Changes bullet position while checking that a wall or tank is not being changed and that bullet is in bounds
     */

    protected void changeBulletPosition(int pos_x, int pos_y, int pos1_x, int pos1_y) {

        if (pos1_x < width && pos1_y < length && pos1_x >= 0 && pos1_y >= 0) {

            if (!mapArray.get(pos_y).get(pos_x).equals('T') && !mapArray.get(pos_y).get(pos_x).equals('W')){

                mapArray.get(pos_y).set(pos_x, ' ');
            }
            if (!mapArray.get(pos1_y).get(pos1_x).equals('W'))

            mapArray.get(pos1_y).set(pos1_x, 'B');
        }
    }

    /**
     *
     * @param pos_x
     * @param pos_y
     * @param pos1_x
     * @param pos1_y
     * boolean to check if bullet moving would be valid
     * used as an escape method from a while loop in tank class
     * if target is hit bullet and target are removed from board
     * if tank is hit you have shot yourself; both tank and bullet are removed
     * if mine is hit bullet and mine are removed from board
     * if wall is hit bullet ricochets and shot is valid(bullet keeps moving)
     * if bullet is out of bounds shot is not valid
     * if nothing is hit shot is valid(bullet keeps moving updated by change bullet position method
     */


    protected boolean ifValidShot(int pos_x, int pos_y, int pos1_x, int pos1_y){

        if (pos1_x < width && pos1_y < length && pos1_x >= 0 && pos1_y >= 0) {

            if (mapArray.get(pos1_y).get(pos1_x).equals('X')) {
                //target is hit

                mapArray.get(pos1_y).set(pos1_x, ' ');
                if (!mapArray.get(pos_y).get(pos_x).equals('T')) {
                    mapArray.get(pos_y).set(pos_x, ' ');
                }

                printMapArray();
                targets--;
                //if not targets left you win game

                if (targets == 0){
                    gameOver = true;
                    wonGame = true;
                }

                System.out.println("You hit a target");
                System.out.println("Number of targets to shoot: " + targets);
                return true;
            }

            if (mapArray.get(pos1_y).get(pos1_x).equals('T')){
                // Tank is hit; you killed yourself game it lost
                    mapArray.get(pos1_y).set(pos1_x, 'D');
                    mapArray.get(pos_y).set(pos_x, ' ');

                    printMapArray();

                    System.out.println("You shot Yourself!");

                    gameOver = true;
                    wonGame = false;
                    return true;
            }
                if (mapArray.get(pos1_y).get(pos1_x).equals('M')) {
                    // Mine is hit and removed

                mapArray.get(pos1_y).set(pos1_x, ' ');
                if (!mapArray.get(pos_y).get(pos_x).equals('T')) {
                    mapArray.get(pos_y).set(pos_x, ' ');
                }

                printMapArray();

                System.out.println("You shot a mine");

                return true;
            }
            if (mapArray.get(pos1_y).get(pos1_x).equals('W')) {
                // Wall is hit
                if (!mapArray.get(pos_y).get(pos_x).equals('T')) {
                    mapArray.get(pos_y).set(pos_x, ' ');
                }

                printMapArray();

                System.out.println("Your shot got blocked by a wall");
                // Bullet ricochets; is not moving in opposite direction
                ricochet = true;
                return false;
            }
            // if nothing is hit
            ricochet = false;
            return false;
        }
        // Bullet hits end of map
        if (!mapArray.get(pos_y).get(pos_x).equals('T')) {
            mapArray.get(pos_y).set(pos_x, ' ');
        }

        printMapArray();
        return true;

        }

}
