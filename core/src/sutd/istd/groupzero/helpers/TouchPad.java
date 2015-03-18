package sutd.istd.groupzero.helpers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import sutd.istd.groupzero.gameobjects.Food;
import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.Monster.Direction;
import sutd.istd.groupzero.gameobjects.PowerUps;
import sutd.istd.groupzero.gameobjects.Tree;
import sutd.istd.groupzero.gameworld.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
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

    private Vector2 touchpadcenter;
	private Vector2 moveUp, moveRight, moveLeft, moveDown;
	private boolean touchUp = false;
	private Monster monster;
	private float x, y, width, height;
    private GameWorld gameworld;
    private Map map;

	public TouchPad(float x, float y, float width, float height, GameWorld gameWorld) {
		touchpadSkin = new Skin();
		touchpadSkin.add("touchKnob", new Texture(Gdx.files.internal("data/touchKnob1.png")));
		touchpadSkin.add("touchBackground", new Texture(Gdx.files.internal("data/touchBackground.png")));

		touchpadStyle = new com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle();
		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;
		touchpad = new com.badlogic.gdx.scenes.scene2d.ui.Touchpad(15, touchpadStyle);
		touchpad.setBounds(x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.monster = gameWorld.getMap().getMonster();
        this.gameworld = gameWorld;
        this.map = gameWorld.getMap();
		moveUp = new Vector2(0, -0.003f);
		moveDown = new Vector2(0, 0.003f);
		moveLeft = new Vector2(-0.003f, 0);
		moveRight = new Vector2(0.003f, 0);
        touchpadcenter = new Vector2(width/2, height/2);
	}


	public Stage createTouchPad() {
		stage = new Stage();
		stage.addActor(touchpad);
		touchpad.addListener(new DragListener() {public void touchDragged(InputEvent event, float x, float y, int pointer) {}});
		touchpad.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchUp = false;
//                Gdx.app.log("x y",  touchpad.getKnobX() + " " + touchpad.getKnobY());

                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {

                    @Override
                    public void run() {
                        while(touchUp == false){
                            float x = touchpad.getKnobX();
                            float y = touchpad.getKnobY();
                            float angle = getAngle(x, y);
//                            Gdx.app.log("angle", angle + "");

                            if(angle>=45 && angle <=135)
                            {
                                monster.setDirection(Direction.TOP);
                                monster.setMyPosition(monster.getMyPosition().add(moveUp));
                                for (Tree t: map.getTreeList() ){
                                    if (Intersector.overlaps(monster.getBound(), t.getBound())){
                                        monster.setMyPosition(monster.getMyPosition().add(moveDown));
                                        break;
                                    }
                                }
                            } else if (angle <= -45 && angle >= -135) {
                                monster.setDirection(Direction.BOTTOM);
                                monster.setMyPosition(monster.getMyPosition().add(moveDown));
                                for (Tree t: map.getTreeList() ){
                                    if (Intersector.overlaps(monster.getBound(), t.getBound())){
                                        monster.setMyPosition(monster.getMyPosition().add(moveUp));
                                        break;
                                    }
                                }
                            } else if (angle>=135 || angle <=-135) {
                                monster.setDirection(Direction.LEFT);

                                monster.setMyPosition(monster.getMyPosition().add(moveLeft));
                                for (Tree t: map.getTreeList() ){
                                    if (Intersector.overlaps(monster.getBound(), t.getBound())){
                                        monster.setMyPosition(monster.getMyPosition().add(moveRight));
                                        break;
                                    }
                                }
                            } else if (angle<=45 && angle >=-45 && angle != 0) {
                                monster.setDirection(Direction.RIGHT);
                                monster.setMyPosition(monster.getMyPosition().add(moveRight));
                                for (Tree t: map.getTreeList() ){
                                    if (Intersector.overlaps(monster.getBound(), t.getBound())){
                                        monster.setMyPosition(monster.getMyPosition().add(moveLeft));
                                        break;
                                    }
                                }
                            }
                            for (Food f: map.getFoodList()){
                                if (Intersector.overlaps(monster.getBound(), f.getBound())){
                                    map.regenFood(f);
                                    break;
                                }
                            }

                            for (PowerUps p: map.getPowerUpsList()){
                                if (Intersector.overlaps(monster.getBound(), p.getBound())){
                                    map.regenPU(p);
                                    break;
                                }
                            }


                        }

                        Direction d = monster.getDirection();
                        switch (d) {
                            case TOP:
                                Gdx.app.log("setdirection", "TOP");
                                monster.setDirection(Direction.STATIONARY_TOP);
                                break;
                            case LEFT:
                                Gdx.app.log("setdirection", "LEFT");

                                monster.setDirection(Direction.STATIONARY_LEFT);
                                break;
                            case RIGHT:
                                Gdx.app.log("setdirection", "RIGHT");

                                monster.setDirection(Direction.STATIONARY_RIGHT);
                                break;
                            case BOTTOM:
                                Gdx.app.log("setdirection", "BOTTOM");

                                monster.setDirection(Direction.STATIONARY_BOTTOM);
                                break;
                            default:
                                break;
                        }

                    }
                });
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchUp = true;

                float xx = touchpad.getKnobX();
                float yy = touchpad.getKnobY();
                float angle = getAngle(xx, yy);
                Gdx.app.log("angle", angle + "");
                }
        });

		return stage;
	}

	
	public float getAngle(float x, float y){
		float temp = (float)Math.atan2(y-touchpadcenter.y, x-touchpadcenter.x);
		temp = (float) (temp * 57.2957795);
		return 	temp;
	}

}


