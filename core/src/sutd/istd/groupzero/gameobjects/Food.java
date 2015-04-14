package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;

/* Food Class to store the position and bound of foods*/
public class Food extends Item{
    /**
     * Food constructor
     * @param _position postion of
     */
    public Food(Vector2 _position){
        super(_position);
        // Sets the object bound to 38 by 29 pixels
        super.setBound(38,29);
    }
}
