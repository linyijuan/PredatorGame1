package sutd.istd.groupzero.helpers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.Monster.Direction;
import sutd.istd.groupzero.gameworld.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TouchPad {
	private Stage stage;
	private com.badlogic.gdx.scenes.scene2d.ui.Touchpad touchpad;
	public com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle touchpadStyle;
	private Drawable touchBackground;
	private Drawable touchKnob;
	public Skin touchpadSkin;
	private Vector2 moveUp, moveRight, moveLeft, moveDown;
	private boolean touchUp = false;
	private Monster monster;
	private float x, y, width, height;

	public TouchPad(float x, float y, float width, float height, GameWorld gameWorld) {
		touchpadSkin = new Skin();
		touchpadSkin.add("touchBackground", new Texture(Gdx.files.internal("data/touchBackground.png")));
		touchpadSkin.add("touchKnob", new Texture(Gdx.files.internal("data/touchKnob.png")));
		touchpadStyle = new com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle();
		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;
		touchpad = new com.badlogic.gdx.scenes.scene2d.ui.Touchpad(10, touchpadStyle);
		touchpad.setBounds(x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.monster = gameWorld.getMap().getMonster();
		moveUp = new Vector2(0, -0.001f);
		moveDown = new Vector2(0, 0.001f);
		moveLeft = new Vector2(-0.001f, 0);
		moveRight = new Vector2(0.001f, 0);
	}


	public Stage createTouchPad() {
		stage = new Stage();
		stage.addActor(touchpad);
		touchpad.addListener(new DragListener() {public void touchDragged(InputEvent event, float x, float y, int pointer) {}});
		touchpad.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchUp = false;
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {

                    @Override
                    public void run() {
                        while(touchUp == false){
                            float x = touchpad.getKnobX();
                            float y = touchpad.getKnobY();
                            if ((x >= 65 && x < 165) && (y >= 160)) {
                                monster.setDirection(Direction.TOP);
                                monster.setMyPosition(monster.getMyPosition().add(moveUp));
                            } else if ((x >= 65 && x <= 165) && (y <= 50)) {
                                monster.setDirection(Direction.BOTTOM);
                                monster.setMyPosition(monster.getMyPosition().add(moveDown));
                            } else if ((x < 100) && (y <= 160)) {
                                monster.setDirection(Direction.LEFT);
                                monster.setMyPosition(monster.getMyPosition().add(moveLeft));
                            } else if ((x > 100) && (y > 65 && y < 165)) {
                                monster.setDirection(Direction.RIGHT);
                                monster.setMyPosition(monster.getMyPosition().add(moveRight));
                            }
                        }
                        Direction d = monster.getDirection();
                        switch (d) {
                            case TOP:
                                monster.setDirection(Direction.STATIONARY_TOP);
                                break;
                            case LEFT:
                                monster.setDirection(Direction.STATIONARY_LEFT);
                                break;
                            case RIGHT:
                                monster.setDirection(Direction.STATIONARY_RIGHT);
                                break;
                            case BOTTOM:
                                monster.setDirection(Direction.STATIONARY_BOTTOM);
                                break;
                            default:
                                break;
                        }
                    }
                });
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {touchUp = true;}
        });
		return stage;
	}


}


