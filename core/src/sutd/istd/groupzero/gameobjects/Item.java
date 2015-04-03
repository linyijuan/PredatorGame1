package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Item {
    private Vector2 position;
    private float boundWidth;
    private float boundHeight;
    private boolean showing;
    private Rectangle bound;
    private Circle walkingBound;

    public Item(Vector2 _position){
        this.position = _position;
        this.showing = true;
    }
    public void setBound(float boundWidth, float boundHeight){
       this.boundWidth = boundWidth;
       this.boundHeight = boundHeight;
       this.bound = new Rectangle(position.x, position.y,boundWidth,boundHeight);
    }

    public void setWalkingBound(float treeWidth, float treeHeight, float radius){
        // Hard coded values are experimented values
        this.walkingBound = new Circle(position.x + (treeWidth/2), position.y + ((treeHeight-10)/2), radius);
    }
    public boolean shouldShow(){return showing;}
    public void setShouldShow(boolean s){this.showing = s;}

    public Vector2 getPosition(){
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
        this.bound = new Rectangle(position.x,position.y,boundWidth,boundHeight);
    }

    public Rectangle getBound(){return bound;}

    public Circle getWalkingBound(){
        return walkingBound;
    }

}
