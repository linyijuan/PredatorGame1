package sutd.istd.groupzero.gameworld;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.helpers.ActionResolver;

public class GameWorld {
	private Map map;
	private Monster monster;
	public GameWorld(float screenWidth, float screenHeight,ActionResolver actionResolver,Map map){
		
		// map should not be the same as screenWidth and Height
		this.map = map;
		monster = map.getMonster();
	}
	public void update(float delta) {
        monster.update(delta);
    }
	
	public Map getMap(){
		return map;
	}
	

}
