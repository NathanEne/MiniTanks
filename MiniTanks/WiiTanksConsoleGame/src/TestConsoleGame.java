import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestConsoleGame {
    Tank tank;

    @Before
    public void createTank(){
        tank = new Tank(7, 7);

    }

    @Test
    public void testRandomNumber(){
        float num = Map.randomNumber(0, 1);
        assertEquals("Random Number generator works", 0.5, num, 0.5);

    }

    @Test
    public void testMoveLeft(){
        boolean ifValidMove = tank.ifValidMove(6, 0);
        tank.move("a");
        if (ifValidMove){
            assertEquals("Tank has moved left", 6, tank.getPos_x());
            assertEquals("Tank has moved left", 0, tank.getPos_y());
        }
        else{
            assertEquals("No movement due to wall or mine", 0, tank.getPos_x());
        }
    }
    @Test
    public void testMoveDown(){
        boolean ifValidMove = tank.ifValidMove(0, 1);
        tank.move("s");
        if (ifValidMove){
            assertEquals("Tank has moved down", 1, tank.getPos_y());
        }
        else{
            assertEquals("No movement due to wall or mine", 0, tank.getPos_y());
        }
    }
    @Test
    public void testMoveUp(){
        boolean ifValidMove = tank.ifValidMove(0, 6);
        tank.move("w");
        if (ifValidMove){
            assertEquals("Tank has moved up", 6, tank.getPos_y());
        }
        else{
            assertEquals("No movement due to wall or mine", 0, tank.getPos_y());
        }
    }
    @Test
    public void testMoveRight(){
        boolean ifValidMove = tank.ifValidMove(1, 0);
        tank.move("d");
        if (ifValidMove){
            assertEquals("Tank has moved right", 1, tank.getPos_x());
        }
        else{
            assertEquals("No movement due to wall or mine", 0, tank.getPos_x());
        }
    }


}
