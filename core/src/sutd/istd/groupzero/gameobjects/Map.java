package sutd.istd.groupzero.gameobjects;

import java.util.ArrayList;
import java.util.Random;

import sutd.istd.groupzero.gameobjects.Monster.Direction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Map{
    //generate random maze every time here - algorithm part
    //need to figure out the way to create and index the grids - easier to refer
    //coordinate system: (0,0) at top left of screen and goes positive from top left to bot right
    private int mapSizeX = 540;  // Not too sure of this value yet
    private int mapSizeY = 960; // Same as for X
    float screenWidth;
    float screenHeight;
    private int noOfObstacles = 20;
    private int noOfFood=5;
    private int noOfPowerUps=3;
    private int obstacleCountTrees = 0;
    private int obstacleCountBoulders = 0;
    private int foodCount = 0;
    private int powerUpCount = 0;
    private ArrayList<Vector2> vector2List = new ArrayList<Vector2>();//marks occupied spots
    private ArrayList<Tree> treeList = new ArrayList<Tree>();
    public ArrayList<Tree> getTreeList() {
        return treeList;
    }
    private ArrayList<Rectangle2D> rectangleList = new ArrayList<Rectangle2D>();
    public void setTreeList(ArrayList<Tree> treeList) {
        this.treeList = treeList;
    }
    private ArrayList<Food> foodList = new ArrayList<Food>();
    private ArrayList<PowerUps> powerUpsList = new ArrayList<PowerUps>();
    private Random r = new Random();
    private Monster monster;

    public Map(float screenWidth, float screenHeight){
        Init();
        monster = new Monster(foodList,powerUpsList,treeList, Direction.BOTTOM , screenWidth, screenHeight);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    private void generateObstacles() {
        boolean toPlace;
        while(obstacleCountTrees < noOfObstacles) {
            toPlace = true;
            //creates noOfObstacles trees
            int x = cap(0,mapSizeX-52);
            int y = cap(0,mapSizeY-52);
            Vector2 positionVector = new Vector2(x,y);
            Rectangle2D rect = new Rectangle2D(x, y, 52.0f, 52.0f); // Possible optimization here
            for (int i=0; i<rectangleList.size(); i++){
                Rectangle2D currRect = rectangleList.get(i);
                if (rect.overlaps(currRect) || rect.contains(currRect) || currRect.overlaps(rect) || currRect.contains(rect)){
                    toPlace = false;
                    break;
                }

            }
            if (toPlace){
                treeList.add(new Tree(positionVector));
                vector2List.add(positionVector);
                rectangleList.add(rect);
                Gdx.app.log("tree pos vector ", positionVector.toString());
                obstacleCountTrees++;
            }

        }

    }

    private void genFood() {
        boolean toPlace;
        while(foodCount < noOfFood) {
            toPlace = true;
            //creates noOfObstacles trees
            int x = cap(0,mapSizeX-30);
            int y = cap(0,mapSizeY-21);

            Vector2 positionVector = new Vector2(x,y);
            Rectangle2D rect = new Rectangle2D(x, y, 30.0f, 21.0f); // Possible optimization here
            for (int i=0; i<rectangleList.size(); i++){
                Rectangle2D currRect = rectangleList.get(i);
                if (rect.overlaps(currRect) || rect.contains(currRect) || currRect.overlaps(rect) || currRect.contains(rect)){
                    toPlace = false;
                    break;
                }
            }
            if (toPlace){
                foodList.add(new Food(positionVector));
                vector2List.add(positionVector);
                rectangleList.add(rect);
                Gdx.app.log("food pos vector ", positionVector.toString());
                foodCount++;
            }
        }
    }

    private void genPowerUps(){
        boolean toPlace;
        while(powerUpCount < noOfPowerUps) {
            toPlace = true;
            //creates noOfObstacles trees
            int x = cap(0,mapSizeX-22);
            int y = cap(0,mapSizeY-21);
            Vector2 positionVector = new Vector2(x,y);
            Rectangle2D rect = new Rectangle2D(x, y, 22.0f, 21.0f); // Possible optimization here
            for (int i=0; i<rectangleList.size(); i++){
                Rectangle2D currRect = rectangleList.get(i);
                if (rect.overlaps(currRect) || rect.contains(currRect) || currRect.overlaps(rect) || currRect.contains(rect)){
                    toPlace = false;
                    break;
                }
            }
            if (toPlace) {
                powerUpsList.add(new PowerUps(positionVector));
                vector2List.add(positionVector);
                rectangleList.add(rect);
                Gdx.app.log("power up pos vector ", positionVector.toString());
                powerUpCount++;
            }
        }

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

    public int cap(int min, int max){
        int  x = r.nextInt();
        while(x > max || x < min)
        {
            x = r.nextInt(max);
        }
        return x;
    }

    private void Init(){
        generateObstacles();
        genFood();
        genPowerUps();
    }

    public Monster getMonster(){
        return monster;
    }

    public boolean canMove(){

        return true;
    }
    public void onTap(int direction){}
}
