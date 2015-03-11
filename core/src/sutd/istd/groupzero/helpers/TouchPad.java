package sutd.istd.groupzero.helpers;

import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.Monster.Direction;
import sutd.istd.groupzero.gameworld.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TouchPad
{
	
	 	private Stage stage;
	    private com.badlogic.gdx.scenes.scene2d.ui.Touchpad touchpad;
	    public com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle touchpadStyle;
	    private Drawable touchBackground;
	    private Drawable touchKnob;
	    public Skin touchpadSkin;
	    private Vector2 moveUp, moveRight, moveLeft, moveDown;
	    private float speed;
	    private Map map;
	    
	    private Monster monster;
	    
	    float x, y, width, height;
	    
	    /*
	     * constructor with bounds
	     */
	    
	    public TouchPad(float x, float y, float width, float height, GameWorld gameWorld){
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
	        this.x = x; this.y = y;this.width = width; this.height = height;
	        
	       
	        this.monster = gameWorld.getMap().getMonster();
	        moveUp= new Vector2(0,6);
			moveDown = new Vector2(0,-6);
			moveLeft = new Vector2(6,0);
			moveRight = new Vector2(-6,0);
			this.map = gameWorld.getMap();
			speed = 0.1f;
	    }


	    public Stage createTouchPad()
	    {
	    	 
	         stage = new Stage();
	         stage.addActor(touchpad);
	         
	         
	         touchpad.addListener(new DragListener()
	         {
	         	public void drag(InputEvent event,float x,float y,int pointer)
	             {
	         		Gdx.app.log("drag", x + "  " + y );
	         		if((x>=65 && x <165 ) && (y>=160))
	         		{
	         			Gdx.app.log("TOP", "TOP");
	         			monster.setDirection(Direction.TOP);
	                	monster.setMapPosition(monster.getMapPosition().add(moveUp));
	                	map.update(moveUp);
	         		}
	         		else if((x>=65 && x <= 165) && (y<= 50 ))
	         		{
	         			Gdx.app.log("BOTTOM", "BOTTOM");
	         			monster.setDirection(Direction.BOTTOM);
	                	monster.setMapPosition(monster.getMapPosition().add(moveDown));
	                	map.update(moveDown);

	         		}
	         		else if((x < 100) && (y<=160))
	         		{
	         			Gdx.app.log("LEFT", "LEFT");
	         			monster.setDirection(Direction.LEFT);
	                	monster.setMapPosition(monster.getMapPosition().add(moveLeft));
	                	map.update(moveLeft);
	         		}
	         		else if((x > 100) && (y> 65 &&  y<165)) 
	         		{
	         			Gdx.app.log("RIGHT", "RIGHT");
	         			monster.setMapPosition(monster.getMapPosition().add(moveRight));
	                	monster.setDirection(Direction.RIGHT);
	                	map.update(moveRight);
	         		}
	         		
	             }
//	         	public boolean touchDown(InputEvent event, float x, float y, int pointer)
//	         	{
//	         		Gdx.app.log("drag", x + "  " + y );
//	         		if((x>=65 && x <165 ) && (y>=160))
//	         		{
//	         			Gdx.app.log("TOP", "TOP");
//	         			monster.setDirection(Direction.TOP);
//	                	monster.setMapPosition(monster.getMapPosition().add(moveUp));
//	                	map.update(moveUp);
//	                	
//	         		}
//	         		else if((x>=65 && x <= 165) && (y<= 50 ))
//	         		{
//	         			Gdx.app.log("BOTTOM", "BOTTOM");
//	         			monster.setDirection(Direction.BOTTOM);
//	                	monster.setMapPosition(monster.getMapPosition().add(moveDown));
//	                	map.update(moveDown);
//
//
//	         		}
//	         		else if((x < 100) && (y<=160))
//	         		{
//	         			Gdx.app.log("LEFT", "LEFT");
//	         			monster.setDirection(Direction.LEFT);
//	                	monster.setMapPosition(monster.getMapPosition().add(moveLeft));
//	                	map.update(moveLeft);
//
//	         		}
//	         		else if((x > 100) && (y> 65 &&  y<165)) 
//	         		{
//	         			Gdx.app.log("RIGHT", "RIGHT");
//	         			monster.setMapPosition(monster.getMapPosition().add(moveRight));
//	                	map.update(moveRight);
//
//	                	monster.setDirection(Direction.RIGHT);
//	         		}
//					return false;
//	         		
//	         	}
	         	public void touchDragged(InputEvent event,float x,float y,int pointer)
	             {
	         		Gdx.app.log("drag", x + "  " + y );
	         		if((x>=65 && x <165 ) && (y>=160))
	         		{
	         			Gdx.app.log("TOP", "TOP");
	         			monster.setDirection(Direction.TOP);
	                	monster.setMapPosition(monster.getMapPosition().add(moveUp));
	                	map.update(moveUp);
	         		}
	         		else if((x>=65 && x <= 165) && (y<= 50 ))
	         		{
	         			Gdx.app.log("BOTTOM", "BOTTOM");
	         			monster.setDirection(Direction.BOTTOM);
	                	monster.setMapPosition(monster.getMapPosition().add(moveDown));

	                	map.update(moveDown);
	         		}
	         		else if((x < 100) && (y<=160))
	         		{
	         			Gdx.app.log("LEFT", "LEFT");
	         			monster.setDirection(Direction.LEFT);
	                	monster.setMapPosition(monster.getMapPosition().add(moveLeft));
	                	map.update(moveLeft);
	         		}
	         		else if((x > 100) && (y> 65 &&  y<165)) 
	         		{
	         			Gdx.app.log("RIGHT", "RIGHT");
	         			monster.setMapPosition(monster.getMapPosition().add(moveRight));
	                	monster.setDirection(Direction.RIGHT);
	                	map.update(moveRight);
	         		}
	         		
	             }
	         });
	         
//	         stage.addListener(new DragListener()
//	         {
//	         	public void drag(InputEvent event,float x,float y,int pointer)
//	             {
//	         		Gdx.app.log("drag", x + "  " + y );
//	         		if((x>=65 && x <165 ) && (y>=160))
//	         		{
//	         			Gdx.app.log("TOP", "TOP");
//	         		}
//	         		else if((x>=65 && x <= 165) && (y<= 50 ))
//	         		{
//	         			Gdx.app.log("BOTTOM", "BOTTOM");
//	         		}
//	         		else if((x < 100) && (y<=160))
//	         		{
//	         			Gdx.app.log("LEFT", "LEFT");
//	         		}
//	         		else if((x > 100) && (y> 65 &&  y<165)) 
//	         		{
//	         			Gdx.app.log("RIGHT", "RIGHT");
//	         		}
//	         		
//	             }
//	         }
//	         );
//	         
	         return stage;
	    }
	    
	   

	
}