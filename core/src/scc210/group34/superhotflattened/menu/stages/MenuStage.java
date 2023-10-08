package scc210.group34.superhotflattened.menu.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import scc210.group34.superhotflattened.menu.actors.ImageActor;
import scc210.group34.superhotflattened.menu.actors.MusicActor;
import scc210.group34.superhotflattened.menu.GameManager;
import scc210.group34.superhotflattened.menu.utils.R;

public class MenuStage extends IStage{
	private ImageButton start;
	private ImageButton setting;
	private ImageButton end;
	private ImageButton move;

	private ImageActor background;
	private MusicActor musicActor;
	private Music music;

	public MenuStage(GameManager gameManager, Viewport view) {
		super(gameManager, view);
		init();
	}

	public void init() {
		//background image
		TextureRegion back = new TextureRegion(getGameManager().getAsset().get(R.menuscreen.IMAGE_IMAGE_BACKGROUND, Texture.class));
		background = new ImageActor(back);
		background.setCenter(getWidth()/2, getHeight()/2);
		this.addActor(background);

		//play button
		start = new ImageButton(new TextureRegionDrawable(
				new TextureRegion(getGameManager().getAsset().get(R.menuscreen.IMAGE_START, Texture.class))));
		start.setX(getWidth()*38/100);
		start.setY(getHeight()*40/100);
		start.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				getGameManager().getGameScreen().showMain();
			}
		});
		this.addActor(start);

		//setting button
		setting = new ImageButton(new TextureRegionDrawable(
				new TextureRegion(getGameManager().getAsset().get(R.menuscreen.IMAGE_SETTING, Texture.class))));
		setting.setX(getWidth()*33/100);
		setting.setY(getHeight()*25/100);
		setting.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				getGameManager().getGameScreen().showPreference();
			}
		});
		this.addActor(setting);

		//setting button
		end = new ImageButton(new TextureRegionDrawable(
				new TextureRegion(getGameManager().getAsset().get(R.menuscreen.IMAGE_END, Texture.class))));
		end.setX(getWidth()*40/100);
		end.setY(getHeight()*12/100);
		end.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Gdx.app.exit();
			}
		});
		this.addActor(end);



		TextureRegion up = new TextureRegion(getGameManager().getAsset().get(R.menuscreen.IMAGE_MORE_UP,Texture.class));
		TextureRegion down = new TextureRegion(getGameManager().getAsset().get(R.menuscreen.IMAGE_MORE_DOWN,Texture.class));
		move = new ImageButton(new TextureRegionDrawable(up),
				new TextureRegionDrawable(new TextureRegion(down)));
		float width = getWidth()*0.3f;
		//System.out.println(width);
		move.setSize(150, 150);
		move.setX(0);
		move.setY((getHeight())-(move.getHeight()+getGameManager().getBlackside()/3));
		this.addActor(move);

		music =getGameManager().getAsset().get(R.Music.song1, Music.class);
		musicActor=new MusicActor(music);

	}

}
