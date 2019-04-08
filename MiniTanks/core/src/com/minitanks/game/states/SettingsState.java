package com.minitanks.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
        stage.addActor(wallpaper);


        // Save button
        ImageButton saveButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("playButton.png"))), new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen.jpg"))));
        saveButton.setPosition(0.5f * Gdx.graphics.getWidth(), 0.5f * Gdx.graphics.getHeight(), Align.center);
        saveButton.setSize(150, 100);
        stage.addActor(saveButton);
        Gdx.input.setInputProcessor(stage);

    }

    public SettingsState(GameStateManager gsm, Tank player) {
        super(gsm);
        stage = new Stage();
        this.player = player;


        // Wallpaper
        Drawable wallpaper1 = new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen.jpg")));
        Image wallpaper = new Image(wallpaper1, Scaling.fillX, Align.center);
        stage.addActor(wallpaper);


        // Save button
        ImageButton saveButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("playButton.png"))), new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen.jpg"))));
        saveButton.setPosition(0.5f * Gdx.graphics.getWidth(), 0.5f * Gdx.graphics.getHeight(), Align.center);
        saveButton.setSize(150, 100);
        stage.addActor(saveButton);
        Gdx.input.setInputProcessor(stage);

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
