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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.minitanks.game.managers.InputManager;

public class SettingsState extends State {
    private Stage stage;

    private SpriteBatch sbatch;
    private ModelBatch batch;
    private InputManager iptMan;
    private Texture img;

    private TextButton button;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;

    private Music lobby_music;

    // Constructor
    public SettingsState(GameStateManager gsm) {
        super(gsm);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        sbatch = new SpriteBatch();
        img = new Texture("MenuScreen.jpg");

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
        sbatch.draw(img, 0, 0);
        sbatch.end();

        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
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
