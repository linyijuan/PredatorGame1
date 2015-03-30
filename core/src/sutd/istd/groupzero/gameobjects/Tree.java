package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;


public class Tree extends Item{
    public Tree(Vector2 _position) {
        super(_position);
//        super.setBound(AssetLoader.tree.getRegionWidth(),AssetLoader.tree.getRegionHeight());
        super.setBound(52,52);
    }


}
