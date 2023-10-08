package scc210.group34.superhotflattened.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import scc210.group34.superhotflattened.menu.stages.*;

public class GameScreen extends ScreenAdapter{
	private GameManager gameManager;

	private MenuStage menu;
	private MainStage main;
	private OverStage over;
	private PreferenceStage pref;


	public GameScreen(GameManager gameManager) {
		this.gameManager = gameManager;
		init();
	}

	public void init() {
		menu = new MenuStage(gameManager, new StretchViewport(gameManager.getWordWidth(), gameManager.getWordHeight()));
		menu.setView(true);
		main = new MainStage(gameManager, new StretchViewport(gameManager.getWordWidth(), gameManager.getWordHeight()));
		main.setView(false);
		
		over = new OverStage(gameManager, new StretchViewport(gameManager.getWordWidth(), gameManager.getWordHeight()));
		over.setView(false);


		Gdx.input.setInputProcessor(menu);
		showMenu();
	}
	
	@Override
	public void render(float delta) {

		super.render(delta);

		if(menu.isView()) {
			menu.act(delta);
			menu.draw();
		}
		
		if(main != null && main.isView()) {
			main.act(delta);
			main.draw();
			main.render(delta);
		}
	
		if(over.isView()) {
			over.act(delta);
			over.draw();
		}

		if(pref != null &&pref.isView()) {
			pref.act(delta);
			pref.draw();
		}
	}

	
	@Override
	public void dispose() {
		super.dispose();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(menu != null) {
			menu.dispose();
		}
		if(main != null) {
			main.dispose();
		}
		
		if(over != null) {
			over.dispose();
		}
		if(pref != null) {
			pref.dispose();
		}
	}

	public void showMain() {
		menu.setView(false);
		if(main == null){
			main = new MainStage(gameManager, new StretchViewport(gameManager.getWordWidth(), gameManager.getWordHeight()));
		}

		main.disport();
		main.addActors();
		main.setView(true);
		Gdx.input.setInputProcessor(main);
	}

	public void showMenu() {
		menu.setView(true);
		Gdx.input.setInputProcessor(menu);
	}

	public void showOver() {
		over.setView(true);
		Gdx.input.setInputProcessor(over);
	}
	public void showPreference() {
		pref = new PreferenceStage(gameManager, new StretchViewport(gameManager.getWordWidth(), gameManager.getWordHeight()));
		pref.setView(true);
		Gdx.input.setInputProcessor(pref);
	}
}
