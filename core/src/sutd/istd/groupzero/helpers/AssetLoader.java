package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class AssetLoader {
	public static TextureRegion[][] texture;
	public static Texture source;
	public static TextureRegion gridBg;
	public static TextureRegion monsterUp,monsterDown, monsterLeft,monsterRight;
	public static Animation upAnimation,downaAnimation, leftaAnimation,rightaAnimation;
	public static Texture tree;
	public static Sound movement, collision, getFood, getPowerUp, foundFriend, winGame, loseGame, fight;
	public static void load(){
		source = new Texture(Gdx.files.internal("data/SpriteSmall.png"));
		texture = TextureRegion.split(source, source.getWidth()/3, source.getHeight()/4);
//		tree = new Texture(Gdx.files.internal("data/tree.png"));
		
		monsterUp = texture[3][0];
		monsterDown = texture[0][0];
		monsterLeft = texture[2][0];
		monsterRight = texture[1][0];
		
		for (TextureRegion[] t:texture)
			for (TextureRegion tt: t)
				tt.flip(false, true);
		
		upAnimation = new Animation(0.5f,new TextureRegion[] {monsterUp,texture[3][1],monsterUp,texture[3][2]});
		downaAnimation = new Animation(0.5f,new TextureRegion[] {monsterDown,texture[0][1],monsterDown,texture[0][2]});
		leftaAnimation = new Animation(0.5f,new TextureRegion[] {monsterLeft,texture[2][1],monsterLeft,texture[2][2]});
		rightaAnimation = new Animation(0.5f,new TextureRegion[] {monsterRight,texture[1][1],monsterRight,texture[1][2]});

	}
	public static void dispose(){
		source.dispose();
	}
	
}
