package scc210.group34.superhotflattened.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import scc210.group34.superhotflattened.entities.Entity;
import scc210.group34.superhotflattened.entities.EntityType;
import scc210.group34.superhotflattened.entities.MeleeEnemy;
import scc210.group34.superhotflattened.entities.Player;
import scc210.group34.superhotflattened.menu.GameManager;

import java.util.ArrayList;

/**
 * Base class that sets up the structure for holding the game map.
 */
public class GameMap
{
    // holds all the entities in play.
    private Array<Entity> entities;

    // Used for keeping the camera centered.
    private Player player;            // reference to the player.
    private Rectangle dynamicCamera;    // area in which camera will move after the player.
    private Rectangle staticCamera;     // area in which the camera remains still while the player can move freely.

    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;

    // objects used by the game map.
    private TiledMap tiledMap;                            // the map itself.
    private OrthogonalTiledMapRenderer tiledMapRenderer;  // render used for the map.
    private int[] renderLayers;
    private int collisionLayer;
    private int entityLayer;
    private GameManager manager;

    private Array<WorldNode> pathMap;

    /**
     * Sets up the game map. At the moment simply loads the pc.
     */
    public GameMap(String levelName, OrthographicCamera camera, SpriteBatch spriteBatch, GameManager gameManager)
    {
        this.camera = camera;
        this.spriteBatch = spriteBatch;
        this.manager=gameManager;

        loadMap(levelName);     // loads in the map tiles and everything
        loadEntities();         // loads the entities out of the map and spawns them.
        generatePathMap();

        generateCameraZones();  // generates the camera zones used to track the player.
    }

    /**
     * Loads the map passed.
     *
     * @param levelName : The name of the level being loaded.
     */
    private void loadMap(String levelName){
        // loads the map.
        tiledMap = new TmxMapLoader().load(levelName + ".tmx");    // loads the map.
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);        // instantiates the renderer.

        // grabs the layers related to specific tasks.
        renderLayers = new int[] {tiledMap.getLayers().getIndex("background"), tiledMap.getLayers().getIndex("main")};
        collisionLayer = tiledMap.getLayers().getIndex("main");
        entityLayer = tiledMap.getLayers().getIndex("entities");
    }

    private void generatePathMap(){
        pathMap = new Array<>();
        pathMap.setSize(getWidth() * getHeight());
        for(int y = 0; y < getHeight(); y++){
            for(int x = 0; x < getWidth(); x++){
                pathMap.set(y * getWidth() + x, new WorldNode(new Vector3(x, y, 1)));
            }
        }

        for(int i = 0; i < (1000 / entities.size) + 1; i++){
            for (int j = 1; j < entities.size; j++){
                findPath(entities.get(j), entities.get(j - 1));
            }
        }
    }

    /**
     * Loads the entities from the map.
     */
    private void loadEntities(){
        entities = new Array<Entity>();     // instantiates entity array.

        // looks for the entities.
        for (int y = 0; y < getHeight(); y++)
        {
            for (int x = 0; x < getWidth(); x++)
            {
                EntityType entityType = getEntityTypeByCoordinate(x, y, getEntityLayer());
                if (entityType != null)
                // found an entity.
                {
                    // spawns the entity at the position it was found.
                    if(entityType.getId() == EntityType.PLAYER.getId()){
                        player = new Player(x * TileType.TILE_SIZE, y * TileType.TILE_SIZE, this);
                        entities.add(player);       // adds the player to the list of entities.
                    }
                    // spawns the entity at the position it was found.
                    if(entityType.getId() == EntityType.ENEMY_MELEE.getId() || entityType.getId() == EntityType.ENEMY_DRAGON.getId()){
                        entities.add(new MeleeEnemy(x * TileType.TILE_SIZE, y * TileType.TILE_SIZE, this));       // adds the player to the list of entities.
                    }
                }
            }
        }
    }

    // generates the different camera zones used to keep the player centered.
    private void generateCameraZones()
    {
        // Uses the golden ration to calculate the zones in which the camera works.
        float a = Gdx.graphics.getWidth() / 5; //1.618f;
        float b = Gdx.graphics.getWidth() - a; //1.618f;
        float width = a;
        float widthOffset = b / 2;

        a = Gdx.graphics.getHeight() / 5;//1.618f;
        b = Gdx.graphics.getHeight() - a;//1.618f;
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
    }

    /**
     * Renders the game map and all entities on the map.
     */
    public void render(float delta)
    {
        tiledMapRenderer.setView(camera);                       // sets the renderer's view / window.
        tiledMapRenderer.render(renderLayers);                  // carries out the actual render.

        spriteBatch.setProjectionMatrix((camera.combined));     // passes the camera view to the batch.
        spriteBatch.begin();                                    // starts up the batch.
        for (Entity entity : entities)
        {
            entity.render(spriteBatch, delta);                  // calls render on each entity.
        }
        spriteBatch.end();                                      // actually starts the rendering.
        
        //ShapeRenderer shapeBatch;
        //
        //shapeBatch = new ShapeRenderer();
        //
        //Gdx.gl.glEnable(GL30.GL_BLEND);
        //Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        //shapeBatch.begin(ShapeRenderer.ShapeType.Filled);
        //shapeBatch.setColor(new Color(1,0,0,0.2f));
        //shapeBatch.rect(dynamicCamera.getX(), dynamicCamera.getY(), dynamicCamera.getWidth(), dynamicCamera.getHeight());
        //shapeBatch.setColor(new Color(0,0,1,0.2f));
        //shapeBatch.rect(staticCamera.getX(), staticCamera.getY(), staticCamera.getWidth(), staticCamera.getHeight());
        //shapeBatch.end();
        //Gdx.gl.glDisable(GL30.GL_BLEND);
    }

    /**
     * Used to update entities in the game map.
     * Basically physics and stuff. Does gravity, player collision and player movement.
     *
     * @param delta  : The amount of time passed.
     */
    public void update(float delta)
    {
        for (Entity entity : entities)
        {
            entity.update(delta, -9.8f); // carries out physics updates on each entity.
        }

        trackEntity(delta, player);
    }

    // Track's the entity with the camera to keep it in the center of the screen.
    public void trackEntity(float delta, Entity entity)
    {
        // carries out some projections, so it knows where things are on the screen.
        Vector3 centerPoint = camera.project(       // entity position(centered).
                new Vector3(entity.getX() + entity.getWidth() / 2f, entity.getY() + entity.getHeight() / 2f, 1));
        Vector3 bottomCorner = camera.project(      // bottom corner of the world.
                new Vector3(0, 0, 0));

        // checks to see if the entity is on the very left edge of the map.
        if (bottomCorner.x < 0)
        // entity is not on the very left edge.
        {
            // checks to see if the entity is outside the static camera zone.
            if (centerPoint.x < staticCamera.getX())
            {
                // re-centers the camera on the entity.
                camera.translate(((centerPoint.x - staticCamera.getX()) / (staticCamera.getX() - dynamicCamera.getX())) * entity.getSpeed() * delta, 0);
            }
        }
        // checks to see if the entity is on the very right edge of the map.
        if (bottomCorner.x + getPixelWidth() - camera.viewportWidth > 0)
        // entity is not on the very right edge of the map.
        {
            // Checks to see if the entity is outside the static camera zone.
            if (centerPoint.x > staticCamera.getX() + staticCamera.getWidth())
            {
                // updates the camera to keep the entity centered.
                camera.translate(((centerPoint.x - staticCamera.getX() - staticCamera.getWidth()) / (staticCamera.getX() - dynamicCamera.getX())) * entity.getSpeed() * delta, 0);
            }
        }

        // does the same thing for top and bottom.
        if (bottomCorner.y < 0)
        {
            if (centerPoint.y < staticCamera.getY())
            {
                camera.translate(0, ((centerPoint.y - staticCamera.getY()) / (staticCamera.getY() - dynamicCamera.getY())) * entity.getSpeed() * delta);
            }
        }
        if (bottomCorner.y + getPixelHeight() - camera.viewportHeight > 0)
        {
            if (centerPoint.y > staticCamera.getY() + staticCamera.getHeight())
            {
                camera.translate(0, ((centerPoint.y - staticCamera.getY() - staticCamera.getHeight()) / (staticCamera.getY() - dynamicCamera.getY())) * entity.getSpeed() * delta);
            }
        }
    }

    public void levelComplete(){
        System.out.println("I WIN!");
    }
    public void gameOver(){
        System.out.println("you're a failure");
    }

    /**
     * Disposes of all the major things placed in memory.
     */
    public void dispose()
    {
        for (Entity entity : entities)
        {
            entities.removeValue(entity, true);
            entity.dispose();
        }

        tiledMap.dispose();
        tiledMapRenderer.dispose();
    }

    /**
     * Returns the type of the tile at pixel location (x, y) on the specified layer.
     * Converts the pixel location to grid / tile location then passes onto relevant function.
     *
     * @param x     :The x pixel of the tile.
     * @param y     :The y pixel of the tile.
     * @param layer :The layer the tile is on.
     * @return The type of tile at the pixel position (x, y).
     */
    public TileType getTileTypeByLocation(float x, float y, int layer)
    {
        int col = (int) (x / TileType.TILE_SIZE);
        int row = (int) (y / TileType.TILE_SIZE);
        
        //loads the cell at the position passed.
        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) tiledMap.getLayers().get(layer)).getCell(col, row);

        //checks to see if the cell exists.
        if (cell != null)
        {

            // gets the tile from the cell.
            TiledMapTile tile = cell.getTile();

            //checks to see if there is anything in the cell.
            if (tile != null)
            {
                // gets the tile type.
                int id = tile.getId();                  // gets the id of the tile.
                return TileType.getTileTypeByID(id);    // gets the tile type with that id.
            }
        }

        // no tile found.
        return null;
    }

    /**
     * Returns the type of the tile at coordinate position (col, row) on the specified layer.
     *
     * @param col   :The col the tile is in.
     * @param row   :The row the tile is in.
     * @param layer :The layer the tile is on.
     * @return The type of tile at the coordinate position.
     */
    public TileType getTileTypeByCoordinate(int col, int row, int layer){
        //loads the cell at the position passed.
        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) tiledMap.getLayers().get(layer)).getCell(col, row);

        //checks to see if the cell exists.
        if (cell != null)
        {

            // gets the tile from the cell.
            TiledMapTile tile = cell.getTile();

            //checks to see if there is anything in the cell.
            if (tile != null)
            {
                // gets the tile type.
                int id = tile.getId();                  // gets the id of the tile.
                return TileType.getTileTypeByID(id);    // gets the tile type with that id.
            }
        }

        // no tile found.
        return null;
    }

    // returns the entity type at the tile passed.
    private EntityType getEntityTypeByCoordinate(int x, int y, int layer)
    {
        //loads the cell at the position passed.
        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) tiledMap.getLayers().get(layer)).getCell(x, y);


        //checks to see if the cell exists.
        if (cell != null)
        {
            // gets the tile from the cell.
            TiledMapTile tile = cell.getTile();

            //checks to see if there is anything in the cell.
            if (tile != null)
            {
                // gets the tile type.
                int id = tile.getId();                  // gets the id of the tile.
                return EntityType.getEntityTypeByID(id);    // gets the entity type with that id.
            }
        }

        // no tile found.
        return null;
    }

    /**
     * Determines whether the rectangle passed collides with the game map.
     *
     * @param x      : The x position of the rect.
     * @param y      : The y position of the rect.
     * @param width  : The width of the rect.
     * @param height : The height of the rect.
     * @return True if there is a collision, false if not.
     */
    public boolean doesRectangleCollideWithGameMap(float x, float y, float width, float height)
    {
        // checks for collision with map borders.
        if (x < 0 || y < 0 || x + width > getPixelWidth() || y + height > getPixelHeight())
        {
            return true;
        }

        // checks for collisions with tiles. Looks through all the tiles the rect sits on top of.
        for (int row = (int) (y / TileType.TILE_SIZE); row < Math.ceil((y + height) / TileType.TILE_SIZE); row++)
        {
            for (int col = (int) (x / TileType.TILE_SIZE); col < Math.ceil((x + width) / TileType.TILE_SIZE); col++)
            {
                // gets the current tile.
                TileType tile = getTileTypeByCoordinate(col, row, getCollisionLayer());

                // checks there is something there. And whether it is collidable.
                if (tile != null && tile.isCollidable())
                {
                    return true;
                }
            }
        }

        // no collision found.
        return false;
    }

    /**
     * Uses the slab method to detect collisions between a box and ray.
     *
     * @param box_x  : x pos of the box.
     * @param box_y  : y pos of the box.
     * @param box_w  : width of the box.
     * @param box_h  : height of the box.
     * @param origin : start of the ray.
     * @param delta  : direction of the ray.
     * @return false if no collision is detected and true if there is one.
     */
    public boolean doesRectangleCollideWithRay(float box_x, float box_y, float box_w, float box_h, Vector3 origin, Vector3 delta)
    {
        float box_xMax = box_x + box_w;
        float box_yMax = box_y + box_h;

        float tNear = Float.NEGATIVE_INFINITY;
        float tFar = Float.POSITIVE_INFINITY;

        float t1, t2, temp;


        if (delta.x == 0)
        {
            // vertical line.

            if (origin.x < box_x || origin.x > box_xMax)
            {
                return false;
            }
        } else
        {
            t1 = (box_x - origin.x) / delta.x;
            t2 = (box_xMax - origin.x) / delta.x;

            if (t1 > t2)
            {
                temp = t2;
                t2 = t1;
                t1 = temp;
            }

            if (t1 > tNear)
            {
                tNear = t1;
            }
            if (t2 < tFar)
            {
                tFar = t2;
            }

            if (tFar < 0 || tNear > tFar)
            {
                return false;
            }
        }

        if (delta.y == 0)
        {
            // horizontal line.

            if (origin.y < box_y || origin.y > box_yMax)
            {
                return false;
            }
        } else
        {
            t1 = (box_y - origin.y) / delta.y;
            t2 = (box_yMax - origin.y) / delta.y;

            if (t1 > t2)
            {
                temp = t2;
                t2 = t1;
                t1 = temp;
            }

            if (t1 > tNear)
            {
                tNear = t1;
            }
            if (t2 < tFar)
            {
                tFar = t2;
            }

            if (tFar < 0 || tNear > tFar)
            {
                return false;
            }
        }

        return true;
    }
    /**
     * Uses the slab method to detect collisions between a box and ray.
     *
     * @param box_x  : x pos of the box.
     * @param box_y  : y pos of the box.
     * @param box_w  : width of the box.
     * @param box_h  : height of the box.
     * @param origin : start of the ray.
     * @param delta  : direction of the ray.
     * @return false if no collision is detected and true if there is one.
     */
    public boolean doesRectangleCollideWithHorizontalLine(float box_x, float box_y, float box_w, float box_h, Vector3 origin, Vector3 delta)
    {
        float box_xMax = box_x + box_w;
        float box_yMax = box_y + box_y;

        float tNear = Float.NEGATIVE_INFINITY;
        float tFar = Float.POSITIVE_INFINITY;

        float t1, t2, temp;


        if (delta.x == 0)
        {
            // vertical line.

            if (origin.x < box_x || origin.x > box_xMax)
            {
                return false;
            }
        } else
        {
            t1 = (box_x - origin.x) / delta.x;
            t2 = (box_xMax - origin.x) / delta.x;

            if (t1 > t2)
            {
                temp = t2;
                t2 = t1;
                t1 = temp;
            }

            if (t1 > tNear)
            {
                tNear = t1;
            }
            if (t2 < tFar)
            {
                tFar = t2;
            }

            if (tFar < 0 || tNear > tFar)
            {
                return false;
            }
        }

        if (origin.y < box_y || origin.y > box_yMax)
        {
            return false;
        }

        if(tNear > 1){
            return false;
        }

        return true;
    }

    public void EntityTileInteractions(Entity entity)
    {
        float x = entity.getX(), y = entity.getY();
        float width = entity.getWidth(), height = entity.getHeight();

        // checks for collisions with tiles. Looks through all the tiles the rect sits on top of.
        for (int row = (int) (y / TileType.TILE_SIZE); row < Math.ceil((y + height) / TileType.TILE_SIZE); row++)
        {
            for (int col = (int) (x / TileType.TILE_SIZE); col < Math.ceil((x + width) / TileType.TILE_SIZE); col++)
            {
                // gets the current tile.
                TileType tile = getTileTypeByCoordinate(col, row, getCollisionLayer());

                if(tile != null){
                    entity.touchingTile(tile);
                }
            }
        }
    }

    public boolean canEntitySeeEntity(Entity e1, Entity e2){
        TileType tile;
        int sum;
        int count;
        Vector3 point = new Vector3(e1.getX() + (e1.getWidth() / 2), e1.getY() + (e1.getHeight() / 2), 0); //set working point to initial point
        Vector3 delta = new Vector3(e2.getX() + (e2.getWidth() / 2), e2.getY() + (e2.getHeight() / 2), 0); //set delta to end point.
        delta.sub(point); // work out delta.

        if(delta.len() > 1000){
            return false;
        }

        // rough guesstimate of number of squares passed through.
        sum = ((int)(Math.abs(Math.ceil(delta.x)) + Math.abs(Math.ceil(delta.y)))) / TileType.TILE_SIZE;
        sum *= 2; // doubled for bit more accuracy.


        delta.scl((float)1/sum); //scales delta down relative to sum.

        count = 0;
        do{
            tile = getTileTypeByLocation(point.x, point.y, getCollisionLayer());
            if(tile != null && tile.isCollidable()){
                return false;
            }
            point.add(delta);
            count++;
        } while(count <= sum);
        return true;
    }

    public Array<Vector3> findPath(Entity a, Entity b){
        resetPathMap();
        int xDest = (int)(b.getX() / TileType.TILE_SIZE); int yDest = (int)(b.getY() / TileType.TILE_SIZE);
        int xCurrent = (int)(a.getX() / TileType.TILE_SIZE); int yCurrent = (int)(a.getY() / TileType.TILE_SIZE);
        int currentNode = xCurrent + yCurrent * getWidth();
        pathMap.get((int)(currentNode)).setDistance(0);

        int currentDistance = 0;
        int oldDistance = 0;

        do{
            currentNode = xCurrent + yCurrent * getWidth();
            currentDistance = pathMap.get(currentNode).getDistance() + 1;

            if(xCurrent != 0 && getTileTypeByCoordinate(xCurrent - 1, yCurrent - 1, collisionLayer) != null && getTileTypeByCoordinate(xCurrent - 1, yCurrent - 1, collisionLayer).isCollidable()){
                oldDistance = pathMap.get((xCurrent - 1) + yCurrent * getWidth()).getDistance();
                if(currentDistance < oldDistance){
                    pathMap.get((xCurrent - 1) + yCurrent * getWidth()).setDistance(currentDistance)
                            .setPrevX(xCurrent)
                            .setPrevY(yCurrent);
                }
            }
            if(xCurrent + 1 != getWidth() && getTileTypeByCoordinate(xCurrent + 1, yCurrent - 1, collisionLayer) != null && getTileTypeByCoordinate(xCurrent + 1, yCurrent - 1, collisionLayer).isCollidable()){
                oldDistance = pathMap.get((xCurrent + 1) + yCurrent * getWidth()).getDistance();
                if(currentDistance < oldDistance){
                    pathMap.get((xCurrent + 1) + yCurrent * getWidth()).setDistance(currentDistance)
                            .setPrevX(xCurrent)
                            .setPrevY(yCurrent);
                }
            }
            if(yCurrent != 0 && getTileTypeByCoordinate(xCurrent, yCurrent - 2, collisionLayer) != null && getTileTypeByCoordinate(xCurrent, yCurrent - 2, collisionLayer).isCollidable()){
                oldDistance = pathMap.get(xCurrent + (yCurrent - 1) * getWidth()).getDistance();
                if(currentDistance < oldDistance){
                    pathMap.get(xCurrent + (yCurrent - 1) * getWidth()).setDistance(currentDistance)
                            .setPrevX(xCurrent)
                            .setPrevY(yCurrent);
                }
            }
            if(yCurrent + 1 != getHeight() && getTileTypeByCoordinate(xCurrent, yCurrent, collisionLayer) != null && getTileTypeByCoordinate(xCurrent, yCurrent, collisionLayer).isCollidable()){
                oldDistance = pathMap.get(xCurrent + (yCurrent + 1) * getWidth()).getDistance();
                if(currentDistance < oldDistance){
                    pathMap.get(xCurrent + (yCurrent + 1) * getWidth()).setDistance(currentDistance)
                            .setPrevX(xCurrent)
                            .setPrevY(yCurrent);
                }
            }
            pathMap.get(currentNode).setVisited(true);

            currentDistance = WorldNode.MAX_DISTANCE;
            for(int y = 0; y < getHeight(); y++){
                for(int x = 0; x < getWidth(); x++){
                    currentNode = x + y * getWidth();
                    if(!pathMap.get(currentNode).isVisited()){
                        oldDistance = pathMap.get(currentNode).getDistance() + (int)((Math.pow(x - xDest, 2) + Math.pow(y - yDest, 2)) * 0.15);

                        if(oldDistance < currentDistance){
                            currentDistance = oldDistance;
                            xCurrent = x;
                            yCurrent = y;
                        }
                    }
                }
            }
        }while(currentDistance != WorldNode.MAX_DISTANCE && (xCurrent != xDest || yCurrent != yDest));

        //Need to extract path.
        Array<Vector3> path = new Array<>();
        if(currentDistance != WorldNode.MAX_DISTANCE){
            path.add(new Vector3(xCurrent, yCurrent, 1));
            currentNode = xCurrent + yCurrent * getWidth();

            do{
                xCurrent = pathMap.get(currentNode).getPrevX();
                yCurrent = pathMap.get(currentNode).getPrevY();
                currentNode = xCurrent + yCurrent * getWidth();

                path.add(new Vector3(xCurrent, yCurrent, 1));
            } while(pathMap.get(currentNode).getDistance() != 0);

            //path.reverse();
        }

        return path;
    }

    public void resetPathMap(){
        for(int i = 0; i < pathMap.size; i++){
            pathMap.get(i).setDistance(WorldNode.MAX_DISTANCE);
            pathMap.get(i).setVisited(false);
        }
    }

    public void entityMeleeAttacks(Entity attacker)
    {
        Vector3 entityPos = new Vector3(attacker.getX() + attacker.getWidth() / 2, attacker.getY() + attacker.getHeight() / 2, 0);
        Vector3 attack = new Vector3(attacker.getMeleeRange() * attacker.getDirection(), 0, 0);

        for (int victim = 0; victim < entities.size; victim++)
        {
            if (entities.get(victim) != attacker && doesRectangleCollideWithHorizontalLine(entities.get(victim).getX(), entities.get(victim).getY(), entities.get(victim).getWidth(), entities.get(victim).getHeight(), entityPos, attack))
            {
                entities.get(victim).adjustHealth(-attacker.getMeleeDamage());

                //===================
                // could put some sort of knock back here.
                // if I bother to make them last more than one hit.
                //===================

            }
        }
    }

    public void enemyDied(Entity enemy)
    {
        entities.removeValue(enemy, true);
        enemy.dispose();
    }
    
    /**
     * Gets the width of the map in pixels.
     *
     * @return : The map width in pixels.
     */
    public int getPixelWidth()
    {
        return getWidth() * TileType.TILE_SIZE;
    }

    /**
     * Gets the pixel height of the game map.
     *
     * @return : The height of map in pixels.
     */
    public int getPixelHeight()
    {
        return getWidth() * TileType.TILE_SIZE;
    }

    /**
     * Gets the width of the game map.
     *
     * @return : The width of the game map.
     */
    public int getWidth()
    {
        return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth();
    }

    /**
     * Gets the height of the game map.
     *
     * @return : The height of the game map.
     */
    public int getHeight()
    {
        return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight();
    }

    /**
     * Gets the number of layers in the game map.
     *
     * @return : The number of layers in the game map.
     */
    public int getLayers()
    {
        return tiledMap.getLayers().getCount();
    }

    /**
     * Returns the layers that are being rendered.
     *
     * @return : An array containing the indexes of layers to be rendered.
     */
    public int[] getRenderLayers(){
        return renderLayers;
    }

    /**
     * Returns the layer being collided with.
     * @return : Returns the index of the layer that contains all collidable objects.
     */
    public int getCollisionLayer(){
        return collisionLayer;
    }

    /**
     * Returns the layer with entities on it.
     * @return : Returns the index of the layer that contains the initial positions of entities.
     */
    public int getEntityLayer(){
        return entityLayer;
    }
    
    public Entity getPlayer() {return player;}

    public OrthographicCamera getCamera(){return camera;}
    public void setCamera(OrthographicCamera camera) {this.camera = camera;}
    public SpriteBatch getSpriteBatch(){return spriteBatch;}
    public void setSpriteBatch(SpriteBatch spriteBatch) { this.spriteBatch = spriteBatch;}
}
