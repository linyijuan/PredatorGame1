package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import sutd.istd.groupzero.helpers.AssetLoader;

public class PowerUps extends Item{
    private String name;
    public PowerUps(Vector2 _position){
        super(_position);
        super.setBound(AssetLoader.powerUp.getRegionWidth(),AssetLoader.powerUp.getRegionHeight());
    }

    public String getName(){
        return name;
    }
}
