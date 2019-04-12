package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.states.PlayState;

public class Camera extends Entity {

    private boolean isPerspective;
    private boolean isMoving = false;
    private Vector3 destination;
    private PerspectiveCamera perspectiveCamera;
    private OrthographicCamera orthographicCamera;
    private Vector3 currFrame = new Vector3(0, 0, 0);
    private PlayState playState;

    /**
     *
     * @param isPerspective
     * @param plst
     * Constructor for camera initializes its distance from board and its dimensions
     *
     */

    public Camera(boolean isPerspective, PlayState plst){
        this.isPerspective = isPerspective;

        if (isPerspective){
            this.perspectiveCamera = new PerspectiveCamera(60, 5000, 5000);
            this.perspectiveCamera.far = 8500;

        }
        else{
            this.orthographicCamera = new OrthographicCamera(8500*(float)(16.0/9), 8500);
            //this.orthographicCamera = new OrthographicCamera(2*38000*(float)(16.0/9), 2*38000);
            this.orthographicCamera.far = 18500;
        }

        this.playState = plst;
    }

    /**
     *
     * @param isPerspective
     * Constructor for test class usage strictly
     *
     */
    public Camera(boolean isPerspective) {
        this.isPerspective = isPerspective;

        if (isPerspective) {
            this.perspectiveCamera = new PerspectiveCamera(60, 5000, 5000);
            this.perspectiveCamera.far = 8500;

        } else {
            this.orthographicCamera = new OrthographicCamera(10500 * (float) (16.0 / 9), 10500);
            this.orthographicCamera.far = 18500;
        }
    }

    /**
     * Getter for camera
     *
     */

    public OrthographicCamera getOrthoCam(){


        return this.orthographicCamera;
    }
    public PerspectiveCamera getPersCam(){
        return this.perspectiveCamera;
    }

    public boolean isPerspective(){
        return isPerspective;
    }

    /**
     *
     * Sets the postion of the camera at a three-dimensional point
     *
     */
    @Override
    public void setPosition(Vector3 position){
        if (isPerspective)
            this.perspectiveCamera.position.set(position);
        else
            this.orthographicCamera.position.set(position);
    }

    // Focuses camera on three dimensional point
    public void lookAt(Vector3 position){
        if (isPerspective)
            this.perspectiveCamera.lookAt(position);
        else
            this.orthographicCamera.lookAt(position);
    }

    /*
    Gets direction at which Camera is aimed
    For example, if camera is aimed directly down at the board its direction will (0, -1, 0)
     */
    public Vector3 getDirection(){
        if (isPerspective) {
            Vector3 direction = this.perspectiveCamera.direction;
            return new Vector3(direction.x,direction.y,direction.z);
        }
        Vector3 direction = this.orthographicCamera.direction;
        return new Vector3(direction.x,direction.y,direction.z);
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

        float thres = 2700; // Units from the side of the screen that trigger camera movement

        // Size of screen (world units)
        float w = 18600/2.0f;
        float h = 10400/2.0f;

        Vector3 camPos = orthographicCamera.position;

        if (isMoving){
            orthographicCamera.position.lerp(destination, 0.07f);
            if (this.destination.dst(orthographicCamera.position)  < 50){

                isMoving = false;
            }
        }
        else{
            if (playerPos.x > camPos.x + h - thres || playerPos.x < camPos.x - h + thres ||
                    playerPos.z > camPos.z + w - thres || playerPos.z < camPos.z - w + thres){
                // Gradually move towards the tank.
                isMoving = true;
                this.destination = new Vector3(playerPos.x, orthographicCamera.position.y, playerPos.z).add(tankDirection.scl(80));
                this.currFrame = playState.updateCamera(this.currFrame, this.destination);
            }
        }
    }
}
