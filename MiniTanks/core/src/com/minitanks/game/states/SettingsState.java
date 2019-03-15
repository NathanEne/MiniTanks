package com.minitanks.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.minitanks.game.managers.InputManager;

public class SettingsState extends State {
    private Stage stage;

    private ModelBatch batch;
    private InputManager iptMan;

    private TextButton button;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;

    // Constructor
    public SettingsState(GameStateManager gsm) {
        super(gsm);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        // Wallpaper
        Drawable wallpaper1 = new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen.jpg")));
        Image wallpaper = new Image(wallpaper1, Scaling.fillX, Align.center);
        stage.addActor(wallpaper);


        // Temporary button
        textButtonStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont();
        textButtonStyle.font = font;

        button = new TextButton("it's a work in progress ok.", textButtonStyle);
        button.setPosition(0.5f * Gdx.graphics.getWidth(), 0.5f * Gdx.graphics.getHeight(), Align.center);
        stage.addActor(button);

    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            gsm.pop();
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
