package sutd.istd.groupzero.predator1.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.predator1.PredatorGame;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener,ActionResolver{
    private GameHelper gameHelper;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new PredatorGame(this), config);

        if (gameHelper == null) {
            gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
            //gameHelper.enableDebugLog(true);
        }
        gameHelper.setup(this);
	}

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }


    @Override
    public boolean getSignedInGPGS() {
        return gameHelper.isSignedIn();
    }

    @Override
    public void loginGPGS() {
        try {
            runOnUiThread(new Runnable(){
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (final Exception ex) {
        }
    }

//    @Override
//    public void submitScoreGPGS(int score,String id) {
//        Games.Leaderboards.submitScore(gameHelper.getApiClient(), id, score);
//
//    }
//
//    @Override
//    public void unlockAchievementGPGS(String achievementId) {
//        Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
//    }
//
//    @Override
//    public void getLeaderboardGPGS(String id) {
//        if (gameHelper.isSignedIn()) {
//            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), id), 100);
//        }
//        else if (!gameHelper.isConnecting()) {
//            loginGPGS();
//        }
//    }
//
//    @Override
//    public void getAchievementsGPGS() {
//        if (gameHelper.isSignedIn()) {
//            startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
//        }
//        else if (!gameHelper.isConnecting()) {
//            loginGPGS();
//        }
//
//    }

}
