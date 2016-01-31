package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;

public class Const {
	public static final float VIEWPORT_WIDTH = Gdx.graphics.getWidth();
	public static final float VIEWPORT_HEIGHT = Gdx.graphics.getHeight();

	public static final float PPM = 100;
	public static final int GAME_HEIGHT = 768;
	public static final int GAME_WIDTH = 1024;//(int) (GAME_HEIGHT*Gdx.graphics.getWidth())/Gdx.graphics.getHeight();
	public static final float GAME_SCALE = GAME_WIDTH/1024;

	public static final String TEXTURE_ATLAS_GAME = "textures/game.atlas";

	//Box2D Collision Bits
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short SHAMAN_BIT = 2;
	public static final short SOUL_BIT = 4;
	public static final short OBJECT_BIT = 8;
	public static final short ENEMY_BIT = 16;
	public static final short ENEMY_BOOM_BIT = 32;
	public static final short FIREBALL_BIT = 128;
	public static final short SHAMAN_BODY_BIT = 256;

	public static final Vector2 BT_SIZE = new Vector2(50f, 43f);
	public static final Music musik = Gdx.audio.newMusic(Gdx.files.internal("mp3/musik.mp3"));;
}
