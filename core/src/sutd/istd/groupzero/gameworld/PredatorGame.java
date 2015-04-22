package sutd.istd.groupzero.gameworld;

import com.badlogic.gdx.Game;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.helpers.AssetLoader;

/* LIBGDX Game Class for game initialization*/
public class PredatorGame extends Game {
    private ActionResolver actionResolver;
    private Map map;
    private GameScreen screen;

    /**
     * PredatorGame constructor
     * @param map  map object associated with the game
     * @param actionResolver handles communication between gps and core project
     */
    public PredatorGame(ActionResolver actionResolver,Map map){
        this.actionResolver = actionResolver;
        this.map = map;
    }

    // called when initializeForView() called in AndroidLauncher
	@Override
	public void create() {
        AssetLoader.actionResolver = actionResolver;
        // called load() to prepare the game picture, font and sound source
		AssetLoader.load();
        // set game screen to allow drawing
        screen = new GameScreen(this,actionResolver,map);
		setScreen(screen);
	}

    // dispose the game
	@Override
    public void dispose() {
        super.dispose();
        // dispose all the source at the same time
        screen.dispose();
        AssetLoader.dispose();
    }

    // start drawing on screen
	 @Override
	 public void render() {
	    super.render();
	 }
}
