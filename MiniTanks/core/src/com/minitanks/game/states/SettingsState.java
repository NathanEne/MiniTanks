package com.minitanks.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.minitanks.game.entities.Tank;
import com.minitanks.game.managers.InputManager;
import com.minitanks.game.managers.SavingManager;

import java.io.IOException;

public class SettingsState extends State {
    private Stage stage;

    private ModelBatch batch;
    private InputManager iptMan;

    private ImageButton saveButton;

    private Tank player;


    // Constructor
    public SettingsState(GameStateManager gsm) {
        super(gsm);
        stage = new Stage();



        // Wallpaper
        Drawable wallpaper1 = new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen.jpg")));
        Image wallpaper = new Image(wallpaper1, Scaling.fillX, Align.center);
        wallpaper.setWidth(Gdx.graphics.getWidth());
        stage.addActor(wallpaper);


        // Settings Text
        Drawable settingstext = new TextureRegionDrawable(new TextureRegion(new Texture("settingsText.png")));
        Image settingsText = new Image(settingstext);
        settingsText.setSize((float)0.4*Gdx.graphics.getWidth(), (float)0.2*Gdx.graphics.getWidth());
        settingsText.setPosition((float)0.5*Gdx.graphics.getWidth(), (float)0.5*Gdx.graphics.getHeight(), Align.center);
        stage.addActor(settingsText);

    }

    public SettingsState(GameStateManager gsm, Tank player) {
        super(gsm);
        stage = new Stage();
        this.player = player;


        // Wallpaper
        Drawable wallpaper1 = new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen.jpg")));
        Image wallpaper = new Image(wallpaper1, Scaling.fillX, Align.center);
        wallpaper.setWidth(Gdx.graphics.getWidth());
        stage.addActor(wallpaper);


        // Save button
        Drawable settingstext = new TextureRegionDrawable(new TextureRegion(new Texture("settingsText.png")));
        Image settingsText = new Image(settingstext);
        settingsText.setSize((float)0.4*Gdx.graphics.getWidth(), (float)0.2*Gdx.graphics.getWidth());
        settingsText.setPosition((float)0.5*Gdx.graphics.getWidth(), (float)0.5*Gdx.graphics.getHeight(), Align.center);
        stage.addActor(settingsText);

    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.pop();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            try {
                SavingManager.writeTankPosition(this.player);
            }
            catch(IOException e) {

            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            try {
                SavingManager.resetTankPosition();
            }
            catch(IOException e) {

            }
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(ModelBatch sb) {
        Gdx.gl.glClearColor(174/255f, 174/255f, 174/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();

    }

    @Override
    public void setInputProcessor() {
        Gdx.input.setInputProcessor(this.iptMan);
    }

    public ModelBatch getBatch() {
        return batch;
    }
}
