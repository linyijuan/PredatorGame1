package sutd.istd.groupzero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.helpers.InputHandler;

public class GridWarScreen implements Screen {
    private float screenWidth;
    private float screenHeight;
    private ActionResolver actionResolver;
    private SpriteBatch batcher;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;

    public GridWarScreen(ActionResolver actionResolver,int myStrength) {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        this.actionResolver = actionResolver;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, screenWidth, screenHeight);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        Gdx.input.setInputProcessor(new InputHandler(actionResolver,2));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

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

