package Sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Sprites.Enemies.Enemy;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import screens.PlayScreen;
import utils.Assets;
import utils.Const;

/**
 * Created by DEDFOX
 */

public class Shaman extends Sprite {
	public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD };
	public State currentState;
	public State previousState;

	public World world;
	public Body b2body;
	public PointLight light;

	private TextureRegion shamanStand;
	private Animation shamanRun;
	private TextureRegion shamanJump;
	private TextureRegion shamanDead;

	private float stateTimer;
	private boolean runningRight;
	private boolean shamanIsDead;
	private PlayScreen screen;
	public  static float defoltdistLight = 1f;
	public static float distLight = defoltdistLight;
	public static int savedSouls = 0;
	BitmapFont font;
	//private Array<FireBall> fireballs;

	public Shaman(PlayScreen screen){
		//initialize default values
		this.screen = screen;
		this.world = screen.getWorld();
		currentState = State.STANDING;
		previousState = State.STANDING;
		stateTimer = 0;
		font = new BitmapFont();
		font.setColor(Color.WHITE);

		runningRight = true;

		shamanRun = Assets.instance.animation.moveRight;

		shamanJump = Assets.instance.gameElements.shaman_jump;

		shamanStand =  Assets.instance.gameElements.shaman_stend;;

		shamanDead = Assets.instance.gameElements.shaman_jump;
		shamanDead.flip(true, false);

		//define  in Box2d
		defineShaman();

		//set initial values for marios location, width and height. And initial frame as marioStand.
		setBounds(0, 0, 32 / Const.PPM, 64 / Const.PPM);
		setRegion(shamanStand);

		//fireballs = new Array<FireBall>();

	}

	public void update(float dt){

		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		//update sprite with the correct frame depending on marios current action
		setRegion(getFrame(dt));

	}

	public TextureRegion getFrame(float dt){
		currentState = getState();

		TextureRegion region;

		//depending on the state, get corresponding animation keyFrame.
		switch(currentState){
		case DEAD:
			region = shamanDead;
			break;
		case JUMPING:
			region = shamanJump;
			break;
		case RUNNING:
			region = shamanRun.getKeyFrame(stateTimer, true);
			if (10*stateTimer%2 > 1.83) Assets.instance.sounds.walk.play();
			break;
		case FALLING:
		case STANDING:
		default:
			region = shamanStand;
			break;
		}

		//if running left and the texture isnt facing left... flip it.
		if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
			region.flip(true, false);
			runningRight = false;
		}

		else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
			region.flip(true, false);
			runningRight = true;
		}

		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		//stateTimer =  stateTimer + dt;
		previousState = currentState;
		return region;

	}

	public State getState(){
		//Test to Box2D for velocity on the X and Y-Axis
		if(shamanIsDead)
			return State.DEAD;
		else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
			return State.JUMPING;
		//if negative in Y-Axis is falling
		else if(b2body.getLinearVelocity().y < 0)
			return State.JUMPING;
		//if positive or negative in the X axis he is running
		else if(b2body.getLinearVelocity().x != 0){
			light.setDistance(defoltdistLight);	
			return State.RUNNING;
		}
		//if none of these return then he must be standing
		else
			return State.STANDING;
	}

	public void die() {

		if (!isDead()) {

			Const.musik.stop();
			//Assets.instance.music.song.stop();
			Assets.instance.sounds.dead.play();
			shamanIsDead = true;
			Filter filter = new Filter();
			filter.maskBits = Const.NOTHING_BIT;

			for (Fixture fixture : b2body.getFixtureList()) {
				fixture.setFilterData(filter);
			}

			b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
		}
	}

	public boolean isDead(){
		return shamanIsDead;
	}



	public void jump(){
		if ( currentState != State.JUMPING ) {
			b2body.applyLinearImpulse(new Vector2(0, 5.2f), b2body.getWorldCenter(), true);
			Assets.instance.sounds.jump.play();
			currentState = State.JUMPING;
		}
	}

	public void hit(Enemy enemy){
		die();
	}



	public void defineShaman(){
		BodyDef bdef = new BodyDef();
		bdef.position.set(64 / Const.PPM, 64 / Const.PPM); // start pos in game
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		Vector2[] vertice = new Vector2[4];
		vertice[0] = new Vector2(-16, -32).scl(1 / Const.PPM);
		vertice[1] = new Vector2(-16, 10).scl(1 / Const.PPM);
		vertice[2] = new Vector2(16, 10).scl(1 / Const.PPM);
		vertice[3] = new Vector2(16, -32).scl(1 / Const.PPM);
		shape.set(vertice);


		fdef.filter.categoryBits = Const.SHAMAN_BIT;
		fdef.filter.maskBits = Const.GROUND_BIT |
				Const.SOUL_BIT |
				Const.ENEMY_BIT |
				//Const.OBJECT_BIT |
				Const.ENEMY_BOOM_BIT |
				Const.SOUL_BIT;

		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);

		PolygonShape head = new PolygonShape();
		Vector2[] vert = new Vector2[4];
		vert[0] = new Vector2(-16, -32).scl(1 / Const.PPM);
		vert[1] = new Vector2(-16, 10).scl(1 / Const.PPM);
		vert[2] = new Vector2(16, 10).scl(1 / Const.PPM);
		vert[3] = new Vector2(16, -32).scl(1 / Const.PPM);
		head.set(vert);
		fdef.filter.categoryBits = Const.SHAMAN_BODY_BIT;
		fdef.shape = head;
		fdef.isSensor = true;

		b2body.createFixture(fdef).setUserData(this);
	}

	public float getStateTimer(){
		return stateTimer;
	}

	//    public void fire(){
	//        fireballs.add(new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
	//    }

	public void draw(Batch batch){ 
		super.draw(batch);

		//font.draw(batch, "Hello World", b2body.getPosition().x, b2body.getPosition().y);
		//        for(FireBall ball : fireballs)
		//            ball.draw(batch);
	}

	public void setLight(RayHandler rHandler) {
		light = new PointLight(rHandler, 50, Color.WHITE, distLight , 0,0);
		light.setSoftnessLength(1f);//radius 
		light.attachToBody(b2body);
	}

	public void fire() {
		if ( currentState == State.STANDING ) {
			distLight +=0.1f;
			Assets.instance.sounds.booben.play();
			light.setDistance(distLight);		
		} 
		else distLight = defoltdistLight;
	}
}
