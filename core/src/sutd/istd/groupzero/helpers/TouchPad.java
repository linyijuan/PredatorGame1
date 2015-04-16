package sutd.istd.groupzero.helpers;

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

/* Input controller for map war */
public class TouchPad {
    private float screenWidth,screenHeight;
	private Stage stage;
	private com.badlogic.gdx.scenes.scene2d.ui.Touchpad touchpad;
	public com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle touchpadStyle;
	private Drawable touchBackground,SkillBackground,touchKnob;
	public Skin touchpadSkin,SkillButton;
    private ActionResolver actionResolver;
	private Vector2 moveUp, moveRight, moveLeft, moveDown,touchpadcenter;
	private boolean touchUp = false;
	private Monster monster;
    private List<Food> foodSynchroList;
    private List<PowerUps> puSynchroList;
    private Sound sboost,vboost,eating;
    public static Button skillButton;
    private Timer speedTimer;

    /**
     * TouchPad constructor
     * @param x x position
     * @param y y position
     * @param width width of the TouchPad
     * @param height height of the TouchPad
     * @param map  map object associated with the game
     * @param actionResolver handles google play services
     */
	public TouchPad(float x, float y, float width, float height, Map map,ActionResolver actionResolver) {
        // obtain screen dimensions
		screenHeight = Gdx.graphics.getHeight();//gets the height of the screen
        screenWidth = Gdx.graphics.getWidth();//gets the width of the screen

        this.actionResolver = actionResolver;//actionResolver instantiation
        this.monster = map.getMonster();//monster instantiation

        // get sound effect from Assetloader
        sboost = AssetLoader.sboost;// speed boost sound
        vboost = AssetLoader.vboost;// visibility boost sound
        eating = AssetLoader.eating;// eating sound

        // Button for activate Predator Mode
        SkillButton = new Skin(); // applying a new skin/ look to the skillbutton
        SkillButton.add("skillButton",new Texture(Gdx.files.internal("data/saiyan.png")));// adds the background texture
        SkillBackground = SkillButton.getDrawable("skillButton");//gets the drawable of the skillButton
        skillButton = new Button(SkillBackground, SkillBackground); // skillButton initialization
        skillButton.setBounds(40*(screenWidth/1080), 50*(screenHeight/1920), screenWidth/5, screenWidth/5);//set bounds of the skillbutton relative to the screen width and height.

        // define the skin of the movement controller - touchpad
        touchpadSkin = new Skin();// new skin/ look for the touchpad.
        touchpadSkin.add("touchKnob", new Texture(Gdx.files.internal("data/touchKnob1.png")));// adding the grey color touchknob to the touchpad skin
		touchpadSkin.add("touchBackground", new Texture(Gdx.files.internal("data/touchBackground.png"))); // adding the touchpad background to the touchpad skin
		touchpadStyle = new com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle(); // initialization of touchpadstyle
		touchBackground = touchpadSkin.getDrawable("touchBackground"); // drawable initialization of touchbackground
		touchKnob = touchpadSkin.getDrawable("touchKnob"); // drawable initialization of touchknob
		touchpadStyle.background = touchBackground; // assign touchpad background with touchbackground drawable.
		touchpadStyle.knob = touchKnob; // assign touchpadknob with touchknob drawable.

        // create touchpad with the defined style
		touchpad = new com.badlogic.gdx.scenes.scene2d.ui.Touchpad(15, touchpadStyle);
        touchpad.setBounds(x, y, width, height);// define the bounds of the touchpad
        touchpadcenter = new Vector2(width/2, height/2); //define the touchpad center to the mid point of the touchpad
	}

    /**
     * @return Stage object
     */
	public Stage createTouchPad() {
        //a stage receives input events that can be distributed to the actors
        // an actor can be a button / some form of input listener.
		stage = new Stage();
		stage.addActor(touchpad); // add touchpad to stage.
        stage.addActor(skillButton); // add skillButton to stage
        //Predator mode activate button listener.
        skillButton.addListener(new InputListener(){
            //adds input listener or listen to button press to activate predator mode
            Timer predatorMode;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // checks if the player can pay the cost of 5 food to activate skill,
                if (monster.getStrength() >= 5) {
                    //checks if the player is already in supersaiyan/ predator mode.
                    if (monster.getSaiyanMode() == true){
                        // Do not return true if monster is in predator mode
                        return false;
                    }
                    return true;
                }else{
                    // else
                    return false;
                }
            }

            @Override
            /**
             * called only if touchdown is true.
             * Predator mode activated. Temporary speed and visibility increase at the cost of 5 strength.
             */
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                monster.addSpeed(1.5f);//increase the speed of the monster
                monster.setSaiyanMode(true);//sets the monster current mode as supersaiyan
                predatorMode = new Timer();//timer initialization to track the predator mode active duration
                // return the monster status back to normal after skill ends and reduce strength count by 5
                // skill lasts for 10 sec
                predatorMode.scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        monster.addSpeed(-1.5f);//decrease the monster movement speed at the end of predator mode
                        monster.setVisibility(1.0f);//sets the monster's visiblity at the end of predator mode.
                        monster.setStrength(monster.getStrength() - 5);//decrease the monster's strength by 5 as cost for predator mode activation
                        monster.setSaiyanMode(false); // deactivation of predator mode.
                    }
                }, 10f);// the task / predator mode is scheduled for 10 secs duration.
                // Start the countdown
                predatorMode.start();// start of the predator mode.

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
                        moveUp = new Vector2(0,-( monster.getSpeed() *0.003f));//monster moves upward vector
                        moveDown = new Vector2(0, monster.getSpeed() * 0.003f);//monster moves downward vector
                        moveLeft = new Vector2( -(monster.getSpeed() * 0.003f), 0);//monster moves left vector
                        moveRight = new Vector2(monster.getSpeed() * 0.003f, 0);//monster moves right vector

                        while(touchUp == false){
                            moveUp.set(0,-( monster.getSpeed() *0.003f)); //initialization of moveUp vector
                            moveDown.set(0, monster.getSpeed() * 0.003f);//initialization of movedown vector
                            moveLeft.set(-(monster.getSpeed() * 0.003f), 0);//initialization of moveleft vector
                            moveRight.set(monster.getSpeed() * 0.003f, 0);//initialization of moveright vector
                            foodSynchroList = actionResolver.requestFoods();//foodList from the action resolver.
                            puSynchroList = actionResolver.requestPUs();//powerUpList from the action resolver.
                            float x = touchpad.getKnobX();//gets touchknob x position
                            float y = touchpad.getKnobY();//gets touchknob y position
                            float angle = getAngle(x, y);//calculates the touchknob position
                            if(angle>=45 && angle <=135){//checks if the touchknob is at the top of the touchpad, relative to the center of the the touchpad center
                                monster.setDirection(Direction.TOP);//sets the monster's direction to top
                                monster.setMyPosition(monster.getMyPosition().add(moveUp));//monster moves upwards while the player doesn't release the touchknob
                                for (Tree t: actionResolver.requestTrees() ){
                                    if (Intersector.overlaps(t.getWalkingBound(), monster.getBound())){//checks if the monster was colliding with the tree
                                        monster.setMyPosition(monster.getMyPosition().add(moveDown));//stops the monster from passing over the trees
                                        break;
                                    }
                                }
                            } else if (angle <= -45 && angle >= -135) {//checks if the touchknob is at the bottom of the touchpad, relative to the center of the the touchpad center
                                monster.setDirection(Direction.BOTTOM);//sets the monster's direction to bottom
                                monster.setMyPosition(monster.getMyPosition().add(moveDown));//monster moves down while the player doesn't release the touchknob
                                for (Tree t: actionResolver.requestTrees() ){
                                    if (Intersector.overlaps(t.getWalkingBound(), monster.getBound())){//checks if the monster was colliding with the tree
                                        monster.setMyPosition(monster.getMyPosition().add(moveUp));//stops the monster from passing over the trees
                                        break;
                                    }
                                }
                            } else if (angle>=135 || angle <=-135) {//checks if the touchknob is at the left of the touchpad, relative to the center of the the touchpad center
                                monster.setDirection(Direction.LEFT);//sets the monster's direction to left
                                monster.setMyPosition(monster.getMyPosition().add(moveLeft));//monster moves left while the player doesn't release the touchknob
                                for (Tree t: actionResolver.requestTrees() ){
                                    if (Intersector.overlaps(t.getWalkingBound(), monster.getBound())){//checks if the monster was colliding with the tree
                                        monster.setMyPosition(monster.getMyPosition().add(moveRight));//stops the monster from passing over the trees
                                        break;
                                    }
                                }
                            } else if (angle<=45 && angle >=-45 && angle != 0) {//checks if the touchknob is at the right of the touchpad, relative to the center of the the touchpad center
                                monster.setDirection(Direction.RIGHT);//sets the monster's direction to right
                                monster.setMyPosition(monster.getMyPosition().add(moveRight));//monster moves right while the player doesn't release the touchknob
                                for (Tree t: actionResolver.requestTrees() ){
                                    if (Intersector.overlaps(t.getWalkingBound(), monster.getBound())){//checks if the monster was colliding with the tree
                                        monster.setMyPosition(monster.getMyPosition().add(moveLeft));//stops the monster from passing over the trees
                                        break;
                                    }
                                }
                            }

                            //foodList synchronziation for concurrent modification
                            synchronized (foodSynchroList) {
                                Food fcopy = null;
                                for (Food f : foodSynchroList) {
                                    if (Intersector.overlaps(monster.getBound(), f.getBound())) {//checks if the player has taken any food.
                                        eating.play();//play eating sound
                                        fcopy = f; //copy food object
                                        actionResolver.eatFood(f);//inform the other player that food "f" is taken by this monster
                                        monster.obtainFood();//adds strength to monster
                                        actionResolver.broadcastMyStrength(monster.getStrength());//informs the other player of this monster's strength increase
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
                                    if (Intersector.overlaps(monster.getBound(), p.getBound())) {//checks if the player has taken any powerups
                                        pcopy = p;
                                        if (p.getKind().equals("s")) {
                                            sboost.play();//play speed boost sound

                                            speedTimer = new Timer();//sets speed increased timer
                                            monster.addSpeed(0.2f);//adds the monster's speed
                                            actionResolver.broadcastMySpeed((float)(Math.round(monster.getSpeed()*10))/10);//inform the other player of this monster's movement speed increase.
                                            speedTimer.scheduleTask(new Timer.Task() {
                                                @Override
                                                public void run() {//schedule the task for 6 sec
                                                    monster.addSpeed(-0.2f);//decrease the speed
                                                    actionResolver.broadcastMySpeed((float)(Math.round(monster.getSpeed()*10))/10);//inform the other player of this monster's speed decrease
                                                }
                                            }, 6);

                                        } else {
                                            vboost.play();//play visibility boost sound
                                            if (!monster.getSaiyanMode()) {
                                                speedTimer = new Timer();//creates a timer for visibility
                                                monster.setVisibility(1.5f);//increase the visibility
                                                speedTimer.scheduleTask(new Timer.Task() {
                                                    @Override
                                                    public void run() {//schedule the visibility increase time for 12 seconds.
                                                        monster.setVisibility(1f);//reduce the visibility
                                                    }
                                                }, 12);
                                                speedTimer.start();//stars the visibility increment counter.
                                            }
                                        }

                                        break;
                                    }
                                }
                                if(pcopy != null)
                                    actionResolver.obtainPowerUp(pcopy);//informs the other player of this monster's powerup message

                            }

                        }
                        //get the monster direction.
                        Direction d = monster.getDirection();
                        switch (d) {
                            case TOP:
                                monster.setDirection(Direction.STATIONARY_TOP);//sets the monster direction to corresponding correct direction at each end of user's control
                                break;
                            case LEFT:
                                monster.setDirection(Direction.STATIONARY_LEFT);//sets the monster direction to corresponding correct direction at each end of user's control
                                break;
                            case RIGHT:
                                monster.setDirection(Direction.STATIONARY_RIGHT);//sets the monster direction to corresponding correct direction at each end of user's control
                                break;
                            case BOTTOM:
                                monster.setDirection(Direction.STATIONARY_BOTTOM);//sets the monster direction to corresponding correct direction at each end of user's control
                                break;
                            default:
                                break;
                        }

                    }
                });
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchUp = true;//touchup boolean to check the actual stage of the boolean
                }
        });

		return stage;
	}

    /**
     * helper method for touchKnob
     * calculates the angle based on the center of the touchpad
     * @param x current touchknob x position
     * @param y current touchknob y position
     * @return
     */
	private float getAngle(float x, float y){
		float temp = (float)Math.atan2(y-touchpadcenter.y, x-touchpadcenter.x); //calculates the angle in radian
		temp = (float) (temp * 57.2957795);//converts the radian to degree
		return 	temp;//returns the angle in degree
	}



}


