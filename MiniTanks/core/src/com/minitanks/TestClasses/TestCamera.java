package com.minitanks.TestClasses;

import static org.junit.Assert.*;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

import com.minitanks.game.entities.Camera;
import org.junit.Before;
import org.junit.Test;



public class TestCamera extends Camera {
    public TestCamera(){
        super(true);
    }
    TestCamera camera;


    @Before
    public void makeCamera(){
        camera = new TestCamera();
    }

    @Test
    public void testConstructerPerspective(){
        assertEquals(" Camera is setup perspective", true, camera.isPerspective());
    }

    @Test
    public void testGetDirection(){
        assertEquals("Camera is in peerspective direction (focused on x-axis)", 10, (int)camera.getDirection().x);
    }

    @Test
    public void testSetCameraPosition(){
        camera.setPosition(new Vector3(10, 15, 20));
        assertEquals("Camera's position x-coordinate is changed to 10", 10, (int)camera.getPersCam().position.x);
    }

    @Test
    public void testRotateOnY(){
        camera.rotateOnY(90);
        assertEquals("Camera rotates on y-axis by 90 degrees", 90, (int)camera.getPersCam().direction.y);
    }

}
