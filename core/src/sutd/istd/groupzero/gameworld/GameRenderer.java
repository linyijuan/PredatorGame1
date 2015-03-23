package sutd.istd.groupzero.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import sutd.istd.groupzero.gameobjects.Food;
import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.Monster.Direction;
import sutd.istd.groupzero.gameobjects.PowerUps;
import sutd.istd.groupzero.gameobjects.Tree;
import sutd.istd.groupzero.helpers.AssetLoader;

public class GameRenderer {
    private float screenWidth;
    private float screenHeight;
    private GameWorld myWorld;
    private Map myMap;
    private Monster myMonster;

    private OrthographicCamera cam;
    private SpriteBatch batcher;
    public static Texture gridBg;
    public static TextureRegion grid;
    public static TextureRegion[] directionSet;
    public static Animation[] animationSet;
    public static TextureRegion monsterUp,monsterDown, monsterLeft,monsterRight;
    public static Animation upAnimation,downaAnimation, leftaAnimation,rightaAnimation;
    private static float scaleX, scaleY;
    private ShapeRenderer shapeRenderer;

    private Texture light;
    private FrameBuffer fbo;
    private boolean lightOscillate = true;
    //out different shaders. currentShader is just a pointer to the 4 others
    private ShaderSelection	shaderSelection = ShaderSelection.Default;
    private ShaderProgram currentShader;
    private ShaderProgram defaultShader;
    private ShaderProgram ambientShader;
    private ShaderProgram lightShader;
    private ShaderProgram finalShader;

    //values passed to the shader
    public static final float ambientIntensity = 0.7f;
    public static final Vector3 ambientColor = new Vector3(0.7f, 0.7f, 0.7f);

    //used to make the light flicker
    public float zAngle;
    public static final float zSpeed = 15.0f;
    public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;

    //read our shader files
    final String vertexShader = (Gdx.files.internal("data/vertexShader.glsl")).readString();
    final String defaultPixelShader = (Gdx.files.internal("data/defaultPixelShader.glsl")).readString();
    final String ambientPixelShader = (Gdx.files.internal("data/ambientPixelShader.glsl")).readString();
    final String lightPixelShader =  (Gdx.files.internal("data/lightPixelShader.glsl")).readString();
    final String finalPixelShader =  (Gdx.files.internal("data/pixelShader.glsl")).readString();




    public enum ShaderSelection{
        Default,
        Ambiant,
        Light,
        Final
    };



    public GameRenderer(GameWorld world, float screenWidth, float screenHeight){
        myWorld = world;
        myMap = myWorld.getMap();
        myMonster = myMap.getMonster();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;


        Music music = Gdx.audio.newMusic(Gdx.files.internal("data/LavenderTown.mp3"));
        music.setVolume(0.5f);                 // sets the volume to half the maximum volume
        music.setLooping(true);
        music.play();

        //help from marcus
//        scaleX = AssetLoader.maskLayer.getWidth()/screenWidth;
//        scaleY = AssetLoader.maskLayer.getHeight()/screenHeight;
        light = new Texture("data/light.png");
        ShaderProgram.pedantic = false;
        defaultShader = new ShaderProgram(vertexShader, defaultPixelShader);
        ambientShader = new ShaderProgram(vertexShader, ambientPixelShader);
        lightShader = new ShaderProgram(vertexShader, lightPixelShader);
        finalShader = new ShaderProgram(vertexShader, finalPixelShader);




        //fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 540, 960, false);

        lightShader.begin();
        lightShader.setUniformi("u_lightmap", 1);
        lightShader.end();

        finalShader.begin();
        finalShader.setUniformi("u_lightmap", 1);
        finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
                ambientColor.z, ambientIntensity);
        finalShader.end();


        lightOscillate = !lightOscillate;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, 180, 360);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        Gdx.app.log("Screen Height and Width ", screenWidth + "  " + screenHeight);

        gridBg = new Texture(Gdx.files.internal("data/map.png"));
        grid = new TextureRegion(gridBg, 600, 600);
        Gdx.app.log(grid.toString(),(gridBg.getWidth()/10f) * 3 +" " + (gridBg.getHeight()/10f) * 6);

        monsterDown = AssetLoader.monsterDown;
        monsterUp = AssetLoader.monsterUp;
        monsterLeft = AssetLoader.monsterLeft;
        monsterRight = AssetLoader.monsterRight;
        upAnimation = AssetLoader.upAnimation;
        downaAnimation = AssetLoader.downaAnimation;
        leftaAnimation = AssetLoader.leftaAnimation;
        rightaAnimation = AssetLoader.rightaAnimation;
        directionSet = new TextureRegion[] {monsterLeft,monsterUp,monsterRight,monsterDown};
        animationSet = new Animation[] {leftaAnimation,upAnimation,rightaAnimation,downaAnimation};
    }
    public void resize(final int width, final int height) {


        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

        lightShader.begin();
        lightShader.setUniformf("resolution", width, height);
        lightShader.end();

        finalShader.begin();
        finalShader.setUniformf("resolution", width, height);
        finalShader.end();
    }

    public void render(float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fbo.begin();
        batcher.setShader(finalShader);
        batcher.begin();
        batcher.enableBlending();
        Direction d = myMonster.getDirection();
        final float dt = Gdx.graphics.getRawDeltaTime();
        zAngle += dt * zSpeed;
        while(zAngle > PI2)
            zAngle -= PI2;


        cam.position.set(myMonster.getMyPosition(), 0);
        cam.update();
        batcher.setProjectionMatrix(cam.combined);
        batcher.draw(gridBg, 0, 0);
        for(sutd.istd.groupzero.gameobjects.Tree tree : myMap.getTreeList()){
            batcher.draw(AssetLoader.tree, tree.getPosition().x,tree.getPosition().y, 0, 0, AssetLoader.tree.getRegionWidth(), AssetLoader.tree.getRegionHeight(), 1f, 1f, 0f);
        }
        for(PowerUps p: myMap.getPowerUpsList()){
            if (p.shouldShow()) {
                batcher.draw(AssetLoader.powerUp, p.getPosition().x, p.getPosition().y, 0, 0, AssetLoader.powerUp.getRegionWidth(), AssetLoader.powerUp.getRegionHeight(), 1f, 1f, 0f);
            }
        }
        for(Food s:myMap.getFoodList()){
            if (s.shouldShow()) {
                batcher.draw(AssetLoader.steak, s.getPosition().x, s.getPosition().y, 0, 0, AssetLoader.steak.getRegionWidth(), AssetLoader.steak.getRegionHeight(), 1f, 1f, 0f);
            }
        }

        switch (d) {
            case TOP:
                batcher.draw(animationSet[1].getKeyFrame(runTime),myMonster.getMyPosition().x,myMonster.getMyPosition().y);
                break;
            case LEFT:
               batcher.draw(animationSet[0].getKeyFrame(runTime),myMonster.getMyPosition().x,myMonster.getMyPosition().y);
                break;
            case RIGHT:
               batcher.draw(animationSet[2].getKeyFrame(runTime),myMonster.getMyPosition().x,myMonster.getMyPosition().y);
                break;
            case BOTTOM:
                batcher.draw(animationSet[3].getKeyFrame(runTime),myMonster.getMyPosition().x,myMonster.getMyPosition().y);
                break;
            default:
                batcher.draw(directionSet[myMonster.getDirection().getKeycode()-5],myMonster.getMyPosition().x,myMonster.getMyPosition().y);
                break;
        }


        float lightSize = screenWidth/5 + 2f * (float)Math.sin(zAngle) + .2f* MathUtils.random();
        batcher.draw(light,myMonster.getMyPosition().x - lightSize*0.4f ,myMonster.getMyPosition().y  - lightSize*0.4f, lightSize, lightSize);

        fbo.end();

        //draw the actual scene



        fbo.getColorBufferTexture().bind(1); //this is important! bind the FBO to the 2nd texture unit
        light.bind(0); //we force the binding of a texture on first texture unit to avoid artefacts
        //this is because our default and ambiant shader dont use multi texturing...
        //youc can basically bind anything, it doesnt matter





        //help from marcus
//        batcher.draw(new TextureRegion(AssetLoader.maskLayer),0,0,0,0, AssetLoader.maskLayer.getWidth(), AssetLoader.maskLayer.getHeight(),scaleX,scaleY, 0);
        batcher.disableBlending();
        batcher.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.setProjectionMatrix(cam.combined);
//        shapeRenderer.rect(myMonster.getBound().x,myMonster.getBound().y,myMonster.getBound().width,myMonster.getBound().height);
        shapeRenderer.end();
    }


}
