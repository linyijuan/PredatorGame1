package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;

public class Tree extends Item{
    private int treeWidth = 39;
    private int treeHeight = 56;

    public Tree(Vector2 _position) {
        super(_position);
        // Sets the rectangle bound
        super.setBound(treeWidth, treeHeight);
        // Sets the circular walking bound
        super.setWalkingBound(treeWidth, treeHeight, 13f);
    }


}
