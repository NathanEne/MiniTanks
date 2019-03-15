package com.minitanks.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
import com.minitanks.game.managers.InputManager;


public class MenuState extends State {
    private Stage stage;

    private ModelBatch batch;
    private InputManager iptMan;

    private ImageButton settingsButton, playButton;
    //private BitmapFont font;

    private Music lobby_music;

    // Constructor
    public MenuState(GameStateManager gsm) {
        super(gsm);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        // Wallpaper and HUD overlay
        Drawable wallpaper1 = new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen.jpg")));
        Image wallpaper = new Image(wallpaper1, Scaling.fillX, Align.center);
        stage.addActor(wallpaper);


        // Lobby Music
        lobby_music = Gdx.audio.newMusic(Gdx.files.internal("wiiGaming.mp3"));
        lobby_music.setLooping(true);
        lobby_music.play();


        // Settings button
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("cogWheel.png")));
        settingsButton = new ImageButton(drawable);
        settingsButton.setSize(60, 60);
        settingsButton.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Align.topRight);
        stage.addActor(settingsButton);


        // Play button
        Drawable drawable1 = new TextureRegionDrawable(new TextureRegion(new Texture("playButton.png")));
        playButton = new ImageButton(drawable1);
        playButton.setSize(150, 100);
        playButton.setPosition(0.5f * Gdx.graphics.getWidth(), 0.5f * Gdx.graphics.getHeight(), Align.center);
        stage.addActor(playButton);

    }

    @Override
    protected void handleInput() {
        if (playButton.isPressed()) {
            settingsButton.toggle();
            gsm.push(new PlayState(gsm));
            gsm.update(Gdx.graphics.getDeltaTime());
            gsm.render(((PlayState) gsm.currentState()).getBatch());

        }
        if(settingsButton.isPressed()) {
            settingsButton.toggle();
            gsm.push(new SettingsState(gsm));
            gsm.update(Gdx.graphics.getDeltaTime());
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
