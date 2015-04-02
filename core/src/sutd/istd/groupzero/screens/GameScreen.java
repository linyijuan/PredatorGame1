package sutd.istd.groupzero.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameworld.GameRenderer;
import sutd.istd.groupzero.gameworld.GameWorld;
import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.helpers.TouchPad;

public class GameScreen implements Screen{
    float screenWidth;
    float screenHeight;
	private GameWorld world;
	private GameRenderer renderer;
	private float runTime = 0;
	private Stage stage;
	
	public GameScreen(Game game,ActionResolver actionResolver,Map map) {
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
        world = new GameWorld(screenWidth, screenHeight,actionResolver,map);
        renderer = new GameRenderer(world, screenWidth, screenHeight,actionResolver,game);

        stage = new TouchPad(screenWidth/2, 15f, 300f, 300f, world,actionResolver,game).createTouchPad();

        Gdx.input.setInputProcessor(stage);
    }

	@Override
    public void render(float delta) {
		runTime += delta;
        renderer.render(runTime);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width,height);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide called");     
    }

    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause called");        
    }

    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume called");       
    }

    @Override
    public void dispose() {
        // Leave blank
    }


    /////////// Getters/////////////
    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

}
