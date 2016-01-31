package com.encounterforsoul.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import screens.SplashScreen;
import utils.Assets;

public class StartGame extends Game {
	
	private Game game;
	public StartGame () {
	}

	@Override
	public void create () {

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Assets.instance.initGame(new AssetManager());
		setScreen(new SplashScreen(this));
		//AudioManager.instance.play(Assets.instance.music.song01);
	}
}
