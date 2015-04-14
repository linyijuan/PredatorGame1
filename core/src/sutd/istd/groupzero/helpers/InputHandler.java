package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.InputProcessor;

/* Screen interaction handler during tug of war */
public class InputHandler implements InputProcessor{
   private ActionResolver actionResolver;
    private int mode;

    /**
     * Initialization of variables within GameRenderer Class
     * @param actionResolver handles communication between gps and core project
     * @param mode different game status during tug of war
     */
	public InputHandler(ActionResolver actionResolver,int mode){
		this.actionResolver = actionResolver;
        this.mode = mode;
	}

    public void setMode(int i ){
        mode = i;
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

    // when user touch up from screen after touch down
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // when still in competing mode of tug of war
        if (mode == 0){
            actionResolver.broadcastMyTapping();
        }
        // when tug of war ends
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
