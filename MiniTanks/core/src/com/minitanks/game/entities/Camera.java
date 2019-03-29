package com.minitanks.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class Camera extends Entity {

    private boolean isPerspective;
    private PerspectiveCamera perspectiveCamera;
    private OrthographicCamera orthographicCamera;

    public Camera(boolean isPerspective){
        this.isPerspective = isPerspective;

        if (isPerspective){
            this.perspectiveCamera = new PerspectiveCamera(60, 5000, 5000);
            this.perspectiveCamera.far = 8500;

        }
        else{
            this.orthographicCamera = new OrthographicCamera(8500, 8500);
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
}
