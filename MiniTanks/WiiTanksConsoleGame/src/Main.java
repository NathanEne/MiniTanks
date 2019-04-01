import java.util.Scanner;

/**
 * class Main is designed to run wii tanks console game
 * includes scanning for user input as well as displaying the game
 */

public class Main {

    public static void main(String[]args) {
        // Introduction to game
        System.out.println("Welcome to the wii tanks console game!(It's pretty lame) \n");
        System.out.println("Press Space + Enter to shoot \n");
        System.out.println("Press a + Enter to move left \n");
        System.out.println("Press s + Enter to move down \n");
        System.out.println("Press w + Enter to move up \n");
        System.out.println("Press d + Enter to move right \n");
        System.out.println("T == you aka Tank \n");
        System.out.println("W == wall ");
        System.out.println("Cannot walk over and will ricochet bullets \n");
        System.out.println("M == mine ");
        System.out.println("Blows you up if you walk over it but can be shot down \n");
        System.out.println("X ==  targets you can shoot down");
        System.out.println("D ==  you are dead");
        System.out.println(" Enjoy! \n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n");

        /*
        User inputs for map width and length
         */
        System.out.println("Choose Map Width (Enter int) \n");
        Scanner keyboard = new Scanner(System.in);
        String input = keyboard.nextLine();
        int width = Integer.parseInt(input);
        System.out.println("Choose Map Length (Enter int) \n");
        int length = Integer.parseInt(keyboard.nextLine());

        /*
         * Initializing tank class which is a daughter class of Map
         * Will take inputs that will update Tank position and Map
         */
        Tank game = new Tank(width, length);

        game.printMapArray();

        System.out.println("Number of targets to shoot: " + game.getTargets());

        //Game is run in while loop until the boolean gameOver is set to true

        while (game.getGameOver() == false) {
            input = keyboard.nextLine();

            if (!input.isEmpty()){
                if (input.equals(" ")){
                    System.out.println("Where do you want to shoot (Up == w, Down == s, Right == d, Left == a)");

                    input = keyboard.nextLine();

                    game.shoot(input);
                }
                else {
                    game.move(input);

                    game.printMapArray();
                }
            }
        }
        /*
        Winning and Losing text depending on if the boolean wonGame is true or false
         */
        System.out.println("Game Over");
        if (game.getWonGame() == true){
            System.out.println("Congratulations you won!");
            System.out.println("Good Job! You shot down all targets!");
        }
        else{
            System.out.println("You Lost!");
            System.out.println("Too Bad!");
        }
    }
    }
