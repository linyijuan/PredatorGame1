package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

import sutd.istd.groupzero.gameobjects.Monster.Direction;

public class Map{
    private int mapSizeX = 540;  // Not too sure of this value yet
    private int mapSizeY = 960; // Same as for X
    private float screenWidth;
    private float screenHeight;

    private int noOfObstacles = 30;
    private int noOfFood=10;
    private int noOfPowerUps=10;

    private ArrayList<Item> itemList = new ArrayList<Item>();
    private ArrayList<Tree> treeList = new ArrayList<Tree>();
    private ArrayList<Food> foodList = new ArrayList<Food>();
    private ArrayList<PowerUps> powerUpsList = new ArrayList<PowerUps>();


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
        // the number in the monster constructor is meant for the player number
        monster = new Monster(1, Direction.BOTTOM, screenWidth, screenHeight);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        genObstacles();
        genFood();
        genPowerUps();

    }

    private void genObstacles() {
        boolean toPlace;
        while(treeList.size() < noOfObstacles) {
            toPlace = true;
            Vector2 v = new Vector2(cap(0,mapSizeX-52),cap(0,mapSizeY-52));
            Tree tree = new Tree(v);
            if (!itemList.isEmpty())
                for (Item i :itemList)
                    if (Intersector.overlaps(i.getBound(), (tree.getBound())) || Intersector.overlaps(tree.getBound(),monster.getBound())){
                        toPlace = false;
                        break;
                    }
            if (toPlace){
                treeList.add(tree);
                itemList.add(tree);
            }
        }
    }

    private void genFood() {
        boolean toPlace;

        while (foodList.size() < noOfFood) {
            toPlace = true;
            Vector2 v = new Vector2(cap(0, mapSizeX - 30), cap(0, mapSizeY - 21));
            Food food = new Food(v);
            if (!itemList.isEmpty())
                for (Item i : itemList)
                    if (Intersector.overlaps(i.getBound(), food.getBound()) || Intersector.overlaps(food.getBound(), monster.getBound())) {
                        toPlace = false;
                        break;
                    }
            if (toPlace) {
                foodList.add(food);
                itemList.add(food);
            }

        }

    }

    private void genPowerUps(){
        boolean toPlace;

        while (powerUpsList.size() < noOfPowerUps) {
            toPlace = true;
            Vector2 v = new Vector2(cap(0, mapSizeX - 22), cap(0, mapSizeY - 21));
            PowerUps powerUp = new PowerUps(v, "s");
            if ((powerUpsList.size() % 2) == 0) {
                powerUp.setKind("v");
            }
            if (!itemList.isEmpty())
                for (Item i : itemList)
                    if (Intersector.overlaps(i.getBound(), powerUp.getBound()) || Intersector.overlaps(powerUp.getBound(), monster.getBound())) {
                        toPlace = false;
                        break;
                    }
            if (toPlace) {
                powerUpsList.add(powerUp);
                itemList.add(powerUp);
            }
        }

    }

    public void regenFood(Food f){

        foodList.remove(f);
        itemList.remove(f);
        genFood();

    }

    public void regenPU(PowerUps p){

        powerUpsList.remove(p);
        itemList.remove(p);
        genPowerUps();

    }


    ///////////////////// Getters and setters /////////////////////////////

    public ArrayList<Item> getItemList(){return itemList;}
    public ArrayList<PowerUps> getPowerUpsList() {
        return powerUpsList;
    }
    public ArrayList<Food> getFoodList() {
        return foodList;
    }
    public ArrayList<Tree> getTreeList() {
        return treeList;
    }

}
