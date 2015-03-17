package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import sutd.istd.groupzero.helpers.AssetLoader;

public class Food extends Item{
    public Food(Vector2 _position){
        super(_position);
        super.setBound(AssetLoader.steak.getRegionWidth(),AssetLoader.steak.getRegionHeight());
    }
}
