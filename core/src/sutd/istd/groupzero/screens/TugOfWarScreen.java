package sutd.istd.groupzero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.helpers.AssetLoader;
import sutd.istd.groupzero.helpers.InputHandler;

public class TugOfWarScreen implements Screen{
    private float screenWidth;
    private float screenHeight;
    private ActionResolver actionResolver;
    private SpriteBatch batcher;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private int playerNum, opponentStrength,myStrength;

    private TextureRegion pic;
    private float ratio;
    private float x,y;


    public TugOfWarScreen(ActionResolver actionResolver,int myStrength) {
        this.actionResolver = actionResolver;
        this.myStrength = myStrength;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, 200, 400);
        screenWidth = 200;
        screenHeight = 400;
        playerNum = actionResolver.requestMyPlayerNum();
        if (playerNum == 1){
            pic = AssetLoader.vsScreenGreenBot;
        }
        else{
            pic = AssetLoader.vsScreenRedBot;
        }
        while(actionResolver.requestOpponentStrength()== -1){
            opponentStrength =actionResolver.requestOpponentStrength();
        }
        opponentStrength =actionResolver.requestOpponentStrength();

        ratio = (-myStrength+opponentStrength+10f)/20f;

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

//        shapeRenderer = new ShapeRenderer();
//        shapeRenderer.setProjectionMatrix(cam.combined);


        Gdx.input.setInputProcessor(new InputHandler(actionResolver,0));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int diff = -actionResolver.requestMyTapCount()-myStrength + actionResolver.requestOppoTapCount()+opponentStrength;
        ratio = (diff+10f)/20f;

        if (ratio >=1){
            batcher.begin();
            AssetLoader.font.draw(batcher,"YOU LOSE",80,150);
            batcher.end();
            Gdx.input.setInputProcessor(new InputHandler(actionResolver,1));
        }
        else if (ratio <=0){
            batcher.begin();
            AssetLoader.font.draw(batcher,"YOU WIN",80,150);
            batcher.end();
            Gdx.input.setInputProcessor(new InputHandler(actionResolver,1));
        }
        else{
            batcher.begin();
            batcher.disableBlending();
            batcher.draw(pic, 0, (screenHeight*ratio)-(screenHeight*1.5f)/2, screenWidth, screenHeight*1.5f);
            batcher.end();
        }




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
