package Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import Sprites.Shaman;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import screens.PlayScreen;
import utils.Assets;
import utils.Const;

/**
 * Created by DEDFOX
 */
public class Souls extends Item {
	
	
    public Souls(PlayScreen screen, float x, float y, RayHandler rHandler) {
        super(screen, x, y, rHandler);
        
        setRegion(Assets.instance.gameElements.soul);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / Const.PPM);
        fdef.filter.categoryBits = Const.SOUL_BIT;
        fdef.filter.maskBits = Const.SHAMAN_BODY_BIT |
        		Const.OBJECT_BIT |
        		Const.GROUND_BIT ;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
       
    }

    @Override
    public void use(Shaman shaman) {
        destroy();
       // bonus.grow();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

}
