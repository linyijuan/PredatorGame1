package sutd.istd.groupzero.helpers;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameworld.GameRenderer;
import sutd.istd.groupzero.gameworld.GameWorld;

import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor{
	private Map map;
	private GameWorld myWorld;
	
	public InputHandler(GameWorld world){
		myWorld = world;
		map = myWorld.getMap();
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
		map.onClick();
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
