package utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Sprites.Shaman;
import Sprites.Enemies.Enemy;
import Sprites.TileObjects.InteractiveTileObject;
import Sprites.TileObjects.Item;

/**
 * Created by brentaureli on 9/4/15.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case Const.SHAMAN_BODY_BIT | Const.SOUL_BIT:
                if(fixA.getFilterData().categoryBits == Const.SHAMAN_BODY_BIT)
                    ((Item) fixB.getUserData()).use((Shaman) fixA.getUserData());
                else
                    ((Item) fixA.getUserData()).use((Shaman) fixB.getUserData());
                break;
            case Const.ENEMY_BOOM_BIT | Const.SHAMAN_BIT:
                if(fixA.getFilterData().categoryBits == Const.ENEMY_BOOM_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead((Shaman) fixB.getUserData());
                else
                    ((Enemy)fixB.getUserData()).hitOnHead((Shaman) fixA.getUserData());
                break;
            case Const.ENEMY_BIT | Const.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Const.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).stopBorder();
                else
                    ((Enemy)fixB.getUserData()).stopBorder();
                break;
            case Const.SHAMAN_BIT | Const.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Const.SHAMAN_BIT)
                    ((Shaman) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                else
                    ((Shaman) fixB.getUserData()).hit((Enemy)fixA.getUserData());
                break;
            case Const.ENEMY_BIT | Const.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).hitByEnemy((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).hitByEnemy((Enemy)fixA.getUserData());
                break;
//            case Const.FIREBALL_BIT | Const.OBJECT_BIT:
//                if(fixA.getFilterData().categoryBits == Const.FIREBALL_BIT)
//                    ((FireBall)fixA.getUserData()).setToDestroy();
//                else
//                    ((FireBall)fixB.getUserData()).setToDestroy();
//                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
