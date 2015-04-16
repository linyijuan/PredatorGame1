package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sutd.istd.groupzero.helpers.ActionResolver;

public class Map{
    private int mapSizeX = 540;
    private int mapSizeY = 960;
    private ActionResolver actionResolver;

    // Variables used for item generation
    private int noOfObstacles = 30;
    private int noOfFood=10;
    private int noOfPowerUps=10;

    private int treeWidth = 39;
    private int treeHeight = 56;

    private Monster monster;

    private ArrayList<Tree> treeList = new ArrayList<Tree>();
    private ArrayList<Food> foodList = new ArrayList<Food>();
    private ArrayList<PowerUps> powerUpsList = new ArrayList<PowerUps>();


    private Random r = new Random();

    public Map(ActionResolver actionResolver){
        // Starts the game with the monster facing bottom
        monster = new Monster(Monster.Direction.BOTTOM);
        // Making a reference to AndroidLauncher by using interface
        this.actionResolver = actionResolver;
        // Generates all the required game items
        genObstacles();
        genFood();
        genPowerUps();

    }

    // To prevent items from spawning out of the play area
    private int cap(int min, int max){
        int  x = r.nextInt();
        while(x > max || x < min){
            x = r.nextInt(max);
        }
        return x;
    }

    private void genObstacles() {
        // toPlace is a boolean to check if the item should be placed at the Vector2 position
        // specified later. If true, the item will be placed, else if false, item will not be placed and would have to find
        // another position
        boolean toPlace;
        // Checking if the number of trees has reached the specified number
        while(treeList.size() < noOfObstacles) {
            // First, assume that the item can be placed
            // Instantiate the object
            toPlace = true;
            Vector2 v = new Vector2(cap((int)monster.getBoundWidth(), mapSizeX - treeWidth - (int)monster.getBoundWidth()), cap((int)monster.getBoundHeight(), mapSizeY - treeHeight - (int)monster.getBoundHeight()));
            Tree tree = new Tree(v);
            // for loop to check if overlap exist
            // If there is an overlap of trees, toPlace becomes false and it breaks out of the for loop
            // Note: Only needs to check against treeList as this is the first generation of algorithm being called so
            // no food or power ups will be present
            if (!treeList.isEmpty()){
                for (Item i : treeList) {
                    // Getting the position of the current tree in context
                    float treeX = i.getPosition().x;
                    float treeY = i.getPosition().y;
                    // Creating a rectangle with padding around the tree object which is enough to fit one monster in
                    Rectangle boundCheck = new Rectangle(treeX - monster.getBoundWidth(), treeY - monster.getBoundHeight(), 2 * monster.getBoundWidth() + i.getBound().getWidth(), 2 * monster.getBoundHeight() + i.getBound().getHeight());
                    if (Intersector.overlaps(boundCheck, (tree.getBound())) || Intersector.overlaps(tree.getBound(), monster.getBound())) {
                        toPlace = false;
                        break;
                    }
                }
            }
            // Adds the tree if it passed the test
            if (toPlace){
                treeList.add(tree);
            }
        }
    }

    private void genFood() {
        // toPlace is a boolean to check if the item should be placed at the Vector2 position
        // specified later. If true, the item will be placed, else if false, item will not be placed and would have to find
        // another position
        boolean toPlace;
        // Checking if the number of food spawned has reached its specified number
        while (foodList.size() < noOfFood) {
            // First, assume that the item can be placed
            // Instantiate the object
            toPlace = true;
            Vector2 v = new Vector2(cap(0, mapSizeX - 30), cap(0, mapSizeY - 21));
            Food food = new Food(v);
            // Undergoes the overlap checking with the list of trees, food and power ups
            if (!treeList.isEmpty()){
                for (Tree t : treeList)
                    if (Intersector.overlaps(t.getBound(), food.getBound()) || Intersector.overlaps(food.getBound(), monster.getBound())) {
                        toPlace = false;
                        break;
                    }
            }
            // If testing against treeList has passed (able to place so far), food is checked against powerUpList
            if (toPlace && !powerUpsList.isEmpty()){
                for (PowerUps p : powerUpsList)
                    if (Intersector.overlaps(p.getBound(), food.getBound()) || Intersector.overlaps(food.getBound(), monster.getBound())) {
                        toPlace = false;
                        break;
                    }
            }
            // If testing against powerUpList has passed (able to place so far), food is checked against foodList
            if (toPlace && !foodList.isEmpty()){
                for (Food f : foodList)
                    if (Intersector.overlaps(f.getBound(), food.getBound()) || Intersector.overlaps(food.getBound(), monster.getBound())) {
                        toPlace = false;
                        break;
                    }
            }
            // Adds the food if it passed the test
            if (toPlace) {
                foodList.add(food);
            }

        }

    }

    private void genPowerUps(){
        // toPlace is a boolean to check if the item should be placed at the Vector2 position
        // specified later. If true, the item will be placed, else if false, item will not be placed and would have to find
        // another position
        boolean toPlace;
        // Checking if the number of power ups spawned has reached its specified number
        while (powerUpsList.size() < noOfPowerUps) {
            // First, assume that the item can be placed
            // Instantiate the object
            toPlace = true;
            Vector2 v = new Vector2(cap(0, mapSizeX - 22), cap(0, mapSizeY - 21));
            PowerUps powerUp = new PowerUps(v, "s");
            // Pseudo-randomizing the type of power ups being spawned
            // If modulus of 2 == 0, it is a visibility power up
            // else, it is a speed power up
            if ((powerUpsList.size() % 2) == 0) {
                powerUp.setKind("v");
            }
            // Undergoes the overlap checking with the list of trees, food and power ups
            if (!treeList.isEmpty()){
                for (Tree t : treeList)
                    if (Intersector.overlaps(t.getBound(), powerUp.getBound()) || Intersector.overlaps(powerUp.getBound(), monster.getBound())) {
                        toPlace = false;
                        break;
                    }
            }
            // If testing against treeList has passed (able to place so far), food is checked against powerUpList
            if (toPlace && !powerUpsList.isEmpty()){
                for (PowerUps p : powerUpsList)
                    if (Intersector.overlaps(p.getBound(), powerUp.getBound()) || Intersector.overlaps(powerUp.getBound(), monster.getBound())) {
                        toPlace = false;
                        break;
                    }
            }
            // If testing against powerUpList has passed (able to place so far), food is checked against foodList
            if (toPlace && !foodList.isEmpty()){
                for (Food f : foodList)
                    if (Intersector.overlaps(f.getBound(), powerUp.getBound()) || Intersector.overlaps(powerUp.getBound(), monster.getBound())) {
                        toPlace = false;
                        break;
                    }
            }
            // Adds the power up if it passed the test
            if (toPlace) {
                powerUpsList.add(powerUp);
            }
        }

    }

    ///////////////////// Getters and setters /////////////////////////////

    public ArrayList<PowerUps> getPowerUpsList() {
        return powerUpsList;
    }
    public ArrayList<Food> getFoodList() {
        return foodList;
    }
    public ArrayList<Tree> getTreeList() {
        return treeList;
    }

    public synchronized void setPowerUpsList(List<PowerUps> powerUpsList) {
        this.powerUpsList = new ArrayList<PowerUps>(powerUpsList);
        genPowerUps();
    }
    public synchronized void setFoodList(List<Food> foodList) {
        this.foodList = new ArrayList<Food>(foodList);
        genFood();
    }
    public synchronized void setTreeList(List<Tree> treeList) {
        this.treeList = new ArrayList<Tree>(treeList);
    }

    public Monster getMonster(){
        return monster;
    }

}
