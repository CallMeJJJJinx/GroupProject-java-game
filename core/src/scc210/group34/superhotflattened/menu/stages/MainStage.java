package scc210.group34.superhotflattened.menu.stages;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import scc210.group34.superhotflattened.menu.GameManager;
import scc210.group34.superhotflattened.menu.utils.R;
import scc210.group34.superhotflattened.world.GameMap;

public class MainStage extends IStage{

	// holds the current game map.
	private GameMap gameMap;

	public void disport() {
	}

	public MainStage(GameManager menu, Viewport view) {
		super(menu, view, new SpriteBatch());
		init();
		disport();
	}

	public void init() {
		OrthographicCamera camera = new OrthographicCamera();  // instantiates the camera.
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());    // tells it to draw things the right way around and the size of the window.
		camera.update();    // actually applies the changes you've given it.
		getViewport().setCamera(camera);


		// loads a map.
		gameMap = new GameMap("testMap4", (OrthographicCamera) getViewport().getCamera(), (SpriteBatch) getBatch(),super.getGameManager());
	}

	public void addActors() {
		R.gamespeed = 1f;
	}

	@Override
	public void dispose(){
		super.dispose();
		gameMap.dispose();
	}

	public void render(float delta){
		//clears the screen to black.
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);


		// gameplay loop.
		gameMap.update(delta);    // apply gravity, move pc and npc AI.
		getViewport().getCamera().update();                                // update camera to new view window.
		gameMap.render(delta);            // carries out the actual render.
	}

}
