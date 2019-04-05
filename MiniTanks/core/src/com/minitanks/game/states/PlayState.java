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
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.minitanks.game.entities.*;
import com.minitanks.game.managers.InputManager;
import com.minitanks.game.managers.SavingManager;
import com.minitanks.world.MapGenerator;
import com.minitanks.world.MyContactListener;
import org.omg.IOP.ENCODING_CDR_ENCAPS;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class PlayState extends State {

    private ArrayList<Entity> entities;
    private ArrayList<Entity> entitiesToAdd;
    private ArrayList<Entity> entitiesToRemove;

    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private MyContactListener contactListener;
    private btBroadphaseInterface broadphase;
    private btCollisionWorld collisionWorld;
    private boolean isPerspectiveCam = true;
    private Environment environment;
    private ModelBatch batch;
    private ModelInstance modelInstance;
    private Tank player;
    private DebugDrawer debugDraw;
    private Vector3 keyInputVector = new Vector3();
    private Vector3 mouseInputVector = new Vector3();
    private InputManager iptMan;
    private ArrayList<Bot> aiTanks = new ArrayList<Bot>();
    private float screenHeight = 10400;
    private float screenWidth = 18600;

    // The Array modelling each chunk of the map. Always 5x5 containing all wall models.
    private ArrayList<ArrayList<ArrayList<Entity>>> wallModels = new ArrayList<ArrayList<ArrayList<Entity>>>(5);


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
        this.entities = new ArrayList<Entity>();
        this.entitiesToAdd = new ArrayList<Entity>();
        this.entitiesToRemove = new ArrayList<Entity>();
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
        for (Entity entity: this.getEntities()) {
            if (entity.hasBody()) {
                entity.getBody().setWorldTransform(entity.getModelInstance().transform);
            }
        }
        collisionWorld.performDiscreteCollisionDetection();
        this.entities.removeAll(entitiesToRemove);
        this.entitiesToRemove.clear();
        this.entities.addAll(entitiesToAdd);
        this.entitiesToAdd.clear();
    }



    @Override
    public void render(ModelBatch sb) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        this.camera.updateCam();


        if (this.camera.isPerspective())
            this.assets.render(this.camera.getPersCam(), environment, this.getEntities());
        else {
            this.assets.render(this.camera.getOrthoCam(), environment, this.getEntities());
        }

        // DEBUGGING FOR COLLISIONS
/*        debugDraw.begin(this.camera.getOrthoCam());
        collisionWorld.debugDrawWorld();
        debugDraw.end();*/
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
        this.addEntities(e);
    }

    public void updateBullets(){
        for (Entity e : this.getEntities()){
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
        float screenWidth = 18600f;
        float screenHeight = 10400f;


        try {
            initializeCamera();
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



            // WALL GENERATION
            // Spawn a chunk in each array spot of 5x5 array
            for (int x = 0; x < 5; x++){
                ArrayList<ArrayList<Entity>> thisRow = new ArrayList<ArrayList<Entity>>(5);
                for (int z = 0; z < 5; z++){
                    Vector3 centre = new Vector3(screenHeight*2 - x*screenHeight, 0, -screenWidth*2+z*screenWidth);

                    thisRow.add(getCluster(centre));
                }
                wallModels.add(thisRow);
            }

            this.addEntityToCollisionAndMap(player.getTankBase(), false);
            this.addEntities(player.getTurret());
            this.addEntities(new Floor(this.assets.createFloorModel(1000, 1000, new Material())));
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
        contactListener = new MyContactListener(this);
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

        obj.getBody().setUserValue(this.getEntities().size());
        obj.getBody().activate();
        obj.getBody().setCollisionFlags(obj.getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        this.addEntities(obj);
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
        this.camera = new Camera(false, this);

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


    /**
     * Should update the cameras position, also update generation functions as chunks.
     *
     * @param destination the spot at which the camera is moving to.
     * @param currFrame the vector3 corresponding to the middle of the chunk the camera is in. Note this is
     *                  not necessarily its position.
     * @return a new currFrame
     */
    public Vector3 updateCamera(Vector3 currFrame, Vector3 destination){
        // Translate walls once they get out of bounds

        if (destination.x > currFrame.x + screenHeight/2){
            // Translate to top

            translateWallsVertical(true, wallModels.get(4));
            ArrayList<ArrayList<Entity>> l = wallModels.remove(4);
            wallModels.add(0, l);

            currFrame.x += screenHeight;
        }
        if (destination.x < currFrame.x - screenHeight/2){
            // Translate to bottom
            translateWallsVertical(false, wallModels.get(0));

            ArrayList<ArrayList<Entity>> l = wallModels.remove(0);
            wallModels.add(l);

            currFrame.x -= screenHeight;
        }

        if (destination.z > currFrame.z + screenWidth/2){
            // Generate on right
            ArrayList<ArrayList<Entity>> leftMostChunks = new ArrayList<ArrayList<Entity>>(5);
            for (ArrayList<ArrayList<Entity>> row : wallModels){
                leftMostChunks.add(row.get(0));
                row.remove(0);
            }

            translateWallsHorizontal(true, leftMostChunks);

            // Add them back on ends

            for (int i = 0; i < 5; i++){
                wallModels.get(i).add(leftMostChunks.get(i));
            }

            currFrame.z += screenWidth;
        }

        if (destination.z < currFrame.z - screenWidth/2){
            // Generate on left

            ArrayList<ArrayList<Entity>> rightMostChunks = new ArrayList<ArrayList<Entity>>(5);
            for (ArrayList<ArrayList<Entity>> row : wallModels){
                rightMostChunks.add(row.get(4));
                row.remove(4);
            }

            translateWallsHorizontal(false, rightMostChunks);

            // Add them back on ends

            for (int i = 0; i < 5; i++){
                wallModels.get(i).add(0, rightMostChunks.get(i));
            }

            currFrame.z -= screenWidth;
        }
        return currFrame;
    }



    /**
     * @param centre a vector3 in world coordinates of the centre of the cluster
     * @return an arraylist of the modelinstances of all the walls generated in this cluster
     */
    private ArrayList<Entity> getCluster(Vector3 centre){
        ArrayList<float[]> Lines = MapGenerator.generateGeometricGraph(screenWidth, screenHeight, 6, 0.47f*5200, 0.35f*5200, 4, centre);
        ArrayList<Entity> modelInList = new ArrayList<Entity>();
        for (int i = 0; i < Lines.size(); i++){
            float[] wallData = MapGenerator.generateWallOnLine(new Vector3(Lines.get(i)[0], 0, Lines.get(i)[1]), new Vector3(Lines.get(i)[2], 0, Lines.get(i)[3]));
            // Stores the first value of length, second value is the radian angle.
            Wall wall = new Wall(this.assets.createWallModel(600, wallData[0], Lines.get(i)[2], Lines.get(i)[3]), wallData[1]);
            wall.getModelInstance().transform.rotateRad(Vector3.Y, wallData[1]);
            this.addEntities(wall);
            this.addEntityToCollisionAndMap(wall, true);
            modelInList.add(wall);
        }
        return modelInList;
    }


    /**
     * Move all walls within a list either up or down
     * @param isUp direction of translation
     * @param chunks the 2d list of all chunks and walls within them
     */
    public void translateWallsVertical(boolean isUp, ArrayList<ArrayList<Entity>> chunks){
        for (ArrayList<Entity> chunk: chunks){
            for (Entity e : chunk){
                Vector3 currPos = e.getModelInstance().transform.getTranslation(new Vector3());
                Quaternion currQuat = e.getModelInstance().transform.getRotation(new Quaternion());
                if (isUp)
                    currPos.x += screenHeight*5;
                else{
                    currPos.x -= screenHeight*5;
                }
                e.getModelInstance().transform.set(currPos, currQuat);
            }
        }
    }

    /**
     *
     * Same as above ^
     */
    public void translateWallsHorizontal(boolean isRight, ArrayList<ArrayList<Entity>> chunks){
        for (ArrayList<Entity> chunk: chunks){
            for (Entity e : chunk){
                Vector3 currPos = e.getModelInstance().transform.getTranslation(new Vector3());
                Quaternion currQuat = e.getModelInstance().transform.getRotation(new Quaternion());
                if (isRight)
                    currPos.z += screenWidth*5;
                else{
                    currPos.z -= screenWidth*5;
                }
                e.getModelInstance().transform.set(currPos, currQuat);
            }
        }
    }





    /**
     * @return the entities
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }


    /**
     * @param entities the entities to set
     */
    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public void addEntities(Entity entities) {
        this.entities.add(entities);
    }
    public void addEntitiesLater(Entity entities) {
        this.entitiesToAdd.add(entities);
    }

    public void removeEntitiesLater(ArrayList<Entity> entities) {
        this.entitiesToRemove.addAll(entities);
    }



}
