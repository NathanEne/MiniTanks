package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class Camera extends Entity {

    private boolean isPerspective;
    private boolean isMoving = false;
    private Vector3 destination;
    private PerspectiveCamera perspectiveCamera;
    private OrthographicCamera orthographicCamera;

    public Camera(boolean isPerspective){
        this.isPerspective = isPerspective;

        if (isPerspective){
            this.perspectiveCamera = new PerspectiveCamera(60, 5000, 5000);
            this.perspectiveCamera.far = 8500;

        }
        else{
            this.orthographicCamera = new OrthographicCamera(10500*(float)(16.0/9), 10500);
            this.orthographicCamera.far = 8500;
        }
    }


    public OrthographicCamera getOrthoCam(){
        return this.orthographicCamera;
    }
    public PerspectiveCamera getPersCam(){
        return this.perspectiveCamera;
    }

    public boolean isPerspective(){
        return isPerspective;
    }

    @Override
    public void setPosition(Vector3 position){
        if (isPerspective)
            this.perspectiveCamera.position.set(position);
        else
            this.orthographicCamera.position.set(position);
    }


    public void lookAt(Vector3 position){
        if (isPerspective)
            this.perspectiveCamera.lookAt(position);
        else
            this.orthographicCamera.lookAt(position);
    }


    public Vector3 getDirection(){
        if (isPerspective)
            return this.perspectiveCamera.direction;
        return this.orthographicCamera.direction;
    }

    public void rotateOnY(float degrees){
        if (isPerspective)
            this.perspectiveCamera.rotate(Vector3.Y, degrees);
        else
            this.orthographicCamera.rotate(Vector3.Y, degrees);
    }

    public void updateCam(){
        if (isPerspective)
            this.perspectiveCamera.update();
        else
            this.orthographicCamera.update();
    }


    public Vector3 unProject(Vector3 screenPos){
        Vector3 worldPos = new Vector3(screenPos.x, screenPos.y, screenPos.z);
        if (isPerspective){
            this.perspectiveCamera.unproject(worldPos);
        }
        else {
            this.orthographicCamera.unproject(worldPos);
        }

        return new Vector3(worldPos.x, worldPos.y + 5000, worldPos.z);
    }


    /**
     * Check to see if the player is nearing the edge of camera, move if so
     * @param playerPos the Vector3 position of the tank
     */
    public void seePlayer(Vector3 playerPos, Vector3 tankDirection){
        // todo: Implment this for a rotated camera angle

        float thres = 2000; // Units from the side of the screen that trigger camera movement

        // Size of screen (world units)
        float w = 18600/2.0f;
        float h = 10400/2.0f;

        Vector3 camPos = orthographicCamera.position;

        if (isMoving){
            orthographicCamera.position.lerp(destination, 0.05f);
            if (this.destination.dst(orthographicCamera.position)  < 100){
                isMoving = false;
            }
        }
        else{
            if (playerPos.x > camPos.x + h - thres || playerPos.x < camPos.x - h + thres ||
                    playerPos.z > camPos.z + w - thres || playerPos.z < camPos.z - w + thres){
                // Gradually move towards the tank.
                isMoving = true;
                this.destination = new Vector3(playerPos.x, orthographicCamera.position.y, playerPos.z).add(tankDirection.scl(70));
            }
        }
    }

}
