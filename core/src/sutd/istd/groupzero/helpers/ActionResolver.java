package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import sutd.istd.groupzero.gameobjects.Food;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.PowerUps;
import sutd.istd.groupzero.gameobjects.Tree;

/* Interface for communication between core and android project */
public interface ActionResolver {
    /* GAME RULE AND PREPARATION*/
    // called to broadcast my start
    public boolean iStart();
    // called to know whether my opponent starts
    public boolean didYouStart();
    // called to get my player role
    public int requestMyPlayerNum();

    /*MAP WAR*/
    // called when monster eat a food on map
    public void eatFood(Food f);
    // called when monster obtain a power up on map
    public void obtainPowerUp(PowerUps p);
    // called to get opponent position
    public Vector2 requestOpponentPosition();
    // called to get opponent direction
    public int requestOpponentDirection();
    // called to let opponent know my position and direction
    public void broadcastMyStatus(Vector2 currentPosition, Monster.Direction currentDirection);
    // called to know how many food opponent eat
    public int requestOpponentStrength();
    // called to broadcast how many food opponent eat
    public void broadcastMyStrength(int strength);
    // called to get my opponent speed
    public float requestOpponentSpeed();
    // called to broadcast my own speed
    public void broadcastMySpeed(float speed);
    // called to get tree positions on map
    public ArrayList<Tree> requestTrees();
    // called to get food positions on map
    public ArrayList<Food> requestFoods();
    // called to get power up positions on map
    public ArrayList<PowerUps> requestPUs();
    // called to notify opponent the meeting of two monsters
    public boolean haveWeMet();
    // called to know whether two monsters met on map
    public void weHaveMet();

    /*TUG OF WAR*/
    // called to let opponent know my tap count
    public void broadcastMyTapping();
    // called to get opponent tap count
    public int requestOppoTapCount();
    // called to get my own tap count
    public int requestMyTapCount();
    // called to know whether opponent won
    public boolean haveYouWin();
    // called to notify my winning
    public void iWin();
    // called to know whether opponent lost
    public boolean haveYouLose();
    // called to notify my losing
    public void iLose();


}

