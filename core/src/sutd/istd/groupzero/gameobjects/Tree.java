package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import sutd.istd.groupzero.helpers.AssetLoader;


public class Tree{

    private Vector2 position;
    private Rectangle bound;
    private float boundWidth;
    private float boundHeight;
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        this.bound = new Rectangle(position.x,position.y,boundWidth,boundHeight);
    }
    public Rectangle getBound(){return bound;}
    public Tree(Vector2 _position) {
        this.position = _position;
        this.boundWidth = AssetLoader.tree.getRegionWidth();
        this.boundHeight = AssetLoader.tree.getRegionHeight();
        this.bound = new Rectangle(_position.x,_position.y,boundWidth,boundHeight);
    }


}
