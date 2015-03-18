package sutd.istd.groupzero.gameobjects;


import sutd.istd.groupzero.gameobjects.Monster.Direction;
import sutd.istd.groupzero.helpers.AssetLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

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
                return 7;
            }
            else if(this.equals(STATIONARY_BOTTOM))
            {
                return 8;
            }
            return 10;
        }
    }


    public Monster(ArrayList<Food> foodList,ArrayList<PowerUps> powerUpsList,ArrayList<Tree> treeList, Direction direction1, float screenWidth, float screenHeight)
    {
        this.direction = direction1;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.myPosition = new Vector2(0,0);
        this.original = new Vector2(0,0);
        this.boundWidth = AssetLoader.monsterUp.getRegionWidth();
        this.boundHeight = AssetLoader.monsterUp.getRegionHeight();
        this.bound = new Rectangle(myPosition.x,myPosition.y,boundWidth,boundHeight);
        this.foodList = foodList;
        this.treeList = treeList;
        this.powerUpsList = powerUpsList;
    }

    public boolean collide(PowerUps p){

        return  false;
    }

    public boolean collide(Food f){
        return  false;
    }

    public boolean collide(ArrayList<Tree> treeList){
        boolean collision = false;
        for (Tree t:treeList){
            if (Intersector.overlaps(bound,t.getBound())){
                collision = true;
            }
        }
        return collision;
    }


    public void update(float delta)
    {

    }


}
