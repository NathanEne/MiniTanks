package com.minitanks.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.minitanks.game.managers.InputManager;

import java.awt.*;


public class MenuState extends State {
    private Stage stage;

    private ModelBatch batch;
    private InputManager iptMan;

    private ImageButton settingsButton, playButton;

    private Music lobby_music;

    // Constructor
    public MenuState(GameStateManager gsm) {
        super(gsm);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        // Wallpaper
        Drawable wallpaper1 = new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen.jpg")));
        Image wallpaper = new Image(wallpaper1, Scaling.fillY, Align.center);
        wallpaper.setWidth(Gdx.graphics.getWidth());
        stage.addActor(wallpaper);


        // Lobby Music
        lobby_music = Gdx.audio.newMusic(Gdx.files.internal("wiiGaming.mp3"));
        lobby_music.setLooping(true);
        lobby_music.play();

        // Text
        Drawable wiitanks = new TextureRegionDrawable(new TextureRegion(new Texture("wiiTanks.png")));
        Image wiiTanks = new Image(wiitanks);
        wiiTanks.setSize((float)0.66*Gdx.graphics.getWidth(), (float)0.33*Gdx.graphics.getWidth());
        wiiTanks.setPosition((float)0.5*Gdx.graphics.getWidth(), (float)0.8*Gdx.graphics.getHeight(), Align.center);
        stage.addActor(wiiTanks);

        Drawable menutext = new TextureRegionDrawable(new TextureRegion(new Texture("menuText.png")));
        Image menuText = new Image(menutext);
        menuText.setSize((float)0.4*Gdx.graphics.getWidth(), (float)0.2*Gdx.graphics.getWidth());
        menuText.setPosition((float)0.5*Gdx.graphics.getWidth(), (float)0.4*Gdx.graphics.getHeight(), Align.center);
        stage.addActor(menuText);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            gsm.push(new PlayState(gsm));
            gsm.render(((PlayState) gsm.currentState()).getBatch());

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.push(new SettingsState(gsm));
            gsm.render(((SettingsState) gsm.currentState()).getBatch());
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
        lobby_music.dispose();

    }

    @Override
    public void setInputProcessor() {
        Gdx.input.setInputProcessor(this.iptMan);
    }

    public ModelBatch getBatch() {
        return batch;
    }


}
