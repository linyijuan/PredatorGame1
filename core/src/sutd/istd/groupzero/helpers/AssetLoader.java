package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class AssetLoader {
    public static TextureRegion vsScreenGreenBot, vsScreenRedBot;
    public static TextureRegion[][] texture,oppoTexture;
    public static Texture source,oppoSource;
    public static TextureRegion monsterUp,monsterDown, monsterLeft,monsterRight;
    public static TextureRegion oppoUp,oppoDown, oppoLeft, oppoRight;
    public static Animation upAnimation,downaAnimation, leftaAnimation,rightaAnimation;
    public static Animation upAnimationoppo,downaAnimationoppo, leftaAnimationoppo,rightaAnimationoppo;
    public static TextureRegion tree,steak,powerUp;
    public static Sound movement, collision, getFood, getPowerUp, foundFriend, winGame, loseGame, fight;
    public static BitmapFont font, shadow;
    public static Texture arrow;
    public static Sprite spriteArrow;
    public static ActionResolver actionResolver;

    public static void load(){
        if (actionResolver.requestMyPlayerNum() == 1){
            source = new Texture(Gdx.files.internal("data/SpriteSmall.png"));
            oppoSource = new Texture(Gdx.files.internal("data/spritesheetred.png"));
        }
        else{
            source = new Texture(Gdx.files.internal("data/spritesheetred.png"));
            oppoSource = new Texture(Gdx.files.internal("data/SpriteSmall.png"));
        }
        texture = TextureRegion.split(source, source.getWidth()/3, source.getHeight()/4);
        oppoTexture = TextureRegion.split(oppoSource, oppoSource.getWidth()/3, oppoSource.getHeight()/4);
        tree = new TextureRegion(new Texture(Gdx.files.internal("data/cooltree.png")));
        tree.flip(false,true);
//        Gdx.app.log("tree",tree.getRegionWidth()+","+tree.getRegionHeight());
        steak = new TextureRegion(new Texture(Gdx.files.internal("data/steak_copy.png")));
        steak.flip(false,true);
//        Gdx.app.log("steak",steak.getRegionWidth()+","+steak.getRegionHeight());
        powerUp = new TextureRegion(new Texture(Gdx.files.internal("data/powerup.png")));
        powerUp.flip(false,true);
//        Gdx.app.log("powerup",powerUp.getRegionWidth()+","+powerUp.getRegionHeight());

        for (TextureRegion[] t:texture)
            for (TextureRegion tt: t)
                tt.flip(false, true);
        for (TextureRegion[] t:oppoTexture)
            for (TextureRegion tt: t)
                tt.flip(false, true);
        monsterUp = texture[3][0];
        monsterDown = texture[0][0];
        monsterLeft = texture[2][0];
        monsterRight = texture[1][0];

        oppoUp = oppoTexture[3][0];
        oppoDown = oppoTexture[0][0];
        oppoLeft = oppoTexture[2][0];
        oppoRight = oppoTexture[1][0];

        vsScreenGreenBot = new TextureRegion(new Texture(Gdx.files.internal("data/vsscreen.png")));
        vsScreenGreenBot.flip(false,true);
        vsScreenRedBot = new TextureRegion(new Texture(Gdx.files.internal("data/vsscreen2.png")));
        vsScreenRedBot.flip(false,true);

        upAnimation = new Animation(0.15f,new TextureRegion[] {monsterUp,texture[3][1],monsterUp,texture[3][2]});
        downaAnimation = new Animation(0.15f,new TextureRegion[] {monsterDown,texture[0][1],monsterDown,texture[0][2]});
        leftaAnimation = new Animation(0.15f,new TextureRegion[] {monsterLeft,texture[2][1],monsterLeft,texture[2][2]});
        rightaAnimation = new Animation(0.15f,new TextureRegion[] {monsterRight,texture[1][1],monsterRight,texture[1][2]});
        upAnimation.setPlayMode(PlayMode.LOOP);
        downaAnimation.setPlayMode(PlayMode.LOOP);
        leftaAnimation.setPlayMode(PlayMode.LOOP);
        rightaAnimation.setPlayMode(PlayMode.LOOP);

        upAnimationoppo = new Animation(0.15f,new TextureRegion[] {oppoUp,oppoTexture[3][1],oppoUp,oppoTexture[3][2]});
        downaAnimationoppo = new Animation(0.15f,new TextureRegion[] {oppoDown,oppoTexture[0][1],oppoDown,oppoTexture[0][2]});
        leftaAnimationoppo = new Animation(0.15f,new TextureRegion[] {oppoLeft,oppoTexture[2][1],oppoLeft,oppoTexture[2][2]});
        rightaAnimationoppo = new Animation(0.15f,new TextureRegion[] {oppoRight,oppoTexture[1][1],oppoRight,oppoTexture[1][2]});
        upAnimationoppo.setPlayMode(PlayMode.LOOP);
        downaAnimationoppo.setPlayMode(PlayMode.LOOP);
        leftaAnimationoppo.setPlayMode(PlayMode.LOOP);
        rightaAnimationoppo.setPlayMode(PlayMode.LOOP);

        font = new BitmapFont(Gdx.files.internal("font/text.fnt"));
        font.setScale(.25f, -.25f);
        shadow = new BitmapFont(Gdx.files.internal("font/shadow.fnt"));
        shadow.setScale(.25f, -.25f);

        arrow = new Texture(Gdx.files.internal("data/tango-left-arrow-red.png"));
        spriteArrow = new Sprite(arrow);

        movement = Gdx.audio.newSound(Gdx.files.internal("data/walking_in_grass.wav"));
    }
    public static void dispose(){
        source.dispose();
        font.dispose();
        shadow.dispose();
    }

}
