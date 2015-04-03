package sutd.istd.groupzero.predator1.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.google.android.gms.common.ConnectionResult;
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

import sutd.istd.groupzero.gameobjects.Food;
import sutd.istd.groupzero.gameobjects.Map;
import sutd.istd.groupzero.gameobjects.Monster;
import sutd.istd.groupzero.gameobjects.PowerUps;
import sutd.istd.groupzero.gameobjects.Tree;
import sutd.istd.groupzero.helpers.ActionResolver;
import sutd.istd.groupzero.predator1.PredatorGame;

public class AndroidLauncher extends AndroidApplication implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, RealTimeMessageReceivedListener,
        RoomStatusUpdateListener, RoomUpdateListener, OnInvitationReceivedListener,ActionResolver{
    final static String TAG = "ButtonClicker2000";
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingConnectionFailure = false;
    private boolean mSignInClicked = false;
    private boolean mAutoStartSignInFlow = true;
    String mRoomId = null;
    boolean mMultiplayer = false;
    ArrayList<Participant> mParticipants = null;
    String mMyId = null;
    String mIncomingInvitationId = null;
    byte[] mMsgBuf = new byte[3];
    private View gameView;
    private Map gameMap;
    private AndroidApplicationConfiguration config;
    private boolean getMapfromOpponent = false;
    private boolean useOppoTree = false;
    private boolean useOppoFood = false;
    private boolean useOppoPU = false;
    private Object foodLock = new Object();
    private Object treeLock = new Object();
    private Object puLock = new Object();
    private int mapSizeX = 540;
    private int mapSizeY = 960;
    private int opponentStrength = -1;

    // Current state of the game:
    ArrayList<Food> foodList = new ArrayList<Food>();
    ArrayList<PowerUps> powerUpList = new ArrayList<PowerUps>();
    ArrayList<Tree> treeList = new ArrayList<Tree>();

    ArrayList<Food> oppofoodList = new ArrayList<Food>();
    ArrayList<PowerUps> oppopowerUpList = new ArrayList<PowerUps>();
    ArrayList<Tree> oppotreeList = new ArrayList<Tree>();
    Vector2 myMonsterPosition;
    int strength;
    float speed;
    Vector2 opponentPosition = null;
    int opponentDirectionKeycode = -100;
    Vector2[] startPos = {new Vector2(0,0),new Vector2(mapSizeX-27,mapSizeY-34)};
    Vector2 mystartPos = null;
    private int myTapCount = 0;
    private int oppoTapCount = 0;
    private int playerNum = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Create the Google Api Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        // set up a click listener for everything we care about
        for (int id : CLICKABLES) {
            findViewById(id).setOnClickListener(this);
        }

        config = new AndroidApplicationConfiguration();
        gameMap = new Map(this);
        foodList = gameMap.getFoodList();
        powerUpList = gameMap.getPowerUpsList();
        treeList = gameMap.getTreeList();
        gameView = initializeForView(new PredatorGame(this,gameMap),config);
//        gameView = initializeForView(new PredatorGame(this),config);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.screen_game);
        linearLayout.addView(gameView,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_single_player:
            case R.id.button_single_player_2:
                // play a single-player game
//                resetGameVars();
                startGame(false);
                break;
            case R.id.button_sign_in:
                // user wants to sign in
                // Check to see the developer who's running this sample code read the instructions :-)
                // NOTE: this check is here only because this is a sample! Don't include this
                // check in your actual production app.
                if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id)) {
                    Log.w(TAG, "*** Warning: setup problems detected. Sign in may not work!");
                }

                // start the sign-in flow
                Log.d(TAG, "Sign-in button clicked");
                mSignInClicked = true;
                mGoogleApiClient.connect();
                break;
            case R.id.button_sign_out:
                // user wants to sign out
                // sign out.
                Log.d(TAG, "Sign-out button clicked");
                mSignInClicked = false;
                Games.signOut(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                switchToScreen(R.id.screen_sign_in);
                break;
            case R.id.button_invite_players:
                // show list of invitable players
                intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 3);
                switchToScreen(R.id.screen_wait);
                startActivityForResult(intent, RC_SELECT_PLAYERS);
                break;
            case R.id.button_see_invitations:
                // show list of pending invitations
                intent = Games.Invitations.getInvitationInboxIntent(mGoogleApiClient);
                switchToScreen(R.id.screen_wait);
                startActivityForResult(intent, RC_INVITATION_INBOX);
                break;
            case R.id.button_accept_popup_invitation:
                // user wants to accept the invitation shown on the invitation popup
                // (the one we got through the OnInvitationReceivedListener).
                acceptInviteToRoom(mIncomingInvitationId);
                mIncomingInvitationId = null;
                break;
            case R.id.button_quick_game:
                // user wants to play against a random opponent right now
                startQuickGame();
                break;
        }
    }

    void startQuickGame() {
        // quick-start a game with 1 randomly selected opponent
        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS, MAX_OPPONENTS, 0);
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
//        resetGameVars();
        Games.RealTimeMultiplayer.create(mGoogleApiClient, rtmConfigBuilder.build());
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        switch (requestCode) {
            case RC_SELECT_PLAYERS:
                // we got the result from the "select players" UI -- ready to create the room
                handleSelectPlayersResult(responseCode, intent);
                break;
            case RC_INVITATION_INBOX:
                // we got the result from the "select invitation" UI (invitation inbox). We're
                // ready to accept the selected invitation:
                handleInvitationInboxResult(responseCode, intent);
                break;
            case RC_WAITING_ROOM:
                // we got the result from the "waiting room" UI.
                if (responseCode == Activity.RESULT_OK) {
                    // ready to start playing
                    Log.d(TAG, "Starting game (waiting room returned OK).");
                    startGame(true);
                } else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player indicated that they want to leave the room
                    leaveRoom();
                } else if (responseCode == Activity.RESULT_CANCELED) {
                    // Dialog was cancelled (user pressed back key, for instance). In our game,
                    // this means leaving the room too. In more elaborate games, this could mean
                    // something else (like minimizing the waiting room UI).
                    leaveRoom();
                }
                break;
            case RC_SIGN_IN:
                Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                        + responseCode + ", intent=" + intent);
                mSignInClicked = false;
                mResolvingConnectionFailure = false;
                if (responseCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                } else {
                    BaseGameUtils.showActivityResultError(this,requestCode,responseCode, R.string.signin_other_error);
                }
                break;
        }
        super.onActivityResult(requestCode, responseCode, intent);
    }

    // Handle the result of the "Select players UI" we launched when the user clicked the
    // "Invite friends" button. We react by creating a room with those players.
    private void handleSelectPlayersResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w(TAG, "*** select players UI cancelled, " + response);
            switchToMainScreen();
            return;
        }

        Log.d(TAG, "Select players UI succeeded.");

        // get the invitee list
        final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        Log.d(TAG, "Invitee count: " + invitees.size());

        // get the automatch criteria
        Bundle autoMatchCriteria = null;
        int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            Log.d(TAG, "Automatch criteria: " + autoMatchCriteria);
        }

        // create the room
        Log.d(TAG, "Creating room...");
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.addPlayersToInvite(invitees);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        if (autoMatchCriteria != null) {
            rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        }
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
//        resetGameVars();
        Games.RealTimeMultiplayer.create(mGoogleApiClient, rtmConfigBuilder.build());
        Log.d(TAG, "Room created, waiting for it to be ready...");
    }

    // Handle the result of the invitation inbox UI, where the player can pick an invitation
    // to accept. We react by accepting the selected invitation, if any.
    private void handleInvitationInboxResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w(TAG, "*** invitation inbox UI cancelled, " + response);
            switchToMainScreen();
            return;
        }

        Log.d(TAG, "Invitation inbox UI succeeded.");
        Invitation inv = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);

        // accept invitation
        acceptInviteToRoom(inv.getInvitationId());
    }

    // Accept the given invitation.
    void acceptInviteToRoom(String invId) {
        // accept the invitation
        Log.d(TAG, "Accepting invitation: " + invId);
        RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
        roomConfigBuilder.setInvitationIdToAccept(invId).setMessageReceivedListener(this).setRoomStatusUpdateListener(this);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
//        resetGameVars();
        Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());
    }

    // Activity is going to the background. We have to leave the current room.
    @Override
    public void onStop() {
        Log.d(TAG, "**** got onStop");

        // if we're in a room, leave it.
        leaveRoom();

        // stop trying to keep the screen on
//        stopKeepingScreenOn();

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){
            switchToScreen(R.id.screen_sign_in);
        }
        else {
            switchToScreen(R.id.screen_wait);
        }
        super.onStop();
    }

    // Activity just got to the foreground. We switch to the wait screen because we will now
    // go through the sign-in flow (remember that, yes, every time the Activity comes back to the
    // foreground we go through the sign-in flow -- but if the user is already authenticated,
    // this flow simply succeeds and is imperceptible).
    @Override
    public void onStart() {
        switchToScreen(R.id.screen_wait);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Log.w(TAG,"GameHelper: client was already connected on onStart()");
        } else {
            Log.d(TAG,"Connecting client.");
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    // Handle back key to make sure we cleanly leave a game if we are in the middle of one
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_game) {
            leaveRoom();
            return true;
        }
        return super.onKeyDown(keyCode, e);
    }

    // Leave the room.
    void leaveRoom() {
        Log.d(TAG, "Leaving room.");
//        stopKeepingScreenOn();
        if (mRoomId != null) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
            mRoomId = null;
            switchToScreen(R.id.screen_wait);
        } else {
            switchToMainScreen();
        }
    }

    // Show the waiting room UI to track the progress of other players as they enter the
    // room and get connected.
    void showWaitingRoom(Room room) {
        // minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, MIN_PLAYERS);
        // show waiting room UI
        startActivityForResult(i, RC_WAITING_ROOM);
    }

    // Called when we get an invitation to play a game. We react by showing that to the user.
    @Override
    public void onInvitationReceived(Invitation invitation) {
        // We got an invitation to play a game! So, store it in
        // mIncomingInvitationId
        // and show the popup on the screen.
        mIncomingInvitationId = invitation.getInvitationId();
        ((TextView) findViewById(R.id.incoming_invitation_text)).setText(invitation.getInviter().getDisplayName() + " " +getString(R.string.is_inviting_you));
        switchToScreen(mCurScreen); // This will show the invitation popup
    }

    @Override
    public void onInvitationRemoved(String invitationId) {
        if (mIncomingInvitationId.equals(invitationId)) {
            mIncomingInvitationId = null;
            switchToScreen(mCurScreen); // This will hide the invitation popup
        }
    }

    /*
     * CALLBACKS SECTION. This section shows how we implement the several games
     * API callbacks.
     */

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "onConnected() called. Sign in successful!");
        Log.d(TAG, "Sign-in succeeded.");

        // register listener so we are notified if we receive an invitation to play
        // while we are in the game
        Games.Invitations.registerInvitationListener(mGoogleApiClient, this);

        if (connectionHint != null) {
            Log.d(TAG, "onConnected: connection hint provided. Checking for invite.");
            Invitation inv = connectionHint.getParcelable(Multiplayer.EXTRA_INVITATION);
            if (inv != null && inv.getInvitationId() != null) {
                // retrieve and cache the invitation ID
                Log.d(TAG,"onConnected: connection hint has a room invite!");
                acceptInviteToRoom(inv.getInvitationId());
                return;
            }
        }
        switchToMainScreen();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);

        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient,
                    connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
        }

        switchToScreen(R.id.screen_sign_in);
    }

    // Called when we are connected to the room. We're not ready to play yet! (maybe not everybody
    // is connected yet).
    @Override
    public void onConnectedToRoom(Room room) {
        Log.d(TAG, "onConnectedToRoom.");
        // get room ID, participants and my ID:
        mRoomId = room.getRoomId();
        mParticipants = room.getParticipants();
        ArrayList<String> ids = room.getParticipantIds();
        Collections.sort(ids);
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(mGoogleApiClient));
        Log.d("roompeople1",String.valueOf(mMyId.equals(mParticipants.get(0).getParticipantId())));
        Log.d("roompeople2",String.valueOf(mMyId.equals(mParticipants.get(1).getParticipantId())));
        mMultiplayer = true;

        if (mMyId.equals(ids.get(0))){
            mystartPos = startPos[0];
            playerNum = 1;
            broadcastMyMap();
        }
        else{
            mystartPos = startPos[1];
            playerNum = 2;
        }


        // print out the list of participants (for debug purposes)
        Log.d(TAG, "Room ID: " + mRoomId);
        Log.d(TAG, "My ID " + mMyId);
        Log.d(TAG, "<< CONNECTED TO ROOM>>");
    }

    // Called when we've successfully left the room (this happens a result of voluntarily leaving
    // via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
    @Override
    public void onLeftRoom(int statusCode, String roomId) {
        // we have left the room; return to main screen.
        Log.d(TAG, "onLeftRoom, code " + statusCode);
        switchToMainScreen();
    }

    // Called when we get disconnected from the room. We return to the main screen.
    @Override
    public void onDisconnectedFromRoom(Room room) {
        mRoomId = null;
        showGameError();

    }

    // Show error message about game being cancelled and return to main screen.
    void showGameError() {
        BaseGameUtils.makeSimpleDialog(this, getString(R.string.game_problem));
        switchToMainScreen();
    }

    // Called when room has been created
    @Override
    public void onRoomCreated(int statusCode, Room room) {
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

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            showGameError();
            return;
        }
        // show the waiting room UI
        showWaitingRoom(room);
    }

    // We treat most of the room update callbacks in the same way: we update our list of
    // participants and update the display. In a real game we would also have to check if that
    // change requires some action like removing the corresponding player avatar from the screen,
    // etc.
    @Override
    public void onPeerDeclined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onP2PDisconnected(String participant) {
    }

    @Override
    public void onP2PConnected(String participant) {
    }

    @Override
    public void onPeerJoined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerLeft(Room room, List<String> peersWhoLeft) {
        updateRoom(room);
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

    @Override
    public void onPeersDisconnected(Room room, List<String> peers) {
        updateRoom(room);
    }

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
                        BaseGameUtils.makeSimpleDialog(this, "Game Notification", "your opponent have left this game");
                        break;
                    }
                }
            }

        }

    }

    /*
     * GAME LOGIC SECTION. Methods that implement the game's rules.
     */

    // Reset game variables in preparation for a new game.
//    void resetGameVars() {
//        gameMap.reset();
//        foodList = gameMap.getFoodList();
//        powerUpList = gameMap.getPowerUpsList();
//        treeList = gameMap.getTreeList();
//    }

    // Start the gameplay phase of the game.
    void startGame(boolean multiplayer) {
        mMultiplayer = multiplayer;
        if(multiplayer){
            gameMap.getMonster().setMyPosition(mystartPos);
            broadcastMyStatus(gameMap.getMonster().getMyPosition(),gameMap.getMonster().getDirection());
        }
        switchToScreen(R.id.screen_game);
    }

    /*
     * COMMUNICATIONS SECTION. Methods that implement the game's network
     * protocol.
     */
    // Called when we receive a real-time message from the network.
    public void broadcastMyTapping(){
        myTapCount++;
        byte[] bytes = new byte[1];
        bytes[0] = 'a';

        for (Participant p : mParticipants) {
            if (p.getParticipantId().equals(mMyId))
                continue;
            if (p.getStatus() != Participant.STATUS_JOINED)
                continue;
            Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null,bytes, mRoomId, p.getParticipantId());
        }

    }
    public int requestOppoTapCount(){
        return oppoTapCount;
    }
    public int requestMyTapCount(){
        return myTapCount;
    }

    public int requestOpponentStrength(){
        return opponentStrength;
    }
    public void broadcastMyStrength(int strength){
        byte[] bytes = new byte[5];
        bytes[0] = 'b';
        int i = strength;
        bytes[1] = (byte) ((i >> 24) & 0xFF);
        bytes[2] = (byte) ((i >> 16) & 0xFF);
        bytes[3] = (byte) ((i >> 8) & 0xFF);
        bytes[4] = (byte) (i & 0xFF);

        for (Participant p : mParticipants) {
            if (p.getParticipantId().equals(mMyId))
                continue;
            if (p.getStatus() != Participant.STATUS_JOINED)
                continue;
            Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null,bytes, mRoomId,p.getParticipantId());
        }
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage rtm) {
        byte[] buf = rtm.getMessageData();
        String sender = rtm.getSenderParticipantId();

        if (buf[0] == 'f'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            oppofoodList.add(new Food(new Vector2(Float.intBitsToFloat(xx),Float.intBitsToFloat(yy))));
        }
        else if (buf[0] == 't'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            oppotreeList.add(new Tree(new Vector2(Float.intBitsToFloat(xx),Float.intBitsToFloat(yy))));
        }
        else if(buf[0] == 'p'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            oppopowerUpList.add(new PowerUps(new Vector2(Float.intBitsToFloat(xx),Float.intBitsToFloat(yy)),buf[9]+""));
        }
        else if(buf[0] == 'e'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            foodList.remove(new Food(new Vector2(Float.intBitsToFloat(xx),Float.intBitsToFloat(yy))));
        }
        else if(buf[0] == 'o'){
            int xx = (buf[1] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[3] & 0xFF) << 16)| ((buf[4] & 0xFF) << 24);
            int yy = (buf[5] & 0xFF)| ((buf[6] & 0xFF) << 8)| ((buf[7] & 0xFF) << 16)| ((buf[8] & 0xFF) << 24);
            powerUpList.remove(new PowerUps(new Vector2(Float.intBitsToFloat(xx),Float.intBitsToFloat(yy)),buf[9]+""));
        }
        else if (buf[0] == 'a'){
            oppoTapCount++;
        }
        else if(buf[0] == 'b'){
            opponentStrength = (buf[4] & 0xFF)| ((buf[3] & 0xFF) << 8)| ((buf[2] & 0xFF) << 16)| ((buf[1] & 0xFF) << 24);
        }
        else{
            opponentDirectionKeycode = (buf[3] & 0xFF)| ((buf[2] & 0xFF) << 8)| ((buf[1] & 0xFF) << 16)| ((buf[0] & 0xFF) << 24);
            int xx = (buf[4] & 0xFF)| ((buf[5] & 0xFF) << 8)| ((buf[6] & 0xFF) << 16)| ((buf[7] & 0xFF) << 24);
            int yy = (buf[8] & 0xFF)| ((buf[9] & 0xFF) << 8)| ((buf[10] & 0xFF) << 16)| ((buf[11] & 0xFF) << 24);
            opponentPosition = new Vector2(Float.intBitsToFloat(xx),Float.intBitsToFloat(yy));
            Log.i("opporeceive",Float.intBitsToFloat(xx)+","+Float.intBitsToFloat(yy));
        }

    }
    public void broadcastMyStatus(Vector2 currentPosition, Monster.Direction currentDirection){
        if (!mMultiplayer)
            return; // playing single-player mode
        // First byte in message indicates monster direction
        int d = currentDirection.getKeycode();
        // Second and third byte are monster position x and y.
        int x = Float.floatToIntBits(currentPosition.x);
        int y = Float.floatToIntBits(currentPosition.y);

        Log.i("opposend",x+","+y);

        byte[] bytes = new byte[12];
        bytes[0] = (byte) ((d >> 24) & 0xFF);
        bytes[1] = (byte) ((d >> 16) & 0xFF);
        bytes[2] = (byte) ((d >> 8) & 0xFF);
        bytes[3] = (byte) (d & 0xFF);

        bytes[4] = (byte)(x & 0xff);
        bytes[5] = (byte)((x >> 8) & 0xff);
        bytes[6] = (byte)((x >> 16) & 0xff);
        bytes[7] = (byte)((x >> 24) & 0xff);

        bytes[8] = (byte)(y & 0xff);
        bytes[9] = (byte)((y >> 8) & 0xff);
        bytes[10] = (byte)((y >> 16) & 0xff);
        bytes[11] = (byte)((y >> 24) & 0xff);


        // Send to every other participant.
        for (Participant p : mParticipants) {
            if (p.getParticipantId().equals(mMyId))
                continue;
            if (p.getStatus() != Participant.STATUS_JOINED)
                continue;
            Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, bytes, mRoomId,p.getParticipantId());
        }
    }
    //pass the map to opponent
    public synchronized void broadcastMyMap(){
        if (mMultiplayer){
            for (Food f:foodList){
                int x = Float.floatToIntBits(f.getPosition().x);
                int y = Float.floatToIntBits(f.getPosition().y);
                byte[] buf = new byte[9];
                buf[0] = 'f';

                buf[1] = (byte)(x & 0xff);
                buf[2] = (byte)((x >> 8) & 0xff);
                buf[3] = (byte)((x >> 16) & 0xff);
                buf[4] = (byte)((x >> 24) & 0xff);

                buf[5] = (byte)(y & 0xff);
                buf[6] = (byte)((y >> 8) & 0xff);
                buf[7] = (byte)((y >> 16) & 0xff);
                buf[8] = (byte)((y >> 24) & 0xff);

                for (Participant p : mParticipants) {
                    if (p.getParticipantId().equals(mMyId))
                        continue;
                    if (p.getStatus() != Participant.STATUS_JOINED)
                        continue;
                    Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, buf, mRoomId, p.getParticipantId());
                }
            }
            for (PowerUps p :powerUpList){
                int x = Float.floatToIntBits(p.getPosition().x);
                int y = Float.floatToIntBits(p.getPosition().y);
                byte[] buf = new byte[10];
                buf[0] = 'p';

                buf[1] = (byte)(x & 0xff);
                buf[2] = (byte)((x >> 8) & 0xff);
                buf[3] = (byte)((x >> 16) & 0xff);
                buf[4] = (byte)((x >> 24) & 0xff);

                buf[5] = (byte)(y & 0xff);
                buf[6] = (byte)((y >> 8) & 0xff);
                buf[7] = (byte)((y >> 16) & 0xff);
                buf[8] = (byte)((y >> 24) & 0xff);

                buf[9] = (byte)(p.getKind().equals("s")?'s':'v');
                for (Participant pp : mParticipants) {
                    if (pp.getParticipantId().equals(mMyId))
                        continue;
                    if (pp.getStatus() != Participant.STATUS_JOINED)
                        continue;
                    Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, buf, mRoomId, pp.getParticipantId());
                }
            }
            for (Tree t:treeList){
                int x = Float.floatToIntBits(t.getPosition().x);
                int y = Float.floatToIntBits(t.getPosition().y);
                byte[] buf = new byte[9];
                buf[0] = 't';

                buf[1] = (byte)(x & 0xff);
                buf[2] = (byte)((x >> 8) & 0xff);
                buf[3] = (byte)((x >> 16) & 0xff);
                buf[4] = (byte)((x >> 24) & 0xff);

                buf[5] = (byte)(y & 0xff);
                buf[6] = (byte)((y >> 8) & 0xff);
                buf[7] = (byte)((y >> 16) & 0xff);
                buf[8] = (byte)((y >> 24) & 0xff);

                for (Participant p : mParticipants) {
                    if (p.getParticipantId().equals(mMyId))
                        continue;
                    if (p.getStatus() != Participant.STATUS_JOINED)
                        continue;
                    Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, buf, mRoomId, p.getParticipantId());
                }
            }
        }
    }

    public void eatFood(Food f){
        if (mMultiplayer) {
            int x = Float.floatToIntBits(f.getPosition().x);
            int y = Float.floatToIntBits(f.getPosition().y);
            byte[] buf = new byte[9];
            buf[0] = 'e';

            buf[1] = (byte)(x & 0xff);
            buf[2] = (byte)((x >> 8) & 0xff);
            buf[3] = (byte)((x >> 16) & 0xff);
            buf[4] = (byte)((x >> 24) & 0xff);

            buf[5] = (byte)(y & 0xff);
            buf[6] = (byte)((y >> 8) & 0xff);
            buf[7] = (byte)((y >> 16) & 0xff);
            buf[8] = (byte)((y >> 24) & 0xff);

            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(mMyId))
                    continue;
                if (p.getStatus() != Participant.STATUS_JOINED)
                    continue;
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, buf, mRoomId, p.getParticipantId());
            }
        }
        foodList.remove(f);

    }
    public void obtainPowerUp(PowerUps p){
        if (mMultiplayer){
            int x = Float.floatToIntBits(p.getPosition().x);
            int y = Float.floatToIntBits(p.getPosition().y);
            byte[] buf = new byte[10];
            buf[0] = 'o';

            buf[1] = (byte)(x & 0xff);
            buf[2] = (byte)((x >> 8) & 0xff);
            buf[3] = (byte)((x >> 16) & 0xff);
            buf[4] = (byte)((x >> 24) & 0xff);

            buf[5] = (byte)(y & 0xff);
            buf[6] = (byte)((y >> 8) & 0xff);
            buf[7] = (byte)((y >> 16) & 0xff);
            buf[8] = (byte)((y >> 24) & 0xff);

            buf[9] = (byte)(p.getKind().equals("s")?'s':'v');
            for (Participant pp : mParticipants) {
                if (pp.getParticipantId().equals(mMyId))
                    continue;
                if (pp.getStatus() != Participant.STATUS_JOINED)
                    continue;
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, buf, mRoomId, pp.getParticipantId());
            }
        }
        powerUpList.remove(p);

    }
    public Vector2 requestOpponentPosition(){
        return opponentPosition;
    }
    public int requestOpponentDirection(){
        return opponentDirectionKeycode;
    }


    public ArrayList<Tree> requestTrees(){
//        synchronized (treeLock) {
        if (oppotreeList.size() > 1) {
            if (!useOppoTree) {
                useOppoTree = true;
                treeList = new ArrayList<Tree>();
                for (Tree t : oppotreeList) {
                    treeList.add(t);
                }
            }
        }
        return treeList;
//        }
    }

    public ArrayList<Food> requestFoods(){
//        synchronized (foodLock) {
        if (oppofoodList.size() > 1) {
            if (!useOppoFood) {
                useOppoFood = true;
                foodList = new ArrayList<Food>();
                for (Food f : oppofoodList) {
                    foodList.add(new Food(f.getPosition()));
                }
            }
        }
//        ArrayList<Food> copy = new ArrayList<Food>();
//        for (Food food : foodList){
//            copy.add(new Food(food.getPosition()));
//        }
        return foodList;
//        }
    }

    public ArrayList<PowerUps> requestPUs(){
//        synchronized (puLock) {
        if (oppopowerUpList.size() > 1) {
            if (!useOppoPU) {
                useOppoPU = true;
                powerUpList = new ArrayList<PowerUps>();
                for (PowerUps p : oppopowerUpList) {
                    powerUpList.add(new PowerUps(p.getPosition(), p.getKind()));
                }
            }
        }
//        ArrayList<PowerUps> copy = new ArrayList<PowerUps>();
//        for (PowerUps pu : powerUpList){
//            copy.add(new PowerUps(pu.getPosition(), pu.getKind()));
//        }
        return powerUpList;
//        }
    }
    public int requestMyPlayerNum(){
        return playerNum;
    }

    public void log(String tag, String s){
        Log.i(tag,s);
    }

    /*
     * UI SECTION. Methods that implement the game's UI.
     */

    // This array lists everything that's clickable, so we can install click
    // event handlers.
    final static int[] CLICKABLES = {
            R.id.button_accept_popup_invitation, R.id.button_invite_players,
            R.id.button_quick_game, R.id.button_see_invitations, R.id.button_sign_in,
            R.id.button_sign_out, R.id.button_single_player,
            R.id.button_single_player_2
    };

    // This array lists all the individual screens our game has.
    final static int[] SCREENS = {R.id.screen_game, R.id.screen_main, R.id.screen_sign_in,R.id.screen_wait};
    int mCurScreen = -1;

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
        } else if (mMultiplayer) {
            // if in multiplayer, only show invitation on main screen
            showInvPopup = (mCurScreen == R.id.screen_main);
        } else {
            // single-player: show on main screen and gameplay screen
            showInvPopup = (mCurScreen == R.id.screen_main || mCurScreen == R.id.screen_game);
        }
        findViewById(R.id.invitation_popup).setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
    }

    void switchToMainScreen() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            switchToScreen(R.id.screen_main);
        }
        else {
            switchToScreen(R.id.screen_sign_in);
        }
    }

    /*
     * MISC SECTION. Miscellaneous methods.
     */
    // Sets the flag to keep this screen on. It's recommended to do that during
    // the
    // handshake when setting up a game, because if the screen turns off, the
    // game will be
    // cancelled.
    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

//    // Clears the flag that keeps the screen on.
//    void stopKeepingScreenOn() {
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//    }

}
