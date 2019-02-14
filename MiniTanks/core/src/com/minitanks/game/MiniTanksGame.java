package com.minitanks.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minitanks.game.states.GameStateManager;

import com.minitanks.game.states.PlayState;


public class MiniTanksGame extends ApplicationAdapter {

	private GameStateManager gsm;

	@Override
	public void create() {

		Gdx.gl.glClearColor(174/255f, 174/255f, 174/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm = new GameStateManager();
		gsm.push(new PlayState(gsm));
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gsm.update(Gdx.graphics.getDeltaTime());
		if(gsm.currentState() instanceof PlayState){
			gsm.render(((PlayState) gsm.currentState()).getBatch());

		}

	}

	@Override
	public void dispose() {

	}
}
