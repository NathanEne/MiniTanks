package com.minitanks.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.entities.*;
import com.minitanks.game.managers.AssetManager;
import com.minitanks.game.managers.InputManager;
import com.minitanks.world.GameMap;
import com.minitanks.world.TiledGameMap;

import java.util.ArrayList;

public class PlayState extends State {

    private boolean isPerspectiveCam = true;
    private GameMap map;
    private Environment environment;
    private ModelBatch batch;
    private ModelInstance modelInstance;
    private Tank player;
    private Vector3 keyInputVector = new Vector3();
    private Vector3 mouseInputVector = new Vector3();
    private InputManager iptMan;
    private ArrayList<Bot> aiTanks = new ArrayList<Bot>();

    public Tank getPlayer(){
        return this.player;
    }

    public Vector3 getKeyInputVector() {
        return keyInputVector;
    }

    public Vector3 getMouseInputVector() {
        return mouseInputVector;
    }

    public PlayState(GameStateManager gsm) {
        super(gsm);
        this.iptMan = new InputManager(this);
        setInputProcessor();
        generateMap();

        this.environment = new Environment();
        this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));
        // environment.add(new PointLight().set(0.8f, 0.8f, 0.8f, 1000f, 200f, 1000f, 1000f));
        environment.add(new DirectionalLight().set(Color.SLATE,1,0.1f,1));

    }



    /**
     * Get the vector3 world position of the corresponding mouse position
     */
    public void setMouseInputVect(int mouseX, int mouseY){
        Vector3 screenInput = new Vector3(mouseX, mouseY, 1);
        Vector3 worldPos = this.camera.unProject(screenInput);
        worldPos.y = 0;
        this.mouseInputVector = worldPos;

        // Move tank to mouse for debugging purposes
        //this.getPlayer().getTankBase().getModelInstance().transform.set(worldPos, this.getPlayer().getTankBase().getModelInstance().transform.getRotation(new Quaternion()));
    }



    @Override
    protected void handleInput() {

        // Player movement
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.keyInputVector.x += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.keyInputVector.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.keyInputVector.z -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.keyInputVector.z += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            gsm.push(new SettingsState(gsm));
            gsm.update(Gdx.graphics.getDeltaTime());
            gsm.render(((SettingsState) gsm.currentState()).getBatch());
        }

        if (this.keyInputVector.len2() != 0 || mouseInputVector.len2() != 0){
            this.player.move(this.keyInputVector.nor(), this.mouseInputVector);
        }
        this.keyInputVector = new Vector3(0, 0, 0);


    }



    @Override
    public void update(float dt) {
        handleInput();
        updateBullets();
        updateAI();

        this.getPlayer().increaseBulletTime();
        for (Tank ai : this.aiTanks){
            ai.increaseBulletTime();
        }
    }



    @Override
    public void render(ModelBatch sb) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        this.camera.updateCam();

        if (this.camera.isPerspective())
            this.assets.render(this.camera.getPersCam(), environment, map.getEntities());
        else
            this.assets.render(this.camera.getOrthoCam(), environment, map.getEntities());
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

    public void setInputProcessor(){
        Gdx.input.setInputProcessor(this.iptMan);
    }

    public void addEntity(Entity e){
        this.map.addEntities(e);
    }

    public void updateBullets(){
        for (Entity e : this.map.getEntities()){
            if (e instanceof Bullets){
                e.getModelInstance().transform.trn(((Bullets) e).getDirection().scl(((Bullets) e).getSpeed()) );
            }
        }
    }



    /**
     * Place all objects in the scene, get data from map generator.
     * FUTURE: add level option and organization
     */
    public void generateMap(){
        initializeCamera();
        this.map = new TiledGameMap();

        // Initializing player
        this.player = new Tank(new Turret(this.assets.initializeModel("wiiTankTurret.g3db"), this),
                new TankBase(this.assets.initializeModel("wiiTankBody.g3db"), this), this, Vector3.Zero, false);

        // Add AI Tanks to the Arraylist instance
        aiTanks.add(new Bot(new Turret(this.assets.initializeModel("wiiTankTurret.g3db"), this),
                new TankBase(this.assets.initializeModel("wiiTankBody.g3db"), this),
                this, new Vector3(2360, 0, 1120), true, 1, this.player));

        // Add AI Tanks to the Arraylist instance
        aiTanks.add(new Bot(new Turret(this.assets.initializeModel("wiiTankTurret.g3db"), this),
                new TankBase(this.assets.initializeModel("wiiTankBody.g3db"), this),
                this, new Vector3(2360, 0, -1120), true, 2, this.player));

        for (Tank ai : aiTanks){
            this.addEntityToCollisionAndMap(ai.getTankBase());
            this.addEntity(ai.getTurret());
        }

        this.map.addEntities(new Wall(this.assets.initializeModel("wiiTankWall.g3db"), 1000, 4200, 9.5f, 0.2f));
        this.map.addEntities(new Wall(this.assets.initializeModel("wiiTankWall.g3db"), 1000, -3000, 9.5f, 0.2f));

        this.addEntityToCollisionAndMap(player.getTankBase());
        this.map.addEntities(player.getTurret());
        this.map.addEntities(new Floor(this.assets.createFloorModel(1000,1000, new Material())));

    }



    /**
     * orient the camera properly depending on the size of the map
     */
    public void initializeCamera(){
        this.camera = new Camera(false);
        this.camera.setPosition(new Vector3(0, 2500, 0));
        this.camera.lookAt(new Vector3(0, 0, 0));
        this.camera.rotateOnY(-90f);
    }


    /*
    For each ai Tank, update their movement and shooting
     */
    public void updateAI(){
        for (Bot ai : aiTanks){
            ai.playBehaviour();
        }

    }
}
