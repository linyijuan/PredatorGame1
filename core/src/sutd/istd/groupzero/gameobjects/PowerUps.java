package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;

public class PowerUps extends Item{

    private String kind;

    public PowerUps(Vector2 _position,String kind){
        super(_position);
        // Sets the bound of the PowerUp Item to 30 by 29
        super.setBound(30,29);

        // the variable "kind" is the type of power up that it is.
        // "v" stands for visibility and "s" stands for speed
        if (kind.equals("v"))
            this.kind = "v";
        else
            this.kind = "s";
    }

    public String getKind(){
        return kind;
    }
    public void setKind(String s){
        kind = s;
    }
}