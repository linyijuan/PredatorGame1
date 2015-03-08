package sutd.istd.groupzero.gameworld;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class GameWorld {
	private Map map;
	private Monster monster;
	public GameWorld(float screenWidth, float screenHeight){
		
		// map should not be the same as screenWidth and Height
		map = new Map(screenWidth, screenHeight);
		monster = map.getMonster();
	}
	public void update(float delta) {
		map.update();
        monster.update(delta);
    }
	
	public Map getMap(){
		return map;
	}
	

}
