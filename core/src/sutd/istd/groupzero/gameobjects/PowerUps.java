package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import sutd.istd.groupzero.helpers.AssetLoader;

public class PowerUps extends Item{
    private PowerUp kind;
    public PowerUp getKind(){
        return kind;
    }
    public void setKind(String s){
        if (s.equals("s")){
            kind = PowerUp.Speed;
        }
        else{
            kind = PowerUp.Visibility;
        }
    }
    public PowerUps(Vector2 _position,String kind){
        super(_position);
        super.setBound(AssetLoader.powerUp.getRegionWidth(),AssetLoader.powerUp.getRegionHeight());
        if (kind.equals("v"))
            this.kind = PowerUp.Visibility;
        else
            this.kind = PowerUp.Speed;
    }

    public enum PowerUp{
        Visibility, Speed;

    }
}
