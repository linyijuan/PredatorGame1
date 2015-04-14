package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/* Super class for all the items on map*/
public class Item {

    private Vector2 position;
    private float boundWidth;
    private float boundHeight;
    private Rectangle bound;
    private Circle walkingBound;

    public Item(Vector2 _position){
        this.position = _position;
    }
    // Getting the reference of the rectangle bound for the item (Tree, Food, PowerUp)
    public Rectangle getBound(){return bound;}

    // Getting the reference of the circular bound for walking collision detection
    public Circle getWalkingBound(){
        return walkingBound;
    }

    public Vector2 getPosition(){
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        this.bound = new Rectangle(position.x,position.y,boundWidth,boundHeight);
    }

    // Setting the bound to prevent item generation overlap and to detect item collision
    public void setBound(float boundWidth, float boundHeight){
        this.boundWidth = boundWidth;
        this.boundHeight = boundHeight;
        this.bound = new Rectangle(position.x, position.y,boundWidth,boundHeight);
    }

    // Setting the bound to prevent player overlap with Tree
    // Circle is used to relax the harshness of the collision
    public void setWalkingBound(float treeWidth, float treeHeight, float radius){
        this.walkingBound = new Circle(position.x + (treeWidth/2), position.y + ((treeHeight-10)/2), radius);
    }
}
