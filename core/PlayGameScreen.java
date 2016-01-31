/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package com.alterego.jelly.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.alterego.jelly.levelobjects.AbstractTextureReg;
import com.alterego.jelly.ui.LevelBuilder;
import com.alterego.jelly.ui.MarketFreezeUI;
import com.alterego.jelly.ui.PauseUI;
import com.alterego.jelly.ui.WinnerUI;
import com.alterego.jelly.utils.Assets;
import com.alterego.jelly.utils.Const;
import com.alterego.jelly.utils.GamePreferences;
import com.alterego.jelly.utils.JellyButton;
import com.alterego.jelly.utils.LevelData;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class PlayGameScreen extends AbstractGameScreen {

	private JellyButton  buttonPause, buttonReplay, buttonBuyFreeze ;
	private Image imgFreeze, star, star1, star2, star3;
	private Image freezeFon, helpNavigate, helpFreeze;
	private Label gameTime, freezes;
	private LevelBuilder levelBuilder;
	private WinnerUI winnerUi;
	private PauseUI pauseUi;
	private MarketFreezeUI marketUi;
	private Stage stage;
	private SpriteBatch batch;
	private TextButton button_text; 
	private String gameLevel;
	private Game game;
	private int stars = 3;
	private Animation animFreeze = null;
	private Animation animFreezeOn = Assets.instance.animation.jellyFreezeOn;
	private Animation animBlick = Assets.instance.animation.jellyIceBlick;
	AbstractTextureReg ice = new AbstractTextureReg(Assets.instance.gameElements.ice);
	private boolean addHelp = false;
	public static enum GameState  {PLAY, MOVING, PAUSE, BEGIN, WINNER, NEXTLVL};
	public static GameState gameState;// = GameState.PLAY;
	public static int freezers;
	private boolean paused = false;
	public float time = 0, stateTime = 0;

	public PlayGameScreen (Game game, String gameLevel) {
		super(game);
		this.game = game;
		this.gameLevel = gameLevel;
		if (gameLevel.equals("1")) addHelp  = true;
		//Gdx.app.log("=====>", "--gameLevel-" + gameLevel);
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		levelBuilder.update(delta);
		levelBuilder.render(batch);
		batch.end();

		stage.act();
		stage.draw();
		if (addHelp) helpFreezAnimation(delta);

		if (gameState == GameState.WINNER && !winnerUi.isVisible) {
			winnerUi.showWindow(true);
			winnerUi.setTime(time);
		}
		if (gameState == GameState.PLAY || gameState == GameState.MOVING) {
			updateGameTime(delta);
		}
		if (freezers != LevelData.freezers){
			updateFreeze();
		}
	}

	private void updateGameTime(float delta) {
		time += delta;
		int seconds = (int) (time) % 60 ;
		int minutes = (int) ((time /60) % 60);
		String t = null;
		if (seconds < 10 ) t = "0" + String.valueOf(seconds);
		else t = String.valueOf(seconds);
		gameTime.setText(String.valueOf(minutes) +":"+ t);
		if (time > 200 && stars == 1) dropStar(star1);
		if (time > 8 && stars == 2) dropStar(star2);
		if (time > 6 && stars == 3) dropStar(star3);
	}

	private void dropStar(Image img) {
		stars--;
		img.setDrawable(new TextureRegionDrawable(Assets.instance.gameElements.starOff));
		star.setPosition(img.getX() - 45, img.getY() - 50);
		star.addAction(Actions.sequence(Actions.scaleTo(0.3f, 0.3f), Actions.alpha(1f), Actions.parallel(Actions.rotateBy(210f, 0.9f), Actions.scaleTo(0.8f, 0.8f, 0.9f),
				Actions.moveTo(star.getX()-45f, star.getY()-210f, 1.2f), Actions.alpha(0f, 0.9f, Interpolation.circleIn) )));
	}


	private void updateFreeze() {
		freezers = LevelData.freezers;
		freezes.setText(String.valueOf(freezers));
		imgFreeze.addAction(Actions.sequence(Actions.scaleTo(0f, 0f), Actions.alpha(1f), Actions.parallel(Actions.rotateBy(360f, 0.7f), Actions.scaleTo(1f, 1f, 0.5f), Actions.alpha(0f, 1.0f, Interpolation.circleIn) )));
	}

	private void helpFreezAnimation(float delta) {
		batch.begin();
		if (ice.animation == animBlick && ice.animation.isAnimationFinished(stateTime)){
			ice.animation = null;
			gameState = GameState.PLAY;
		}
		if (ice.animation != null && !ice.animation.isAnimationFinished(stateTime)){

			ice.setRegion(ice.animation.getKeyFrame(stateTime+=delta, false));

			batch.draw(ice.getTexture(), ice.position.x, ice.position.y, ice.origin.x, ice.origin.y, ice.dimension.x, ice.dimension.y, ice.scale.x, ice.scale.y,
					ice.rotation, ice.getRegionX(), ice.getRegionY(), ice.getRegionWidth(), ice.getRegionHeight(), ice.flipX, ice.flipY);

			if (ice.animation != animBlick && ice.animation.isAnimationFinished(stateTime)){
				setAnimation(animBlick);
			}
		}
		batch.end();
	}

	public void setAnimation (Animation animation){
		stateTime = 0;
		ice.animation = animation;
		if (ice.animation == animFreezeOn )	ice.scale.set(2.5f, 2.6f);
		else ice.scale.set(1.1f, 1.1f);
		ice.position.x = LevelData.posX+84;
		ice.position.y = LevelData.posY-68;
	}

	@Override
	public void resize(int width, int height) {  
		stage.getViewport().setScreenSize(width, height);
	}


	@Override
	public void show () {
		Camera cam = new OrthographicCamera(Const.GAME_WIDTH, Const.GAME_HEIGHT);
		StretchViewport view = new StretchViewport(Const.GAME_WIDTH, Const.GAME_HEIGHT, cam);
		stage = new Stage(view);
		batch = (SpriteBatch) stage.getBatch();

		Gdx.input.setInputProcessor(stage);

		winnerUi = new WinnerUI(game);
		pauseUi = new PauseUI(game);
		marketUi= new MarketFreezeUI(this);

		levelBuilder = new LevelBuilder(gameLevel);

		rebuildStage();
		init();
	}

	private void init() {

	}

	private void rebuildStage () {
		buttonPause = new JellyButton(Assets.instance.gameElements.pause);
		buttonPause.setMyScaleSize(1.3f, 1.3f);
		buttonPause.setPosition(8, Const.GAME_HEIGHT - buttonPause.getHeight() - 8);
		buttonPause.addTouchListener(new RunnableAction() {
			public void run() {
				pauseUi.showWindow(true);
			}
		}, null);

		buttonReplay = new JellyButton(Assets.instance.gameElements.replay);
		buttonReplay.setMyScaleSize(1.3f, 1.3f);
		buttonReplay.setPosition(8, Const.GAME_HEIGHT - 2*buttonReplay.getHeight() - 15);
		buttonReplay.addTouchListener(new RunnableAction() {
			public void run() {
				winnerUi.showWindow(true);
				winnerUi.setTime(110);
			}
		}, null);

		Label.LabelStyle labelStyle = new Label.LabelStyle(Assets.instance.fonts.freezers, Color.WHITE);
		gameTime = new Label("0:00", labelStyle);
		gameTime.setAlignment(Align.left);
		gameTime.setPosition(Const.GAME_WIDTH - 135, Const.GAME_HEIGHT - 60);
		gameTime.setWrap(true);
		gameTime.setWidth(350f);


		star = new Image(Assets.instance.gameElements.star);
		star.setScale(0.3f);
		star.setOrigin(star.getWidth()/2, star.getHeight()/2);
		star.addAction(Actions.alpha(0f));
		star1 = new Image(Assets.instance.gameElements.star);
		star1.setScale(0.3f);
		star1.setPosition(Const.GAME_WIDTH - 60, Const.GAME_HEIGHT - 92);
		star2 = new Image(Assets.instance.gameElements.star);
		star2.setScale(0.3f);
		star2.setPosition(star1.getX() - 40, Const.GAME_HEIGHT - 92);
		star3 = new Image(Assets.instance.gameElements.star);
		star3.setScale(0.3f);
		star3.setPosition(star2.getX() - 40, Const.GAME_HEIGHT - 92);

		Label.LabelStyle labelStyle2 = new Label.LabelStyle(Assets.instance.fonts.freezers, Color.WHITE);
		freezes = new Label("0", labelStyle2);
		freezes.setText(String.valueOf(LevelData.freezers));
		freezes.setAlignment(Align.right);
		freezes.setPosition(Const.GAME_WIDTH - 200, Const.GAME_HEIGHT - 170);
		freezes.setWrap(true);
		freezes.setWidth(100f);

		freezeFon = new Image(Assets.instance.gameElements.freezeBuyFon);
		freezeFon.setPosition(Const.GAME_WIDTH - 158, Const.GAME_HEIGHT - buttonReplay.getHeight() - 107);
		freezeFon.setScale(0.6f);

		buttonBuyFreeze = new JellyButton(Assets.instance.gameElements.freezeBuy);
		buttonBuyFreeze.setMyScaleSize(1.6f, 1.6f);
		buttonBuyFreeze.setPosition(Const.GAME_WIDTH - buttonBuyFreeze.getWidth() - 15, Const.GAME_HEIGHT - buttonReplay.getHeight() - 100);
		buttonBuyFreeze.addTouchListener(new RunnableAction() {
			public void run() {
				marketUi.showWindow(true);
			}
		}, null);

		imgFreeze= new Image(Assets.instance.gameElements.freeze);
		imgFreeze.setOrigin(imgFreeze.getWidth()/2, imgFreeze.getHeight()/2);
		imgFreeze.setScale(0.7f);
		imgFreeze.setPosition(freezeFon.getX() - 17, freezeFon.getY() - 25);
		imgFreeze.addAction(Actions.alpha(0f));

		if (addHelp) {
			float delay = 5f;
			if (GamePreferences.instance.isSensorXYZ){
				delay = 4f;
				helpNavigate = new Image(Assets.instance.menuElements.navigate_tel);
				helpNavigate.setOrigin(helpNavigate.getWidth()/2, helpNavigate.getHeight()/2);
				helpNavigate.setScale(0.7f);
				helpNavigate.setPosition(Const.GAME_WIDTH - helpNavigate.getWidth() - 25, Const.GAME_HEIGHT - 500);
				helpNavigate.addAction(sequence(Actions.alpha(0f), Actions.delay(2f),
						new RunnableAction() {
					public void run() {
						gameState = GameState.PAUSE;
					}},
					Actions.alpha(1f, 0.5f),
					Actions.rotateBy(15f, 0.4f, Interpolation.fade), Actions.rotateBy(-30f, 0.8f, Interpolation.fade),
					Actions.rotateBy(25f, 0.7f, Interpolation.fade), Actions.alpha(0f, 0.4f) ));
			} else {
				helpNavigate = new Image(Assets.instance.menuElements.navigate_finger);
				helpNavigate.setOrigin(helpNavigate.getWidth()/2, helpNavigate.getHeight()/2);
				helpNavigate.setScale(0.7f);
				helpNavigate.setPosition(Const.GAME_WIDTH - helpNavigate.getWidth() - 25, Const.GAME_HEIGHT - 500);
				helpNavigate.addAction(sequence(Actions.alpha(0f), Actions.delay(2f),
						new RunnableAction() {
					public void run() {
						gameState = GameState.PAUSE;
					}},
					Actions.alpha(1f, 0.5f), Actions.moveBy(-150f, 0, 0.7f, Interpolation.fade),
					Actions.moveBy(80f, 0, 0.7f, Interpolation.fade), Actions.moveBy(0, 150f, 0.7f, Interpolation.fade),
					Actions.moveBy(0, -200f, 0.7f, Interpolation.linear),Actions.alpha(0f, 0.4f) ));
			}
			helpFreeze = new Image(Assets.instance.menuElements.navigate_finger);
			helpFreeze.setOrigin(helpFreeze.getWidth()/2, helpFreeze.getHeight()/2);
			helpFreeze.setScale(-0.7f, 0.7f);
			helpFreeze.setPosition(helpFreeze.getWidth() + 35, Const.GAME_HEIGHT - 500);
			helpFreeze.addAction(sequence(Actions.alpha(0f), Actions.delay(delay), Actions.alpha(1f, 0.5f),
					Actions.moveBy(LevelData.posX - helpFreeze.getX() + 20, LevelData.posY - helpFreeze.getY() - helpFreeze.getHeight() - 28, 0.7f, Interpolation.fade),
					new RunnableAction() {
				public void run() {
					setAnimation(animFreezeOn);
				}},
				Actions.alpha(0f, 0.2f) ));
		}

		if (addHelp) {
			stage.addActor(helpNavigate);
			stage.addActor(helpFreeze);
		}
		stage.addActor(buttonPause);
		stage.addActor(buttonReplay);
		stage.addActor(gameTime);
		stage.addActor(freezeFon);
		stage.addActor(freezes);
		stage.addActor(buttonBuyFreeze);
		stage.addActor(star1);
		stage.addActor(star2);
		stage.addActor(star3);
		stage.addActor(star);
		stage.addActor(imgFreeze);
		stage.addActor(levelBuilder);
		stage.addActor(winnerUi);
		stage.addActor(marketUi);
		stage.addActor(pauseUi);

	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
	}

	@Override
	public void pause () {
		paused = true;
		gameState = GameState.PAUSE;
	}

	@Override
	public void resume () {
		super.resume();
		// Only called on Android!
		gameState = GameState.PLAY;
		paused = false;
		pauseUi.showWindow(true);
	}

	@Override
	public void hide() {
	}

}
