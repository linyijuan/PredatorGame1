package sutd.istd.groupzero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

import sutd.istd.groupzero.gameworld.GameRenderer;
import sutd.istd.groupzero.gameworld.GameWorld;
import sutd.istd.groupzero.helpers.InputHandler;
import sutd.istd.groupzero.helpers.TouchPad;

public class GameScreen implements Screen{
    float screenWidth;
    float screenHeight;
	private GameWorld world;
	private GameRenderer renderer;
	private float runTime = 0;
	private Stage stage;
	
	public GameScreen() {
		screenWidth = 180;
		screenHeight = 360;
        world = new GameWorld(screenWidth, screenHeight);
        renderer = new GameRenderer(world, screenWidth, screenHeight);
        stage = new TouchPad(15f, 15f, 200f, 200f, world).createTouchPad();
        Gdx.input.setInputProcessor(stage);
    }

	@Override
    public void render(float delta) {
		runTime += delta;
        world.update(delta);
        renderer.render(runTime);
        stage.draw();       
        
    }

    @Override
    public void resize(int width, int height) {
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
