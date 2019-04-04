package com.minitanks.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.minitanks.game.entities.*;
import com.minitanks.game.managers.InputManager;
import com.minitanks.game.managers.SavingManager;
import com.minitanks.world.GameMap;
import com.minitanks.world.MapGenerator;
import com.minitanks.world.MyContactListener;
import com.minitanks.world.TiledGameMap;

import java.io.IOException;
import java.util.ArrayList;

public class PlayState extends State {

    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private MyContactListener contactListener;
    private btBroadphaseInterface broadphase;
    private btCollisionWorld collisionWorld;
    private boolean isPerspectiveCam = true;
    private GameMap map;
    private Environment environment;
    private ModelBatch batch;
    private ModelInstance modelInstance;
    private Tank player;
    private DebugDrawer debugDraw;
    private Vector3 keyInputVector = new Vector3();
    private Vector3 mouseInputVector = new Vector3();
    private InputManager iptMan;
    private ArrayList<Bot> aiTanks = new ArrayList<Bot>();
    final static short WALL_FLAG = 1 << 8;
    final static short TANK_FLAG = 1 << 9;
    final static short BULLET_FLAG = 1 << 7;
    final static short ALL_FLAG = -1;

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
        Bullet.init();
        this.iptMan = new InputManager(this);
        setInputProcessor();
        generateMap();
        initializeLighting();

    }


    public void initializeLighting(){
        this.environment = new Environment();
        this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));
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
            //gsm.push(new SettingsState(gsm));
            //gsm.update(Gdx.graphics.getDeltaTime());
            //gsm.render(((SettingsState) gsm.currentState()).getBatch());
            try {
                SavingManager.writeTankPosition(player);
            }
            catch(IOException e) {

            }
        }

        // Call the move function.
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

        this.camera.seePlayer(this.player.getTankBase().getModelInstance().transform.getTranslation(new Vector3()), this.getKeyInputVector());
        this.getPlayer().increaseBulletTime();

        for (Tank ai : this.aiTanks){
            ai.increaseBulletTime();
        }
        for (Entity entity: map.getEntities()) {
            if (entity.hasBody()) {
                entity.getBody().setWorldTransform(entity.getModelInstance().transform);
            }
        }
        collisionWorld.performDiscreteCollisionDetection();
    }



    @Override
    public void render(ModelBatch sb) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        this.camera.updateCam();


        if (this.camera.isPerspective())
            this.assets.render(this.camera.getPersCam(), environment, map.getEntities());
        else {
            this.assets.render(this.camera.getOrthoCam(), environment, map.getEntities());
        }

        // DEBUGGING FOR COLLISIONS
        debugDraw.begin(this.camera.getOrthoCam());
        collisionWorld.debugDrawWorld();
        debugDraw.end();
    }



    @Override
    public void dispose() {
        batch.dispose();
    }
    public ModelBatch getBatch() {
        return batch;
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
        try {
            initializeCamera();
            this.map = new TiledGameMap();
            initializeCollisionEngine();
            SavingManager tankVector = new SavingManager();

            // Initializing player
            this.player = new Tank(new Turret(this.assets.initializeModel("wiiTankTurret.g3db"), this),
                    new TankBase(this.assets.initializeModel("wiiTankBody.g3db"), this), this, tankVector.getTankVector(), false);

                // Add AI Tanks to the Arraylist instance
            aiTanks.add(new Bot(new Turret(this.assets.initializeModel("wiiTankTurret.g3db"), this),
                    new TankBase(this.assets.initializeModel("wiiTankBody.g3db"), this),
                    this, new Vector3(2360, 0, 1120), true, 1, this.player));

            // Add AI Tanks to the Arraylist instance
            aiTanks.add(new Bot(new Turret(this.assets.initializeModel("wiiTankTurret.g3db"), this),
                    new TankBase(this.assets.initializeModel("wiiTankBody.g3db"), this),
                    this, new Vector3(2360, 0, -1120), true, 2, this.player));

            for (Tank ai : aiTanks){
                this.addEntityToCollisionAndMap(ai.getTankBase(),false);
                this.addEntity(ai.getTurret());
            }


            /*ArrayList<float[]> Lines = MapGenerator.generateGeometricGraph(1.5f*18600, 1.5f*10400, 8*12, 0.47f*5200, 0.35f*5200, 4);
            for (int i = 0; i < Lines.size(); i++){
                ArrayList<float[]> WallsNeeded = MapGenerator.generateWallOnLine(new Vector3(Lines.get(i)[0], 0, Lines.get(i)[1]), new Vector3(Lines.get(i)[2], 0, Lines.get(i)[3]));
                for (int i2 = 0; i2 < WallsNeeded.size(); i2++){
                    this.addEntityToCollisionAndMap(new Wall(this.assets.initializeModel("wiiTankWall.g3db"), WallsNeeded.get(i2)[0], WallsNeeded.get(i2)[1], WallsNeeded.get(i2)[2]),true);
                }

            }*/
            Wall wall = new Wall(this.assets.createWallModel(600, 2600, new Material()));
            wall.getModelInstance().transform.rotateRad(Vector3.Y, (float)Math.PI/6);
            this.map.addEntities(wall);
            this.addEntityToCollisionAndMap(wall, true);



            this.addEntityToCollisionAndMap(player.getTankBase(), false);
            this.map.addEntities(player.getTurret());
            this.map.addEntities(new Floor(this.assets.createFloorModel(1000, 1000, new Material())));
        }
        catch(IOException ioe) {

        }

    }


    /**
     * Sets up all required objects for later use in collision detection
     */
    public void initializeCollisionEngine(){


        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
        contactListener = new MyContactListener(map);
        debugDraw = new DebugDrawer();

        collisionWorld.setDebugDrawer(debugDraw);
        debugDraw.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);


    }

    public void addEntityToCollisionAndMap(Entity obj, boolean wall){
        obj.setBody(new btCollisionObject());
        BoundingBox a = new BoundingBox();
        obj.getModelInstance().calculateBoundingBox(a);
        obj.getBody().setCollisionShape(new btBoxShape(a.getDimensions(new Vector3()).scl(0.5f)));
        obj.getBody().setWorldTransform(obj.getModelInstance().transform);

        obj.getBody().setUserValue(map.getEntities().size());
        obj.getBody().activate();
        obj.getBody().setCollisionFlags(obj.getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        map.addEntities(obj);
        if(wall){
            collisionWorld.addCollisionObject(obj.getBody(),ALL_FLAG,ALL_FLAG);
        }else{
            collisionWorld.addCollisionObject(obj.getBody(),ALL_FLAG,ALL_FLAG);
        }
    }



    /**
     * orient the camera properly depending on the size of the map
     */
    public void initializeCamera(){
        this.camera = new Camera(false);

        // Birds eye
        //this.camera.setPosition(new Vector3(0, 2500, 0));

        // Offset view for 3D effect
        this.camera.setPosition(new Vector3(0, 2500, 300));

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
