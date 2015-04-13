package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import sutd.istd.groupzero.gameobjects.Food;
import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.Monster.Direction;
import sutd.istd.groupzero.gameobjects.PowerUps;
import sutd.istd.groupzero.gameobjects.Tree;

public class TouchPad {
	private Stage stage;
	private com.badlogic.gdx.scenes.scene2d.ui.Touchpad touchpad;
	public com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle touchpadStyle;
	private Drawable touchBackground,SkillBackground;
	private Drawable touchKnob;
	public Skin touchpadSkin,SkillButton;
    private ActionResolver actionResolver;
    private Vector2 touchpadcenter;
	private Vector2 moveUp, moveRight, moveLeft, moveDown;
	private boolean touchUp = false;
	private Monster monster;
//    private GameWorld gameworld;
    private Map map;
    private Game game;
    private List<Food> foodSynchroList;
    private List<PowerUps> puSynchroList;

    private Sound movement;
    private Sound sboost;
    private Sound vboost;
    private Sound eating;


    public static Button skillButton;

    private Timer speedTimer;//WIN ___ Timer

    private float screenWidth;
    private float screenHeight;

    /**
     * TouchPad constructor
     * @param x x position
     * @param y y position
     * @param width width of the TouchPad
     * @param height height of the TouchPad
     * @param map  map object associated with the game
     * @param actionResolver handles google play services
     * @param game game object
     */
	public TouchPad(float x, float y, float width, float height, Map map,ActionResolver actionResolver,Game game) {
		screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        touchpadSkin = new Skin();
		SkillButton = new Skin();
        touchpadSkin.add("touchKnob", new Texture(Gdx.files.internal("data/touchKnob1.png")));
		touchpadSkin.add("touchBackground", new Texture(Gdx.files.internal("data/touchBackground.png")));
        SkillButton.add("skillButton",new Texture(Gdx.files.internal("data/saiyan.png")));
        SkillBackground = SkillButton.getDrawable("skillButton");
        this.actionResolver = actionResolver;
		touchpadStyle = new com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle();
		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;

		touchpad = new com.badlogic.gdx.scenes.scene2d.ui.Touchpad(15, touchpadStyle);
        touchpad.setBounds(x, y, width, height);

		this.monster = map.getMonster();
        this.map = map;
        touchpadcenter = new Vector2(width/2, height/2);
        this.game = game;

        movement = AssetLoader.movement;
        sboost = Gdx.audio.newSound(Gdx.files.internal("data/boost.wav"));
        vboost = Gdx.audio.newSound(Gdx.files.internal("data/visible.wav"));
        eating = Gdx.audio.newSound(Gdx.files.internal("data/eating.mp3"));
        skillButton = new Button(SkillBackground, SkillBackground);
        skillButton.setBounds(40*(screenWidth/1080), 50*(screenHeight/1920), screenWidth/5, screenWidth/5);
	}

    /**
     *
     * @return Stage object
     */
	public Stage createTouchPad() {
		stage = new Stage();
		stage.addActor(touchpad);
        stage.addActor(skillButton);
        //Predator mode activate button listener.
        skillButton.addListener(new InputListener(){
            Timer predatorMode;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Button", "Button pressed");
                if (monster.getStrength() >= 5) {
                    return true;
                }else{
                    Gdx.app.log("Predator mode", "Cannot activate");
                    return false;
                }
            }

            @Override
            /**
             * called only if touchdown is true.
             * Predator mode activated. Temporary speed and visibility increase at the cost of 5 strength.
             */
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                monster.addSpeed(1.5f);
                monster.setSaiyanMode(true);
                predatorMode = new Timer();
                predatorMode.scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        monster.addSpeed(-1.5f);
                        monster.setVisibility(1.0f);
                        monster.setStrength(monster.getStrength() - 5);
                        monster.setSaiyanMode(false);
                    }
                }, 10f);
                predatorMode.start();
                skillButton.setDisabled(true);
            }



        });
        //TouchPad listener to handle monster movement.
		touchpad.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchUp = false;
                Executor executor = Executors.newSingleThreadExecutor();
                //a thread to handle TouchKnob dragging
                //a new thread is created due to the limitations of touchpad ui object.
                executor.execute(new Runnable() {

                    @Override
                    public void run() {
                        moveUp = new Vector2(0,-( monster.getSpeed() *0.003f));
                        moveDown = new Vector2(0, monster.getSpeed() * 0.003f);
                        moveLeft = new Vector2( -(monster.getSpeed() * 0.003f), 0);
                        moveRight = new Vector2(monster.getSpeed() * 0.003f, 0);

                        while(touchUp == false){
                            moveUp.set(0,-( monster.getSpeed() *0.003f));
                            moveDown.set(0, monster.getSpeed() * 0.003f);
                            moveLeft.set(-(monster.getSpeed() * 0.003f), 0);
                            moveRight.set(monster.getSpeed() * 0.003f, 0);
                            foodSynchroList = actionResolver.requestFoods();//foodList from the action resolver.
                            puSynchroList = actionResolver.requestPUs();//powerUpList from the action resolver.
                            float x = touchpad.getKnobX();
                            float y = touchpad.getKnobY();
                            float angle = getAngle(x, y);
                            if(angle>=45 && angle <=135){
                                monster.setDirection(Direction.TOP);
                                monster.setMyPosition(monster.getMyPosition().add(moveUp));
                                for (Tree t: actionResolver.requestTrees() ){
                                    if (Intersector.overlaps(t.getWalkingBound(), monster.getBound())){
                                        monster.setMyPosition(monster.getMyPosition().add(moveDown));
                                        break;
                                    }
                                }
                            } else if (angle <= -45 && angle >= -135) {
                                monster.setDirection(Direction.BOTTOM);
                                monster.setMyPosition(monster.getMyPosition().add(moveDown));
                                for (Tree t: actionResolver.requestTrees() ){
                                    if (Intersector.overlaps(t.getWalkingBound(), monster.getBound())){
                                        monster.setMyPosition(monster.getMyPosition().add(moveUp));
                                        break;
                                    }
                                }
                            } else if (angle>=135 || angle <=-135) {
                                monster.setDirection(Direction.LEFT);

                                monster.setMyPosition(monster.getMyPosition().add(moveLeft));
                                for (Tree t: actionResolver.requestTrees() ){
                                    if (Intersector.overlaps(t.getWalkingBound(), monster.getBound())){
                                        monster.setMyPosition(monster.getMyPosition().add(moveRight));
                                        break;
                                    }
                                }
                            } else if (angle<=45 && angle >=-45 && angle != 0) {
                                monster.setDirection(Direction.RIGHT);
                                monster.setMyPosition(monster.getMyPosition().add(moveRight));
                                for (Tree t: actionResolver.requestTrees() ){
                                    if (Intersector.overlaps(t.getWalkingBound(), monster.getBound())){
                                        monster.setMyPosition(monster.getMyPosition().add(moveLeft));
                                        break;
                                    }
                                }
                            }

                            //foodList synchronziation for concurrent modification
                            synchronized (foodSynchroList) {
                                Food fcopy = null;
                                for (Food f : foodSynchroList) {
                                    if (Intersector.overlaps(monster.getBound(), f.getBound())) {
                                        eating.play();
                                        fcopy = f;
                                        actionResolver.eatFood(f);
                                        monster.obtainFood();
                                        actionResolver.broadcastMyStrength(monster.getStrength());
                                        break;
                                    }
                                }
                                if(fcopy != null)
                                    actionResolver.eatFood(fcopy);
                            }

                            //powerUpList synchronziation for concurrent modification
                            synchronized (puSynchroList) {
                                PowerUps pcopy = null;
                                for (PowerUps p : puSynchroList) {
                                    if (Intersector.overlaps(monster.getBound(), p.getBound())) {
                                        pcopy = p;
                                        if (p.getKind().equals("s")) {
                                            sboost.play();

                                            speedTimer = new Timer();
                                            monster.addSpeed(0.2f);
                                            actionResolver.broadcastMySpeed((float)(Math.round(monster.getSpeed()*10))/10);
                                            speedTimer.scheduleTask(new Timer.Task() {
                                                @Override
                                                public void run() {
                                                    monster.addSpeed(-0.2f);
                                                    actionResolver.broadcastMySpeed((float)(Math.round(monster.getSpeed()*10))/10);
                                                }
                                            }, 6);

                                        } else {
                                            vboost.play();
                                            if (!monster.getSaiyanMode()) {
                                                speedTimer = new Timer();
                                                monster.setVisibility(1.5f);
                                                speedTimer.scheduleTask(new Timer.Task() {
                                                    @Override
                                                    public void run() {
                                                        monster.setVisibility(1f);
                                                    }
                                                }, 12);
                                                speedTimer.start();
                                            }
                                        }

                                        break;
                                    }
                                }
                                if(pcopy != null)
                                    actionResolver.obtainPowerUp(pcopy);

                            }

                        }
                        //get the monster direction.
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


