package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor{
   private ActionResolver actionResolver;
    private int mode;
	public InputHandler(ActionResolver actionResolver,int mode){
		this.actionResolver = actionResolver;
        this.mode = mode;
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
        if (mode == 0){
            actionResolver.broadcastMyTapping();
        }
        else if (mode == 1){

        }

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
