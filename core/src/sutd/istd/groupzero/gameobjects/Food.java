package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import sutd.istd.groupzero.helpers.AssetLoader;

public class Food {
    private Rectangle bound;
    private float boundWidth;
    private float boundHeight;
    private boolean showing;
    public boolean shouldShow(){return showing;}
    public void setShouldShow(boolean s){this.showing = s;}
    private Vector2 position;
    public Vector2 getPosition(){
        return position;
    }
    public  Rectangle getBound(){return bound;}
    public void setPosition(Vector2 position) {
        this.position = position;
        this.bound = new Rectangle(position.x,position.y,boundWidth,boundHeight);
    }
    public Food(Vector2 _position)
    {
        this.position = _position;
        this.boundWidth = AssetLoader.steak.getRegionWidth();
        this.boundHeight = AssetLoader.steak.getRegionHeight();
        this.bound = new Rectangle(_position.x,_position.y,boundWidth,boundHeight);
        this.showing = true;
    }
}
