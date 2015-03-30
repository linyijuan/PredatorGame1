package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import sutd.istd.groupzero.gameobjects.Food;
import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.Monster.Direction;
import sutd.istd.groupzero.gameobjects.PowerUps;
import sutd.istd.groupzero.gameobjects.Tree;
import sutd.istd.groupzero.gameworld.GameWorld;

public class TouchPad {
	private Stage stage;
	private com.badlogic.gdx.scenes.scene2d.ui.Touchpad touchpad;
	public com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle touchpadStyle;
	private Drawable touchBackground;
	private Drawable touchKnob;
	public Skin touchpadSkin;
    private ActionResolver actionResolver;
    private Vector2 touchpadcenter;
	private Vector2 moveUp, moveRight, moveLeft, moveDown;
	private boolean touchUp = false;
	private Monster monster;
	private float x, y, width, height;
    private GameWorld gameworld;
    private Map map;


    private Timer speedTimer;//WIN ___ Timer

	public TouchPad(float x, float y, float width, float height, GameWorld gameWorld,ActionResolver actionResolver) {
		touchpadSkin = new Skin();
		touchpadSkin.add("touchKnob", new Texture(Gdx.files.internal("data/touchKnob1.png")));
		touchpadSkin.add("touchBackground", new Texture(Gdx.files.internal("data/touchBackground.png")));
        this.actionResolver = actionResolver;
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

        touchpadcenter = new Vector2(width/2, height/2);
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

                        moveUp = new Vector2(0,-( monster.getSpeed() *0.003f));
                        moveDown = new Vector2(0, monster.getSpeed() * 0.003f);
                        moveLeft = new Vector2( -(monster.getSpeed() * 0.003f), 0);
                        moveRight = new Vector2(monster.getSpeed() * 0.003f, 0);

                        while(touchUp == false){
                            float x = touchpad.getKnobX();
                            float y = touchpad.getKnobY();
                            float angle = getAngle(x, y);
                            if(angle>=45 && angle <=135){
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
                            actionResolver.broadcastMyStatus(monster.getMyPosition(),monster.getDirection());
                            for (Food f: map.getFoodList()){
                                if (Intersector.overlaps(monster.getBound(), f.getBound())){
                                    actionResolver.eatFood(f);
                                    monster.obtainFood();
                                    break;
                                }
                            }

                            for (PowerUps p: map.getPowerUpsList()){
                                if (Intersector.overlaps(monster.getBound(), p.getBound())){
                                    if (p.getKind().equals("s")){
                                        //WIN ___ Timer
                                        speedTimer = new Timer();
                                        monster.addSpeed(0.2f);
                                        speedTimer.scheduleTask(new Timer.Task() {
                                            @Override
                                            public void run() {
                                                monster.addSpeed(-0.2f);
                                            }
                                        },6);

                                    }
                                    else{
                                        speedTimer = new Timer();
                                        monster.setVisibility(1.5f);
                                        speedTimer.scheduleTask(new Timer.Task() {
                                            @Override
                                            public void run() {
                                                monster.setVisibility(1f);
                                            }
                                        },6);
                                        speedTimer.start();

                                    }
                                    actionResolver.obtainPowerUp(p);
                                    break;
                                }
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
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchUp = true;

                float xx = touchpad.getKnobX();
                float yy = touchpad.getKnobY();
                float angle = getAngle(xx, yy);
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


