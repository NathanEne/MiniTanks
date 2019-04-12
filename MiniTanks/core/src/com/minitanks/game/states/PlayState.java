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
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayState extends State {

    private ArrayList<Entity> entities;
    private ArrayList<Entity> entitiesToAdd;
    private ArrayList<Entity> entitiesToRemove;
    private long score=0;
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
    private final float screenHeight = 10400;
    private final float screenWidth = 18600;

    // The Array modelling each chunk of the map. Always 5x5 containing all wall models.
    private ArrayList<ArrayList<ArrayList<Entity>>> wallModels = new ArrayList<ArrayList<ArrayList<Entity>>>(5);

    // A 2D Array storing all the bots for each chunk.
    private ArrayList<ArrayList<ArrayList<Bot>>> aiEntities = new ArrayList<ArrayList<ArrayList<Bot>>>(5);

    // A 2D array storing all the initial positions of the tanks
    private ArrayList<ArrayList<ArrayList<Vector3>>> positions = new ArrayList<ArrayList<ArrayList<Vector3>>>(5);

    final static short WALL_FLAG = 1 << 8;
    final static short TANK_FLAG = 1 << 9;
    final static short BULLET_FLAG = 1 << 7;
    final static short ALL_FLAG = -1;

    public btCollisionWorld getCollisionWorld() {
        return collisionWorld;
    }

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
    public PlayState(GameStateManager gsm, final RenderContext context, final ShaderProvider shaderProvider, final RenderableSorter sorter) {
        super(gsm, context, shaderProvider, sorter);
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

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.push(new SettingsState(gsm, this.player));
            //gsm.update(Gdx.graphics.getDeltaTime());
            gsm.render(((SettingsState) gsm.currentState()).getBatch());

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

        this.camera.seePlayer(this.player.getTankBase().getModelInstance().transform.getTranslation(new Vector3()), this.getKeyInputVector());
        this.getPlayer().increaseBulletTime();

        for (Entity entity: this.getEntities()) {


            if (entity instanceof Bullets){
                entity.getModelInstance().transform.trn(((Bullets) entity).getDirection().scl(((Bullets) entity).getSpeed()) );
            }

            if (entity.hasBody()) {
                entity.getBody().setWorldTransform(entity.getModelInstance().transform);
            }
//            if(entity instanceof TankBase && entity.getModelInstance().transform.getTranslation(new Vector3()).y>9000){
//                ((TankBase)entity).getOwner().getTurret().getModelInstance().transform.trn(0,10000,0);
//
//            }


        }


        float maxDistance = 65f; // The maximum amount of units AI tanks can be away from tank before getting activated

        for (ArrayList<ArrayList<Bot>> row : aiEntities){
            for (ArrayList<Bot> chunk: row){
                for (Bot ai : chunk){
                    Vector3 aiSpot = ai.getTankBase().getModelInstance().transform.getTranslation(new Vector3());
                    Vector3 playerSpot = this.player.getTankBase().getModelInstance().transform.getTranslation(new Vector3());
                    Vector3 between = new Vector3(aiSpot).sub(playerSpot).scl(0.001f);
                    if (between.x*between.x + between.z*between.z < maxDistance){
                        ai.setActive(true);
                        ai.playBehaviour();
                        ai.increaseBulletTime();
                    }
                    else{
                        ai.setActive(false);
                    }
                    if(ai.getTankBase().getModelInstance().transform.getTranslation(new Vector3()).y > 9000){
                        ai.getTurret().getModelInstance().transform.trn(0,10000,0);
                    }
                }
            }
        }


        collisionWorld.performDiscreteCollisionDetection();
        this.entities.removeAll(entitiesToRemove);
        this.entitiesToRemove.clear();
        this.entities.addAll(entitiesToAdd);
        this.entitiesToAdd.clear();
        if(this.player.getTankBase().getModelInstance().transform.getTranslation(new Vector3()).y>=500){
            gsm.push(new LoseState(gsm,(int)this.score));
            gsm.render(((LoseState) gsm.currentState()).getBatch());

        }
        this.player.setNumberOfKills((int)this.getScore());

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



    /**
     * Place all objects in the scene, get data from map generator.
     * FUTURE: add level option and organization
     */
    public void generateMap(){
        try {
            initializeCamera();

            initializeCollisionEngine();

            SavingManager tankVector = new SavingManager();
            // Initializing player
            this.player = new Tank(new Turret(this.assets.initializeModel("wiiTankTurret.g3db")),
                    new TankBase(this.assets.initializeModel("wiiTankBody.g3db"),this.player), this, tankVector.getTankVector(), false);
            this.addEntityToCollisionAndMap(player.getTankBase(), false);
            this.addEntities(player.getTurret());


            // Initialize Arraylist
            for (int i = 0; i < 5; i++){
                ArrayList<ArrayList<Vector3>> row = new ArrayList<ArrayList<Vector3>>(5);
                for (int j = 0; j < 5; j++){
                    row.add(new ArrayList<Vector3>());
                }
                positions.add(row);
            }


            // WALL AND AI GENERATION
            // Spawn a chunk of walls and ai bots in each array spot of 5x5 array
            for (int x = 0; x < 5; x++){
                ArrayList<ArrayList<Entity>> thisRowWalls = new ArrayList<ArrayList<Entity>>(5);
                ArrayList<ArrayList<Bot>> thisRowAI = new ArrayList<ArrayList<Bot>>(5);
                for (int z = 0; z < 5; z++){
                    Vector3 centre = new Vector3(screenHeight*2 - x*screenHeight, 0, -screenWidth*2+z*screenWidth);

                    ArrayList<Entity> walls = getCluster(centre);
                    thisRowWalls.add(walls);
                    ArrayList<Vector3> invalidPoints = new ArrayList<Vector3>(); // The places where ais should spawn depend on where the walls are
                    for (Entity wall : walls){
                        invalidPoints.add(wall.getModelInstance().transform.getTranslation(new Vector3()));
                    }
                    thisRowAI.add(spawnChunkAI(centre, this.player.getNumberOfKills(), invalidPoints, x, z));
                }
                wallModels.add(thisRowWalls);
                aiEntities.add(thisRowAI);
            }

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
        if (wall){
            obj.getBody().setCollisionShape(new btSphereShape(50));
        }else{
            obj.getBody().setCollisionShape(new btBoxShape(a.getDimensions(new Vector3()).scl(0.5f)));

        }

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
        this.camera.setPosition(new Vector3(0, 2500, 0));

        // Offset view for 3D effect
        //this.camera.setPosition(new Vector3(0, 2500, 300));

        this.camera.lookAt(new Vector3(0, 0, 0));
        this.camera.rotateOnY(-90f);
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
            //this.addEntities(wall);
            this.addEntityToCollisionAndMap(wall, false);
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

        Vector3 vectToAdd = new Vector3(screenHeight*5, 0, 0);
        int rowI = 4;
        if (!isUp){
            vectToAdd.scl(-1f);
            rowI = 0;
        }


        // Then respawn the AIs
        int index = 0;
        for (ArrayList<Bot> chunk : aiEntities.get(rowI)){
            int index2 = 0;
            for (Bot ai : chunk){
                Vector3 pos = positions.get(rowI).get(index).get(index2).add(vectToAdd);
                ai.getTankBase().getModelInstance().transform.set(pos, new Quaternion());
                ai.getTurret().getModelInstance().transform.set(pos, new Quaternion());
                index2++;
            }
            index++;
        }

        // Move the elements in the list
        if (isUp){
            ArrayList<ArrayList<Vector3>> endRow = positions.remove(4);
            positions.add(0, endRow);
            ArrayList<ArrayList<Bot>> endRowTanks = aiEntities.remove(4);
            aiEntities.add(0, endRowTanks);
        }
        else{
            ArrayList<ArrayList<Vector3>> topRow = positions.remove(0);
            positions.add(topRow);
            ArrayList<ArrayList<Bot>> topRowTanks = aiEntities.remove(0);
            aiEntities.add(topRowTanks);
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

        // Then translate the AIs
        Vector3 vectToAdd = new Vector3(0, 0, screenWidth*5);
        int colI = 0;
        if (!isRight){
            vectToAdd.scl(-1f);
            colI = 4;
        }


        // Then respawn the AIs
        int index = 0;
        for (ArrayList<ArrayList<Bot>> row :aiEntities){
            int index2 = 0;
            for (Bot ai : row.get(colI)){
                Vector3 pos = positions.get(index).get(colI).get(index2).add(vectToAdd);
                ai.getTankBase().getModelInstance().transform.set(pos, new Quaternion());
                ai.getTurret().getModelInstance().transform.set(pos, new Quaternion());
                index2++;
            }
            index++;
        }

        // Move the elements in the list
        if (isRight){
            for (ArrayList<ArrayList<Bot>> row :aiEntities){
                ArrayList<Bot> firstChunk = row.remove(0);
                row.add(firstChunk);
            }
            for (ArrayList<ArrayList<Vector3>> row :positions){
                ArrayList<Vector3> firstChunk = row.remove(0);
                row.add(firstChunk);
            }
        }
        else{
            for (ArrayList<ArrayList<Bot>> row :aiEntities){
                ArrayList<Bot> lastChunk = row.remove(4);
                row.add(0, lastChunk);
            }
            for (ArrayList<ArrayList<Vector3>> row :positions){
                ArrayList<Vector3> lastChunk = row.remove(4);
                row.add(0, lastChunk);
            }
        }

    }


    /**
     *
     * Generating AIs as the game goes on. The level of AI depends on how many kills the player has. Higher tank behaviour
     * corresponds to more difficult opponents.
     *
     * @param x the index of grid currently generating, x vertical
     * @param z the corresponding z index of the grid generating, z horizonal
     * @param numOfKills the number of kills the tank has
     * @param centre the centre position of the chunk which AIs should be spawned around.
     * @return an arraylist of entities spawned around the centre position
     */
    private ArrayList<Bot> spawnChunkAI(Vector3 centre, int numOfKills, ArrayList<Vector3> wallPoints, int x, int z){
        // Number of ai bots per chunk should be random.
        float ran = MapGenerator.randomNumber(2, 5);

        // Map the corresponding 3D models to the AI type.
        HashMap<Integer, String[]> getAiModel = new HashMap<Integer, String[]>();
        getAiModel.put(1, new String[] {"wiiTankTurretGreen.g3db", "wiiTankBodyGreen.g3db"});
        getAiModel.put(2, new String[] {"wiiTankTurretBlue.g3db", "wiiTankBodyBlue.g3db"});
        getAiModel.put(3, new String[] {"wiiTankTurretPurple.g3db", "wiiTankBodyPurple.g3db"});
        getAiModel.put(4, new String[] {"wiiTankTurretBlack.g3db", "wiiTankBodyBlack.g3db"});

        /* To determine which behaviour of AI will be spawned, using a random number between zero and pi, setting boundaries
           for each player, these bondaries will grow as the number of kills increases.
        */

        float min1 = 0;
        float min2 = Math.max((float)Math.PI/2 - 0.23f*numOfKills, 0);
        float min3 = Math.max(4*(float)Math.PI/5 - 0.13f*numOfKills, 0);
        float min4 = Math.max(9*(float)Math.PI /10 - 0.85f*numOfKills, (float)Math.PI/2);

        // After many kills of the player, the probability of tank 3 and 4 should be 50/50%

        ArrayList<Bot> tanks = new ArrayList<Bot>();

        for (int i = 0; i < ran; i++){
            float ranZeroPI = MapGenerator.randomNumber(0, (float)Math.PI);
            int aiType = 0;
            if (ranZeroPI >= min4)
                aiType = 4;
            else if (ranZeroPI >= min3)
                aiType = 3;
            else if (ranZeroPI >= min2)
                aiType = 2;
            else if (ranZeroPI >= min1)
                aiType = 1;

            Vector3 ranPoint;
            boolean valid = true;
            do {
                ranPoint = new Vector3(centre.x + MapGenerator.randomNumber(-screenHeight / 2, screenHeight / 2)*0.85f, 0, centre.z + MapGenerator.randomNumber(-screenWidth / 2, screenWidth / 2)*0.85f);
                for (Vector3 p : wallPoints) {
                    if (ranPoint.dst2(p) < 32800) {
                        valid = false;
                        break;
                    }
                }
            }
            while(!valid);

            wallPoints.add(new Vector3(ranPoint));
//            positions.get(x).get(z).add(new Vector3(ranPoint));
            Bot newTank = null;
             newTank = new Bot(new Turret(this.assets.initializeModel(getAiModel.get(aiType)[0])),
                    new TankBase(this.assets.initializeModel(getAiModel.get(aiType)[1]),newTank),
                    this, ranPoint, true, 2, this.player);

            tanks.add(newTank);
            this.addEntityToCollisionAndMap(newTank.getTankBase(),false);
            this.addEntity(newTank.getTurret());
        }

        return tanks;
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

    public long getScore() {
        return score;
    }

    public void scored() {
        this.score++;
    }

}
