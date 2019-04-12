package com.minitanks.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.minitanks.game.entities.Tank;
import com.minitanks.game.managers.InputManager;
import com.minitanks.game.managers.SavingManager;

import java.io.IOException;

public class LoseState extends State {
    private Stage stage;
    private SpriteBatch sb = new SpriteBatch();
    private ModelBatch batch;
    private InputManager iptMan;

   // private SpriteBatch sb = new SpriteBatch();

  //  private Tank player;
    private int score;


    // Constructor
    public LoseState(GameStateManager gsm, int score) {
        super(gsm);
        this.score = score;
        stage = new Stage();
       // this.player = player;

        try{
            SavingManager.resetTankPosition();

        }catch (IOException e){

        }


        // Wallpaper
        Drawable wallpaper1 = new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen.jpg")));
        Image wallpaper = new Image(wallpaper1, Scaling.fillX, Align.center);
        wallpaper.setWidth(Gdx.graphics.getWidth());
        stage.addActor(wallpaper);

        Drawable menutext = new TextureRegionDrawable(new TextureRegion(new Texture("loseText.png")));
        Image menuText = new Image(menutext);
        menuText.setSize((float)0.4*Gdx.graphics.getWidth(), (float)0.2*Gdx.graphics.getWidth());
        menuText.setPosition((float)0.5*Gdx.graphics.getWidth(), (float)0.4*Gdx.graphics.getHeight(), Align.center);
        stage.addActor(menuText);
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            System.exit(1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            gsm.push(new PlayState(gsm));
            gsm.render(((PlayState) gsm.currentState()).getBatch());
        }


    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void render(ModelBatch s) {
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
