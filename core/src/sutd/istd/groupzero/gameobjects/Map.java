package sutd.istd.groupzero.gameobjects;

import java.util.ArrayList;
import java.util.Random;
import sutd.istd.groupzero.gameobjects.Monster.Direction;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class Map{
    private int mapSizeX = 540;  // Not too sure of this value yet
    private int mapSizeY = 960; // Same as for X
    private float screenWidth;
    private float screenHeight;

    private int noOfObstacles = 20;
    private int noOfFood=5;
    private int noOfPowerUps=3;
    public void setNoOfObstacles(int noOfObstacles) {
        this.noOfObstacles = noOfObstacles;
    }
    public void setNoOfFood(int noOfFood) {
        this.noOfFood = noOfFood;
    }
    public void setNoOfPowerUps(int noOfPowerUps) {
        this.noOfPowerUps = noOfPowerUps;
    }

    private ArrayList<Item> itemList = new ArrayList<Item>();
    public synchronized ArrayList<Item> getItemList(){return itemList;}
    private ArrayList<Tree> treeList = new ArrayList<Tree>();
    public synchronized ArrayList<Tree> getTreeList() {
        return treeList;
    }
    public void setTreeList(ArrayList<Tree> treeList) {
        this.treeList = treeList;
    }
    private ArrayList<Food> foodList = new ArrayList<Food>();
    public synchronized ArrayList<Food> getFoodList() {
        return foodList;
    }
    private ArrayList<PowerUps> powerUpsList = new ArrayList<PowerUps>();
    public synchronized ArrayList<PowerUps> getPowerUpsList() {
        return powerUpsList;
    }

    private Random r = new Random();
    public int cap(int min, int max){
        int  x = r.nextInt();
        while(x > max || x < min){
            x = r.nextInt(max);
        }
        return x;
    }

    private Monster monster;
    public Monster getMonster(){
        return monster;
    }

    public Map(float screenWidth, float screenHeight){
        genObstacles();
        genFood();
        genPowerUps();
        monster = new Monster(foodList,powerUpsList,treeList, Direction.BOTTOM, screenWidth, screenHeight);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    private synchronized void genObstacles() {
        boolean toPlace;
        while(treeList.size() < noOfObstacles) {
            toPlace = true;
            Vector2 v = new Vector2(cap(0,mapSizeX-52),cap(0,mapSizeY-52));
            if (!treeList.isEmpty() & !itemList.isEmpty())
                for (Item i :itemList)
                    if (Intersector.overlaps(i.getBound(), (new Tree(v)).getBound())){
                        toPlace = false;
                        break;
                    }
            if (toPlace){
                treeList.add(new Tree(v));
                itemList.add(new Tree(v));
            }
        }
    }

    private synchronized void genFood() {
        boolean toPlace;
        while(foodList.size() < noOfFood) {
            toPlace = true;
            Vector2 v = new Vector2(cap(0,mapSizeX-30),cap(0,mapSizeY-21));
            if (!foodList.isEmpty()& !itemList.isEmpty())
                for (Item i :itemList)
                    if (Intersector.overlaps(i.getBound(),(new Food(v)).getBound())){
                        toPlace = false;
                        break;
                    }
            if (toPlace){
                foodList.add(new Food(v));
                itemList.add(new Tree(v));
            }
        }
    }

    private synchronized void genPowerUps(){
        boolean toPlace;
        while(powerUpsList.size() < noOfPowerUps) {
            toPlace = true;
            Vector2 v = new Vector2(cap(0,mapSizeX-22),cap(0,mapSizeY-21));
            if (!powerUpsList.isEmpty()& !itemList.isEmpty())
                for (Item i :itemList)
                    if (Intersector.overlaps(i.getBound(),(new PowerUps(v)).getBound())){
                        toPlace = false;
                        break;
                    }
            if (toPlace) {
                powerUpsList.add(new PowerUps(v));
                itemList.add(new Tree(v));
            }
        }

    }

    public synchronized void regenFood(Food f){
        foodList.remove(f);
        itemList.remove(f);
        genFood();
    }

    public synchronized void regenPU(PowerUps p){
        powerUpsList.remove(p);
        itemList.remove(p);
        genPowerUps();
    }
}
