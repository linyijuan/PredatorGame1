package sutd.istd.groupzero.gameobjects;

import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;

import com.badlogic.gdx.Gdx;

public class Monster {
	float screenWidth;
    float screenHeight;
	private final float STEP_SIZE = 5;
	private float X = 0;
	private float Y = 0;
	private boolean movingLeft = false;
	private boolean movingRight = false;
	private boolean movingUp = false;
	private boolean movingDown = false;
	private int facing = 6;
	private int direction = 2;
	private boolean[] moving = {movingLeft,movingUp,movingRight,movingDown};
	private float[] movingX = {-1,0,1,0};
	private float[] movingY = {0,-1,0,1};
	
	public Monster (float screenWidth, float screenHeight){
		this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
	}
	
	public void update(float delta){
		
	}
	public int getMovement(){
		boolean isMoving = false;
		for (int i =0;i<moving.length;i++){
			if (moving[i] == true){
				facing = i+4;
				direction = i;
				isMoving = true;
			}
		}
		if (isMoving){
			moving[direction] = false;
			return direction;
		}
		else 
			return facing;
		
			
	}

	public void move(int screenX, int screenY){
		int dir = -1;
		float dx = screenX/(float)2.5 - X;
        float dy = screenY/(float)2.3 - Y;
        Gdx.app.log("cp", "x:"+X+"  y:"+Y);
//        Gdx.app.log("cp", screenHeight+"");
        float d = Math.abs(dx) - Math.abs(dy);
        if (dx > 0 && d > 0) dir = 2;
        if (dx < 0 && d > 0) dir = 0;
        if (dy < 0 && d < 0) dir = 1;
        if (dy > 0 && d < 0) dir = 3;
		for (boolean dire:moving)
			if (dire == true)
				return;
		if (dir >=0) {
			moving[dir] = true;
			X += movingX[dir]*STEP_SIZE;
			Y += movingY[dir]*STEP_SIZE;
		}
//		if (movingX[direction]*STEP_SIZE > 0 && movingX[direction]*STEP_SIZE < screenWidth){
//			if (movingY[direction]*STEP_SIZE > 0 && movingY[direction]*STEP_SIZE <screenHeight){
				
//			}			
//		}		
	}
	
	public float getX(){
		return X;
	}
	public float getY(){
		return Y;
	}

}
