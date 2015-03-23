package sutd.istd.groupzero.predator1;

import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.helpers.AssetLoader;
import sutd.istd.groupzero.screens.GameScreen;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PredatorGame extends Game {
    private ActionResolver actionResolver;

    public PredatorGame(ActionResolver actionResolver){this.actionResolver = actionResolver;}
	@Override
	public void create() {
		Gdx.app.log("Predator1", "created");
		AssetLoader.load();
		setScreen(new GameScreen(this));
//        actionResolver.loginGPGS();
//        actionResolver.getSignedInGPGS();
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
