package test;

import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.entities.Camera;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * Test for Camera class
 * New constructor had to be made to exclude playstate
 * Note, all other entities could not have text classes since they involve playstate which involves assetManager
 * Else, they directly use assetManager which produces an error.
 * This error was unable to be resolved which in turn significantly limited the testing aspect of this project.
 *
 */

public class TestCam extends GameTest{
    Camera camera;


    @Before
    public void makeCamera(){

        this.camera = new Camera(true);
        this.camera.setPosition(new Vector3(0, 2500, 0));
        this.camera.lookAt(new Vector3(0, 0, 0));
        this.camera.rotateOnY(-90f);
    }

    @Test
    public void testConstructerPerspective(){
        assertEquals(" Camera is setup perspective", true, camera.isPerspective());
    }

    @Test
    public void testConstructorInitialPosition(){
        assertEquals("Camera is above map", 2500, (int)camera.getPersCam().position.y);
    }

    @Test
    public void testGetDirection(){
        camera.setPosition(new Vector3(10, 10, 10));
        assertEquals("Camera is in peerspective direction facing downwards; map appears 2D", new Vector3(0,-1,0), camera.getDirection());
}

    @Test
    public void testSetCameraPosition(){
        camera.setPosition(new Vector3(10, 15, 20));
        assertEquals("Camera's position x-coordinate is changed to 10", 10, (int)camera.getPersCam().position.x);
        assertEquals("Camera's position y-coordinate is changed to 15", 15, (int)camera.getPersCam().position.y);
        assertEquals("Camera's position z-coordinate is changed to 20", 20, (int)camera.getPersCam().position.z);
    }

    @Test
    public void testRotateOnY(){
        this.camera.setPosition(new Vector3(0, 0, 0));
        this.camera.lookAt(new Vector3(10, 0, 0));
        assertEquals("Camera is looking 'up' on x-axis", 1, (int)camera.getPersCam().direction.x);
        camera.rotateOnY(-90f);
        assertEquals("Camera is rotated 90 degrees on y-axis", 0, (int)camera.getPersCam().direction.x);
    }
    @Test
    public void privacyLeakGetDirection(){
        Vector3 vector3 = this.camera.getDirection();
        assertEquals("Direction is (0, -1 0)", new Vector3(0, -1, 0), vector3);
        vector3.y = 1;
        assertEquals("Direction should not be changed", -1, (int)camera.getDirection().y);
    }
}