package sutd.istd.groupzero.gameobjects;


import sutd.istd.groupzero.gameobjects.Monster.Direction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Monster {
	
	private int speed;
	private Direction direction;
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}




	private float screenWidth, screenHeight;
	
	

	private Vector2 mapPosition;
	
	public Vector2 getMapPosition() {
		return mapPosition;
	}


	public void setMapPosition(Vector2 mapPosition) {
		this.mapPosition = mapPosition;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "this monster is at" + this.mapPosition.x + ", " + this. mapPosition.y;
	}

	public enum Direction
	{
		TOP, RIGHT, LEFT, BOTTOM;
		
		

		public int getKeycode() {
			// TODO Auto-generated method stub

			if(this.equals(TOP))
			{
				return 1;
			}
			else if(this.equals(LEFT))
			{
				return 0;
			}
			else if(this.equals(RIGHT))
			{
				return 2;
			}
			else 
			{
				return 3;
			}
		}
	}
		
	
	public Monster(int speed,  Vector2 currentMapPosition, Direction direction1, float screenWidth, float screenHeight)
	{
		this.direction = direction1;
		this.speed = speed;
		this.mapPosition = currentMapPosition;
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
			setDirection(d);
			setMapPosition(mapPosition);
			break;
		case RIGHT:
			setDirection(d);
			setMapPosition(mapPosition);

			break;
		case LEFT:
			setDirection(d);
			setMapPosition(mapPosition);

			break;
		case BOTTOM:
			setDirection(d);
			setMapPosition(mapPosition);

			break;

		default:
			
			break;
		}
	}


}
