package sutd.istd.groupzero.gameobjects;

import com.badlogic.gdx.math.Vector2;

public class Boulder{
	private Vector2 position;
    public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	public Boulder(Vector2 _position) {
        this.position = _position;
    }
    
    
    
}
