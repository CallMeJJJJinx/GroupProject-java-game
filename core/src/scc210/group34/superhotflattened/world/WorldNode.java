package scc210.group34.superhotflattened.world;

import com.badlogic.gdx.math.Vector3;
import org.graalvm.compiler.phases.util.BlockWorkList;

public class WorldNode
{
    public static final int MAX_DISTANCE = 10000000;

    private Vector3 position;
    private boolean visited;
    private int distance;
    private int prevX, prevY;

    public WorldNode(Vector3 position)
    {
        this.position = position;

        visited = false;
        distance = MAX_DISTANCE;
        prevX = 0; prevY = 0;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public WorldNode setVisited(boolean visited)
    {
        this.visited = visited;
        return this;
    }

    public int getDistance()
    {
        return distance;
    }

    public WorldNode setDistance(int distance)
    {
        this.distance = distance;
        return this;
    }

    public int getPrevX()
    {
        return prevX;
    }

    public WorldNode setPrevX(int prevX)
    {
        this.prevX = prevX;
        return this;
    }

    public int getPrevY()
    {
        return prevY;
    }

    public WorldNode setPrevY(int prevY)
    {
        this.prevY = prevY;
        return this;
    }
}
