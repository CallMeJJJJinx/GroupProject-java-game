package scc210.group34.superhotflattened.entities;


import java.util.HashMap;

/**
 * Enum defining different entity types.
 */
public enum EntityType
{
    PLAYER(7,"player", 57, 90, 300, 500, 1000, 10, 10, 120),
    ENEMY_MELEE(8,"melee_enemy", 40, 50, 300, 300, 900, 10, 10, 50),
    ENEMY_DRAGON(9,"dragon", 320, 360, 0, 300, 0, 100, 10, 50);

    // entity properties.
    private final int id;
    private final String type;
    private final int width;
    private final int height;
    private final float mass;
    private final int speed;
    private final int jumpForce;
    private final int health;
    private final int meleeDamage;
    private final int meleeRange;

    // constructor.
    private EntityType(int id, String type, int width, int height, float mass, int speed, int jumpForce, int health, int meleeDamage, int meleeRange){
        this.id = id;
        this.type = type;
        this.width = width;
        this.height = height;
        this.mass = mass;
        this.speed = speed;
        this.jumpForce = jumpForce;
        this.health = health;
        this.meleeDamage = meleeDamage;
        this.meleeRange = meleeRange;
    }

    /**
     * Gets the id of the entity
     * @return : THe id of the entity.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Gets the entity type
     * @return : THe type of the entity.
     */
    public String getType()
    {
        return type;
    }

    /**
     * Gets the width of the entity.
     * @return : The width of the entity.
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Gets the height of the entity.
     * @return : The height of the entity.
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Gets the mass of the entity.
     * @return : The entity's mass.
     */
    public float getMass()
    {
        return mass;
    }

    /**
     * Gets the speed of the entity.
     * @return : The entity's speed.
     */
    public int getSpeed() { return speed;}

    /**
     * Gets the jump force of the entity.
     *
     * @return : The entity's jump force.
     */
    public int getJumpForce() { return jumpForce;}

    /**
     * Gets the default health of an entity.
     *
     * @return : The default health for an entity.
     */
    public int getHealth() { return health;}

    /**
     *  Gets the default melee attack damage of an entity.
     *
     * @return : The default melee damage for an entity.
     */
    public int getMeleeDamage(){ return meleeDamage;}

    /**
     *  Gets the default melee attack range of an entity.
     *
     * @return : The default melee range for an entity.
     */
    public int getMeleeRange(){ return meleeRange;}

    // hash map used to relate each EntityType Enum to its id.
    private static HashMap<Integer, EntityType> entityMap;
    static { // static constructor for the hash map.
        // inits entity Map.
        entityMap = new HashMap<Integer, EntityType>();
        //adds all the entities to the hash map.
        for (EntityType entityType : EntityType.values()) {
            entityMap.put(entityType.getId(), entityType);
        }
    }

    /**
     * Returns the entity type associated with the given id.
     *
     * @param id :The id of the entity type desired.
     * @return The entity type associated with the given id.
     */
    public static EntityType getEntityTypeByID(int id){
        return entityMap.get(id);
    }
}
