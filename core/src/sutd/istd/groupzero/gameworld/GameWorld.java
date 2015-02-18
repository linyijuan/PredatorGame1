package sutd.istd.groupzero.gameworld;

import sutd.istd.groupzero.gameobjects.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class GameWorld {
	private Map map;
	
	public GameWorld(){
		map = new Map();
	}
	public void update(float delta) {
        Gdx.app.log("GameWorld", "update");
        
    }
	
	public Map getMap(){
		return map;
	}
	

}
