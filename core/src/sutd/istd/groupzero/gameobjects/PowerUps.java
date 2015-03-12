package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import sutd.istd.groupzero.helpers.AssetLoader;

/**
 * Created by win on 2/17/15.
 */

// Need to pseudo random power up here

public class PowerUps {
    private Vector2 position;
    private float boundWidth;
    private float boundHeight;
    private String name;
    private Rectangle bound;
    public PowerUps(Vector2 _position)
    {
        this.position = _position;
        this.boundWidth = AssetLoader.powerUp.getRegionWidth();
        this.boundHeight = AssetLoader.powerUp.getRegionHeight();
        this.bound = new Rectangle(_position.x,_position.y,boundWidth,boundHeight);
    }
    public Rectangle getBound(){return bound;}
    public Vector2 getPosition(){
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
        this.bound = new Rectangle(position.x,position.y,boundWidth,boundHeight);
    }

    public String getName(){
        return name;
    }
}
