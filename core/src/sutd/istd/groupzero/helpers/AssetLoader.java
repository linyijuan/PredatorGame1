package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
	public static Texture texture;
	public static TextureRegion gridBg;
	public static TextureRegion monsterUp,monsterDown, monsterLeft,monsterRight;
	public static TextureRegion monsterMoveUp1,monsterMoveUp2,monsterMoveUp3;
	public static TextureRegion monsterMoveDown1,monsterMoveDown2,monsterMoveDown3;
	public static TextureRegion monsterMoveLeft1,monsterMoveLeft2,monsterMoveLeft3;
	public static TextureRegion monsterMoveRight1,monsterMoveRight2,monsterMoveRight3;
	public static Animation upAnimation,downaAnimation, leftaAnimation,rightaAnimation;
	
	public static Sound movement, collision, getFood, getPowerUp, foundFriend, winGame, loseGame, fight;
	public static void load(){}
	public static void dispose(){}
	
}
