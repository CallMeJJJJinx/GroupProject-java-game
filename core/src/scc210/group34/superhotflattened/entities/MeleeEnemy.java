package scc210.group34.superhotflattened.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import scc210.group34.superhotflattened.world.GameMap;
import scc210.group34.superhotflattened.world.TileType;

public class MeleeEnemy extends Entity
{
    private Texture texture;
    private Array<Vector3> path;
    private float pathUpdate;

    public MeleeEnemy(float x, float y, GameMap gameMap){
        super(x, y, EntityType.ENEMY_MELEE, gameMap);

        texture = new Texture("enemy.png");
        path = new Array<>();
        pathUpdate = 2;
    }

    @Override
    public void update(float deltaTime, float gravity){
        super.update(deltaTime, gravity);

        pathUpdate += deltaTime;

        //need to check sight lines.
        if(pathUpdate > 0.5 && gameMap.canEntitySeeEntity(this, gameMap.getPlayer())){
            // update path.
            pathUpdate = 0;
            path = gameMap.findPath(this, gameMap.getPlayer());
        }

        // need to find path to player.
            // AI can move to any tile with ground underneath it. Can also jump.
        if(0 < path.size)
        {
            if (path.peek().x < getX() / TileType.TILE_SIZE)
            {
                moveX(-getSpeed() * deltaTime);
                direction = Direction.LEFT;
            } else
            {
                moveX(getSpeed() * deltaTime);
                direction = Direction.RIGHT;
            }
            if (path.peek().x == (int) (getX() / TileType.TILE_SIZE))
            {
                path.pop();
            }
            if(path.size == 0){
                pathUpdate = 2;

                //attack.
                gameMap.entityMeleeAttacks(this);
            }
        }

    }

    @Override
    public void onDeath(){
        gameMap.enemyDied(this);
    }

    @Override
    public void render(SpriteBatch batch, float delta){
        // adds enemy entity to the batch group.
        batch.draw(texture, position.x, position.y, getWidth(), getHeight());
    }

    @Override
    public void dispose(){
        texture.dispose();
    }
}
