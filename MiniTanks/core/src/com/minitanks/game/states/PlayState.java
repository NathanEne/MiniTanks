package com.minitanks.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.entities.*;
import com.minitanks.world.GameMap;
import com.minitanks.world.TiledGameMap;
import com.badlogic.gdx.graphics.PerspectiveCamera;

public class PlayState extends State {


    private boolean auto = false;
    private GameMap map;
    private Environment environment;
    private ModelBatch batch;
    private ModelInstance modelInstance;
    private boolean screenShot = false;
    public PlayState(GameStateManager gsm) {
        super(gsm);
        this.map = new TiledGameMap();
        this.cam = new PerspectiveCamera(-165f, 1, 1);
        this.cam.position.set(50f,1600f,-77f);
        this.cam.lookAt(new Vector3(0, 0, 0));
        this.cam.near = 0.1f;
        this.cam.far = 3000.0f;
        this.map.addEntities(new TankBase(this.assets.initializeModel("wiiTankBody.g3db")));
        this.map.addEntities(new Turret(this.assets.initializeModel("wiiTankTurret.g3db")));
        this.map.addEntities(new Wall(this.assets.initializeModel("wiiTankWall.g3db")));
        //this.map.addEntities(new Floor(this.assets.initializeModel("wiiTankFloor.g3db")));

        this.environment = new Environment();
        this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.cam.position.set(this.cam.position.x,this.cam.position.y+5,this.cam.position.z);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.cam.position.set(this.cam.position.x,this.cam.position.y-5,this.cam.position.z);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.cam.position.set(this.cam.position.x-5,this.cam.position.y,this.cam.position.z);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.cam.position.set(this.cam.position.x+5,this.cam.position.y,this.cam.position.z);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            cam.rotateAround(Vector3.Zero, new Vector3(0,1,0),1f);

        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            cam.rotateAround(Vector3.Zero, new Vector3(0,1,0),-1f);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            this.cam.position.set(this.cam.position.x,this.cam.position.y,this.cam.position.z+5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            this.cam.position.set(this.cam.position.x,this.cam.position.y,this.cam.position.z-5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.cam.lookAt(5f,5f,5f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.cam.lookAt(0f,0f,0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            System.out.println(this.cam.position);
            if(this.auto){
                this.auto = false;

            }else{
                this.auto = true;
            }
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(ModelBatch sb) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        this.cam.update();

        if(auto){
        cam.rotateAround(Vector3.Zero, new Vector3(0,1,0),1f);

        }

        this.assets.render(cam,environment,map.getEntities());
        //this.getTiledMap().update(Gdx.graphics.getDeltaTime());
        //this.getTiledMap().render(this.cam, sb);



    }

    @Override
    public void dispose() {
    batch.dispose();
    }
    public ModelBatch getBatch() {
        return batch;
    }
    public GameMap getTiledMap() {
        return map;
    }

}
