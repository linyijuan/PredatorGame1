package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.beans.Visibility;


public class AssetLoader {
    public static TextureRegion vsScreenGreenBot, vsScreenRedBot,menuBg;
    public static TextureRegion[][] texture,oppoTexture;
    public static Texture source,oppoSource;
    public static TextureRegion monsterUp,monsterDown, monsterLeft,monsterRight;
    public static TextureRegion oppoUp,oppoDown, oppoLeft, oppoRight;
    public static Animation upAnimation,downaAnimation, leftaAnimation,rightaAnimation,victoryAnimation;
    public static Animation upAnimationoppo,downaAnimationoppo, leftaAnimationoppo,rightaAnimationoppo,Vpowerup,Spowerup;
    public static TextureRegion tree,steak,powerUp;
    public static Sound movement, collision, getFood, getPowerUp, foundFriend, winGame, loseGame, fight;
    public static BitmapFont font, shadow;
    public static Texture arrow;
    public static Sprite spriteArrow;
    public static ActionResolver actionResolver;
    public static TextureRegion myHead,oppoHead;
    public static Animation clock;
    private  static float screenwidth, screenheight;
    public static TextureRegion victorybg1,losebg,victorMonster,loseMonster;
    public static TextureRegion Spic,Vpic;

    public static void load(){
        screenwidth = Gdx.graphics.getWidth();
        screenheight = Gdx.graphics.getHeight();
        if (actionResolver.requestMyPlayerNum() == 1){
            source = new Texture(Gdx.files.internal("data/SpriteSmall.png"));
            oppoSource = new Texture(Gdx.files.internal("data/spritesheetred.png"));
            myHead = new TextureRegion(new Texture(Gdx.files.internal("data/greenhead.png")));
            oppoHead = new TextureRegion(new Texture(Gdx.files.internal("data/redhead.png")));
            victorMonster = new TextureRegion(new Texture(Gdx.files.internal("data/vmonster.png")));
            loseMonster = new TextureRegion(new Texture(Gdx.files.internal("data/lmonster.png")));

        }
        else{
            source = new Texture(Gdx.files.internal("data/spritesheetred.png"));
            oppoSource = new Texture(Gdx.files.internal("data/SpriteSmall.png"));
            myHead = new TextureRegion(new Texture(Gdx.files.internal("data/redhead.png")));
            oppoHead = new TextureRegion(new Texture(Gdx.files.internal("data/greenhead.png")));
            victorMonster = new TextureRegion(new Texture(Gdx.files.internal("data/vmonsterred.png")));
            loseMonster = new TextureRegion(new Texture(Gdx.files.internal("data/lmonsterred.png")));
        }

        myHead.flip(false,true);
        oppoHead.flip(false,true);
        victorMonster.flip(false,true);
        texture = TextureRegion.split(source, source.getWidth()/3, source.getHeight()/4);
        oppoTexture = TextureRegion.split(oppoSource, oppoSource.getWidth()/3, oppoSource.getHeight()/4);
        tree = new TextureRegion(new Texture(Gdx.files.internal("data/cooltree.png")));
        tree.flip(false,true);
        steak = new TextureRegion(new Texture(Gdx.files.internal("data/steak_copy.png")));
        steak.flip(false,true);
        powerUp = new TextureRegion(new Texture(Gdx.files.internal("data/powerup.png")));
        powerUp.flip(false,true);
        menuBg = new TextureRegion(new Texture(Gdx.files.internal("data/menubg.png")));
        menuBg.flip(false,true);


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

        Vpowerup = new Animation(1f,Spic);
        Spowerup = new Animation(1f,Vpic);


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
        Spic = new TextureRegion(new Texture(Gdx.files.internal("data/speed.png")));
        Vpic = new TextureRegion(new Texture(Gdx.files.internal("data/visibility.png")));
        losebg =  new TextureRegion(new Texture(Gdx.files.internal("data/losebg.png")));
        losebg.flip(false,true);
        victorybg1 = new TextureRegion(new Texture(Gdx.files.internal("data/victorybg.png")));
        victorybg1.flip(false,true);
        TextureRegion[] clocks = new TextureRegion[8];
        clocks[0] = new TextureRegion(new Texture(Gdx.files.internal("data/clock0.png")));
        clocks[1] = new TextureRegion(new Texture(Gdx.files.internal("data/clock1.png")));
        clocks[2] = new TextureRegion(new Texture(Gdx.files.internal("data/clock2.png")));
        clocks[3] = new TextureRegion(new Texture(Gdx.files.internal("data/clock3.png")));
        clocks[4] = new TextureRegion(new Texture(Gdx.files.internal("data/clock4.png")));
        clocks[5] = new TextureRegion(new Texture(Gdx.files.internal("data/clock5.png")));
        clocks[6] = new TextureRegion(new Texture(Gdx.files.internal("data/clock6.png")));
        clocks[7] = new TextureRegion(new Texture(Gdx.files.internal("data/clock7.png")));
        for (TextureRegion t:clocks){
            t.flip(false,true);
        }
        clock = new Animation(.5f,clocks);
        clock.setPlayMode(PlayMode.LOOP);


        font = new BitmapFont(Gdx.files.internal("font/text.fnt"));
        font.setScale(screenwidth/1080,-screenheight/1920);
        shadow = new BitmapFont(Gdx.files.internal("font/shadow.fnt"));
        shadow.setScale(screenwidth/1080,-screenheight/1920);

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
