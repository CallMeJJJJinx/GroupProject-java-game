package scc210.group34.superhotflattened.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import scc210.group34.superhotflattened.world.GameMap;
import scc210.group34.superhotflattened.world.TileType;

/**
 * Player instance of an Entity.
 */
public class Player extends Entity
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
    public Player(float x, float y, GameMap gameMap)
    {
        // calls the super constructor passing in the player type.
        super(x, y, EntityType.PLAYER, gameMap);

        // sets the texture to the players type.
        texture = new Texture("player.png");

        deltaSum = 0;
        frame = 0;
        frameCount = texture.getWidth() / getWidth();
        moving = false;
    }


    /**
     * Carries out player actions and calls the super class function to implement gravity.
     *
     * @param deltaTime : The time passed since the last update.
     * @param gravity : The strength of gravity.
     */
    @Override
    public void update(float deltaTime, float gravity)
    {
        // calls super class function.
        super.update(deltaTime, gravity);

        // checks to see if space key is being pressed.
        if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP))
        {
            // checks to see if player is touching the ground.
            if (grounded)
            // player is touching the ground.
            {
                // applies jump.
                velocity.y += getJumpForce();
            }
            // checks if player is still moving up.
            else if (velocity.y > 0)
            // player is not grounded and moving up.
            {
                // applies portion of the jump force.
                velocity.y += getJumpForce() * deltaTime;
            }
        }

        moving = false;
        // looks for movement input.
        if (Gdx.input.isKeyPressed(Keys.A)) // Left
        {
            moveX(-getSpeed()  * deltaTime);
            direction = Direction.LEFT;
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Keys.D)) // Right
        {
            moveX(getSpeed() * deltaTime);
            direction = Direction.RIGHT;
            moving = true;
        }

        if(Gdx.input.isKeyPressed(Keys.LEFT)){
            direction = Direction.LEFT;
            gameMap.entityMeleeAttacks(this);
        }
        if(Gdx.input.isKeyPressed(Keys.RIGHT)){
            direction = Direction.RIGHT;
            gameMap.entityMeleeAttacks(this);
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            gameMap.entityMeleeAttacks(this);
        }
    }

    @Override
    public void touchingTile(TileType tile){
        super.touchingTile(tile);
        String name = tile.getName();

        if(name == "flag_base" || name == "flag_pole" || name == "flag_flag"){
            gameMap.levelComplete();
        }
    }

    /**
     * Renders the current entity.
     *
     * @param batch : The sprite batch that's going to be drawing all entities.
     */
    @Override
    public void render(SpriteBatch batch, float delta)
    {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight(), getWidth() * frame,0,getWidth(), getHeight(), getDirection() == Direction.LEFT.getDirection() ? true : false, false);
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
    public void onDeath(){
        // needs to bring up the game over screen. An option to restart and ect.

        System.out.println("DEAD");
        gameMap.gameOver();
    }
}
