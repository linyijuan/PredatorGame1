package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.Monster.Direction;
import sutd.istd.groupzero.gameworld.GameWorld;
import sutd.istd.groupzero.screens.GameScreen;

public class InputHandler implements InputProcessor{
	private Map map;
	private GameWorld myWorld;
	private Monster monster;
    private float screenWidth;
    private float screenHeight;
    private float layer1;
    private float layer2;
    private Vector2 moveUp, moveRight, moveLeft, moveDown;
    
    private Vector2 center;

    //                  Game Screen
    //
    //      ==========================================
    //      = *                                    *  =
    //      =     *            UP             *       =
    //      =         *                   *           =
    //      =              *          *               =
    //      =    LEFT            *      RIGHT         =        GameHeight
    //      =              *          *               =
    //      =         *                   *           =
    //      =    *           DOWN             *       =
    //      = *                                    *  =
    //      ==========================================
    //                       GameWidth
    //
    //  Should we have a reference to the character position vector in this class
    //  so that we can adjust the position vector of the characer directly from here?
    //
    //
    //

    
	public InputHandler(GameWorld world, float screenHeight,float screenWidth){
		myWorld = world;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        layer1 = screenWidth/screenHeight;
        layer2 = -screenHeight/screenWidth;
		map = myWorld.getMap();
		monster = map.getMonster();
		moveUp= new Vector2(0,60);
		moveDown = new Vector2(0,-60);
		moveLeft = new Vector2(60,0);
		moveRight = new Vector2(-60,0);
		
		center = new Vector2(screenWidth/2, screenHeight/2);
		
	}
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}
            
            
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
