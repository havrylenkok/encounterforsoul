package Sprites.TileObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import Sprites.Shaman;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import screens.PlayScreen;
import utils.Const;

/**
 * Created by DEDFOX
 */
public abstract class ItemBord extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;
    protected PointLight light;

    public ItemBord(PlayScreen screen, float x, float y, RayHandler rHandler){
        this.screen = screen;
        this.world = screen.getWorld();
        toDestroy = false;
        destroyed = false;
        setPosition(x, y);
        setBounds(getX(), getY(), 40 / Const.PPM, 40 / Const.PPM);
        defineItem();
    }

    public abstract void defineItem();
    public abstract void use(Shaman shaman);

    public void update(float dt){
    }

    public void draw(Batch batch){
    }

    public void destroy(){
       
    }
    public void reverseVelocity(boolean x, boolean y){
    }
}
