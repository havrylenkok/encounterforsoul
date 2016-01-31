/*******************************************************************************
 * Copyright DEDFOX
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

	public static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();

	private AssetManager assetManagerGame;

	public AssetFonts fonts;
	// game 
	public AssetAnimations animation;
	//public AssetGameElements gameElements;
	// main menu, settings and levels
	public AssetGameElements gameElements;
	// audio
	public AssetSounds sounds;
	public AssetMusic music;

	// singleton: prevent instantiation from other classes
	private Assets () {
	}

	public class AssetFonts {
		public final BitmapFont gameTime;
		public final BitmapFont wndTitle;
		public final BitmapFont freezers;
		public final BitmapFont costFreezers;
		public final BitmapFont levelNumber;
		public final BitmapFont timeScore;

		public AssetFonts () {
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/sys.ttf"));
			FreeTypeFontParameter parameter = new FreeTypeFontParameter();
			parameter.size = 35;
			parameter.characters = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
			parameter.borderColor = Color.valueOf("ff4c86"); 
			parameter.borderWidth = 2f;
			wndTitle = generator.generateFont(parameter);

			parameter.size = 50;
			parameter.borderColor = Color.valueOf("eeb369"); 
			timeScore = generator.generateFont(parameter);

			parameter.size = 80;
			parameter.borderWidth = 6f;
			parameter.borderColor = Color.valueOf("e35e5c"); 
			levelNumber = generator.generateFont(parameter);

			parameter.size = 50;
			parameter.borderWidth = 2f;
			parameter.borderColor = Color.valueOf("3986bf"); 
			//			parameter.shadowColor = Color.BLACK;
			//			parameter.shadowOffsetX = 2;
			//			parameter.shadowOffsetY = 2;
			freezers = generator.generateFont(parameter);
			gameTime = generator.generateFont(parameter);
			freezers.setColor(Color.BLACK);

			parameter.size = 25;
			parameter.borderWidth = 2f;
			parameter.borderColor = Color.valueOf("846039"); 
			costFreezers = generator.generateFont(parameter);
			costFreezers.setColor(Color.WHITE);

			//					font.drawMultiLine(batch, "your text", x, y, widthOfTheLine, HAlignment.CENTER);
			gameTime.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			wndTitle.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			freezers.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			costFreezers.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			levelNumber.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			timeScore.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			generator.dispose();
		}
	}

	public class AssetAnimations {
		public final Animation moveRight;
		public final Animation demonRigh;
		public final Animation demonLeft;

		public AssetAnimations (TextureAtlas atlas) {
			Array<AtlasRegion> regions = null;

			regions = atlas.findRegions("move");
			moveRight = new Animation(1.0f / 14.0f, regions, Animation.PlayMode.NORMAL); //10 кадров
			regions = atlas.findRegions("memon");
			demonRigh = new Animation(1.0f / 7.0f, regions, Animation.PlayMode.NORMAL);
			regions = atlas.findRegions("ldemon");
			demonLeft = new Animation(1.0f / 7.0f, regions, Animation.PlayMode.NORMAL);
		}
	}

	public class AssetGameElements {

		public final AtlasRegion shaman_jump;
		public final AtlasRegion shaman_stend;
		public final AtlasRegion soul;

		public AssetGameElements (TextureAtlas atlas) {

			shaman_jump = atlas.findRegion("jump");
			shaman_stend = atlas.findRegion("stand");
			soul = atlas.findRegion("soul");

		}
	}

	public class AssetSounds {
		public  Sound jump;
		public  Sound walk;
		public  Sound booben;
		public  Sound dead;
		private AssetManager am;

		public AssetSounds (AssetManager am) {
			this.am = am;
		}

		private void getGameSounds (){
			jump = am.get("mp3/jump.wav", Sound.class);
			walk = am.get("mp3/walk.wav", Sound.class);
			booben = am.get("mp3/booben.wav", Sound.class);
			dead = am.get("mp3/l_booben.wav", Sound.class);

			//			jump = am.get("mp3/jump.mp3", Sound.class);
			//			wing = am.get("mp3/wing.mp3", Sound.class);
			//			fall = am.get("mp3/fall.mp3", Sound.class);
			//			rocket = am.get("mp3/rocket.mp3", Sound.class);
			//			rocketLong = am.get("mp3/rocketLong.mp3", Sound.class);
			//			swipe = am.get("mp3/swipe.mp3", Sound.class);
			//			click =am.get("mp3/click.mp3", Sound.class);
			//			bonus = am.get("mp3/bonus.mp3", Sound.class);
		}
	}

	public class AssetMusic {
		public final Music song;

		public AssetMusic (AssetManager am) {
			song = am.get("mp3/musik.mp3", Music.class);
		}
	}


	public void initGame (AssetManager assetManager) {
		this.assetManagerGame = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		// load texture atlas
		assetManager.load(Const.TEXTURE_ATLAS_GAME, TextureAtlas.class);
		// load sounds
		assetManager.load("mp3/jump.wav", Sound.class);
		assetManager.load("mp3/walk.wav", Sound.class);
		assetManager.load("mp3/l_booben.wav", Sound.class);
		assetManager.load("mp3/booben.wav", Sound.class);
		assetManager.load("mp3/musik.mp3", Music.class);

		//		assetManager.load("mp3/fall.mp3", Sound.class);
		//		assetManager.load("mp3/rocket.mp3", Sound.class);
		//		assetManager.load("mp3/rocketLong.mp3", Sound.class);
		//		assetManager.load("mp3/bonus.mp3", Sound.class);
		assetManager.finishLoading();

		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		TextureAtlas atlas = assetManager.get(Const.TEXTURE_ATLAS_GAME);

		// enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		// create game resource objects
		fonts = new AssetFonts();
		animation = new AssetAnimations(atlas);
		gameElements = new AssetGameElements(atlas);
		sounds = new AssetSounds(assetManager);
		sounds.getGameSounds();
		music = new AssetMusic(assetManager);
	}

	public void dispose () {
		if (assetManagerGame != null)
			assetManagerGame.dispose();

		fonts.gameTime.dispose();
		fonts.wndTitle.dispose();
		fonts.freezers.dispose();
	}

	public boolean assetsUpdate () {
		if (assetManagerGame != null)
			return assetManagerGame.update();
		return false;
	}

	public void error (String filename, Class type, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception)throwable);
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {	}

}

