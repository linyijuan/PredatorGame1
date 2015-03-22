package sutd.istd.groupzero.predator1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import sutd.istd.groupzero.predator1.PredatorGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Predator1.0";
		config.width = 540;
		config.height= 980;
		new LwjglApplication(new PredatorGame(), config);
	}
}
