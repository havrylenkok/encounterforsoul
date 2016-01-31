package utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import Sprites.Enemies.Demon;
import Sprites.Enemies.Enemy;
import Sprites.TileObjects.Border;
import Sprites.TileObjects.Souls;
import box2dLight.RayHandler;
import screens.PlayScreen;

/**
 * Created by DEDFOX
 */
public class B2WorldCreator {
    private Array<Demon> demons;
    private Array<Souls> souls;
    private Array<Border> bords;
    RayHandler rHandler;

    public B2WorldCreator(PlayScreen screen, RayHandler rHandler){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        //create body and fixture variables
        this.rHandler = rHandler;
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create ground bodies/fixtures
        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Const.PPM, (rect.getY() + rect.getHeight() / 2) / Const.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Const.PPM, rect.getHeight() / 2 / Const.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //create runas bodies/fixtures
        souls = new Array<Souls>();
        for(MapObject object : map.getLayers().get("soul").getObjects().getByType(RectangleMapObject.class)){
        	 Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	 souls.add(new Souls(screen, rect.getX() / Const.PPM, rect.getY() / Const.PPM, rHandler));
        }

      //create enemy borders 
        bords = new Array<Border>();
        for(MapObject object : map.getLayers().get("bord").getObjects().getByType(RectangleMapObject.class)){
        	 Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	 bords.add(new Border(screen, rect.getX() / Const.PPM, rect.getY() / Const.PPM, rHandler));
        }
        
        //create all Demons
        demons = new Array<Demon>();
        for(MapObject object : map.getLayers().get("demon").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            demons.add(new Demon(screen, rect.getX() / Const.PPM, rect.getY() / Const.PPM));
        }
        
        //create souls bodies/fixtures
//        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
//
//            new Soul(screen, object);
//        }
    }
    
    

    public Array<Demon> getDemons() {
        return demons;
    }
    public Array<Souls> getSouls() {
        return souls;
    }
    
    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(demons);
        return enemies;
    }



	
}
