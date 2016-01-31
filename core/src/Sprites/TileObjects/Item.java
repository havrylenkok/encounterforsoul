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
public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;
    protected PointLight light;
    private float time = (float) (Math.random() * 2);

    public Item(PlayScreen screen, float x, float y, RayHandler rHandler){
        this.screen = screen;
        this.world = screen.getWorld();
        toDestroy = false;
        destroyed = false;
        setPosition(x, y);
        setBounds(getX(), getY(), 40 / Const.PPM, 40 / Const.PPM);
        defineItem();
        light = new PointLight(rHandler, 50, Color.WHITE, 0.5f, 0,0);
		light.setSoftnessLength(1f);//radius 
		light.attachToBody(body);
    }

    public abstract void defineItem();
    public abstract void use(Shaman shaman);

    public void update(float dt){
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            light.remove();
            Shaman.defoltdistLight +=0.5f;
            Shaman.savedSouls +=1;
            Shaman.distLight = Shaman.defoltdistLight;
            destroyed = true;
        }
        time += dt;
        light.setDistance((float) (Math.sin(2*time)));
    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy(){
        toDestroy = true;
       
    }
    public void reverseVelocity(boolean x, boolean y){
    }
}
