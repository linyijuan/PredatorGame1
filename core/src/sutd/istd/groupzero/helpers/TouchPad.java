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
	private float speed;
	private Map map;
	private boolean touchUp = false;
	handleHold handleHoldThread;
	private Monster monster;
	
	float x, y, width, height;

	/*
	 * constructor with bounds
	 */

	public TouchPad(float x, float y, float width, float height, GameWorld gameWorld) {
		//Create a touchpad skin
		touchpadSkin = new Skin();
		//Set background image
		touchpadSkin.add("touchBackground", new Texture(Gdx.files.internal("data/touchBackground.png")));
		//Set knob image


		touchpadSkin.add("touchKnob", new Texture(Gdx.files.internal("data/touchKnob.png")));
		//	        touchpadSkin.add("none", new Texture("badlogic.jpg"));
		//Create TouchPad Style
		touchpadStyle = new com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle();
		//Create Drawable's from TouchPad skin
		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");


		//Apply the Drawables to the TouchPad Style
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;

		//Create new TouchPad with the created style
		touchpad = new com.badlogic.gdx.scenes.scene2d.ui.Touchpad(10, touchpadStyle);
		//setBounds(x,y,width,height)
		touchpad.setBounds(x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;


		this.monster = gameWorld.getMap().getMonster();
		moveUp = new Vector2(0, 0.01f);
		moveDown = new Vector2(0, -0.01f);
		moveLeft = new Vector2(0.01f, 0);
		moveRight = new Vector2(-0.01f, 0);
		this.map = gameWorld.getMap();
		speed = 0.1f;
	}


	public Stage createTouchPad() {

		stage = new Stage();
		stage.addActor(touchpad);

		DragListener myDragListener = new DragListener() {
			public void touchDragged(InputEvent event, float x, float y, int pointer) {				
				
			}
		};
	
		
		
		touchpad.addListener(myDragListener);
		InputListener myInputListener = new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				touchUp = false;
				Executor executor = Executors.newSingleThreadExecutor();
				executor.execute(new Runnable() {
					
					@Override
					public void run() {
						while(touchUp == false)
						{
							float x = touchpad.getKnobX();
							float y = touchpad.getKnobY();
//							Gdx.app.log("touchdragged", x + "  " + y);
//							Gdx.app.log("PERCENT", touchpad.getKnobPercentX() + " " + touchpad.getKnobPercentY());
							if ((x >= 65 && x < 165) && (y >= 160)) {
								Gdx.app.log("TOP", "TOP");
								monster.setDirection(Direction.TOP);
								monster.setMapPosition(monster.getMapPosition().add(moveUp));
								map.update(moveUp);
							} else if ((x >= 65 && x <= 165) && (y <= 50)) {
								Gdx.app.log("BOTTOM", "BOTTOM");
								monster.setDirection(Direction.BOTTOM);
								monster.setMapPosition(monster.getMapPosition().add(moveDown));
				
								map.update(moveDown);
							} else if ((x < 100) && (y <= 160)) {
								Gdx.app.log("LEFT", "LEFT");
								monster.setDirection(Direction.LEFT);
								monster.setMapPosition(monster.getMapPosition().add(moveLeft));
								map.update(moveLeft);
							} else if ((x > 100) && (y > 65 && y < 165)) {
								Gdx.app.log("RIGHT", "RIGHT");
								monster.setMapPosition(monster.getMapPosition().add(moveRight));
								monster.setDirection(Direction.RIGHT);
								map.update(moveRight);
							}
						}
						Gdx.app.log(monster.getDirection().toString() , monster.getDirection().toString());
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
		};



		touchpad.addListener(myInputListener);
		return stage;
	}
	
	public class handleHold extends Thread
	{
		private boolean condition;
		public handleHold() {
			// TODO Auto-generated constructor stub
			
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Gdx.app.log("Boolean", String.valueOf(touchUp));
			while(touchUp == false)
			{
				float x = touchpad.getKnobX();
				float y = touchpad.getKnobY();
//				Gdx.app.log("touchdragged", x + "  " + y);
//				Gdx.app.log("PERCENT", touchpad.getKnobPercentX() + " " + touchpad.getKnobPercentY());
				if ((x >= 65 && x < 165) && (y >= 160)) {
					Gdx.app.log("TOP", "TOP");
					monster.setDirection(Direction.TOP);
					monster.setMapPosition(monster.getMapPosition().add(moveUp));
					map.update(moveUp);
				} else if ((x >= 65 && x <= 165) && (y <= 50)) {
					Gdx.app.log("BOTTOM", "BOTTOM");
					monster.setDirection(Direction.BOTTOM);
					monster.setMapPosition(monster.getMapPosition().add(moveDown));
	
					map.update(moveDown);
				} else if ((x < 100) && (y <= 160)) {
					Gdx.app.log("LEFT", "LEFT");
					monster.setDirection(Direction.LEFT);
					monster.setMapPosition(monster.getMapPosition().add(moveLeft));
					map.update(moveLeft);
				} else if ((x > 100) && (y > 65 && y < 165)) {
					Gdx.app.log("RIGHT", "RIGHT");
					monster.setMapPosition(monster.getMapPosition().add(moveRight));
					monster.setDirection(Direction.RIGHT);
					map.update(moveRight);
				}
			}
			Gdx.app.log(monster.getDirection().toString() , monster.getDirection().toString());
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
	}


}


