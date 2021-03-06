package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/* Class to prepare text, picture and sound display*/
public class AssetLoader {
    private  static float screenwidth, screenheight;

    public static Texture arrow,source,oppoSource;
    public static TextureRegion menuBg,logo,myHead,oppoHead;
    public static TextureRegion tree,steak,powerUp;
    public static TextureRegion monsterUp,monsterDown, monsterLeft,monsterRight;
    public static TextureRegion vsOppo,vsOppo2,vsMe,vsMe2,vsBg;
    public static TextureRegion victorybg1,victorybg2,victorMonster,loseMonster,losebg;
    public static TextureRegion oppoUp,oppoDown, oppoLeft, oppoRight;
    public static TextureRegion Spic,Vpic;
    public static TextureRegion[][] texture,oppoTexture;
    public static TextureRegion quickgame,invite,viewInvitation,quickgamed,invited,viewInvitationd,signOut;

    public static Animation upAnimation,downaAnimation, leftaAnimation,rightaAnimation,victoryAnimation;
    public static Animation upAnimationoppo,downaAnimationoppo, leftaAnimationoppo,rightaAnimationoppo,Vpowerup,Spowerup;
    public static Animation clock;

    public static Sound sboost,vboost,eating;
    public static BitmapFont font, shadow;
    public static Sprite spriteArrow;
    public static ActionResolver actionResolver;

    public static void load(){
        // obtain screen width and height
        screenwidth = Gdx.graphics.getWidth();
        screenheight = Gdx.graphics.getHeight();
        // load text display and set size
        font = new BitmapFont(Gdx.files.internal("font/text.fnt"));
        font.setScale(screenwidth/1080,-screenheight/1920);
        shadow = new BitmapFont(Gdx.files.internal("font/shadow.fnt"));
        shadow.setScale(screenwidth/1080,-screenheight/1920);

        /* Set according to different roles*/
        if (actionResolver.requestMyPlayerNum() == 1){
            // monster and opponent monster image source
            source = new Texture(Gdx.files.internal("data/SpriteSmall.png"));
            oppoSource = new Texture(Gdx.files.internal("data/spritesheetred.png"));

            // status bar profile picture
            myHead = new TextureRegion(new Texture(Gdx.files.internal("data/greenhead.png")));
            oppoHead = new TextureRegion(new Texture(Gdx.files.internal("data/redhead.png")));

            // ending scene
            victorMonster = new TextureRegion(new Texture(Gdx.files.internal("data/vmonster.png")));
            loseMonster = new TextureRegion(new Texture(Gdx.files.internal("data/lmonster.png")));
            losebg = new TextureRegion(new Texture(Gdx.files.internal("data/greenlose.png")));

            // tug of war monster monster and background
            vsOppo = new TextureRegion(new Texture(Gdx.files.internal("data/redvsmonster.png")));
            vsOppo2 = new TextureRegion(new Texture(Gdx.files.internal("data/redvsmonster2.png")));
            vsMe = new TextureRegion(new Texture(Gdx.files.internal("data/greenvsmonster.png")));
            vsMe2 = new TextureRegion(new Texture(Gdx.files.internal("data/greenvsmonster2.png")));
            vsBg = new TextureRegion(new Texture(Gdx.files.internal("data/vsbg.png")));
        }
        else{
            // monster and opponent monster image source
            source = new Texture(Gdx.files.internal("data/spritesheetred.png"));
            oppoSource = new Texture(Gdx.files.internal("data/SpriteSmall.png"));

            // status bar profile picture
            myHead = new TextureRegion(new Texture(Gdx.files.internal("data/redhead.png")));
            oppoHead = new TextureRegion(new Texture(Gdx.files.internal("data/greenhead.png")));

            // ending scene
            victorMonster = new TextureRegion(new Texture(Gdx.files.internal("data/vmonsterred.png")));
            loseMonster = new TextureRegion(new Texture(Gdx.files.internal("data/lmonsterred.png")));
            losebg = new TextureRegion(new Texture(Gdx.files.internal("data/redlose.png")));

            // tug of war monster and background
            vsOppo = new TextureRegion(new Texture(Gdx.files.internal("data/greenvsmonster.png")));
            vsOppo2 = new TextureRegion(new Texture(Gdx.files.internal("data/greenvsmonster2.png")));
            vsMe = new TextureRegion(new Texture(Gdx.files.internal("data/redvsmonster.png")));
            vsMe2 = new TextureRegion(new Texture(Gdx.files.internal("data/redvsmonster2.png")));
            vsBg = new TextureRegion(new Texture(Gdx.files.internal("data/vsbg2.png")));
        }
        // set the original direction due to libgdx default setting
        vsBg.flip(false,true);
        vsMe.flip(false,true);
        vsMe2.flip(false,true);
        myHead.flip(false,true);
        oppoHead.flip(false,true);
        victorMonster.flip(false,true);
        loseMonster.flip(false,true);
        losebg.flip(false,true);

        /* Set common pictures*/
        menuBg = new TextureRegion(new Texture(Gdx.files.internal("data/menubg.png")));
        menuBg.flip(false,true);
        logo = new TextureRegion(new Texture(Gdx.files.internal("data/logo.png")));
        logo.flip(false,true);
        quickgame = new TextureRegion(new Texture(Gdx.files.internal("data/quickgame.png")));
        quickgamed = new TextureRegion(new Texture(Gdx.files.internal("data/quickgamed.png")));
        invite = new TextureRegion(new Texture(Gdx.files.internal("data/inviteplayers.png")));
        invited = new TextureRegion(new Texture(Gdx.files.internal("data/inviteplayersd.png")));
        viewInvitation = new TextureRegion(new Texture(Gdx.files.internal("data/viewinvites.png")));
        viewInvitationd = new TextureRegion(new Texture(Gdx.files.internal("data/viewinvitesd.png")));

        // items on map
        tree = new TextureRegion(new Texture(Gdx.files.internal("data/cooltree.png")));
        tree.flip(false,true);
        steak = new TextureRegion(new Texture(Gdx.files.internal("data/steak.png")));
        steak.flip(false,true);
        powerUp = new TextureRegion(new Texture(Gdx.files.internal("data/powerupsmall.png")));
        powerUp.flip(false,true);

        // power up icon
        Spic = new TextureRegion(new Texture(Gdx.files.internal("data/speed.png")));
        Spic.flip(false, true);
        Vpic = new TextureRegion(new Texture(Gdx.files.internal("data/visibility.png")));
        Vpic.flip(false, true);
        Vpowerup = new Animation(1f,Spic);
        Spowerup = new Animation(1f,Vpic);

        // arrow to hint opponent position
        arrow = new Texture(Gdx.files.internal("data/tango-left-arrow-red.png"));
        spriteArrow = new Sprite(arrow);

        // victory ending scene
        victorybg1 = new TextureRegion(new Texture(Gdx.files.internal("data/victorybg.png")));
        victorybg1.flip(false,true);
        victorybg2 = new TextureRegion(new Texture(Gdx.files.internal("data/victorybg2.png")));
        victorybg2.flip(false,true);
        victoryAnimation = new Animation(0.2f,new TextureRegion[] {victorybg1,victorybg2});
        victoryAnimation.setPlayMode(PlayMode.LOOP);

        // monster and opponent monster walking animation
        texture = TextureRegion.split(source, source.getWidth()/3, source.getHeight()/4);
        oppoTexture = TextureRegion.split(oppoSource, oppoSource.getWidth()/3, oppoSource.getHeight()/4);
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

        // status bar timer clock animation
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

        // sound effect
        sboost = Gdx.audio.newSound(Gdx.files.internal("data/boost.wav"));
        vboost = Gdx.audio.newSound(Gdx.files.internal("data/visible.wav"));
        eating = Gdx.audio.newSound(Gdx.files.internal("data/eating.mp3"));
    }

    // when dispose the game, dispose all the resource as well
    public static void dispose(){
        source.dispose();
        oppoSource.dispose();
        font.dispose();
        shadow.dispose();
        sboost.dispose();
        vboost.dispose();
        eating.dispose();
    }

}
