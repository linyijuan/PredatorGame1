package sutd.istd.groupzero.gameworld;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.helpers.*;

public class GameRenderer {
	private float screenWidth;
    private float screenHeight;
	private GameWorld myWorld;
	private Map myMap;
	private Monster myMonster;
	private OrthographicCamera cam;
	private SpriteBatch batcher;
	public static TextureRegion gridBg;
	public static TextureRegion[] directionSet;
	public static Animation[] animationSet;
	public static TextureRegion monsterUp,monsterDown, monsterLeft,monsterRight;
	public static Animation upAnimation,downaAnimation, leftaAnimation,rightaAnimation;	
	private ShapeRenderer shapeRenderer;
	
	public GameRenderer(GameWorld world, float screenWidth, float screenHeight){
		myWorld = world;
		myMap = myWorld.getMap();
		myMonster = myMap.getMonster();
		this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, 204);
		
		batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
		
		shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        
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
	public void render(float runTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batcher.begin();
        batcher.enableBlending();
        int direction = myMonster.getMovement();
        if (direction < 4)
        	batcher.draw(animationSet[direction].getKeyFrame(runTime),myMonster.getX(),myMonster.getY());        	
        else 
        	batcher.draw(directionSet[direction-4],myMonster.getX(),myMonster.getY());
		
        batcher.end();
    }


}
