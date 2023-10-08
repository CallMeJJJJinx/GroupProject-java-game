package scc210.group34.superhotflattened;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import scc210.group34.superhotflattened.world.*;

/**
 * The base level of the application. Pretty much tells everything else what to do.
 */
public class SuperHotFlattened extends ApplicationAdapter
{
    // batch renderer. Mostly used to render entities.
    private SpriteBatch spriteBatch;
    // the camera used to control game view.
    private OrthographicCamera camera;
    // holds the current game map.
    private GameMap gameMap;



    /**
     * Initial setup and build.
     */
    @Override
    public void create()
    {

        /*
        ====================================
        Once you've got some levels and gameplay down need to figure out where you want the camera placed and how its going to scale.
        ==================================== */
        // instantiates the spriteBatch.
        spriteBatch = new SpriteBatch();
        // generates the camera.
        camera = new OrthographicCamera();  // instantiates the camera.
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());    // tells it to draw things the right way around and the size of the window.
        camera.update();    // actually applies the changes you've given it.


    }

    /**
     * Controls all the graphics and rendering of things.
     */
    @Override
    public void render()
    {
        //clears the screen to black.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();

        // gameplay loop.
        gameMap.update(delta);    // apply gravity, move pc and npc AI.
        camera.update();                                // update camera to new view window.
        gameMap.render(delta);            // carries out the actual render.
    }

    /**
     * disposes things loaded into memory before the program finishes.
     */
    @Override
    public void dispose()
    {
        gameMap.dispose();
        spriteBatch.dispose();
    }

    // used for testing purposes to draw on camera zones.
    private void renderZoneRectangles(){
        Rectangle dynamicCamera;
        Rectangle staticCamera;

        float a = Gdx.graphics.getWidth() / 1.618f;
        float b = a / 1.618f;
        float width = a;
        float widthOffset = b / 2;

        a = Gdx.graphics.getHeight() / 1.618f;
        b = a / 1.618f;
        float height = a;
        float heightOffset = b / 2;

        dynamicCamera = new Rectangle(widthOffset, heightOffset, width, height);

        a = width / 1.618f;
        b = a / 1.618f;
        width = a;
        widthOffset = (b / 2) + widthOffset;

        a = height / 1.618f;
        b = a / 1.618f;
        height = a;
        heightOffset = (b / 2) + heightOffset;

        staticCamera = new Rectangle(widthOffset, heightOffset, width, height);

        ShapeRenderer shapeBatch;

        shapeBatch = new ShapeRenderer();

        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        shapeBatch.begin(ShapeRenderer.ShapeType.Filled);
        shapeBatch.setColor(new Color(1,0,0,0.2f));
        shapeBatch.rect(dynamicCamera.getX(), dynamicCamera.getY(), dynamicCamera.getWidth(), dynamicCamera.getHeight());
        shapeBatch.setColor(new Color(0,0,1,0.2f));
        shapeBatch.rect(staticCamera.getX(), staticCamera.getY(), staticCamera.getWidth(), staticCamera.getHeight());
        shapeBatch.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);
    }
}
