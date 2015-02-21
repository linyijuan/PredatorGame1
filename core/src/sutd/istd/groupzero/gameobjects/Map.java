package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Map {
    //generate random maze every time here - algorithm part
    //need to figure out the way to create and index the grids - easier to refer
    //coordinate system: (0,0) at top left of screen and goes positive from top left to bot right
    private int mapSizeX = 50;  // Not too sure of this value yet
    private int mapSizeY = 50; // Same as for X
    private int noOfObstacles = 10;
    private int noOfFood=5;
    private int noOfPowerUps=3;
    private ArrayList<Obstacles> obstaclesList = new ArrayList<Obstacles>();
    private ArrayList<Food> foodList = new ArrayList<Food>();
    private ArrayList<PowerUps> powerUpsList = new ArrayList<PowerUps>();
    private Random r = new Random();

    private void generateObstacles() {


        for (int i = 0; i < noOfObstacles/2; i++) {
            //creates noOfObstacles trees
            obstaclesList.add(new Tree(new Vector2(r.nextInt(mapSizeX),r.nextInt(mapSizeY))));
        }
        for (int i = 0; i < noOfObstacles/2; i++) {
            //creates noOfObstacles trees
            obstaclesList.add(new Boulder(new Vector2(r.nextInt(mapSizeX),r.nextInt(mapSizeY))));
        }
    }

    private void genFood() {
        for (int i = 0; i < noOfFood; i++) {
            foodList.add(new Food(new Vector2(r.nextInt(mapSizeX), r.nextInt(mapSizeY))));
        }
    }

    private void genPowerUps()
    {
        for (int i = 0; i < noOfPowerUps; i++)
        {
            powerUpsList.add(new PowerUps(new Vector2(r.nextInt(mapSizeX), r.nextInt(mapSizeY))));
        }

    }


    public ArrayList<Obstacles> getObstaclesList() {
        return obstaclesList;
    }

    public ArrayList<PowerUps> getPowerUpsList() {
        return powerUpsList;
    }

    public ArrayList<Food> getFoodList() {
        return foodList;
    }

    public void setNoOfObstacles(int noOfObstacles) {
        this.noOfObstacles = noOfObstacles;
    }

    public void setNoOfFood(int noOfFood) {
        this.noOfFood = noOfFood;
    }

    public void setNoOfPowerUps(int noOfPowerUps) {
        this.noOfPowerUps = noOfPowerUps;
    }

    private void Init()
    {
        generateObstacles();
        genFood();
        genPowerUps();
    }


    public Map()
    {
        Init();
    }


    //use tree and something else as attributes here to represent the obstacles
    //place food(strength) and power-ups(3 types) in the rest of the map - limit the total number
    //give all the dimensions and positions of power-ups and obstacles to GameWorld

    //update Map when monster got food and power-ups, update the map to dismiss the items obtained
    //the rest are all static - no need to update
    public void update(){}

    public void onClick(){}
}
