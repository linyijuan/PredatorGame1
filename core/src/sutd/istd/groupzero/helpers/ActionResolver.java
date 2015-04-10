package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import sutd.istd.groupzero.gameobjects.Food;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.PowerUps;
import sutd.istd.groupzero.gameobjects.Tree;

public interface ActionResolver {
    public void eatFood(Food f);
    public void obtainPowerUp(PowerUps p);
    public Vector2 requestOpponentPosition();
    public int requestOpponentDirection();
    public void broadcastMyStatus(Vector2 currentPosition, Monster.Direction currentDirection);
    public ArrayList<Tree> requestTrees();
    public ArrayList<Food> requestFoods();
    public ArrayList<PowerUps> requestPUs();
    public void broadcastMyTapping();
    public int requestOppoTapCount();
    public int requestMyTapCount();
    public int requestMyPlayerNum();
    public int requestOpponentStrength();
    public void broadcastMyStrength(int strength);
    public float requestOpponentSpeed();
    public void broadcastMySpeed(float speed);
    public boolean haveWeMet();
    public void weHaveMet();
    public boolean haveYouWin();
    public void iWin();
    public boolean haveYouLose();
    public void iLose();
    public void iStart();
    public boolean didYouStart();
}

