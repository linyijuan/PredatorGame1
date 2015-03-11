package sutd.istd.groupzero.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.Monster.Direction;
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
	private ShapeRenderer shapeRenderer;
	
	public GameRenderer(GameWorld world, float screenWidth, float screenHeight){
		myWorld = world;
		myMap = myWorld.getMap();
		myMonster = myMap.getMonster();
		this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
		
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
	public void render(float runTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	//Gdx.app.log("Screen Height", "Screen Width :" + screenWidth+ "  ScreenHeight :"+ screenHeight);
        batcher.begin();
        batcher.enableBlending();
        Direction d = myMonster.getDirection();
        Vector2 mapPos = myMonster.getMapPosition();
        
      
        
        switch (d) {
		case TOP:
			batcher.draw(gridBg, mapPos.x, mapPos.y);
        	
        	batcher.draw(animationSet[1].getKeyFrame(runTime),90,180);

			break;
		case LEFT:

			batcher.draw(gridBg, mapPos.x, mapPos.y);

        	
        	batcher.draw(animationSet[0].getKeyFrame(runTime),90, 180);        	

			
			break;
		case RIGHT:
			batcher.draw(gridBg, mapPos.x, mapPos.y);
        	
        	batcher.draw(animationSet[2].getKeyFrame(runTime),90, 180);        	

			
			break;
		case BOTTOM:
			batcher.draw(gridBg, mapPos.x, mapPos.y);
        	batcher.draw(animationSet[3].getKeyFrame(runTime),90, 180);   
			break;

		default:
			batcher.draw(gridBg, mapPos.x, mapPos.y);
        	batcher.draw(directionSet[myMonster.getDirection().getKeycode()-5],90,180);

			break;
		}
        

        for(sutd.istd.groupzero.gameobjects.Tree tree : myMap.getTreeList())
        {
//        	Gdx.app.log("tree count" , myMap.getTreeList().size()+"");

        	batcher.draw(new TextureRegion(AssetLoader.tree), tree.getPosition().x,tree.getPosition().y, 0, 0, AssetLoader.tree.getWidth(), AssetLoader.tree.getHeight(), 1f, 1f, -180f);
        }
       
    
        batcher.disableBlending();
        batcher.end();
    }


}
