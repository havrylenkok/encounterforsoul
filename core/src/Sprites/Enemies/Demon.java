package Sprites.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import Sprites.Shaman;
import screens.PlayScreen;
import utils.Assets;
import utils.Const;

/**
 * Created by DEDFOX
 */
public class Demon extends Enemy
{
	private float stateTime;
	private Animation walkAnimation;
	private boolean setToDestroy;
	private boolean destroyed;
	float angle;


	public Demon(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		walkAnimation = Assets.instance.animation.demonLeft;
		stateTime = 0;
		setBounds(getX(), getY(), 64 / Const.PPM, 64 / Const.PPM);
		setToDestroy = false;
		destroyed = false;
		angle = 0;
	}

	public void update(float dt){
		stateTime += dt;
		if(setToDestroy && !destroyed){
			world.destroyBody(b2body);
			destroyed = true;
			stateTime = 0;
		}
		else if(!destroyed) {
			b2body.setLinearVelocity(velocity);
			setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
			setRegion(walkAnimation.getKeyFrame(stateTime, true));
		}
	}

	@Override
	protected void defineEnemy() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(getX(), getY());
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

		//        CircleShape shape = new CircleShape();
		//        shape.setRadius(16 / Const.PPM);
		fdef.filter.categoryBits = Const.ENEMY_BIT;
		fdef.filter.maskBits = Const.GROUND_BIT |
				Const.SOUL_BIT |
				Const.ENEMY_BIT |
				Const.OBJECT_BIT |
				Const.SHAMAN_BIT;

		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);

		//Create the Head here:
		PolygonShape head = new PolygonShape();
		Vector2[] vert = new Vector2[4];
		vert[0] = new Vector2(-5, 8).scl(1 / Const.PPM);
		vert[1] = new Vector2(5, 8).scl(1 / Const.PPM);
		vert[2] = new Vector2(-3, 3).scl(1 / Const.PPM);
		vert[3] = new Vector2(3, 3).scl(1 / Const.PPM);
		head.set(vertice);

		fdef.shape = head;
		fdef.restitution = 0.5f;
		fdef.filter.categoryBits = Const.ENEMY_BOOM_BIT;
		b2body.createFixture(fdef).setUserData(this);

	}

	public void draw(Batch batch){
		if(!destroyed || stateTime < 1)
			super.draw(batch);
	}



	@Override
	public void hitOnHead(Shaman mario) {
		setToDestroy = true;
		//s.play();
	}

	@Override
	public void hitByEnemy(Enemy enemy) {
		reverseVelocity(true, false);
		if(velocity.x > 0) walkAnimation = Assets.instance.animation.demonRigh;
		else walkAnimation = Assets.instance.animation.demonLeft;
	}

	@Override
	public void stopBorder() {
		reverseVelocity(true, false);
		if(velocity.x > 0) walkAnimation = Assets.instance.animation.demonRigh;
		else walkAnimation = Assets.instance.animation.demonLeft;
	}
}
