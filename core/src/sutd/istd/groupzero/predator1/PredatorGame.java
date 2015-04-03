package sutd.istd.groupzero.predator1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.helpers.AssetLoader;
import sutd.istd.groupzero.screens.GameScreen;

public class PredatorGame extends Game {
    private ActionResolver actionResolver;
    private Map map;
    public PredatorGame(ActionResolver actionResolver,Map map){
        this.actionResolver = actionResolver;
        this.map = map;
    }

    public PredatorGame(ActionResolver actionResolver){this.actionResolver = actionResolver;}
	@Override
	public void create() {
		Gdx.app.log("Predator1", "created");
        AssetLoader.actionResolver = actionResolver;
		AssetLoader.load();
		setScreen(new GameScreen(this,actionResolver,map));
	}
	
	@Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
	
	 @Override
	 public void render() {
	        super.render();
	 }

}
