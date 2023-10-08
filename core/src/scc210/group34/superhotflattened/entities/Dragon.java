package scc210.group34.superhotflattened.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import scc210.group34.superhotflattened.world.GameMap;
import scc210.group34.superhotflattened.world.TileType;

/**
 * Player instance of an Entity.
 */
public class Dragon extends Entity
{

    // player texture.
    private Texture texture;
    private boolean moving;
    private float deltaSum;
    private int frame;
    private int frameCount;


    /**
     * Constructor. Builds a player entity at position (x, y) on gameMap.
     *
     * @param x       : x position of the player.
     * @param y       : y position of the player.
     * @param gameMap : game map the player is spawned on.
     */
    public Dragon(float x, float y, GameMap gameMap)
    {
        // calls the super constructor passing in the player type.
        super(x, y, EntityType.ENEMY_DRAGON, gameMap);

        // sets the texture to the players type.
        texture = new Texture("e1.png");

        deltaSum = 0;
        frame = 0;
        frameCount = 3;
        moving = false;
    }

    /**
     * Carries out player actions and calls the super class function to implement gravity.
     * @param deltaTime : The time passed since the last update.
     * @param gravity : The strength of gravity.
     */
    @Override
    public void update(float deltaTime, float gravity) {
        // calls super class function.
        super.update(deltaTime, gravity);
        moving = true;
    }

    /**
     * Renders the current entity.
     *
     * @param batch : The sprite batch that's going to be drawing all entities.
     */
    @Override
    public void render(SpriteBatch batch, float delta)
    {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight(), getWidth() ,getHeight()*(1/3*frame),getWidth(), getHeight(), getDirection() == Direction.LEFT.getDirection() ? true : false, false);
        deltaSum += delta;
        if(moving){
            if(deltaSum > 0.25){
                frame++;
                if(frame >= frameCount){
                    frame = 0;
                }
            }
        }else{
            frame = 0;
        }
    }

    /**
     * Disposes of the texture when finished with.
     */
    @Override
    public void dispose()
    {
        texture.dispose();
    }

    @Override
    public void onDeath() {

    }
}
