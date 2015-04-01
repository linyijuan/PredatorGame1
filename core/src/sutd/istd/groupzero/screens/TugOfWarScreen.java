package sutd.istd.groupzero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.helpers.AssetLoader;
import sutd.istd.groupzero.helpers.InputHandler;

public class TugOfWarScreen implements Screen{
    private ActionResolver actionResolver;
    private SpriteBatch batcher;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;

    public TugOfWarScreen(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, 180, 360);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

//        shapeRenderer = new ShapeRenderer();
//        shapeRenderer.setProjectionMatrix(cam.combined);

        Gdx.input.setInputProcessor(new InputHandler(actionResolver));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batcher.begin();
        AssetLoader.font.draw(batcher,"ME:"+actionResolver.requestMyTapCount(),100,100);
        AssetLoader.font.draw(batcher,"OPPO:"+actionResolver.requestOppoTapCount(),100,150);
        batcher.end();


    }

    @Override
    public void resize(int width, int height) {

    }

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
