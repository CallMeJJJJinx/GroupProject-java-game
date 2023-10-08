package scc210.group34.superhotflattened.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import scc210.group34.superhotflattened.menu.actors.MusicActor;
import scc210.group34.superhotflattened.menu.actors.SoundActor;
import scc210.group34.superhotflattened.menu.utils.R;

public class GameManager extends Game{

	private AssetManager asset;

	private float wordWidth;
	private float wordHeight;
	private float blackside;

	private MusicActor musicActor;
	private SoundActor soundActor;

	private GameScreen gameScreen;
	private fps Fps;

	public void create() {
		// load resources
		asset = new AssetManager();

		//load picture for menu screen
		asset.load(R.menuscreen.IMAGE_IMAGE_BACKGROUND,Texture.class);
		asset.load(R.menuscreen.IMAGE_START,Texture.class);
		asset.load(R.menuscreen.IMAGE_SETTING,Texture.class);
		asset.load(R.menuscreen.IMAGE_END,Texture.class);
		asset.load(R.menuscreen.IMAGE_MORE_DOWN,Texture.class);
		asset.load(R.menuscreen.IMAGE_MORE_UP,Texture.class);

        //music
		asset.load(R.Music.song1, Music.class);
		asset.load(R.Music.song2, Music.class);
		asset.load(R.Music.song3, Music.class);

        //fps font
		asset.load(R.Font.FONT, BitmapFont.class);

		asset.finishLoading();

		//set word width for viewport
		Texture t = asset.get(R.menuscreen.IMAGE_IMAGE_BACKGROUND,Texture.class);
		wordWidth = t.getWidth();
		wordHeight = wordWidth*Gdx.graphics.getHeight()/Gdx.graphics.getWidth();
		wordHeight = t.getHeight();
		blackside = (wordHeight - t.getHeight()) /2.0f;



		Fps=new fps(asset);
		//music
		Music music = getAsset().get(R.Music.song1, Music.class);
		musicActor=new MusicActor(music);

		//sound
		soundActor=new SoundActor();

		//make game stage
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		gameScreen.dispose();
		if(asset !=null) {
			asset.dispose();
		}
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		Fps.render();
	}

	public MusicActor getMusicActor(){
		return musicActor;
	}
	public SoundActor getSoundActor(){
		return soundActor;
	}
	public boolean getFPS() {
		return Fps.getFps();
	}
	public void setFPS(boolean bool) {
		Fps.setFps(bool);
	}
	public AssetManager getAsset() {
		return asset;
	}
	public float getWordWidth() {
		return wordWidth;
	}
	public float getWordHeight() {
		return wordHeight;
	}
	public GameScreen getGameScreen() {
		return gameScreen;
	}
	public float getBlackside() {return blackside;}
}
