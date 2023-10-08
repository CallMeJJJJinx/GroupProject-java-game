package scc210.group34.superhotflattened.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import scc210.group34.superhotflattened.world.GameMap;
import scc210.group34.superhotflattened.world.TileType;

/**
 * Abstract class for implementing entities.
 */
public abstract class Entity
{
    // Entity properties.
    protected Vector3 position;
    protected Vector3 velocity;
    protected EntityType type;
    protected GameMap gameMap;
    protected boolean grounded;
    protected int health;
    protected int meleeDamage;
    protected int meleeRange;

    protected Direction direction;
    public enum Direction{
        LEFT(-1), RIGHT(1);

        private int direction;

        private Direction(int direction){
            this.direction = direction;
        }

        public int getDirection()
        {
            return direction;
        }
    }

    /**
     * Entity Constructor. Builds a new entity.
     *
     * @param x         : The x position of the entity.
     * @param y         : The y position of the entity.
     * @param type      : The type of the entity.
     * @param gameMap   : The game map the entity is placed on.
     */
    public Entity(float x, float y, EntityType type, GameMap gameMap)
    {
        this.position = new Vector3(x, y, 1);         // creates a vector to store the entity position.
        this.velocity = new Vector3(0, 0, 1);  // creates a vector to store the entity velocity.
        this.type = type;
        this.health = type.getHealth();
        this.meleeDamage = type.getMeleeDamage();
        this.meleeRange = type.getMeleeRange();
        this.gameMap = gameMap;
        this.grounded = false;

        direction = Direction.LEFT;
    }

    /**
     * Updates the entity. Carries out gravity.
     *
     * @param deltaTime : Time passed since last update.
     * @param gravity   : Strength of gravity.
     */
    public void update(float deltaTime, float gravity)
    {
        // the y position to be after advancing.
        float newY = position.y;

        // works out the new y position.
        velocity.y += gravity * deltaTime * getMass();  // applies gravity to the entity.
        newY += velocity.y * deltaTime;                 // advances the entity due to gravity.

        // checks to see if the entity is colliding with the map.
        if (gameMap.doesRectangleCollideWithGameMap(position.x, newY, getWidth(), getHeight()))
        {
            // checks to see if the entity is travelling down.
            if (velocity.y < 0)
            {
                // grounds the player.
                position.y = (float) Math.floor(position.y);
                grounded = true;
            }

            // Arrests movement. Either it's collided with the top or the bottom of the map.
            // But either way can't continue in the direction its travelling.
            velocity.y = 0;
        } else
        {
            // No collision so updates the player position.
            position.y = newY;
            grounded = false;
        }

        gameMap.EntityTileInteractions(this);

        if(getHealth() <= 0){
            onDeath();
        }
    }

    public void touchingTile(TileType tile){
        int damage = tile.getDamage();

        if(damage > 0){
            adjustHealth(-damage);
            velocity.y += getJumpForce() * damage / 20;
        }
    }

    protected void attackMelee(){

    }
    protected void attackRanged(){

    }

    /**
     * Used to render the entity.
     *
     * @param batch : the batch renderer.
     */
    public abstract void render(SpriteBatch batch, float delta);

    /**
     * disposes of the entities textures.
     */
    public abstract void dispose();

    public abstract void onDeath();

    /**
     * moves the entity left or right.
     *
     * @param amount : amount to move by.
     */
    protected void moveX(float amount)
    {
        // x position after moving.
        float newX = position.x + amount;

        // checks for collision with the game map.
        if (!gameMap.doesRectangleCollideWithGameMap(newX, position.y, getWidth(), getHeight()))
        {
            // no collision so advances entity.
            position.x = newX;
        }
    }

    /**
     * Adjusts the entity's health by the amount passed.
     *
     * @param amount : the amount by which to adjust their health.
     */
    public void adjustHealth(int amount){
        health += amount;
    }

    /**
     * gets the position of the entity.
     *
     * @return : The position of the entity.
     */
    public Vector3 getPosition()
    {
        return position;
    }

    /**
     * gets the x position of the entity.
     *
     * @return : The x position of the entity.
     */
    public float getX()
    {
        return position.x;
    }

    /**
     * gets the y position of the entity.
     * @return : the y position of the entity.
     */
    public float getY()
    {
        return position.y;
    }

    /**
     * Gets the velocity of the entity.
     *
     * @return : the velocity of the entity.
     */
    public Vector3 getVelocity()
    {
        return velocity;
    }

    /**
     * Gets the type of entity.
     *
     * @return : The entity type.
     */
    public EntityType getType()
    {
        return type;
    }

    /**
     * Gets the width of the entity.
     *
     * @return : The width of the entity.
     */
    public int getWidth()
    {
        return type.getWidth();
    }

    /**
     * Gets the height of the entity.
     *
     * @return : The height of the entity.
     */
    public int getHeight()
    {
        return type.getHeight();
    }

    /**
     * Gets the mass of the entity.
     *
     * @return : The entities mass.
     */
    public float getMass()
    {
        return type.getMass();
    }

    /**
     * Gets the speed of the entity.
     * @return : Tbe entity's speed.
     */
    public int getSpeed() { return type.getSpeed();}

    /**
     * Gets the jump force of the entity.
     * @return : The entity's jump force.
     */
    public int getJumpForce(){return type.getJumpForce();}

    public int getHealth() {return health;}

    public int getMeleeDamage() { return meleeDamage;}
    public int getMeleeRange() {return meleeRange;}

    public int getDirection() { return direction.getDirection();}

    /**
     * Finds out whether the entity is touching the ground.
     *
     * @return : is the entity touching the ground.
     */
    public boolean isGrounded()
    {
        return grounded;
    }
}
