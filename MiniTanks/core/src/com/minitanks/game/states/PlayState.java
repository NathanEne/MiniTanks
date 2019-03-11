package com.minitanks.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.entities.*;
import com.minitanks.game.managers.InputManager;
import com.minitanks.world.GameMap;
import com.minitanks.world.TiledGameMap;

public class PlayState extends State {

    private boolean dynamicCam=false;
    private GameMap map;
    private Environment environment;
    private ModelBatch batch;
    private ModelInstance modelInstance;
    private Tank player;
    private Vector3 keyInputVector = new Vector3();
    private Vector3 mouseInputVector = new Vector3();
    private InputManager iptMan;

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

        this.map = new TiledGameMap();
        this.player = new Tank(new Turret(this.assets.initializeModel("wiiTankTurret.g3db"), this), new TankBase(this.assets.initializeModel("wiiTankBody.g3db"), this), this);
        this.map.addEntities(player.getTankBase());
        this.map.addEntities(player.getTurret());
        this.map.addEntities(new Bullet(assets.initializeModel("wiiTankBullet.g3db"), Vector3.X, 5f));
        //this.map.addEntities(new Wall(this.assets.initializeModel("wiiTankWall.g3db")));
        this.camOrth = new OrthographicCamera(5000, 5000);
        //this.camOrth.position.set(-1500,3500,0);
        this.camOrth.position.set(0,0,-2000);
        this.camOrth.lookAt(new Vector3(0, 0, 0));

        this.camOrth.far = 10000f;

        this.environment = new Environment();
        this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));

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
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            this.dynamicCam = !this.dynamicCam;
        }
        if (this.keyInputVector != Vector3.Zero)
            this.player.move(this.keyInputVector.nor(), this.mouseInputVector);
        this.keyInputVector = new Vector3(0, 0, 0);

    }

    @Override
    public void update(float dt) {
        handleInput();
        updateBullets();
    }

    @Override
    public void render(ModelBatch sb) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        this.camOrth.position.set(new Vector3(-1300,2500,0));
        this.camOrth.lookAt(new Vector3(0,0,0));

        this.camOrth.update();

        this.assets.render(camOrth, environment, map.getEntities());
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
            if (e instanceof Bullet){
                e.getModelInstance().transform.trn(((Bullet) e).getDirection().scl(((Bullet) e).getSpeed()) );
            }
        }
    }
}
