package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import sutd.istd.groupzero.gameobjects.Food;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.PowerUps;
import sutd.istd.groupzero.gameobjects.Tree;

/**
 * Created by linyijuan on 18/3/15.
 */
public interface ActionResolver {
    public void eatFood(Food f);
    public void obtainPowerUp(PowerUps p);
    public Vector2 requestOpponentPosition();
    public int requestOpponentDirection();
    public void broadcastMyStatus(Vector2 currentPosition, Monster.Direction currentDirection);
    //    public void broadcastMyFood(Food f);
//    public void broadcastMyPU(PowerUps p);
//    public void broadcastMyTree(Tree t);
    public void broadcastMyMap();
    public ArrayList<Tree> requestTrees();
    public ArrayList<Food> requestFoods();
    public ArrayList<PowerUps> requestPUs();
    public void log(String tag, String s);
}
