package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import sutd.istd.groupzero.helpers.AssetLoader;


public class Tree extends Item{
    public Tree(Vector2 _position) {
        super(_position);
        super.setBound(AssetLoader.tree.getRegionWidth(),AssetLoader.tree.getRegionHeight());
    }


}
