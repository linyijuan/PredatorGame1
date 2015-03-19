package sutd.istd.groupzero.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

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
    public  Texture gridBg;
    public  TextureRegion grid;
    public  TextureRegion[] directionSet;
    public  Animation[] animationSet;
    public  TextureRegion monsterUp,monsterDown, monsterLeft,monsterRight;
    public  Animation upAnimation,downaAnimation, leftaAnimation,rightaAnimation;
    private float scaleX, scaleY;
    private ShapeRenderer shapeRenderer;

    private Texture arrow;
    private Sprite spriteArrow;

    public GameRenderer(GameWorld world, float screenWidth, float screenHeight){
        myWorld = world;
        myMap = myWorld.getMap();
        myMonster = myMap.getMonster();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        //help from marcus
//        scaleX = AssetLoader.maskLayer.getWidth()/screenWidth;
//        scaleY = AssetLoader.maskLayer.getHeight()/screenHeight;

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

        // testing arrow drawing
        arrow = new Texture(Gdx.files.internal("data/tango-left-arrow-red.png"));
        spriteArrow = new Sprite(arrow);



    }
    public void render(float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        batcher.enableBlending();
        Direction d = myMonster.getDirection();


        Vector2 camPost = new Vector2(myMonster.getMyPosition().x + myMonster.getBoundWidth()/2, myMonster.getMyPosition().y + myMonster.getBoundHeight()/2);
        cam.position.set(camPost, 0);
        cam.update();
        batcher.setProjectionMatrix(cam.combined);
        batcher.draw(gridBg, 0, 0);
        for(Tree tree : myMap.getTreeList()){
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
                batcher.draw(directionSet[myMonster.getDirection().getKeycode()%4], myMonster.getMyPosition().x, myMonster.getMyPosition().y);
                break;
        }

        // Drawing of arrow
        spriteArrow.setRotation(myMonster.getAngle());
        spriteArrow.setBounds(myMonster.getArrowPostX(), myMonster.getArrowPostY(), myMonster.getBoundWidth(), myMonster.getBoundWidth());
        spriteArrow.setOriginCenter();
        spriteArrow.draw(batcher);

        //help from marcus
//        batcher.draw(new TextureRegion(AssetLoader.maskLayer),0,0,0,0, AssetLoader.maskLayer.getWidth(), AssetLoader.maskLayer.getHeight(),scaleX,scaleY, 0);
//        batcher.disableBlending();
        batcher.end();




//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.setProjectionMatrix(cam.combined);
////        shapeRenderer.rect(myMonster.getBound().x,myMonster.getBound().y,myMonster.getBound().width,myMonster.getBound().height);
//        shapeRenderer.end();
    }


}
