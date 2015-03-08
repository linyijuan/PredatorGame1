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
		moveUp= new Vector2(10,0);
		moveDown = new Vector2(-10,0);
		moveLeft = new Vector2(0,-20);
		moveRight = new Vector2(0,20);
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
		//use user's position to determine the direction to go
		//call method inside Map to determine the movement
        // Debugging purposes
        Gdx.app.log("touchDown", "User touched down at X: " + screenX + " Y: " + screenY);
        
        
        if ((screenX/screenY) >= layer1){
            if (((screenY-screenHeight)/screenX) <= layer2){
            	monster.setDirection(Direction.TOP);
                Gdx.app.log("touchDown", "top of screen");
            }else{
            	
            	monster.setDirection(Direction.RIGHT);

                Gdx.app.log("touchDown", "right of screen");
            }
        }else{
            if (((screenY-screenHeight)/screenX) >= layer2){
            	monster.setDirection(Direction.BOTTOM);

                Gdx.app.log("touchDown", "bottom of screen");
            }else{
            	monster.setDirection(Direction.LEFT);
                Gdx.app.log("touchDown", "left of screen");
            }
        }

		return false;
	}
            
            
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Debugging purposes
//        Gdx.app.log("touchUp", "User touched up at X: " + screenX + " Y: " + screenY);
//        Gdx.app.log("touchUp", "Button: " + button);
        return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Debugging purposes
//        Gdx.app.log("KeyDown", "User touched dragged at X: " + screenX + " Y: " + screenY);
        //
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
