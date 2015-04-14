package sutd.istd.groupzero.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameworld.GameRenderer;
import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.helpers.AssetLoader;
import sutd.istd.groupzero.helpers.TouchPad;

public class GameScreen implements Screen{
    float screenWidth;
    float screenHeight;
	private GameRenderer renderer;
	private float runTime = 0;
	private Stage stage;
    private ActionResolver actionResolver;
    private OrthographicCamera cam2;
    private SpriteBatch batcher;
    private Monster monster;
	
	public GameScreen(Game game,ActionResolver actionResolver,Map map) {
        this.actionResolver = actionResolver;
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
        monster = map.getMonster();
        stage = new TouchPad(screenWidth/2-screenWidth/8, 15f, screenWidth/4, screenWidth/4, map,actionResolver).createTouchPad();
        Gdx.input.setInputProcessor(stage);
        renderer = new GameRenderer(map, screenWidth, screenHeight,actionResolver,game,stage);
        cam2 = new OrthographicCamera();
        cam2.setToOrtho(true, screenWidth, screenHeight);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam2.combined);
    }

	@Override
    public void render(float delta) {
        // if block to ensure that both players start at the same time and not lag behind
        if (!actionResolver.didYouStart()){
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batcher.begin();
            batcher.enableBlending();
            batcher.setProjectionMatrix(cam2.combined);
            batcher.draw(AssetLoader.menuBg,0,0,screenWidth,screenHeight);
            AssetLoader.shadow.draw(batcher, "Loading Game...", screenWidth/2f-AssetLoader.shadow.getBounds("Loading Game...").width/2-1, screenHeight/2.5f-1);
            AssetLoader.font.draw(batcher, "Loading Game...", screenWidth /2f-AssetLoader.font.getBounds("Loading Game...").width/2, screenHeight/2.5f);
            batcher.end();
        }
        else{
            runTime += delta;
            renderer.render(runTime);
            monster.update();
        }
        actionResolver.iStart();
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
