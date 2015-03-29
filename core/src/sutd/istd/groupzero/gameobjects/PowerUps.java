package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;

public class PowerUps extends Item{
    private String kind;
    public String getKind(){
        return kind;
    }
    public void setKind(String s){
        kind = s;
    }
    public PowerUps(Vector2 _position,String kind){
        super(_position);
//        super.setBound(AssetLoader.powerUp.getRegionWidth(),AssetLoader.powerUp.getRegionHeight());
        super.setBound(22,21);
        if (kind.equals("v"))
            this.kind = "v";
        else
            this.kind = "s";
    }

}