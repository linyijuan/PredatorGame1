package sutd.istd.groupzero.gameworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.scenes.scene2d.Stage;

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
import sutd.istd.groupzero.helpers.InputHandler;

public class GameRenderer {
    //values passed to the shader
    public static final float ambientIntensity = 0.1f;
    public static final Vector3 ambientColor = new Vector3(0.7f, 0.7f, 0.7f);
    //used to make the light flicker
    public static final float zSpeed = 15.0f;
    public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;
    private float screenWidth,screenHeight;
    private Map myMap;
    private Game game;
    private boolean left = false;
    private Monster myMonster;
    private Stage stage;
    private ActionResolver actionResolver;
    private OrthographicCamera cam,cam2;
    private SpriteBatch batcher;
    public static BitmapFont font, shadow;
    public  Texture gridBg, light, saiyan;
    public  TextureRegion menuBg,grid,pic,myHead,oppoHead,food,speed,monsterUp,monsterDown, monsterLeft,monsterRight, Spic, Vpic;
    public  TextureRegion[] directionSet,directionSetoppo,vsMe,vsOppo;
    public  Animation[] animationSet,animationSetoppo;
    public  Animation upAnimation,downaAnimation, leftaAnimation,rightaAnimation,clock,victorybg;
    private ShapeRenderer shapeRenderer;
    private FrameBuffer fbo;
    private boolean lightOscillate = true;
    private Sprite spriteArrow;
    private Vector2 directionVectorToTarget = new Vector2();
    private float angle,arrowPostX,arrowPostY,ratio;
    private float round2Start = 180;
    private float radius = 35f;
    private boolean shouldStartRound2 = false;
    private int opponentStrength,myStrength;
    private InputHandler handler;
    //out different shaders. currentShader is just a pointer to the 4 others
    private ShaderSelection	shaderSelection = ShaderSelection.Default;
    private ShaderProgram currentShader;
    private ShaderProgram defaultShader;
    private ShaderProgram ambientShader;
    private ShaderProgram lightShader;
    private ShaderProgram finalShader;
    public float zAngle;
    private Vector2 oppo_pos;
    private float shake = 5f;
    private int diff;
    //read our shader files
    final String vertexShader = (Gdx.files.internal("data/vertexShader.glsl")).readString();
    final String defaultPixelShader = (Gdx.files.internal("data/defaultPixelShader.glsl")).readString();
    final String ambientPixelShader = (Gdx.files.internal("data/ambientPixelShader.glsl")).readString();
    final String lightPixelShader =  (Gdx.files.internal("data/lightPixelShader.glsl")).readString();
    final String finalPixelShader =  (Gdx.files.internal("data/pixelShader.glsl")).readString();
    public Music music = Gdx.audio.newMusic(Gdx.files.internal("data/Mt.Moon.mp3"));
    public enum ShaderSelection{
        Default,
        Ambiant,
        Light,
        Final
    };


    public GameRenderer(Map map, float screenWidth, float screenHeight,ActionResolver actionResolver,Game game,Stage stage){
        this.game = game;
        myMap = map;
        myMonster = myMap.getMonster();
        this.actionResolver = actionResolver;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        Gdx.app.log("GAME RENDERER WIDTH", Float.toString(screenWidth));
        Gdx.app.log("GAME RENDERER HEIGHT", Float.toString(screenHeight));

        music.setVolume(0.5f);                 // sets the volume to half the maximum volume
        music.setLooping(true);
        music.play();

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
        finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,ambientColor.z, ambientIntensity);
        finalShader.end();

        lightOscillate = !lightOscillate;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, 180, 360);
        cam2 = new OrthographicCamera();
        cam2.setToOrtho(true, screenWidth, screenHeight);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam2.combined);

        gridBg = new Texture(Gdx.files.internal("data/map.png"));
        grid = new TextureRegion(gridBg, 600, 600);


        Spic = AssetLoader.Spic;
        Vpic = AssetLoader.Vpic;
        monsterDown = AssetLoader.monsterDown;
        monsterUp = AssetLoader.monsterUp;
        monsterLeft = AssetLoader.monsterLeft;
        monsterRight = AssetLoader.monsterRight;
        upAnimation = AssetLoader.upAnimation;
        downaAnimation = AssetLoader.downaAnimation;
        leftaAnimation = AssetLoader.leftaAnimation;
        rightaAnimation = AssetLoader.rightaAnimation;
        clock = AssetLoader.clock;
        directionSet = new TextureRegion[] {monsterLeft,monsterUp,monsterRight,monsterDown};
        directionSetoppo = new TextureRegion[] {AssetLoader.oppoLeft,AssetLoader.oppoUp,AssetLoader.oppoRight,AssetLoader.oppoDown};
        animationSet = new Animation[] {leftaAnimation,upAnimation,rightaAnimation,downaAnimation};
        animationSetoppo = new Animation[]{AssetLoader.leftaAnimationoppo,AssetLoader.upAnimationoppo,AssetLoader.rightaAnimationoppo,AssetLoader.downaAnimationoppo};
        myHead = AssetLoader.myHead;
        oppoHead = AssetLoader.oppoHead;
        food = AssetLoader.steak;
        speed = AssetLoader.powerUp;
        spriteArrow = AssetLoader.spriteArrow;
        menuBg = AssetLoader.menuBg;
        font = AssetLoader.font;
        shadow = AssetLoader.shadow;
        victorybg = AssetLoader.victoryAnimation;

        saiyan = new Texture(Gdx.files.internal("data/saiyan.png"));
        pic = AssetLoader.vsBg;
        vsMe = new TextureRegion[] {AssetLoader.vsMe,AssetLoader.vsMe2};
        vsOppo = new TextureRegion[] {AssetLoader.vsOppo,AssetLoader.vsOppo2};

        handler = new InputHandler(actionResolver,0);
        this.stage = stage;
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
        if (runTime < 2){
            drawRound1Waiting(runTime);
        }
        else if(!shouldStartRound2 && runTime <= round2Start) {
            drawMapWar(runTime);
        }
        else if (shouldStartRound2|| (runTime > round2Start && runTime < round2Start+2)){
            drawRound2Waiting(runTime);
        }
        else{
            drawTugOfWar(runTime);
        }

    }

    public void drawMapWar(float runTime){
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
            if (actionResolver.haveWeMet() || Intersector.overlaps(myMonster.getBound(), new Rectangle(actionResolver.requestOpponentPosition().x, actionResolver.requestOpponentPosition().y, 27, 34))) {
                actionResolver.weHaveMet();
                actionResolver.broadcastMyStrength(myMonster.getStrength());
                shouldStartRound2 = true;
                round2Start = runTime;
            }
        }
        if (!shouldStartRound2) {
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
            float lightSize = (screenWidth / 3 + (float) Math.sin(zAngle)) * visibility;
            float lightposx = (myMonster.getMyPosition().x + myMonster.getBound().width / 2 - lightSize * 0.5f);
            float lightposy = (myMonster.getMyPosition().y + myMonster.getBound().height / 2 - lightSize * 0.5f);
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
            batcher.draw(gridBg, 0, 0);

            for (Tree tree : trees) {
                batcher.draw(AssetLoader.tree, tree.getPosition().x, tree.getPosition().y, 0, 0, AssetLoader.tree.getRegionWidth(), AssetLoader.tree.getRegionHeight(), 1f, 1f, 0f);
            }
            synchronized (powerUpsList) {
                for (PowerUps p : powerUpsList) {
                        batcher.draw(AssetLoader.powerUp, p.getPosition().x, p.getPosition().y, 0, 0, AssetLoader.powerUp.getRegionWidth(), AssetLoader.powerUp.getRegionHeight(), 1f, 1f, 0f);
                }
            }
            synchronized (foodList) {
                for (Food s : foodList) {
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



            if (actionResolver.requestOpponentDirection() != -100 && actionResolver.requestOpponentPosition() != null) {
                oppo_pos = actionResolver.requestOpponentPosition();
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

                // Drawing of arrow
                directionVectorToTarget = directionVectorToTarget.set(oppo_pos.x - myMonster.getMyPosition().x, oppo_pos.y - myMonster.getMyPosition().y);
                angle = directionVectorToTarget.angle() - 180;
                arrowPostX = myMonster.getMyPosition().x + (radius * MathUtils.cos(directionVectorToTarget.angleRad()));
                arrowPostY = myMonster.getMyPosition().y + (radius * MathUtils.sin(directionVectorToTarget.angleRad()));
                batcher.setShader(defaultShader);
                // Drawing of arrow
                spriteArrow.setRotation(angle);
                spriteArrow.setBounds(arrowPostX, arrowPostY, myMonster.getBoundWidth(), myMonster.getBoundWidth());
                spriteArrow.setOriginCenter();
                spriteArrow.draw(batcher);
            }
            batcher.end();

            batcher.setProjectionMatrix(cam2.combined);

            batcher.begin();
            batcher.enableBlending();

            if(myMonster.getSpeed()>1.0f && !myMonster.getSaiyanMode()){
                batcher.draw(Spic, 40 * (screenWidth / 1080) + screenWidth/10,45 * (screenHeight / 1920) + 150 * (screenHeight / 1920),screenWidth/10,screenWidth/10);
            }
            if(myMonster.getVisibility()>1.0f && !myMonster.getSaiyanMode()){
                batcher.draw(Vpic, 40 * (screenWidth / 1080),45 * (screenHeight / 1920) + 150 * (screenHeight / 1920),screenWidth/10,screenWidth/10);
            }
            if(myMonster.getSaiyanMode()){
                batcher.draw(saiyan, 40 * (screenWidth / 1080),45 * (screenHeight / 1920) + 150 * (screenHeight / 1920),screenWidth/10,screenWidth/10);
            }
            batcher.draw(myHead, 40 * (screenWidth / 1080), 45 * (screenHeight / 1920), 0, 0, myHead.getRegionWidth() * (screenWidth / 1080), myHead.getRegionHeight() * (screenHeight / 1920), 1, 1, 0);
            batcher.draw(food, 40 * (screenWidth / 1080) + 10 * (screenWidth / 1080) + myHead.getRegionWidth() * (screenWidth / 1080), 45 * (screenHeight / 1920), 0, 0, (myHead.getRegionWidth() / 2) * (screenWidth / 1080), (myHead.getRegionHeight() / 2) * (screenHeight / 1920), 1, 1, 0);
            batcher.draw(speed, 40 * (screenWidth / 1080) + 10 * (screenWidth / 1080) + myHead.getRegionWidth() * (screenWidth / 1080), 45 * (screenHeight / 1920) + 80 * (screenHeight / 1920), 0, 0, (myHead.getRegionWidth() / 2) * (screenWidth / 1080), (myHead.getRegionHeight() / 2) * (screenHeight / 1920), 1, 1, 0);
            // strength display

            AssetLoader.font.draw(batcher, "" + myMonster.getStrength(), 40 * (screenWidth / 1080) + 20 * (screenWidth / 1080) + myHead.getRegionWidth() * (screenWidth / 1080) + 10 * (screenWidth / 1080) + (myHead.getRegionWidth() / 2) * (screenWidth / 1080), 40 * (screenHeight / 1920));
            //speed display
            AssetLoader.font.draw(batcher, "" + (float) (Math.round(myMonster.getSpeed() * 10)) / 10, 40 * (screenWidth / 1080) + 20 * (screenWidth / 1080) + myHead.getRegionWidth() * (screenWidth / 1080) + 10 * (screenWidth / 1080) + (myHead.getRegionWidth() / 2) * (screenWidth / 1080), 45 * (screenHeight / 1920) + 80 * (screenHeight / 1920));//y=125

            //time display
            float timeToDisplay = 180 - runTime;
            int min = (int) (timeToDisplay / 60);
            int sec = (int) (timeToDisplay % 60);
            String format = "%02d:%02d";
            AssetLoader.font.draw(batcher, String.format(format,min,sec)  , screenWidth * (6f / 14f), 120f * screenHeight / 1920f);
            batcher.draw(clock.getKeyFrame(runTime), screenWidth * (11f / 24f), 45f * screenHeight / 1920f, 62f * (screenWidth / 1080f), 63f * (screenHeight / 1920f));

            //opponent information display
            batcher.draw(oppoHead, screenWidth - 40 * (screenWidth / 1080) - oppoHead.getRegionWidth() * (screenWidth / 1080), 65 * (screenHeight / 1920), 0, 0, myHead.getRegionWidth() * (screenWidth / 1080), myHead.getRegionHeight() * (screenHeight / 1920), 1, 1, 0);

            batcher.draw(food, screenWidth - 40 * (screenWidth / 1080) - oppoHead.getRegionWidth() * (screenWidth / 1080) - 10 * (screenWidth / 1080) - (myHead.getRegionWidth() / 2) * (screenWidth / 1080), 50 * (screenHeight / 1920f), (myHead.getRegionWidth() / 2) * (screenWidth / 1080f), (myHead.getRegionHeight() / 2) * (screenHeight / 1920f));
            batcher.draw(speed, screenWidth - 40 * (screenWidth / 1080) - oppoHead.getRegionWidth() * (screenWidth / 1080) - 10 * (screenWidth / 1080) - (myHead.getRegionWidth() / 2) * (screenWidth / 1080), 130 * (screenHeight / 1920f), (myHead.getRegionWidth() / 2) * (screenWidth / 1080f), (myHead.getRegionHeight() / 2) * (screenHeight / 1920f));
            // strength display
            AssetLoader.font.draw(batcher, "" + actionResolver.requestOpponentStrength(), screenWidth - 139 * (screenWidth / 1080f) - myHead.getRegionWidth() * (screenWidth / 1080f) - (myHead.getRegionWidth() / 2) * (screenWidth / 1080f), 40 * (screenHeight / 1920f));
            //speed display
            AssetLoader.font.draw(batcher, "" + actionResolver.requestOpponentSpeed(), screenWidth - 169 * (screenWidth / 1080f) - myHead.getRegionWidth() * (screenWidth / 1080f) - (myHead.getRegionWidth() / 2) * (screenWidth / 1080f), 125 * (screenHeight / 1920f));
            if (oppo_pos.dst(myMonster.getMyPosition()) <= 150){
                shadow.draw(batcher,"Enemy approaching!", screenWidth/2f-shadow.getBounds("Enemy approaching!").width/2f -1+shake
                        , screenHeight/2f-shadow.getBounds("Enemy approaching!").height/2f - 200-1);
                font.draw(batcher,"Enemy approaching!", screenWidth/2f-font.getBounds("Enemy approaching!").width/2f+shake
                        , screenHeight/2f-font.getBounds("Enemy approaching!").height/2f - 200);
                shake = (shake >=0.2f)?shake*0.5f:5f;
            }
            batcher.end();
            stage.draw();
        }
    }

    public void drawTugOfWar(float runTime){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        int initial = 0;

        int myTapCount = actionResolver.requestMyTapCount();
        int oppoTapCount = actionResolver.requestOppoTapCount();

        if ((opponentStrength + myStrength) != 0)
            initial = (int)((opponentStrength - myStrength)/(float)(opponentStrength + myStrength)*10);
        int diff = -myTapCount + oppoTapCount + initial;
        ratio = (diff+20f)/40f;
        batcher.begin();
        batcher.setProjectionMatrix(cam2.combined);

        if (actionResolver.haveYouWin() || ratio >=1){
            actionResolver.iLose();
            batcher.draw(AssetLoader.losebg,0,0,screenWidth,screenHeight);
            shadow.draw(batcher,"YOU LOSE!",screenWidth/2-shadow.getBounds("YOU LOSE!").width/2-1,screenHeight/2.5f - AssetLoader.victorMonster.getRegionHeight()/2-1);
            font.draw(batcher,"YOU LOSE!",screenWidth/2-font.getBounds("YOU LOSE!").width/2,screenHeight/2.5f - AssetLoader.victorMonster.getRegionHeight()/2);
            handler.setMode(1);
        }
        else if (actionResolver.haveYouLose() || ratio <=0){
            actionResolver.iWin();
            handler.setMode(1);
            batcher.draw(victorybg.getKeyFrame(runTime),0,0,screenWidth,screenHeight);
            batcher.draw(AssetLoader.victorMonster,screenWidth/2-AssetLoader.victorMonster.getRegionWidth()/2,screenHeight/2 - AssetLoader.victorMonster.getRegionHeight()/2);
            shadow.draw(batcher,"YOU WIN!",screenWidth/2-shadow.getBounds("YOU WIN!").width/2-1,screenHeight/2.5f - AssetLoader.victorMonster.getRegionHeight()/2-1);
            font.draw(batcher,"YOU WIN!",screenWidth/2-font.getBounds("YOU WIN!").width/2,screenHeight/2.5f - AssetLoader.victorMonster.getRegionHeight()/2);
        }
        else{
            TextureRegion me = vsMe[myTapCount%2];
            TextureRegion op = vsOppo[oppoTapCount%2];
            batcher.enableBlending();
            batcher.draw(pic, 0, (screenHeight*ratio)-(screenHeight*2f)/2, screenWidth, screenHeight*2f);
            batcher.draw(op,0,(screenHeight*ratio)-(screenHeight*2f)/2 + screenHeight-op.getRegionHeight()/op.getRegionWidth()*screenWidth*1.2f
                    ,screenWidth,op.getRegionHeight()/op.getRegionWidth()*screenWidth);
            batcher.draw(me,0,(screenHeight*ratio)-(screenHeight*2f)/2 + screenHeight+me.getRegionHeight()/me.getRegionWidth()*screenWidth*.2f
                    ,screenWidth,me.getRegionHeight()/me.getRegionWidth()*screenWidth);
        }
        batcher.end();
    }

    public void drawRound1Waiting(float runTime){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        batcher.enableBlending();
        batcher.setProjectionMatrix(cam2.combined);
        batcher.draw(menuBg,0,0,screenWidth,screenHeight);
        shadow.draw(batcher, "ROUND 1", screenWidth/2f-font.getBounds("ROUND 1").width/2-1, screenHeight/2.5f-1);
        font.draw(batcher, "ROUND 1", screenWidth /2f-font.getBounds("ROUND 1").width/2, screenHeight/2.5f);
        if (runTime <=1.5){
            AssetLoader.shadow.draw(batcher, "" + (int)(4-runTime*2), screenWidth/2f-font.getBounds("" + (int)(4-runTime)).width/2-1, screenHeight/2f-font.getBounds("" + (int)(4-runTime)).height/2-1);
            AssetLoader.font.draw(batcher, "" + (int)(4-runTime*2), screenWidth/2f-font.getBounds("" + (int)(4-runTime)).width/2, screenHeight/2f-font.getBounds("" + (int)(4-runTime)).height/2);
        }
        else{
            AssetLoader.shadow.draw(batcher, "START", screenWidth/2f-font.getBounds("START").width/2-1, screenHeight/2f-font.getBounds("START").height/2-1);
            AssetLoader.font.draw(batcher, "START", screenWidth/2f-font.getBounds("START").width/2, screenHeight/2f-font.getBounds("START").height/2);
        }
        batcher.end();
    }

    public void drawRound2Waiting(float runTime){
        music.stop();
        opponentStrength =actionResolver.requestOpponentStrength();
        myStrength = myMonster.getStrength();
        Gdx.input.setInputProcessor(handler);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        batcher.enableBlending();
        batcher.setProjectionMatrix(cam2.combined);
        batcher.draw(menuBg,0,0,screenWidth,screenHeight);
        shadow.draw(batcher, "ROUND 2", screenWidth/2f-font.getBounds("ROUND 2").width/2-1, screenHeight/2.5f-1);
        font.draw(batcher, "ROUND 2", screenWidth /2f-font.getBounds("ROUND 2").width/2, screenHeight/2.5f);
        if (runTime <= round2Start+1.5){
            AssetLoader.shadow.draw(batcher, "" + (int)(4-(runTime-round2Start)*2), screenWidth/2f-font.getBounds("" + (int)(4-runTime)).width/2-1, screenHeight/2f-font.getBounds("" + (int)(4-runTime)).height/2-1);
            AssetLoader.font.draw(batcher, "" + (int)(4-(runTime-round2Start)*2), screenWidth/2f-font.getBounds("" + (int)(4-runTime)).width/2, screenHeight/2f-font.getBounds("" + (int)(4-runTime)).height/2);
        }
        else{
            AssetLoader.shadow.draw(batcher, "START", screenWidth/2f-font.getBounds("START").width/2-1, screenHeight/2f-font.getBounds("START").height/2-1);
            AssetLoader.font.draw(batcher, "START", screenWidth/2f-font.getBounds("START").width/2, screenHeight/2f-font.getBounds("START").height/2);
            shouldStartRound2 = false;
        }
        batcher.end();
    }


}
