package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;



public class Tree extends Item{
    private Circle bound;
    public Tree(Vector2 _position) {
        super(_position);
//        super.setBound(AssetLoader.tree.getRegionWidth(),AssetLoader.tree.getRegionHeight());
//        this.bound = new Circle(_position,(float)49);
        super.setBound(52,49);
    }


}
