package sutd.istd.groupzero.gameworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import sutd.istd.groupzero.gameobjects.Food;
import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.Monster.Direction;
import sutd.istd.groupzero.gameobjects.PowerUps;
import sutd.istd.groupzero.gameobjects.Tree;
import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.helpers.AssetLoader;
import sutd.istd.groupzero.screens.TugOfWarScreen;

public class GameRenderer {
    private float screenWidth;
    private float screenHeight;
    private GameWorld myWorld;
    private Map myMap;
    private Game game;
    private Monster myMonster;
    private ActionResolver actionResolver;
    private OrthographicCamera cam;
    private OrthographicCamera cam2;
    private SpriteBatch batcher;
    public  Texture gridBg;
    public  TextureRegion grid;
    public  TextureRegion[] directionSet,directionSetoppo;
    public  Animation[] animationSet;
    public  Animation[] animationSetoppo;
    public  TextureRegion monsterUp,monsterDown, monsterLeft,monsterRight;
    public  Animation upAnimation,downaAnimation, leftaAnimation,rightaAnimation;
    private float scaleX, scaleY;
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
    public static final float ambientIntensity = 0.1f;
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


    private Texture arrow;
    private Sprite spriteArrow;
    private Vector2 directionVectorToTarget = new Vector2();
    private float angle;
    private float radius = 35f;
    private float arrowPostX;
    private float arrowPostY;

    public GameRenderer(GameWorld world, float screenWidth, float screenHeight,ActionResolver actionResolver,Game game){
        this.game = game;
        myWorld = world;
        myMap = myWorld.getMap();
        myMonster = myMap.getMonster();
        this.actionResolver = actionResolver;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

//        Music music = Gdx.audio.newMusic(Gdx.files.internal("data/LavenderTown.mp3"));
//        music.setVolume(0.5f);                 // sets the volume to half the maximum volume
//        music.setLooping(true);
//        music.play();

        light = new Texture("data/light.png");
        ShaderProgram.pedantic = false;
        defaultShader = new ShaderProgram(vertexShader, defaultPixelShader);
        ambientShader = new ShaderProgram(vertexShader, ambientPixelShader);
        lightShader = new ShaderProgram(vertexShader, lightPixelShader);
        finalShader = new ShaderProgram(vertexShader, finalPixelShader);

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 540, 960, false);

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
        cam2 = new OrthographicCamera();
        cam2.setToOrtho(true, screenWidth, screenHeight);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

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
        directionSetoppo = new TextureRegion[] {AssetLoader.oppoLeft,AssetLoader.oppoUp,AssetLoader.oppoRight,AssetLoader.oppoDown};
        animationSet = new Animation[] {leftaAnimation,upAnimation,rightaAnimation,downaAnimation};
        animationSetoppo = new Animation[]{AssetLoader.leftaAnimationoppo,AssetLoader.upAnimationoppo,AssetLoader.rightaAnimationoppo,AssetLoader.downaAnimationoppo};

        // testing arrow drawing
        arrow = AssetLoader.arrow;
        spriteArrow = AssetLoader.spriteArrow;
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
        if(runTime < 180) {
            // To prevent traversing through the arraylist while it is being modified
            List<Food> foods = Collections.synchronizedList(actionResolver.requestFoods());
            List<PowerUps> pus = Collections.synchronizedList(actionResolver.requestPUs());
            ArrayList<Tree> trees = actionResolver.requestTrees();
            CopyOnWriteArrayList<PowerUps> powerUpsList = new CopyOnWriteArrayList<PowerUps>(pus);
            CopyOnWriteArrayList<Food> foodList = new CopyOnWriteArrayList<Food>(foods);

            myMap.setFoodList(foodList);
            myMap.setPowerUpsList(powerUpsList);
            myMap.setTreeList(trees);

            actionResolver.broadcastMyStatus(myMonster.getMyPosition(), myMonster.getDirection());
            if (actionResolver.requestOpponentPosition() != null) {
                Gdx.app.log("GameRenderer oppo post", "" + actionResolver.requestOpponentPosition().x + ", " + actionResolver.requestOpponentPosition().y);
                Gdx.app.log("GameRenderer my post", "" + myMonster.getMyPosition().x + ", " + myMonster.getMyPosition().y);
                if (Intersector.overlaps(myMonster.getBound(), new Rectangle(actionResolver.requestOpponentPosition().x, actionResolver.requestOpponentPosition().y, 27, 34))) {
                    actionResolver.broadcastMyStrength(myMonster.getStrength());
                    Gdx.app.log("GameRenderer", "Setting new screen");
                    game.setScreen(new TugOfWarScreen(actionResolver, myMonster.getStrength()));
                }
            }

//            Gdx.gl.glClearColor(0, 0, 0, 1);

            final float dt = Gdx.graphics.getRawDeltaTime();
            zAngle += dt * zSpeed;
            while (zAngle > PI2)
                zAngle -= PI2;

            fbo.begin();
            batcher.setShader(finalShader);
            batcher.setProjectionMatrix(cam.combined);
            batcher.begin();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            float visibility = myMonster.getVisibility();
            float lightSize = (screenWidth / 3 + 5f * (float) Math.sin(zAngle) + .2f * MathUtils.random()) *visibility;
            if(visibility > 1f){
                visibility = 1.05f;
            }
            float lightposx =  (myMonster.getMyPosition().x -(lightSize * 0.43f*visibility));
            float lightposy =  (myMonster.getMyPosition().y - (lightSize * 0.42f*visibility));
            batcher.draw(light, lightposx, lightposy, lightSize, lightSize);
            batcher.end();
            fbo.end();

            batcher.begin();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batcher.enableBlending();
            Direction d = myMonster.getDirection();
            fbo.getColorBufferTexture().bind(1); //this is important! bind the FBO to the 2nd texture unit
            light.bind(0); //we force the binding of a texture on first texture unit to avoid artefacts
            //this is because our default and ambiant shader dont use multi texturing...
            //youc can basically bind anything, it doesnt matter

            Vector2 camPost = new Vector2(myMonster.getMyPosition().x + myMonster.getBoundWidth() / 2, myMonster.getMyPosition().y + myMonster.getBoundHeight() / 2);
            cam.position.set(camPost, 0);
            cam.update();
            batcher.setProjectionMatrix(cam.combined);
//        shapeRenderer.setProjectionMatrix(cam.combined);
            batcher.draw(gridBg, 0, 0);


//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            for (Tree tree : trees) {
//            shapeRenderer.setColor(Color.BLUE);
//            shapeRenderer.circle(tree.getWalkingBound().x, tree.getWalkingBound().y, tree.getWalkingBound().radius);
                batcher.draw(AssetLoader.tree, tree.getPosition().x, tree.getPosition().y, 0, 0, AssetLoader.tree.getRegionWidth(), AssetLoader.tree.getRegionHeight(), 1f, 1f, 0f);
            }
//        shapeRenderer.end();

            for (PowerUps p : powerUpsList) {
                if (p.shouldShow()) {
                    batcher.draw(AssetLoader.powerUp, p.getPosition().x, p.getPosition().y, 0, 0, AssetLoader.powerUp.getRegionWidth(), AssetLoader.powerUp.getRegionHeight(), 1f, 1f, 0f);
                }
            }

            for (Food s : foodList) {
                if (s.shouldShow()) {
                    batcher.draw(AssetLoader.steak, s.getPosition().x, s.getPosition().y, 0, 0, AssetLoader.steak.getRegionWidth(), AssetLoader.steak.getRegionHeight(), 1f, 1f, 0f);
                }
            }


            switch (d) {
                case TOP:
                    batcher.draw(animationSet[1].getKeyFrame(runTime), myMonster.getMyPosition().x, myMonster.getMyPosition().y);
                    break;
                case LEFT:
                    batcher.draw(animationSet[0].getKeyFrame(runTime), myMonster.getMyPosition().x, myMonster.getMyPosition().y);
                    break;
                case RIGHT:
                    batcher.draw(animationSet[2].getKeyFrame(runTime), myMonster.getMyPosition().x, myMonster.getMyPosition().y);
                    break;
                case BOTTOM:
                    batcher.draw(animationSet[3].getKeyFrame(runTime), myMonster.getMyPosition().x, myMonster.getMyPosition().y);
                    break;
                default:
                    batcher.draw(directionSet[myMonster.getDirection().getKeycode() % 4], myMonster.getMyPosition().x, myMonster.getMyPosition().y);
                    break;
            }

            // Drawing of arrow
            if (actionResolver.requestOpponentDirection() != -100 && actionResolver.requestOpponentPosition() != null) {
                Vector2 oppo_pos = actionResolver.requestOpponentPosition();
                int oppo_d = actionResolver.requestOpponentDirection();
                switch (oppo_d) {
                    case 1:
                        batcher.draw(animationSetoppo[1].getKeyFrame(runTime), oppo_pos.x, oppo_pos.y);
                        break;
                    case 0:
                        batcher.draw(animationSetoppo[0].getKeyFrame(runTime), oppo_pos.x, oppo_pos.y);
                        break;
                    case 2:
                        batcher.draw(animationSetoppo[2].getKeyFrame(runTime), oppo_pos.x, oppo_pos.y);
                        break;
                    case 3:
                        batcher.draw(animationSetoppo[3].getKeyFrame(runTime), oppo_pos.x, oppo_pos.y);
                        break;
                    default:
                        batcher.draw(directionSetoppo[oppo_d % 4], oppo_pos.x, oppo_pos.y);
                        break;
                }


                directionVectorToTarget = directionVectorToTarget.set(oppo_pos.x - myMonster.getMyPosition().x, oppo_pos.y - myMonster.getMyPosition().y);
                angle = directionVectorToTarget.angle() - 180;
                arrowPostX = myMonster.getMyPosition().x + (radius * MathUtils.cos(directionVectorToTarget.angleRad()));
                arrowPostY = myMonster.getMyPosition().y + (radius * MathUtils.sin(directionVectorToTarget.angleRad()));

                // Drawing of arrow
                spriteArrow.setRotation(angle);
                spriteArrow.setBounds(arrowPostX, arrowPostY, myMonster.getBoundWidth(), myMonster.getBoundWidth());
                spriteArrow.setOriginCenter();
                spriteArrow.draw(batcher);
            }
            batcher.end();

            batcher.setProjectionMatrix(cam2.combined);
            batcher.setShader(defaultShader);
            batcher.begin();
            batcher.disableBlending();
            // strength display
            AssetLoader.shadow.draw(batcher, "STR: " + myMonster.getStrength(), 5, 79);
            AssetLoader.font.draw(batcher, "STR: " + myMonster.getStrength(), 4, 80);

            //speed display
            AssetLoader.shadow.draw(batcher, "SPD: " + myMonster.getSpeed(), 5, 159);
            AssetLoader.font.draw(batcher, "SPD: " + myMonster.getSpeed(), 4, 160);

            //time display
            // the values 75 and 74 aligns the font to the left side of the screen
            AssetLoader.shadow.draw(batcher, "TIME: " + (int)runTime, 5, 1);
            AssetLoader.font.draw(batcher, "TIME: " + (int)runTime/60 + ":" + (int)runTime%60, 4, 0);

            batcher.end();

        }
        else
        {
            actionResolver.broadcastMyStrength(myMonster.getStrength());
            Gdx.app.log("GameRenderer", "Setting new screen");
            game.setScreen(new TugOfWarScreen(actionResolver, myMonster.getStrength()));
        }

    }


}
