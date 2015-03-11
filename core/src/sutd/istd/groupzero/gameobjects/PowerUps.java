package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by win on 2/17/15.
 */

// Need to pseudo random power up here

public class PowerUps {
    private Vector2 position;
    private String name;

    public PowerUps(Vector2 _position)
    {
        this.position = _position;
    }

    public Vector2 getPosition(){
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public String getName(){
        return name;
    }
}
