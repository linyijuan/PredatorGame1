package sutd.istd.groupzero.gameobjects;


import sutd.istd.groupzero.gameobjects.Monster.Direction;
import sutd.istd.groupzero.helpers.AssetLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Monster {
    private int speed;
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

    private Vector2 mapPosition;
    private Vector2 myPosition;
    public Vector2 getMapPosition() {
        return mapPosition;
    }
    public Vector2 getMyPosition(){return  myPosition;}

    public void setMapPosition(Vector2 mapPosition) {
        this.mapPosition = mapPosition;
    }
    public void setMyPosition(Vector2 myPosition){
        this.bound = new Rectangle(myPosition.x,myPosition.y,boundWidth,boundHeight);
        this.myPosition = myPosition;
    }

    @Override
    public String toString() {
        return "this monster is at" + this.mapPosition.x + ", " + this. mapPosition.y;
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
                return 6;
            }
            else if(this.equals(STATIONARY_LEFT))
            {
                return 5;
            }
            else if(this.equals(STATIONARY_RIGHT))
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


    public Monster(int speed,  Vector2 currentMapPosition, Direction direction1, float screenWidth, float screenHeight)
    {
        this.direction = direction1;
        this.speed = speed;
        this.mapPosition = currentMapPosition;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.myPosition = new Vector2(90,180);
        this.boundWidth = AssetLoader.monsterUp.getRegionWidth();
        this.boundHeight = AssetLoader.monsterUp.getRegionHeight();
        this.bound = new Rectangle(myPosition.x,myPosition.y,boundWidth,boundHeight);
    }


    public void update(float delta)
    {

    }

    public void move(Direction d, Vector2 mapPosition )
    {
        setDirection(d);
        setMapPosition(mapPosition);
    }


}
