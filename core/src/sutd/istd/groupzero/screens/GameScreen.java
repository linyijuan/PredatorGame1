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
        world = new GameWorld(map);
        stage = new TouchPad(screenWidth/2-screenWidth/8, 15f, screenWidth/4, screenWidth/4, map,actionResolver,game).createTouchPad();
        Gdx.input.setInputProcessor(stage);
        renderer = new GameRenderer(map, screenWidth, screenHeight,actionResolver,game,stage);
    }

	@Override
    public void render(float delta) {
		runTime += delta;
        renderer.render(runTime);
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

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    }

}
