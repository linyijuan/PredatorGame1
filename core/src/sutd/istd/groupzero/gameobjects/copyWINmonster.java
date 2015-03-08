package sutd.istd.groupzero.gameobjects;


import sutd.istd.groupzero.gameobjects.copyWINmonster.Direction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class copyWINmonster {
	
	private int speed;
	private Direction direction;
	private float screenWidth, screenHeight;
	
	public Direction getDirection() {
		return direction;
	}



	private Vector2 mapPosition;
	
	public Vector2 getMapPosition() {
		return mapPosition;
	}


	public void setMapPosition(Vector2 mapPosition) {
		this.mapPosition = mapPosition;
	}



	public enum Direction
	{
		TOP, RIGHT, LEFT, BOTTOM;
	}
		
	public copyWINmonster(int speed,  Vector2 currentMapPosition, Direction direction1, float screenWidth, float screenHeight)
	{
		this.direction = direction1;
		this.speed = speed;
		this.mapPosition = mapPosition;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	
	public void update(float delta)
	{
		
	}
	
	
	
	public void move(Direction d, Vector2 mapPosition )
	{
		switch (d) {
		case TOP:
			setMapPosition(mapPosition);
			break;
		case RIGHT:
			setMapPosition(mapPosition);

			break;
		case LEFT:
			setMapPosition(mapPosition);

			break;
		case BOTTOM:
			setMapPosition(mapPosition);

			break;

		default:

			break;
		}
	}


}
