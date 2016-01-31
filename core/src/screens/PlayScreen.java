package screens;

import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Sprites.Shaman;
import Sprites.Enemies.Enemy;
import Sprites.TileObjects.Item;
import Sprites.TileObjects.ItemDef;
import Sprites.TileObjects.Souls;
import box2dLight.RayHandler;
import utils.B2WorldCreator;
import utils.Const;
import utils.WorldContactListener;

/**
 * Created by DEADFOX
 */
public class PlayScreen implements Screen{
	//Reference to our Game, used to set Screens
	private Game game;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	public static boolean alreadyDestroyed = false;

	//basic playscreen variables
	private OrthographicCamera gamecam;
	private Viewport gamePort;
	private Hud hud;

	//Tiled map variables
	private TmxMapLoader maploader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	//Box2d variables
	private World world;
	private Box2DDebugRenderer b2dr;
	private RayHandler rHandler;
	private B2WorldCreator creator;

	//sprites
	private Shaman player;
	private Array<Item> items;
	private LinkedBlockingQueue<ItemDef> itemsToSpawn;


	public PlayScreen(Game game){
		this.game = game;
		//create cam used to follow mario through cam world
		gamecam = new OrthographicCamera();

		//create a FitViewport to maintain virtual aspect ratio despite screen size
		gamePort = new FitViewport(Const.GAME_WIDTH/1.7f / Const.PPM, Const.GAME_HEIGHT/1.7f /Const.PPM, gamecam);

		batch = new SpriteBatch();

		//create our game HUD for scores/timers/level info

		//Load our map and setup our map renderer
		maploader = new TmxMapLoader();
		map = maploader.load("sprites/tile/level1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1  / Const.PPM);

		//initially set our gamcam to be centered correctly at the start of of map
		gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

		//create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
		world = new World(new Vector2(0, -10), true);
		//allows for debug lines of our box2d world.
		b2dr = new Box2DDebugRenderer();
		rHandler = new RayHandler(world);
		creator = new B2WorldCreator(this, rHandler);
		player = new Shaman(this);
		player.setLight(rHandler);
		hud = new Hud(batch);

		items = new Array<Item>();
		itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

		world.setContactListener(new WorldContactListener());

		//AudioManager.instance.play(Assets.instance.music.song);

	}


	public TextureAtlas getAtlas(){
		return atlas;
	}

	public void spawnItem(ItemDef idef){
		itemsToSpawn.add(idef);
	}


	//    public void handleSpawningItems(){
	//        if(!itemsToSpawn.isEmpty()){
	//            ItemDef idef = itemsToSpawn.poll();
	//            if(idef.type == Souls.class){
	//                items.add(new Souls(this, idef.position.x, idef.position.y));
	//            }
	//        }
	//    }

	@Override
	public void show() {

	}

	public void handleInput(float dt){
		//control our player using immediate impulses
		if(player.currentState != Shaman.State.DEAD) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
				player.jump();
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 1.5f)
				player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -1.5f)
				player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
				player.fire();
		}

	}

	public void update(float dt){
		//handle user input first
		handleInput(dt);

		//takes 1 step in the physics simulation(60 times per second)
		world.step(1 / 60f, 6, 2);

		player.update(dt);
		rHandler.update();
		rHandler.setCombinedMatrix(gamecam.combined);
		for(Enemy enemy : creator.getEnemies()) {
			enemy.update(dt);
			if(enemy.getX() < player.getX() + 224 / Const.PPM) {
				enemy.b2body.setActive(true);
			}
		}

		for (Souls soul : creator.getSouls())
			soul.update(dt);

		hud.update(dt);

		//attach our gamecam to our players.x coordinate
		if(player.currentState != Shaman.State.DEAD) {
			gamecam.position.x = player.b2body.getPosition().x;
			gamecam.position.y = player.b2body.getPosition().y;
		}

		//update our gamecam with correct coordinates after changes
		gamecam.update();
		//tell our renderer to draw only what our camera can see in our game world.
		renderer.setView(gamecam);

	}


	@Override
	public void render(float delta) {
		//separate our update logic from render
		update(delta);

		//Clear the game screen with Black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//render our game map
		renderer.render();

		//renderer our Box2DDebugLines
		//b2dr.render(world, gamecam.combined);
		batch.setProjectionMatrix(gamecam.combined);
		batch.begin();
		for (Enemy enemy : creator.getEnemies())
			enemy.draw(batch);
		batch.end();
		rHandler.render();
		batch.begin();
		for (Souls soul : creator.getSouls())
			soul.draw(batch);
		player.draw(batch);
		//Assets.instance.fonts.gameTime.draw(batch, "Hello World", 200, 200);
		batch.end();

		//Set our batch to now draw what the Hud camera sees.
		batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();

		if(gameOver()){
			game.setScreen(new GameOverScreen(game));
			dispose();
		}

	}

	public boolean gameOver(){
		if(player.currentState == Shaman.State.DEAD && player.getStateTimer() > 3){
			return true;
		}
		return false;
	}

	@Override
	public void resize(int width, int height) {
		//updated our game viewport
		gamePort.update(width,height);

	}

	public TiledMap getMap(){
		return map;
	}
	public World getWorld(){
		return world;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		//dispose of all our opened resources
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		batch.dispose();
		//hud.dispose();
	}

	//public Hud getHud(){ return hud; }
}
