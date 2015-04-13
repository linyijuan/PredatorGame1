package sutd.istd.groupzero.gameworld;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;

public class GameWorld {
	private Map map;
	private Monster monster;
	public GameWorld(Map map){
		this.map = map;
		monster = map.getMonster();
	}
	
	public Map getMap(){
        return map;
	}

    // update method being called at every render
    public void update(){
        monster.update();
    }
}
