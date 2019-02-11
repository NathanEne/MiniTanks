package com.minitanks.game.states;
import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.minitanks.world.MapGenerator;

public class MapDebugging extends State{

    private ModelBatch batch;
    private ModelInstance modelInstance;
    private ShapeRenderer shapeRender;
    private ArrayList<float[]> shapeLocations;
    private boolean firstRender = true;

    public MapDebugging(GameStateManager gsm){
        super(gsm);
        shapeRender = new ShapeRenderer();
        this.camOrth = new OrthographicCamera();
        this.camOrth.position.set(new Vector3(0, 0, 0));
        //this.camOrth.lookAt(new Vector3(1, 1, 0));
        shapeLocations = MapGenerator.generateWallPoints(50, 50);
    }

    public void update(float dt){handleInput();}

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.camOrth.position.set(this.camOrth.position.x,this.camOrth.position.y+5,this.camOrth.position.z);
            this.camOrth.translate(-20, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.camOrth.position.set(this.camOrth.position.x,this.camOrth.position.y-5,this.camOrth.position.z);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.camOrth.position.set(this.camOrth.position.x-5,this.camOrth.position.y,this.camOrth.position.z);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.camOrth.position.set(this.camOrth.position.x+5,this.camOrth.position.y,this.camOrth.position.z);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            this.camOrth.position.set(this.camOrth.position.x,this.camOrth.position.y,this.camOrth.position.z+5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            this.camOrth.position.set(this.camOrth.position.x,this.camOrth.position.y,this.camOrth.position.z-5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.camOrth.lookAt(5f,5f,5f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.camOrth.lookAt(0f,0f,0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            System.out.println(this.camOrth.position);
        }

    }

    public void render(ModelBatch mb){

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        this.camOrth.update();
        shapeRender.begin(ShapeRenderer.ShapeType.Filled);
        shapeRender.setProjectionMatrix(this.camOrth.combined);
        shapeRender.setColor(Color.BLUE);
        shapeRender.circle(0, 0, 10);
        shapeRender.setColor(Color.BLACK);
        for (float[] p : shapeLocations){
            shapeRender.circle(p[0] + 200, p[1] + 200, p[2]);
        }

        shapeRender.end();
    }
    public void dispose() {
        batch.dispose();
    }
    public ModelBatch getBatch() {
        return batch;
    }

}
