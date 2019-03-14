package com.minitanks.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.minitanks.game.managers.InputManager;

public class MenuState extends State {
    private Stage stage;

    private SpriteBatch sbatch;
    private ModelBatch batch;
    private InputManager iptMan;
    private Texture wallpaper, cogwheel;
    private Texture settings;

    private TextButton button;
    private TextButton.TextButtonStyle textButtonStyle;
    private ImageButton imgButton;
    private BitmapFont font;

    private Music lobby_music;

    // Constructor
    public MenuState(GameStateManager gsm) {
        super(gsm);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        sbatch = new SpriteBatch();
        wallpaper = new Texture("MenuScreen.jpg");

        // Lobby Music
        lobby_music = Gdx.audio.newMusic(Gdx.files.internal("wiiGaming.mp3"));
        lobby_music.setLooping(true);
        lobby_music.play();

        // Settings button
        settings = new Texture("cogWheels.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(settings));
        imgButton = new ImageButton(drawable);
        imgButton.setSize(100, 100);
        imgButton.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 100);

        stage.addActor(imgButton);

        // Play button
        textButtonStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont();
        textButtonStyle.font = font;

        button = new TextButton("PRESS P TO PLAY", textButtonStyle);
        button.setPosition(0.5f * Gdx.graphics.getWidth(), 0.5f * Gdx.graphics.getHeight());

        stage.addActor(button);

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            lobby_music.stop();
            gsm.push(new PlayState(gsm));
            gsm.update(Gdx.graphics.getDeltaTime());
            gsm.render(((PlayState) gsm.currentState()).getBatch());

        }
        if(imgButton.isChecked()) {
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

        sbatch.begin();
        sbatch.draw(wallpaper, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sbatch.end();

        stage.draw();

    }

    @Override
    public void dispose() {
        batch.dispose();
        wallpaper.dispose();
        stage.dispose();
        lobby_music.dispose();
        sbatch.dispose();

    }

    @Override
    public void setInputProcessor() {
        Gdx.input.setInputProcessor(this.iptMan);
    }

    public ModelBatch getBatch() {
        return batch;
    }


}
