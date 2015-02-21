package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by win on 2/17/15.
 */
public class Obstacles {
    private Vector2 position;

    public Obstacles(Vector2 _position)
    {
        this.position = _position;
    }

    public Vector2 getPosition(){
        return position;
    }
}
