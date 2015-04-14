package sutd.istd.groupzero.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/* Game pivture and sound source preparation class*/
public class AssetLoader {
    private  static float screenwidth, screenheight;

    public static Texture source,oppoSource,arrow;
    public static TextureRegion menuBg,myHead,oppoHead;
    public static TextureRegion monsterUp,monsterDown, monsterLeft,monsterRight;
    public static TextureRegion oppoUp,oppoDown, oppoLeft, oppoRight;
    public static TextureRegion vsOppo,vsOppo2,vsMe,vsMe2,vsBg;
    public static TextureRegion tree,steak,powerUp;
    public static TextureRegion Spic,Vpic;
    public static TextureRegion victorybg1,victorybg2,victorMonster,loseMonster,losebg;
    public static TextureRegion[][] texture,oppoTexture;

    public static Animation clock;
    public static Animation upAnimation,downaAnimation, leftaAnimation,rightaAnimation,victoryAnimation;
    public static Animation upAnimationoppo,downaAnimationoppo, leftaAnimationoppo,rightaAnimationoppo,Vpowerup,Spowerup;

    public static BitmapFont font, shadow;
    public static Sprite spriteArrow;
    public static ActionResolver actionResolver;

    public static void load(){
        // obtain screen width and height
        screenwidth = Gdx.graphics.getWidth();
        screenheight = Gdx.graphics.getHeight();

        // initialize different picture for different player role
        if (actionResolver.requestMyPlayerNum() == 1){
            // monster and opponent image and animation
            source = new Texture(Gdx.files.internal("data/SpriteSmall.png"));
            oppoSource = new Texture(Gdx.files.internal("data/spritesheetred.png"));

            // map war status bar
            myHead = new TextureRegion(new Texture(Gdx.files.internal("data/greenhead.png")));
            oppoHead = new TextureRegion(new Texture(Gdx.files.internal("data/redhead.png")));

            //endding scene
            victorMonster = new TextureRegion(new Texture(Gdx.files.internal("data/vmonster.png")));
            loseMonster = new TextureRegion(new Texture(Gdx.files.internal("data/lmonster.png")));
            losebg = new TextureRegion(new Texture(Gdx.files.internal("data/greenlose.png")));

            // tug of war pictures
            vsOppo = new TextureRegion(new Texture(Gdx.files.internal("data/redvsmonster.png")));
            vsOppo2 = new TextureRegion(new Texture(Gdx.files.internal("data/redvsmonster2.png")));
            vsMe = new TextureRegion(new Texture(Gdx.files.internal("data/greenvsmonster.png")));
            vsMe2 = new TextureRegion(new Texture(Gdx.files.internal("data/greenvsmonster2.png")));
            vsBg = new TextureRegion(new Texture(Gdx.files.internal("data/vsbg.png")));
        }
        else{
            // monster and opponent image and animation
            source = new Texture(Gdx.files.internal("data/spritesheetred.png"));
            oppoSource = new Texture(Gdx.files.internal("data/SpriteSmall.png"));

            // map war status bar
            myHead = new TextureRegion(new Texture(Gdx.files.internal("data/redhead.png")));
            oppoHead = new TextureRegion(new Texture(Gdx.files.internal("data/greenhead.png")));

            //endding scene
            victorMonster = new TextureRegion(new Texture(Gdx.files.internal("data/vmonsterred.png")));
            loseMonster = new TextureRegion(new Texture(Gdx.files.internal("data/lmonsterred.png")));
            losebg = new TextureRegion(new Texture(Gdx.files.internal("data/redlose.png")));

            // tug of war pictures
            vsOppo = new TextureRegion(new Texture(Gdx.files.internal("data/greenvsmonster.png")));
            vsOppo2 = new TextureRegion(new Texture(Gdx.files.internal("data/greenvsmonster2.png")));
            vsMe = new TextureRegion(new Texture(Gdx.files.internal("data/redvsmonster.png")));
            vsMe2 = new TextureRegion(new Texture(Gdx.files.internal("data/redvsmonster2.png")));
            vsBg = new TextureRegion(new Texture(Gdx.files.internal("data/vsbg2.png")));
        }

        // adjust the direction of the picture due to libgdx default setting
        vsBg.flip(false,true);
        vsMe.flip(false,true);
        vsMe2.flip(false,true);
        myHead.flip(false,true);
        oppoHead.flip(false,true);
        victorMonster.flip(false,true);
        loseMonster.flip(false,true);
        losebg.flip(false,true);

        // initialize common items for different players
        tree = new TextureRegion(new Texture(Gdx.files.internal("data/cooltree.png")));
        tree.flip(false,true);
        steak = new TextureRegion(new Texture(Gdx.files.internal("data/steak.png")));
        steak.flip(false,true);
        powerUp = new TextureRegion(new Texture(Gdx.files.internal("data/powerupsmall.png")));
        powerUp.flip(false,true);
        menuBg = new TextureRegion(new Texture(Gdx.files.internal("data/menubg.png")));
        menuBg.flip(false,true);

        // prepare monster and opponent monster source of actions and animations
        texture = TextureRegion.split(source, source.getWidth()/3, source.getHeight()/4);
        oppoTexture = TextureRegion.split(oppoSource, oppoSource.getWidth()/3, oppoSource.getHeight()/4);
        for (TextureRegion[] t:texture)
            for (TextureRegion tt: t)
                tt.flip(false, true);
        for (TextureRegion[] t:oppoTexture)
            for (TextureRegion tt: t)
                tt.flip(false, true);

        // static monster and opponent monster picture
        monsterUp = texture[3][0];
        monsterDown = texture[0][0];
        monsterLeft = texture[2][0];
        monsterRight = texture[1][0];

        oppoUp = oppoTexture[3][0];
        oppoDown = oppoTexture[0][0];
        oppoLeft = oppoTexture[2][0];
        oppoRight = oppoTexture[1][0];

        // define monster walking animation: time duration 0.15s
        upAnimation = new Animation(0.15f,new TextureRegion[] {monsterUp,texture[3][1],monsterUp,texture[3][2]});
        downaAnimation = new Animation(0.15f,new TextureRegion[] {monsterDown,texture[0][1],monsterDown,texture[0][2]});
        leftaAnimation = new Animation(0.15f,new TextureRegion[] {monsterLeft,texture[2][1],monsterLeft,texture[2][2]});
        rightaAnimation = new Animation(0.15f,new TextureRegion[] {monsterRight,texture[1][1],monsterRight,texture[1][2]});
        upAnimation.setPlayMode(PlayMode.LOOP);
        downaAnimation.setPlayMode(PlayMode.LOOP);
        leftaAnimation.setPlayMode(PlayMode.LOOP);
        rightaAnimation.setPlayMode(PlayMode.LOOP);

        //define opponent monste walking animtaion: time duration 0.15s
        upAnimationoppo = new Animation(0.15f,new TextureRegion[] {oppoUp,oppoTexture[3][1],oppoUp,oppoTexture[3][2]});
        downaAnimationoppo = new Animation(0.15f,new TextureRegion[] {oppoDown,oppoTexture[0][1],oppoDown,oppoTexture[0][2]});
        leftaAnimationoppo = new Animation(0.15f,new TextureRegion[] {oppoLeft,oppoTexture[2][1],oppoLeft,oppoTexture[2][2]});
        rightaAnimationoppo = new Animation(0.15f,new TextureRegion[] {oppoRight,oppoTexture[1][1],oppoRight,oppoTexture[1][2]});
        upAnimationoppo.setPlayMode(PlayMode.LOOP);
        downaAnimationoppo.setPlayMode(PlayMode.LOOP);
        leftaAnimationoppo.setPlayMode(PlayMode.LOOP);
        rightaAnimationoppo.setPlayMode(PlayMode.LOOP);

        // powerUp icon
        Spic = new TextureRegion(new Texture(Gdx.files.internal("data/speed.png")));
        Spic.flip(false, true);
        Vpic = new TextureRegion(new Texture(Gdx.files.internal("data/visibility.png")));
        Vpic.flip(false, true);
        Vpowerup = new Animation(1f,Spic);
        Spowerup = new Animation(1f,Vpic);

        // tug of war animation and pictures
        victorybg1 = new TextureRegion(new Texture(Gdx.files.internal("data/victorybg.png")));
        victorybg1.flip(false,true);
        victorybg2 = new TextureRegion(new Texture(Gdx.files.internal("data/victorybg2.png")));
        victorybg2.flip(false,true);
        victoryAnimation = new Animation(0.2f,new TextureRegion[] {victorybg1,victorybg2});
        victoryAnimation.setPlayMode(PlayMode.LOOP);

        // map war status bar clock animation
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

        // prepare for text display
        font = new BitmapFont(Gdx.files.internal("font/text.fnt"));
        font.setScale(screenwidth/1080,-screenheight/1920);
        shadow = new BitmapFont(Gdx.files.internal("font/shadow.fnt"));
        shadow.setScale(screenwidth/1080,-screenheight/1920);

        // arrow to hint opponent position
        arrow = new Texture(Gdx.files.internal("data/tango-left-arrow-red.png"));
        spriteArrow = new Sprite(arrow);

    }

    //dispose related object when super dispose was called
    public static void dispose(){
        source.dispose();
        font.dispose();
        shadow.dispose();
    }

}
