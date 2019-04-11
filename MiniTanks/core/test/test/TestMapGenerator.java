package test;

import com.badlogic.gdx.math.Vector3;
import com.minitanks.world.MapGenerator;
import org.junit.Test;

import static com.minitanks.world.MapGenerator.generateWallOnLine;
import static com.minitanks.world.MapGenerator.randomNumber;
import static org.junit.Assert.assertEquals;

/**
 *
 * Test class for mapGenerator
 * Ensures proper function of a couple methods
 *
 */

public class TestMapGenerator extends  GameTest{

    private MapGenerator mapGenerator;


    @Test
    public void testRandomNumber(){
        float num = randomNumber(0,1);
        assertEquals("Random Number Function Works", 0.5, num, 0.5);

    }

    @Test
    public void testGenerateWallOnLine(){
        float[] floatarray = generateWallOnLine(new Vector3(0,0,0), new Vector3(1,1,1));
        assertEquals("Both theta and distance are correct", 3.1459/4.0, floatarray[1], 0.01);
        assertEquals("Distance", Math.sqrt(2), floatarray[0], 0.01);

    }


}
