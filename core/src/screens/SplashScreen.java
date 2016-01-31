package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FillViewport;

import utils.Assets;
import utils.Const;

public class SplashScreen extends AbstractGameScreen {

	public static Music musik;
	
	public SplashScreen(Game game) {
		super(game);
	}

	private Texture texture = new Texture(Gdx.files.internal("logo/logo1.png"));
	private Texture texture2 = new Texture(Gdx.files.internal("logo/logo2.png"));
	private Image splashImage = new Image(texture);
	private Image splashImage2 = new Image(texture2);
	//private Image fon = new Image(fon_texture);
	private Stage stage;
	public boolean animationDone = false;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

		if(Assets.instance.assetsUpdate()){ // check if all files are loaded

			if(animationDone){ // when the animation is finished, go to MainMenu()
				//Assets.setMenuSkin(); // uses files to create menuSkin
				game.setScreen(new PlayScreen(game));
			}
		}
	}

	@Override
	public void resize(int width, int height) {  
		stage.getViewport().setScreenSize(width, height);
	}

	@Override
	public void show() {
		Camera cam = new OrthographicCamera(Const.GAME_WIDTH, Const.GAME_HEIGHT);
		FillViewport view = new FillViewport(Const.GAME_WIDTH, Const.GAME_HEIGHT, cam);
		stage = new Stage(view);

		//fon.setFillParent(true);
		//fon.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//stage.addActor(fon);
		splashImage.setPosition(Const.GAME_WIDTH/2 - splashImage.getWidth()/2 , Const.GAME_HEIGHT/2 + splashImage.getHeight()/2);
		splashImage2.setPosition(Const.GAME_WIDTH/2 - splashImage2.getWidth()/2 , Const.GAME_HEIGHT/2 - splashImage2.getHeight()/1.5f);
		stage.addActor(splashImage);
		stage.addActor(splashImage2);


		splashImage.addAction(Actions.sequence(Actions.alpha(0),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						Const.musik.play();
					}
				}),
				Actions.fadeIn(1.0f),Actions.delay(2.0f),
				Actions.alpha(1),Actions.fadeOut(1.0f),
				Actions.run(new Runnable() {
					@Override
					public void run() {
					}
				})));

		splashImage2.addAction(Actions.sequence(Actions.alpha(0),
				Actions.delay(2.0f),
				Actions.fadeIn(1.0f),Actions.delay(1.0f),
				Actions.alpha(1),Actions.fadeOut(1.0f),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						animationDone = true;
					}
				})));
		//Assets.manager.clear(); 
		//not necessary, only when splash called more then once
		//Assets.instance.queueLoading(); 
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {       
	}

	@Override
	public void resume() {      
	}

	@Override
	public void dispose() {
		texture.dispose();
		texture2.dispose();
		stage.dispose();
	}
}