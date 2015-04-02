package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;

public class Food extends Item{
    public Food(Vector2 _position){
        super(_position);
//        super.setBound(AssetLoader.steak.getRegionWidth(),AssetLoader.steak.getRegionHeight());
        super.setBound(17,15);    }
}
