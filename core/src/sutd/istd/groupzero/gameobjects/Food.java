package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by win on 2/17/15.
 */
public class Food {

    private Vector2 position;
    public Vector2 getPosition(){
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public Food(Vector2 _position)
    {
        this.position = _position;
    }
}
