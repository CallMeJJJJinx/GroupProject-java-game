package scc210.group34.superhotflattened.menu.stages;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import scc210.group34.superhotflattened.menu.GameManager;

public class IStage extends Stage{
	private boolean isView;
	private GameManager gameManager;
	public IStage(GameManager gameManager, Viewport view) {
		super(view);
		this.gameManager = gameManager;
	}
	public IStage(GameManager gameManager, Viewport view, SpriteBatch spriteBatch) {
		super(view, spriteBatch);
		this.gameManager = gameManager;
	}

	public boolean isView() {
		return isView;
	}

	public void setView(boolean isView) {
		this.isView = isView;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setMain(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
