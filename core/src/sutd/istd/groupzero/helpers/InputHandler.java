package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
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
        // Splitting the screen to 4 parts for user to tap to control character
//        if ((screenX/screenY) >= layer1){
//            // Belong to the sector top/right of the screen
////            Gdx.app.log("touchDown", "top/right of screen");
//            if (((screenY-screenHeight)/screenX) <= layer2){
//            	map.onTap(1);
//                Gdx.app.log("touchDown", "top of screen");
//            }else{
//            	map.onTap(2);
//                Gdx.app.log("touchDown", "right of screen");
//            }
//        }else{
//            // Belong to the bottom/left of the screen
////            Gdx.app.log("touchDown", "bottom/left of screen");
//            if (((screenY-screenHeight)/screenX) >= layer2){
//            	map.onTap(3);
//                Gdx.app.log("touchDown", "bottom of screen");
//            }else{
//                map.onTap(0);
//                Gdx.app.log("touchDown", "left of screen");
//            }
//        }
        monster.move(screenX, screenY);

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
