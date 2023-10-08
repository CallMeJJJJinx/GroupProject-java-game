package scc210.group34.superhotflattened.world;

import java.util.HashMap;

/**
 * Used to keep track of tile types and their properties.
 */
public enum TileType {
    // defines all the tiles and their properties.
    BLANK(1, false, "blank"),
    BLOCK(2, true,  "block"),
    SPIKE(3, false,  "spike", 10),
    FLAG_BASE(4, false, "flag_base"),
    FLAG_POLE(5, false, "flag_pole"),
    FLAG_FLAG(6,false, "flag_flag");

    /**
     * The size of all tiles.
     */
    public static final int TILE_SIZE = 32;

    // variables that hold each tiles properties.
    private int id;
    private boolean collidable;
    private String name;
    private int damage;

    /*
        Constructor for non-damaging TileTypes.
        Takes the id of the tile whether it is collidable and its name.
     */
    private TileType(int id, boolean collidable, String name) {
        this(id, collidable, name, 0);
    }
    // The same as the above except for tiles which can inflict damage.
    private TileType(int id, boolean collidable, String name, int damage){
        this.id = id;
        this.collidable = collidable;
        this.name = name;
        this.damage = damage;
    }

    /**
     * Getter.
     * @return The id of the current tile.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter.
     * @return Whether the current tile can be collided with.
     */
    public boolean isCollidable() {
        return collidable;
    }

    /**
     * Getter.
     * @return The name of the current tile.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter.
     * @return The damage inflicted by the current tile.
     */
    public int getDamage() {
        return damage;
    }

    // hash map used to relate each TileType Enum to its id.
    private static HashMap<Integer, TileType> tileMap;
    static { // static constructor for the hash map.
        // inits tile Map.
        tileMap = new HashMap<Integer, TileType>();
        //adds all the tiles to the hash map.
        for (TileType tileType : TileType.values()) {
            tileMap.put(tileType.getId(), tileType);
        }
    }

    /**
     * Returns the tile type associated with the given id.
     * @param id :The id of the tile type desired.
     * @return The tile type associated with the given id.
     */
    public static TileType getTileTypeByID(int id){
        return tileMap.get(id);
    }
};
