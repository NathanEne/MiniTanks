package com.minitanks.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.minitanks.game.states.*;


public class MiniTanksGame extends ApplicationAdapter {

	private GameStateManager gsm;

	/**
	 * Methods from ApplicationAdapter are overridden to create and render the game
	 *
	 */

	@Override
	public void create() {

		Gdx.gl.glClearColor(174/255f, 174/255f, 174/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm = new GameStateManager();
		//gsm.push(new MenuState(gsm));
		gsm.push(new PlayState(gsm));

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());

		if(gsm.currentState() instanceof MenuState){
			gsm.render(((MenuState) gsm.currentState()).getBatch());
		}

		if(gsm.currentState() instanceof PlayState) {
			gsm.render(((PlayState) gsm.currentState()).getBatch());
		}
		if(gsm.currentState() instanceof SettingsState) {
			gsm.render(((SettingsState) gsm.currentState()).getBatch());
		}
		if(gsm.currentState() instanceof LoseState) {
			gsm.render(((LoseState) gsm.currentState()).getBatch());
		}


	}

	@Override
	public void dispose() {

	}
}
