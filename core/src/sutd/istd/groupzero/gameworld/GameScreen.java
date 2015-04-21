package sutd.istd.groupzero.gameworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
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
    private TextureRegion menuBg,logo;
    private ImageButton quickgame,invite,viewInvitation;
    private TextButton signOut;
    private Stage mainMenuStage;
    private boolean enterGameScreen = false;
	
	public GameScreen(Game game,ActionResolver actionResolver,Map map) {
        this.actionResolver = actionResolver;
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
        monster = map.getMonster();
        TouchPad touchPad = new TouchPad(screenWidth/2-screenWidth/8, 15f, screenWidth/4, screenWidth/4, map,actionResolver);
        stage = touchPad.createTouchPad();
        renderer = new GameRenderer(map, screenWidth, screenHeight,actionResolver,stage, touchPad);
        cam2 = new OrthographicCamera();
        cam2.setToOrtho(true, screenWidth, screenHeight);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam2.combined);
        prepareMainMenu();
    }

    private void prepareMainMenu(){
        menuBg = AssetLoader.menuBg;
        logo = AssetLoader.logo;

        quickgame = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/quickgame.png")))),new TextureRegionDrawable(AssetLoader.quickgamed));
        invite = new ImageButton(new TextureRegionDrawable(AssetLoader.invite),new TextureRegionDrawable(AssetLoader.invited));
        viewInvitation = new ImageButton(new TextureRegionDrawable(AssetLoader.viewInvitation),new TextureRegionDrawable(AssetLoader.viewInvitationd));

        quickgame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actionResolver.startQuickGame();
            };
        });
        float drawWidth = screenWidth/1.2f;
        float drawHeight = AssetLoader.quickgame.getRegionHeight()*drawWidth/AssetLoader.quickgame.getRegionWidth();
        float x = screenWidth/2f - drawWidth/2f;
        float y = screenHeight/2f - drawHeight/2f;
        quickgame.setBounds(x,y,drawWidth,drawHeight);

        invite.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actionResolver.sendInvitation();
            };
        });
        drawHeight = AssetLoader.invite.getRegionHeight()*drawWidth/AssetLoader.invite.getRegionWidth();
        x = screenWidth/2f - drawWidth/2f;
        y = screenHeight/2.5f - drawHeight/2f;
        invite.setBounds(x,y,drawWidth,drawHeight);

        viewInvitation.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actionResolver.seeInvitations();
            };
        });
        drawHeight = AssetLoader.viewInvitation.getRegionHeight()*drawWidth/AssetLoader.viewInvitation.getRegionWidth();
        x = screenWidth/2f - drawWidth/2f;
        y = screenHeight/3.5f - drawHeight/2f;
        viewInvitation.setBounds(x,y,drawWidth,drawHeight);

        mainMenuStage = new Stage(new ExtendViewport(screenWidth,screenHeight));
        mainMenuStage.addActor(quickgame);
        mainMenuStage.addActor(invite);
        mainMenuStage.addActor(viewInvitation);
        Gdx.input.setInputProcessor(mainMenuStage);
    }

	@Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // if block to ensure that both players start at the same time and not lag behind
        if (actionResolver.showScreen() == 0){
            batcher.begin();
            batcher.enableBlending();
            batcher.setProjectionMatrix(cam2.combined);
            batcher.draw(menuBg, 0, 0, screenWidth, screenHeight);
            float drawWidth = screenWidth/1.5f;
            float drawHeight = logo.getRegionHeight()*drawWidth/logo.getRegionWidth();
            float x = screenWidth/2f - drawWidth/2f;
            float y = screenHeight/5f - drawHeight/2f;
            batcher.draw(logo,x,y,drawWidth,drawHeight);
            batcher.end();
            mainMenuStage.draw();
        }
        else if (actionResolver.showScreen() == 1){
            batcher.begin();
            batcher.enableBlending();
            batcher.setProjectionMatrix(cam2.combined);
            batcher.draw(AssetLoader.menuBg,0,0,screenWidth,screenHeight);
            float drawWidth = screenWidth/1.5f;
            float drawHeight = logo.getRegionHeight()*drawWidth/logo.getRegionWidth();
            float x = screenWidth/2f - drawWidth/2f;
            float y = screenHeight/2.5f - drawHeight/2f;
            batcher.draw(logo,x,y,drawWidth,drawHeight);
            AssetLoader.shadow.draw(batcher, "Loading Game...", screenWidth/2f-AssetLoader.shadow.getBounds("Loading Game...").width/2-1, screenHeight/2f-1);
            AssetLoader.font.draw(batcher, "Loading Game...", screenWidth /2f-AssetLoader.font.getBounds("Loading Game...").width/2, screenHeight/2f);
            batcher.end();
        }
        else{
            Gdx.input.setInputProcessor(stage);
            runTime += delta;
            renderer.render(runTime);
            monster.update();
        }
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
        renderer.dispose();

    }

}
