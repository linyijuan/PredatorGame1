package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;



public class Tree extends Item{
    private Circle bound;
    private int treeWidth = 39;
    private int treeHeight = 56;
    private int offset = 15;

    public Tree(Vector2 _position) {
        super(_position);
//        super.setBound(AssetLoader.tree.getRegionWidth(),AssetLoader.tree.getRegionHeight());
//        this.bound = new Circle(_position,(float)49);
        super.setBound(treeWidth, treeHeight);
        super.setWalkingBound(treeWidth, treeHeight - offset);
    }


}
