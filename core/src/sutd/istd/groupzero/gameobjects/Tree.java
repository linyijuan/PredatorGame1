package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;

/* Tree Class to store the position and bound of trees*/
public class Tree extends Item{
    private int treeWidth = 39;
    private int treeHeight = 56;

    /**
     * Tree constructor
     * @param _position vector2 coordinate
     */
    public Tree(Vector2 _position) {
        super(_position);
        // Sets the rectangle bound
        super.setBound(treeWidth, treeHeight);
        // Sets the circular walking bound
        super.setWalkingBound(treeWidth, treeHeight, 13f);
    }


}
