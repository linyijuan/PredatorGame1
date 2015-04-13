package sutd.istd.groupzero.gameobjects;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Monster {
    private int mapSizeX = 540;
    private int mapSizeY = 960;

    private Direction direction;

    private Rectangle bound;
    private float boundWidth;
    private float boundHeight;

    private Vector2 myPosition;

    private int strength = 0;

    private float speed = 1;
    private float visibility = 1;

    // Saiyan mode (aka Predator mode) becomes true when the player presses the skill button
    private boolean saiyanMode = false;

    // radius of the arrow's orbit around the player

    public Monster(Direction direction1)
    {
        this.direction = direction1;
        this.boundWidth = 27;
        this.boundHeight = 34;
        this.myPosition = new Vector2(MathUtils.random(0, mapSizeX / 5), MathUtils.random(0, mapSizeY - this.boundHeight));
        this.bound = new Rectangle(myPosition.x,myPosition.y,boundWidth,boundHeight);
    }

    public void update(){
        if (saiyanMode == true){
            this.setVisibility(2.0f);
        }
    }

    // Method is called when monster eats food
    public void obtainFood(){
        strength++;
    }

    // Method called when monster obtain speed power up
    public void addSpeed(float speedIncrement)
    {
        this.speed += speedIncrement;
    }

    // enum type of the direction the monster is facing
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


    // Getters and setters for the variables in monster
    public void setStrength(int s){
        strength = s;
    }

    public void setVisibility(float v){
        visibility = v;
    }

    public void setSaiyanMode(boolean bool){
        saiyanMode = bool;
    }

    public void setMyPosition(Vector2 myPosition){
        this.myPosition = myPosition;

        // Sets the cap to prevent monster from moving out of the play area
        if (myPosition.x < 0){this.myPosition.x = 0;}
        if (myPosition.x > mapSizeX - boundWidth){this.myPosition.x = mapSizeX - boundWidth;}
        if (myPosition.y < 0){this.myPosition.y = 0;}
        if (myPosition.y > mapSizeY - boundHeight){this.myPosition.y = mapSizeY- boundHeight;}

        this.bound = new Rectangle(this.myPosition.x,this.myPosition.y,boundWidth,boundHeight);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Rectangle getBound(){return bound;}

    public Direction getDirection() {
        return direction;
    }

    public float getSpeed()
    {
        return this.speed;
    }

    public int getStrength(){
        return strength;
    }

    public float getVisibility(){
        return visibility;
    }

    public float getBoundWidth() {
        return boundWidth;
    }

    public float getBoundHeight() {
        return boundHeight;
    }

    public boolean getSaiyanMode(){
        return saiyanMode;
    }

    public Vector2 getMyPosition(){return  myPosition;}
}
