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
    private int mapSizeX = 600;  // Not too sure of this value yet
    private int mapSizeY = 600; // Same as for X
    float screenWidth;
    float screenHeight;
    private int noOfObstacles = 10;
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

	public void setTreeList(ArrayList<Tree> treeList) {
		this.treeList = treeList;
	}
	private ArrayList<Food> foodList = new ArrayList<Food>();
    private ArrayList<PowerUps> powerUpsList = new ArrayList<PowerUps>();
    private Random r = new Random();
    private Monster monster;
    
    public Map(float screenWidth, float screenHeight){
        Init();
        Gdx.app.log("map", "map created");
        monster = new Monster(10, new Vector2(0,0), Direction.BOTTOM , screenWidth, screenHeight);
        Gdx.app.log("map", monster.toString());
//        monster = new Monster2(screenWidth, screenHeight);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
    
    private void generateObstacles() {
        while(obstacleCountTrees < 7) {
            //creates noOfObstacles trees
            int x = r.nextInt(mapSizeX);
            int y = r.nextInt(mapSizeY);
            Vector2 positionVector = new Vector2(x,y);
            if (vector2List.contains(positionVector)){
                continue;
            }else {
//                obstaclesList.add(new Tree(positionVector));
                treeList.add(new Tree(positionVector));
                vector2List.add(positionVector);
                Gdx.app.log("tree pos vector ", positionVector.toString());
                obstacleCountTrees++;
            }
            
            
        }
        
//        while(obstacleCountBoulders < noOfObstacles/2) {
//            //creates noOfObstacles trees
//            int x = r.nextInt(mapSizeX);
//            int y = r.nextInt(mapSizeY);
//            Vector2 positionVector = new Vector2(x,y);
//            if (vector2List.contains(positionVector)){
//                continue;
//            }else {
//                obstaclesList.add(new Boulder(positionVector));
//                vector2List.add(positionVector);
//                obstacleCountBoulders++;
//            }
//        }
    }

    private void genFood() {
        while(foodCount < noOfFood) {
            //creates noOfObstacles trees
            int x = r.nextInt(mapSizeX);
            int y = r.nextInt(mapSizeY);
            Vector2 positionVector = new Vector2(x,y);
            if (vector2List.contains(positionVector)){
                continue;
            }else {
                foodList.add(new Food(positionVector));
                vector2List.add(positionVector);
                foodCount++;
            }
        }
    }

    private void genPowerUps(){
        while(powerUpCount < noOfPowerUps) {
            //creates noOfObstacles trees
            int x = r.nextInt(mapSizeX);
            int y = r.nextInt(mapSizeY);
            Vector2 positionVector = new Vector2(x,y);
            if (vector2List.contains(positionVector)){
                continue;
            }else {
                powerUpsList.add(new PowerUps(positionVector));
                vector2List.add(positionVector);
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

    private void Init()
    {
        generateObstacles();
        genFood();
        genPowerUps();
        Gdx.app.log("Init", "Init Completed Map.java");
        
        
        for(int i = 0 ; i < treeList.size(); i++)
        {
        	Gdx.app.log(treeList.get(i).toString(), treeList.get(i).getPosition().x + " "+ treeList.get(i).getPosition().y);
        }
    }

    //use tree and something else as attributes here to represent the obstacles
    //place food(strength) and power-ups(3 types) in the rest of the map - limit the total number
    //give all the dimensions and positions of power-ups and obstacles to GameWorld

    //update Map when monster got food and power-ups, update the map to dismiss the items obtained
    //the rest are all static - no need to update
    public Monster getMonster(){
    	return monster;
    }
    
    //
    public void update(Vector2 posDif)
    {
    	for(Tree tree : treeList)
    	{
    		tree.setPosition(tree.getPosition().add(posDif));
    	}
    }
    public void onTap(int direction){}
}
