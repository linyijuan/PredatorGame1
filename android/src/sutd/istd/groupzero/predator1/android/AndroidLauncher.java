package sutd.istd.groupzero.predator1.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.plus.Plus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import sutd.istd.groupzero.gameobjects.Food;
import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.PowerUps;
import sutd.istd.groupzero.gameobjects.Tree;
import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.gameworld.PredatorGame;

public class AndroidLauncher extends AndroidApplication implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, RealTimeMessageReceivedListener,
        RoomStatusUpdateListener, RoomUpdateListener, OnInvitationReceivedListener,ActionResolver{
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;
    final static int RC_SIGN_IN = 9001;
    private int mapSizeX = 540;
    private int mapSizeY = 960;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingConnectionFailure, mSignInClicked= false;
    private boolean  mAutoStartSignInFlow= true;
    private ArrayList<Participant> mParticipants = null;
    private String mRoomId = null;
    private String mMyId = null;
    private String mIncomingInvitationId = null;
    private AndroidApplicationConfiguration config;
    private View gameView;
    private Map gameMap;
    private boolean useOppoTree,useOppoFood,useOppoPU = false;
    private Rectangle myCurrentBound = null;

    // Current state of the game:
    private ArrayList<Food> foodList = new ArrayList<Food>();
    private ArrayList<PowerUps> powerUpList = new ArrayList<PowerUps>();
    private ArrayList<Tree> treeList = new ArrayList<Tree>();

    private ArrayList<Food> oppofoodList = new ArrayList<Food>();
    private ArrayList<PowerUps> oppopowerUpList = new ArrayList<PowerUps>();
    private ArrayList<Tree> oppotreeList = new ArrayList<Tree>();
    private Vector2 opponentPosition = null;

    private int opponentDirectionKeycode = -100;
    private Vector2[] startPos = {new Vector2(0,0),new Vector2(mapSizeX-27,mapSizeY-34)};
    private Vector2 mystartPos = null;
    private int myTapCount,oppoTapCount,playerNum,opponentStrength;
    private float oppoSpeed = 1.0f;
    private boolean met,oppoWin,oppoLose,iStart,oppoStart  = false;
    private LinearLayout linearLayout;

    // This array lists all the individual screens our game has.
    final static int[] SCREENS = {R.id.screen_game, R.id.screen_main, R.id.screen_sign_in,R.id.screen_wait};
    int mCurScreen = -1;

    // This array lists everything that's clickable, so we can install click event handlers.
    final static int[] CLICKABLES = {
            R.id.button_accept_popup_invitation, R.id.button_invite_players,
            R.id.button_quick_game, R.id.button_see_invitations, R.id.button_sign_in,
            R.id.button_sign_out
    };

    // Reset game variables in preparation for a new game.
    private void resetGameVars() {
        gameMap = new Map(this);
        foodList = gameMap.getFoodList();
        powerUpList = gameMap.getPowerUpsList();
        treeList = gameMap.getTreeList();
        oppofoodList = new ArrayList<Food>();
        oppopowerUpList = new ArrayList<PowerUps>();
        oppotreeList = new ArrayList<Tree>();
        opponentPosition = null;
        linearLayout.removeViewAt(0);
        gameView = initializeForView(new PredatorGame(this,gameMap),config);
        linearLayout.addView(gameView,0,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        opponentStrength = 0;
        oppoSpeed = 1.0f;
        myTapCount = 0;
        oppoWin = false;
        oppoLose = false;
        oppoTapCount = 0;
        met = false;
        iStart = false;
        oppoStart = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Create the Google Api Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).addApi(Games.API).addScope(Games.SCOPE_GAMES).build();

        // set up a click listener defined below for different types of buttons on the menu main screen
        for (int id : CLICKABLES) {
            findViewById(id).setOnClickListener(this);
        }

        // when start the game for the first time:
        // 1. configure the libgdx setting for Android platform
        config = new AndroidApplicationConfiguration();
        // 2. initialize the concurrent and common map with related variables
        gameMap = new Map(this);
        foodList = gameMap.getFoodList();
        powerUpList = gameMap.getPowerUpsList();
        treeList = gameMap.getTreeList();
        // 3. attach libgdx game view in Android layout
        gameView = initializeForView(new PredatorGame(this,gameMap),config);
        linearLayout = (LinearLayout) findViewById(R.id.screen_game);
        linearLayout.addView(gameView,0,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
    }

    //onClickListener for all the buttons visible and hidden in main menu screen
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_sign_in:
                // start the sign-in flow.
                mSignInClicked = true;
                mGoogleApiClient.connect();
                break;
            case R.id.button_sign_out:
                // sign out and switch back to sign in screen.
                mSignInClicked = false;
                Games.signOut(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                switchToScreen(R.id.screen_sign_in);
                break;
            case R.id.button_invite_players:
                // show list of invitable players.
                intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 1);
                switchToScreen(R.id.screen_wait);
                // once player select opponents, start new game.
                startActivityForResult(intent, RC_SELECT_PLAYERS);
                break;
            case R.id.button_see_invitations:
                // show list of pending invitations
                intent = Games.Invitations.getInvitationInboxIntent(mGoogleApiClient);
                switchToScreen(R.id.screen_wait);
                startActivityForResult(intent, RC_INVITATION_INBOX);
                break;
            case R.id.button_accept_popup_invitation:
                // when user wants to accept the invitation shown on the invitation popup
                acceptInviteToRoom(mIncomingInvitationId);
                mIncomingInvitationId = null;
                break;
            case R.id.button_quick_game:
                // when user wants to play against a random opponent right now
                startQuickGame();
                break;
        }
    }

    /*
     * COMMUNICATIONS SECTION. Methods that implement the game's network
     * protocol.
     */

    //handle the activity result triggered by user interaction
    @Override
    public void onActivityResult(int requestCode, int responseCode,Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        switch (requestCode) {
            case RC_SELECT_PLAYERS:
                // got the result from the "select players" UI -- ready to create the room
                handleSelectPlayersResult(responseCode, intent);
                break;
            case RC_INVITATION_INBOX:
                // got the result from the "select invitation" UI (invitation inbox)and ready to accept the selected invitation:
                handleInvitationInboxResult(responseCode, intent);
                break;
            case RC_WAITING_ROOM:
                // got the result from the "waiting room" UI.
                if (responseCode == Activity.RESULT_OK) {
                    // ready to start playing
                    startGame();
                } else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player indicated that they want to leave the room
                    leaveRoom();
                } else if (responseCode == Activity.RESULT_CANCELED) {
                    // Dialog was cancelled (user pressed back key, for instance) and leave the room
                    leaveRoom();
                }
                break;
            case RC_SIGN_IN:
                // sign in
                mSignInClicked = false;
                mResolvingConnectionFailure = false;
                if (responseCode == RESULT_OK)
                    mGoogleApiClient.connect();
                else
                    showActivityResultError(this, requestCode, responseCode, R.string.signin_other_error);
                break;
        }
        super.onActivityResult(requestCode, responseCode, intent);
    }

    // Handle the result of the "Select players UI" launched when "Invite friends" button clicked.
    // React by creating a room with those players.
    private void handleSelectPlayersResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            switchToMainScreen();
            return;
        }
        // get the invitee list
        final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        // get the automatch criteria
        Bundle autoMatchCriteria = null;
        int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0)
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(minAutoMatchPlayers, maxAutoMatchPlayers, 0);
        // create game room
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.addPlayersToInvite(invitees);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        if (autoMatchCriteria != null)
            rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        Games.RealTimeMultiplayer.create(mGoogleApiClient, rtmConfigBuilder.build());
        //Room created, waiting for it to be ready...
    }

    // Handle the result of the invitation inbox UI, where the player can pick an invitation to accept.
    // React by accepting the selected invitation, if any.
    private void handleInvitationInboxResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            switchToMainScreen();
            return;
        }
        //Invitation inbox UI succeeded.
        Invitation inv = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);
        // accept invitation
        acceptInviteToRoom(inv.getInvitationId());
    }

    // Accept the given invitation.
    void acceptInviteToRoom(String invId) {
        // accept the invitation and join the game room before start the game
        RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
        roomConfigBuilder.setInvitationIdToAccept(invId).setMessageReceivedListener(this).setRoomStatusUpdateListener(this);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());
    }

    // Activity is going to the background. We have to leave the current room.
    @Override
    public void onStop() {
        // if we're in a room, leave it.
        leaveRoom();
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected())
            switchToScreen(R.id.screen_sign_in);
        else
            switchToScreen(R.id.screen_wait);
        super.onStop();
    }

    // Activity just got to the foreground.
    // Switch to the wait screen because we will now go through the sign-in flow
    @Override
    public void onStart() {
        switchToScreen(R.id.screen_wait);
        //connect GoogleApiClient if haven't
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
        } else {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    // Handle back key to make sure we cleanly leave a game if we are in the middle of one
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        // if user in game and press back key, we treat as leave room signal
        if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_game) {
            leaveRoom();
            return true;
        }
        return super.onKeyDown(keyCode, e);
    }

    // Show the waiting room UI to track the progress of other players as they enter the room and get connected.
    void showWaitingRoom(Room room) {
        // Since we require everyone to join the game before we start it, minimum number of players required for our game
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        // show waiting room UI
        startActivityForResult(Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, MIN_PLAYERS), RC_WAITING_ROOM);
    }

    // Called when we get an invitation to play a game. React by showing that to the user.
    @Override
    public void onInvitationReceived(Invitation invitation) {
        // got invitation to play a game -> Store it in mIncomingInvitationId and show the popup on the screen.
        mIncomingInvitationId = invitation.getInvitationId();
        ((TextView) findViewById(R.id.incoming_invitation_text)).setText(invitation.getInviter().getDisplayName() + " " +getString(R.string.is_inviting_you));
        // This will show the invitation popup
        switchToScreen(mCurScreen);
    }

    //Called when invitation is removed
    @Override
    public void onInvitationRemoved(String invitationId) {
        if (mIncomingInvitationId.equals(invitationId)) {
            mIncomingInvitationId = null;
            // This will hide the invitation popup
            switchToScreen(mCurScreen);
        }
    }

    /*
     * CALLBACKS SECTION. This section implements the gamesAPI callbacks.
     */

    // Called when sign in succeeded
    @Override
    public void onConnected(Bundle connectionHint) {
        // register listener so we are notified if we receive an invitation to play while we are in the game
        Games.Invitations.registerInvitationListener(mGoogleApiClient, this);
        if (connectionHint != null) {
            //connection hint provided. Checking for invite.
            Invitation inv = connectionHint.getParcelable(Multiplayer.EXTRA_INVITATION);
            if (inv != null && inv.getInvitationId() != null) {
                // retrieve and cache the invitation ID
                acceptInviteToRoom(inv.getInvitationId());
                return;
            }
        }
        switchToMainScreen();
    }

    // Called when connection is still suspended -> trying to reconnect
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    // Called when connection failed
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            //onConnectionFailed() ignoring connection failure; already resolving.
            return;
        }
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = resolveConnectionFailure(this, mGoogleApiClient,
                    connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
        }
        switchToScreen(R.id.screen_sign_in);
    }

    // Called when we are connected to the room but not ready to play yet.
    @Override
    public void onConnectedToRoom(Room room) {
        // get room ID, participants and my ID:
        mRoomId = room.getRoomId();
        mParticipants = room.getParticipants();
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(mGoogleApiClient));
        ArrayList<String> ids = room.getParticipantIds();
        // sort id to determine the player role for the two player
        Collections.sort(ids);
        if (mMyId.equals(ids.get(0))){
            // player 1 start from the left upper corner in the map
            mystartPos = new Vector2(startPos[0].x,startPos[0].y);
            playerNum = 1;
            // both players will use player 1's map
            broadcastMyMap();
        }
        else{
            // player 2 start from the right bottom corner in the map
            mystartPos = new Vector2(startPos[1].x,startPos[1].y);
            playerNum = 2;
        }
    }

    // Leave the room.
    void leaveRoom() {
        if (mRoomId != null) {
            // Google API for disconnecting from the room
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
            switchToScreen(R.id.screen_wait);
        }
    }
    // Called when we've successfully left the room as a result of calling leaveRoom().
    // If we get disconnected, we get onDisconnectedFromRoom()).
    @Override
    public void onLeftRoom(int statusCode, String roomId) {
        // see the leaving is because whether I or my opponent leave
        if (mRoomId !=null) {
            // we have left the room; return to main screen.
            switchToMainScreen();
            // alert dialog show "You have quit the game." if I volunteer to leave the game
            makeSimpleDialog(this, "Game Notification", "You have quit the game.").show();
        }
        // reset game variable for next round of game
        resetGameVars();
    }

    // Called when we get disconnected from the room. We return to the main screen.
    @Override
    public void onDisconnectedFromRoom(Room room) {
        Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
        mRoomId = null;
        // Give player a hint why disconnection happen
        showGameError();
    }

    // Show error message about game being cancelled and return to main screen.
    void showGameError() {
        makeSimpleDialog(this, getString(R.string.game_problem));
        switchToMainScreen();
    }

    // Called when room has been created
    @Override
    public void onRoomCreated(int statusCode, Room room) {
        // give player a hint why the matching failed
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            showGameError();
            return;
        }
        // show the waiting room UI
        showWaitingRoom(room);
    }

    // Called when room is fully connected.
    @Override
    public void onRoomConnected(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            showGameError();
            return;
        }
        updateRoom(room);
    }

    // Called when player join the room after room is created
    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            showGameError();
            return;
        }
        // show the waiting room UI
        showWaitingRoom(room);
    }

    // Treat update callbacks :
    // Update our list of participants and update the display.
    @Override
    public void onPeerDeclined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onP2PDisconnected(String participant) {}

    @Override
    public void onP2PConnected(String participant) {}

    @Override
    public void onPeerJoined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    // when peer leave the room, player will also leave the room automatically.
    @Override
    public void onPeerLeft(Room room, List<String> peersWhoLeft) {
        // playe leave the room due to leaving of peer
        if (mRoomId != null) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
            mRoomId = null;
        }
        // go back to main screen
        switchToScreen(R.id.screen_wait);
        switchToMainScreen();
        //notify that opponet leave the game
        makeSimpleDialog(this, "Game Notification", "Your opponent has quit the game.").show();
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        updateRoom(room);
    }

    @Override
    public void onRoomConnecting(Room room) {
        updateRoom(room);
    }

    @Override
    public void onPeersConnected(Room room, List<String> peers) {
        updateRoom(room);
    }

    // when peer disconnect from the room, player will also disconnect automatically.
    @Override
    public void onPeersDisconnected(Room room, List<String> peers) {
        // peer left will be called before this method,
        // so this is just to handle the situation when onPeerLeft() has not been called successfully
        if (mRoomId != null){
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
            mRoomId = null;
            switchToScreen(R.id.screen_wait);
            switchToMainScreen();
            makeSimpleDialog(this, "Game Notification", "Your opponent has quit the game.").show();
        }
    }

    // check the room status and leave the room when any peer leave the game
    void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
        }
        if (mParticipants != null) {
            if (mRoomId != null) {
                for (Participant p : mParticipants) {
                    String pid = p.getParticipantId();
                    if (pid.equals(mMyId))
                        continue;
                    if (p.getStatus() != Participant.STATUS_JOINED) {
                        leaveRoom();
                        break;
    }}}}}


    /*
     * GAME LOGIC SECTION. Methods that implement the game's rules.
     */

    // Start the gameplay phase of the game.
    void startGame() {
        // show waiting screen when initializing the game
        switchToScreen(R.id.screen_wait);
        // set the starting position of the monster
        gameMap.getMonster().setMyPosition(mystartPos);
        // tell opponent my position information
        broadcastMyStatus(gameMap.getMonster().getMyPosition(), gameMap.getMonster().getDirection());
        // show game play screen
        switchToScreen(R.id.screen_game);
    }

    // quick-start a game with 1 randomly selected opponent
    void startQuickGame() {
        // requirement of the number of players in one round
        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS, MAX_OPPONENTS, 0);
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        //create game room for randomly paired players
        Games.RealTimeMultiplayer.create(mGoogleApiClient, rtmConfigBuilder.build());
    }

    // Called when player receive real-time message from the network and handle different information
    @Override
    public void onRealTimeMessageReceived(RealTimeMessage rtm) {
        byte[] buf = rtm.getMessageData();
        // Received food position from broadcastMyMap():
        if (buf[0] == 'f'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            oppofoodList.add(new Food(new Vector2(Float.intBitsToFloat(xx),Float.intBitsToFloat(yy))));
        }
        // Received tree position from broadcastMyMap():
        else if (buf[0] == 't'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            oppotreeList.add(new Tree(new Vector2(Float.intBitsToFloat(xx),Float.intBitsToFloat(yy))));
        }
        // Received powerUp position from broadcastMyMap():
        else if(buf[0] == 'p'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            oppopowerUpList.add(new PowerUps(new Vector2(Float.intBitsToFloat(xx),Float.intBitsToFloat(yy)),(char)buf[9]+""));
        }
        // Received food eatten from eatFood():
        else if(buf[0] == 'e'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            float x = Float.intBitsToFloat(xx);
            float y = Float.intBitsToFloat(yy);
            Food remove = null;
            synchronized (foodList) {
                for (Food f : foodList) {
                    if (f.getPosition().x == x && f.getPosition().y == y) {
                        remove = f;
                        break;
                    }
                }
                boolean a = foodList.remove(remove);
            }
        }
        // Received food regenerated from eatFood():
        else if(buf[0] == 'd'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            synchronized (foodList) {
                foodList.add(new Food(new Vector2(Float.intBitsToFloat(xx), Float.intBitsToFloat(yy))));
            }
        }
        // Received powerUp obtained from obtainPowerUp():
        else if(buf[0] == 'o'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            float x = Float.intBitsToFloat(xx);
            float y = Float.intBitsToFloat(yy);
            PowerUps remove = null;
            synchronized (powerUpList) {
                for (PowerUps p : powerUpList) {
                    if (p.getPosition().x == x && p.getPosition().y == y) {
                        remove = p;
                        break;
                    }
                }
                powerUpList.remove(remove);
            }
        }
        // Received powerUp regenerated from obtainPowerUp():
        else if(buf[0] == 'c'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            synchronized (powerUpList) {
                powerUpList.add(new PowerUps(new Vector2(Float.intBitsToFloat(xx), Float.intBitsToFloat(yy)), (char)buf[9] + ""));
            }
        }
        // Received tapping count from broadcastMyTapping():
        else if (buf[0] == 'a'){
            oppoTapCount++;
        }
        // Received strength from broadcastMyStrength():
        else if(buf[0] == 'b'){
            opponentStrength = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
        }
        // Received speed from broadcastMySpeed():
        else if(buf[0] == 's'){
            int s = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            oppoSpeed = Float.intBitsToFloat(s);
        }
        // Received met signal from weHaveMet():
        else if(buf[0] == 'z'){
            met = true;
        }
        // Received win signal from iWin():
        else if(buf[0] == 'w'){
            oppoWin = true;
        }
        // Received lose signal from iLose():
        else if(buf[0] == 'l'){
            oppoLose = true;
        }
        // Received start signal from iStart():
        else if(buf[0] == 'g'){
            oppoStart = true;
        }
        // Received position and direction from broadcastMyStatus():
        else{
            opponentDirectionKeycode = (buf[0] & 0xFF)| ((buf[1] & 0xFF) << 8)| ((buf[2] & 0xFF) << 16)| ((buf[3] & 0xFF) << 24);
            int xx = (buf[4] & 0xFF)| ((buf[5] & 0xFF) << 8)| ((buf[6] & 0xFF) << 16)| ((buf[7] & 0xFF) << 24);
            int yy = (buf[8] & 0xFF)| ((buf[9] & 0xFF) << 8)| ((buf[10] & 0xFF) << 16)| ((buf[11] & 0xFF) << 24);
            opponentPosition = new Vector2(Float.intBitsToFloat(xx),Float.intBitsToFloat(yy));
        }
    }

    // methods implemented in interface ActionResolver for communication between Android and Core Porjects
    /* Start of Game: broadcast my start and receive my opponent's start*/
    public boolean iStart(){
        if(iStart == false){
            iStart = true;
            byte[] bytes = new byte[1];
            bytes[0] = 'g';
            broadcastMsg(bytes);
        }
        return iStart;

    }
    public boolean didYouStart(){
        return oppoStart;
    }

    // Player 1 broadcast the map, so player 2 can use player 1's map
    public synchronized void broadcastMyMap(){
        // send food position
        synchronized (foodList){
            for (Food f:foodList){
                byte[] buf = new byte[9];
                buf[0] = 'f';
                storeFloat(buf,f.getPosition().x,1);
                storeFloat(buf,f.getPosition().y,5);
                broadcastMsg(buf);
            }}
        // send powerup position
        synchronized (powerUpList){
            for (PowerUps p :powerUpList){
                byte[] buf = new byte[10];
                buf[0] = 'p';
                storeFloat(buf,p.getPosition().x,1);
                storeFloat(buf,p.getPosition().y,5);
                buf[9] = (byte)(p.getKind().equals("s")?'s':'v');
                broadcastMsg(buf);
            }}
        // send tree position
        for (Tree t:treeList){
            byte[] buf = new byte[9];
            buf[0] = 't';
            storeFloat(buf,t.getPosition().x,1);
            storeFloat(buf,t.getPosition().y,5);
            broadcastMsg(buf);
        }
    }

    /* Map War: broadcast my position and receive opponent's position*/
    public void broadcastMyStatus(Vector2 currentPosition, Monster.Direction currentDirection){
        myCurrentBound = new Rectangle(currentPosition.x,currentPosition.y,27,34);
        byte[] bytes = new byte[12];
        storeInt(bytes,currentDirection.getKeycode(),0);
        storeFloat(bytes,currentPosition.x,4);
        storeFloat(bytes,currentPosition.y,8);

        if (mRoomId !=null){
            // Send to every other participant.
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(mMyId))
                    continue;
                if (p.getStatus() != Participant.STATUS_JOINED)
                    continue;
                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, bytes, mRoomId,p.getParticipantId());
            }
        }
    }
    public Vector2 requestOpponentPosition(){
        return opponentPosition;
    }
    public int requestOpponentDirection(){
        return opponentDirectionKeycode;
    }

    /* Map War: broadcast my strength and receive opponent's strength*/
    public void broadcastMyStrength(int strength){
        byte[] bytes = new byte[5];
        bytes[0] = 'b';
        storeInt(bytes,strength,1);
        broadcastMsg(bytes);
    }
    public int requestOpponentStrength(){
        return opponentStrength;
    }

    /* Map War: broadcast my speed and receive opponent's speed*/
    public float requestOpponentSpeed(){return oppoSpeed;}
    public void broadcastMySpeed(float speed){
        byte[] buf = new byte[5];
        buf[0] = 's';
        storeFloat(buf,speed,1);
        broadcastMsg(buf);
    }

    /* Map War: notify the meeting of two monsters to each other*/
    public boolean haveWeMet(){
        return met;
    }
    public void weHaveMet(){
        byte[] b = new byte[1];
        b[0] = 'z';
        broadcastMsg(b);
    }

    /* Map War:when eat a food on the map:
    // 1. remove from my local list and regenerate one on local map
    // 2. broadcast the eatten food position and regenerated food position
    */
    public void eatFood(Food f){
        byte[] buf = new byte[9];
        buf[0] = 'e';
        storeFloat(buf,f.getPosition().x,1);
        storeFloat(buf,f.getPosition().y,5);
        broadcastMsg(buf);
        synchronized (foodList) {
            foodList.remove(f);
            //food regeneration & check the overlapping with other items
            boolean toPlace;
            while (foodList.size() < 10) {
                toPlace = true;
                Vector2 v = new Vector2(cap(0, mapSizeX - 30), cap(0, mapSizeY - 21));
                Food food = new Food(v);
                if (!treeList.isEmpty()) {
                    for (Tree t : treeList)
                        if (Intersector.overlaps(t.getBound(), food.getBound()) || Intersector.overlaps(food.getBound(), myCurrentBound)) {
                            toPlace = false;
                            break;
                        }
                }
                if (toPlace && !powerUpList.isEmpty()) {
                    for (PowerUps p : powerUpList)
                        if (Intersector.overlaps(p.getBound(), food.getBound()) || Intersector.overlaps(food.getBound(), myCurrentBound)) {
                            toPlace = false;
                            break;
                        }
                }

                if (toPlace && !foodList.isEmpty()) {
                    for (Food ff : foodList)
                        if (Intersector.overlaps(ff.getBound(), food.getBound()) || Intersector.overlaps(food.getBound(), myCurrentBound)) {
                            toPlace = false;
                            break;
                        }
                }
                if (toPlace) {
                    foodList.add(food);
                    byte[] buf1 = new byte[9];
                    buf1[0] = 'd';
                    storeFloat(buf1, food.getPosition().x, 1);
                    storeFloat(buf1, food.getPosition().y, 5);
                    broadcastMsg(buf1);
                }
            }
        }

    }

    /* Map War:when eat a powerUp on the map:
    // 1. remove from my local list and regenerate one on local map
    // 2. broadcast the obtained powerUp position and regenerated powerUp position
    */
    public void obtainPowerUp(PowerUps p){
        // broadcast the obtained powerUp position
        byte[] buf = new byte[10];
        buf[0] = 'o';
        storeFloat(buf,p.getPosition().x,1);
        storeFloat(buf,p.getPosition().y,5);
        buf[9] = (byte)(p.getKind().equals("s")?'s':'v');
        broadcastMsg(buf);
        synchronized (powerUpList) {
            //remove from myPowerUpList
            powerUpList.remove(p);
            //powerup regeneration & check the overlapping with other items
            boolean toPlace;
            while (powerUpList.size() < 10) {
                toPlace = true;
                Vector2 v = new Vector2(cap(0, mapSizeX - 22), cap(0, mapSizeY - 21));
                PowerUps powerUp = new PowerUps(v, "s");
                powerUp.setKind(p.getKind());
                if (!treeList.isEmpty()) {
                    for (Tree t : treeList)
                        if (Intersector.overlaps(t.getBound(), powerUp.getBound()) || Intersector.overlaps(powerUp.getBound(), myCurrentBound)) {
                            toPlace = false;
                            break;
                        }
                }
                if (toPlace && !powerUpList.isEmpty()) {
                    for (PowerUps pp : powerUpList)
                        if (Intersector.overlaps(pp.getBound(), powerUp.getBound()) || Intersector.overlaps(powerUp.getBound(), myCurrentBound)) {
                            toPlace = false;
                            break;
                        }
                }

                if (toPlace && !foodList.isEmpty()) {
                    for (Food f : foodList)
                        if (Intersector.overlaps(f.getBound(), powerUp.getBound()) || Intersector.overlaps(powerUp.getBound(), myCurrentBound)) {
                            toPlace = false;
                            break;
                        }
                }
                if (toPlace) {
                    // add to myPowerUpList
                    powerUpList.add(powerUp);
                    //broadcast regenerated powerUp position
                    byte[] buf1 = new byte[10];
                    buf1[0] = 'c';
                    storeFloat(buf1, powerUp.getPosition().x, 1);
                    storeFloat(buf1, powerUp.getPosition().y, 5);
                    buf1[9] = (byte) (powerUp.getKind().equals("s") ? 's' : 'v');
                    broadcastMsg(buf1);
                }
            }
        }

    }
    // random generation method
    private Random r = new Random();
    public int cap(int min, int max){
        int  x = r.nextInt();
        while(x > max || x < min){
            x = r.nextInt(max);
        }
        return x;
    }

    /* Tug of War: broadcast my tapping count and receive opponent's tapping count*/
    public void broadcastMyTapping(){
        myTapCount++;
        byte[] bytes = new byte[1];
        bytes[0] = 'a';
        broadcastMsg(bytes);
    }
    public int requestOppoTapCount(){
        return oppoTapCount;
    }
    public int requestMyTapCount(){
        return myTapCount;
    }

    /* Tug of War: broadcast the win and lose ending to each other*/
    public boolean haveYouWin(){
        return oppoWin;
    }
    public void iWin(){
        byte[] b = new byte[1];
        b[0] = 'w';
        broadcastMsg(b);
    }
    public boolean haveYouLose(){
        return oppoLose;
    }
    public void iLose(){
        byte[] b = new byte[1];
        b[0] = 'l';
        broadcastMsg(b);
    }

    // generic pattern for converting float to byte array
    public void storeFloat(byte[] b, float f, int start){
        int x = Float.floatToIntBits(f);
        storeInt(b,x,start);
    }
    // generic pattern for converting integer to byte array
    public void storeInt(byte[] b, int x, int start){
        b[start] = (byte)(x & 0xff);
        b[start+1] = (byte)((x >> 8) & 0xff);
        b[start+2] = (byte)((x >> 16) & 0xff);
        b[start+3] = (byte)((x >> 24) & 0xff);
    }
    // generic patter for passing message in byte array
    public void broadcastMsg(byte[] bytes){
        if (mRoomId!=null) {
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(mMyId))
                    continue;
                if (p.getStatus() != Participant.STATUS_JOINED)
                    continue;
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, bytes, mRoomId, p.getParticipantId());
            }
        }
    }

    // return the treeList that player should use
    public ArrayList<Tree> requestTrees(){
        if (oppotreeList.size() > 1) {
            if (!useOppoTree) {
                // if received tree list from player 1 and have not change local list, update myTreeList
                useOppoTree = true;
                treeList = new ArrayList<Tree>();
                for (Tree t : oppotreeList) {
                    treeList.add(t);
                }
            }
        }
        return treeList;
   }
    // return the powerUpList that player should use
    public ArrayList<Food> requestFoods(){
        if (oppofoodList.size() > 1) {
            if (!useOppoFood) {
                // if received food list from player 1 and have not change local list, update myFoodList
                useOppoFood = true;
                foodList = new ArrayList<Food>();
                for (Food f : oppofoodList) {
                    foodList.add(new Food(f.getPosition()));
                }
            }
        }
        return foodList;
   }
    // return the powerUpList that player should use
    public ArrayList<PowerUps> requestPUs(){
        if (oppopowerUpList.size() > 1) {
            if (!useOppoPU) {
                // if received powerUp list from player 1 and have not change local list, update myPowerUpList
                useOppoPU = true;
                powerUpList = new ArrayList<PowerUps>();
                for (PowerUps p : oppopowerUpList) {
                    powerUpList.add(new PowerUps(p.getPosition(), p.getKind()));
                }
            }
        }
        return powerUpList;
   }

    // return my role in the game decided in onConnectedToRoom()
    public int requestMyPlayerNum(){
        return playerNum;
    }

    /*
     * UI SECTION. Methods that implement the game's UI.
     */
    // screen switching
    void switchToScreen(int screenId) {
        // make the requested screen visible; hide all others.
        for (int id : SCREENS) {
            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
            gameView.setVisibility(screenId == R.id.screen_game? View.VISIBLE: View.GONE);
        }
        mCurScreen = screenId;

        // should we show the invitation popup?
        boolean showInvPopup;
        if (mIncomingInvitationId == null) {
            // no invitation, so no popup
            showInvPopup = false;
        } else {
            // only show invitation on main screen
            showInvPopup = (mCurScreen == R.id.screen_main);
        }
        findViewById(R.id.invitation_popup).setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
    }
    // switch screen to main
    void switchToMainScreen() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            switchToScreen(R.id.screen_main);
        }
        else {
            switchToScreen(R.id.screen_sign_in);
        }
    }

    /*
     * BASE GAME UTILIZATION TOOLS. Provided basic game functions.
     */
    // Sets the flag to keep this screen on during the handshake,because if the screen turns off, the game will be cancelled.
    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * Show an {@link android.app.AlertDialog} with an 'OK' button and a message.
     *
     * @param activity the Activity in which the Dialog should be displayed.
     * @param message the message to display in the Dialog.
     */
    public static void showAlert(Activity activity, String message) {
        (new AlertDialog.Builder(activity)).setMessage(message)
                .setNeutralButton(android.R.string.ok, null).create().show();
    }

    /**
     * Resolve a connection failure from
     * {@link com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)}
     *
     * @param activity the Activity trying to resolve the connection failure.
     * @param client the GoogleAPIClient instance of the Activity.
     * @param result the ConnectionResult received by the Activity.
     * @param requestCode a request code which the calling Activity can use to identify the result
     *                    of this resolution in onActivityResult.
     * @param fallbackErrorMessage a generic error message to display if the failure cannot be resolved.
     * @return true if the connection failure is resolved, false otherwise.
     */
    public static boolean resolveConnectionFailure(Activity activity,
                                                   GoogleApiClient client, ConnectionResult result, int requestCode,
                                                   String fallbackErrorMessage) {

        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(activity, requestCode);
                return true;
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                client.connect();
                return false;
            }
        } else {
            // not resolvable... so show an error message
            int errorCode = result.getErrorCode();
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
                    activity, requestCode);
            if (dialog != null) {
                dialog.show();
            } else {
                // no built-in dialog: show the fallback error message
                showAlert(activity, fallbackErrorMessage);
            }
            return false;
        }
    }

    /**
     * Show a {@link android.app.Dialog} with the correct message for a connection error.
     *  @param activity the Activity in which the Dialog should be displayed.
     * @param requestCode the request code from onActivityResult.
     * @param actResp the response code from onActivityResult.
     * @param errorDescription the resource id of a String for a generic error message.
     */
    public static void showActivityResultError(Activity activity, int requestCode, int actResp, int errorDescription) {
        if (activity == null) {
            return;
        }
        Dialog errorDialog;

        switch (actResp) {
            case GamesActivityResultCodes.RESULT_APP_MISCONFIGURED:
                errorDialog = makeSimpleDialog(activity,
                        activity.getString(R.string.app_misconfigured));
                break;
            case GamesActivityResultCodes.RESULT_SIGN_IN_FAILED:
                errorDialog = makeSimpleDialog(activity,
                        activity.getString(R.string.sign_in_failed));
                break;
            case GamesActivityResultCodes.RESULT_LICENSE_FAILED:
                errorDialog = makeSimpleDialog(activity,
                        activity.getString(R.string.license_failed));
                break;
            default:
                // No meaningful Activity response code, so generate default Google
                // Play services dialog
                final int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
                errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
                        activity, requestCode, null);
                if (errorDialog == null) {
                    // get fallback dialog
                    errorDialog = makeSimpleDialog(activity, activity.getString(errorDescription));
                }
        }

        errorDialog.show();
    }

    /**
     * Create a simple {@link android.app.Dialog} with an 'OK' button and a message.
     *
     * @param activity the Activity in which the Dialog should be displayed.
     * @param text the message to display on the Dialog.
     * @return an instance of {@link android.app.AlertDialog}
     */
    public static Dialog makeSimpleDialog(Activity activity, String text) {
        return (new AlertDialog.Builder(activity)).setMessage(text)
                .setNeutralButton(android.R.string.ok, null).create();
    }

    /**
     * Create a simple {@link android.app.Dialog} with an 'OK' button, a title, and a message.
     *
     * @param activity the Activity in which the Dialog should be displayed.
     * @param title the title to display on the dialog.
     * @param text the message to display on the Dialog.
     * @return an instance of {@link android.app.AlertDialog}
     */
    public static Dialog makeSimpleDialog(Activity activity, String title, String text) {
        return (new AlertDialog.Builder(activity))
                .setTitle(title)
                .setMessage(text)
                .setNeutralButton(android.R.string.ok, null)
                .create();
    }

}
