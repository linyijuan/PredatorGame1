package sutd.istd.groupzero.gameobjects;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import sutd.istd.groupzero.helpers.AssetLoader;

public class Monster {
    private int mapSizeX = 540;  // Not too sure of this value yet
    private int mapSizeY = 960; // Same as for X
    private ArrayList<Tree> treeList;
    private ArrayList<Food> foodList;
    private ArrayList<PowerUps> powerUpsList;
    private Direction direction;
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    private Rectangle bound;
    private float boundWidth;
    private float boundHeight;
    public Rectangle getBound(){return bound;}
    public Direction getDirection() {
        return direction;
    }
    private float screenWidth, screenHeight;
    private Vector2 myPosition;
    private Vector2 original;
    private Vector2 directionVectorToTarget = new Vector2();
    private Vector2 target;
    private float angle;
    private float arrowPostX;
    private float arrowPostY;
    private int strength = 0;
    private int visibility = 1;//max=5

    // radius of the arrow's orbit around the player
    private float radius = 35f;

    public Vector2 getMyPosition(){return  myPosition;}

    public void setMyPosition(Vector2 myPosition){
        this.myPosition = myPosition;
        this.bound = new Rectangle(this.myPosition.x,this.myPosition.y,boundWidth,boundHeight);

        if (myPosition.x < 0){this.myPosition.x = 0;}
        if (myPosition.x > mapSizeX - boundWidth){this.myPosition.x = mapSizeX - boundWidth;}
        if (myPosition.y < 0){this.myPosition.y = 0;}
        if (myPosition.y > mapSizeY - boundHeight){this.myPosition.y = mapSizeY- boundHeight;}
    }

    public enum Direction{
        TOP, RIGHT, LEFT, BOTTOM, STATIONARY_TOP, STATIONARY_LEFT,STATIONARY_RIGHT,STATIONARY_BOTTOM;
        public int getKeycode() {
            if(this.equals(TOP))
            {
                return 1;
            }
            else if(this.equals(LEFT))
            {
                return 0;
            }
            else if(this.equals(RIGHT))
            {
                return 2;
            }
            else if(this.equals(BOTTOM))
            {
                return 3;
            }
            else if(this.equals(STATIONARY_TOP))
            {
                return 5;
            }
            else if(this.equals(STATIONARY_RIGHT))
            {
                return 6;
            }
            else if(this.equals(STATIONARY_LEFT))
            {
                return 8;
            }
            else if(this.equals(STATIONARY_BOTTOM))
            {
                return 7;
            }
            return 10;
        }
    }

    // Can help see if this is still needed?
    public Monster(/*ArrayList<Food> foodList,ArrayList<PowerUps> powerUpsList,ArrayList<Tree> treeList, */Direction direction1, float screenWidth, float screenHeight)
    {
        this.direction = direction1;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.myPosition = new Vector2(0,0);
        this.original = new Vector2(0,0);
        this.boundWidth = AssetLoader.monsterUp.getRegionWidth();
        this.boundHeight = AssetLoader.monsterUp.getRegionHeight();
        this.bound = new Rectangle(myPosition.x,myPosition.y,boundWidth,boundHeight);
        // This is not needed right?

//        this.foodList = foodList;
//        this.treeList = treeList;
//        this.powerUpsList = powerUpsList;

        // Arbitary target for now
        target = new Vector2(270, 480);
    }



    public void update(float delta)
    {
        directionVectorToTarget = directionVectorToTarget.set(target.x - myPosition.x, target.y - myPosition.y);
        angle = directionVectorToTarget.angle() - 180;
        arrowPostX = myPosition.x + (radius * MathUtils.cos(directionVectorToTarget.angleRad()));
        arrowPostY = myPosition.y + (radius * MathUtils.sin(directionVectorToTarget.angleRad()));

    }

    public float getBoundWidth() {
        return boundWidth;
    }

    public float getBoundHeight() {
        return boundHeight;
    }

    public Vector2 getDirectionVectorToTarget() {
        return directionVectorToTarget;
    }

    public float getAngle() {
        return angle;
    }

    public float getArrowPostY() {
        return arrowPostY;
    }

    public float getArrowPostX() {
        return arrowPostX;
    }

    public void setStrength(int s){
        strength = s;
    }
    public void obtainFood(){
        strength++;
    }
    public int getStrength(){
        return strength;
    }

    public int getVisibility(){
        return visibility;
    }
    public void setVisibility(int v){
        if (v<=5)
            visibility = v;
    }
    public void obtainVisibility(){
        if (visibility <5)
            visibility++;
    }
}
