# Predator Game
###50.003 Elements of Software Construction 

Team 0:
Lin Yijuan
Peh Qin Cheng
Win Thu Latt
Marcus Tan

##Introduction
Our team has decided to create a 2-player action-adventure game to implement the concepts of concurrency that we learnt in class. This game makes the 2 players assume the role of 2 monsters respectively and they have to outwit each other to take the other out. During the game, there would be power-ups to give players an edge over the other and food to help the player during the final confrontation battle. Also, the players will have access to a skill button that they can activate only if they have enough food collected, which gives them a temporary boost to find their opponent fast or else they would have to subject themselves to the penalty of activating the skill.

##System Requirements

####Requirements for Main Screen
|Functional Requirements|
|:--------------------------|
|On tapping the “Quick Game” button, the player will be taken to the Google Play Service’s waiting room, where the player will be randomly matched to an opponent.|
|On tapping the “Invite Player” button, the player would be able to send an invite to his or her friends to compete against|
|On tapping the “View Invites” button, the player would be able to view all pending invites|
|On tapping the “Sign out” button, the player signs out of of his or her Google Play Service’s account|

####Requirements for the main game play
|Functional Requirements|Non-functional requirements|
|:--------------------- |:--------------------------|
|When the player drags the virtual joystick, the monster that the player controls should move in the desired direction|The time difference between the updating of the map on both phones should not be too large(max 0.3 seconds)|
|When the monster picks up a power-up, the effects must be reflected accurately (speed or visibility)|The opponent’s status should be reflected on the player’s phone’s screen accurately|
|On detecting a tap on the skill button, the skill must be activated if the player has more than 5 food|The power-up’s icon should be reflected accurately at the top left hand corner (speed and visibility power-up have their own respective icons)|
|The monster must not be able to walk through the obstacle|The skill icon should be shown at the top left hand corner of the screen (should override the power-up icons if already activated) when it is activated|
|When the timer hits 0:00, both players would be transported to the final battle screen whereby they will fight it out in a tug-of-war|When the monster picks up a game object  (food or power-up), a sound would be played depending on the game object.|

####Requirements for the Tug-of-war
|Functional Requirements|Non-functional requirements|
|:--------------------- |:--------------------------|
|The starting position of the half-way mark should be closer towards the player with a higher strength count|The lag time between the change in the half-way point position must not be more than 0.5 seconds|
|As the player taps the screen, the half-way mark should be moving up, pushing the opponent’s monster out of the screen.|The player with the higher score will be shown the “You win” screen while the other being shown the “You lose” screen, together with the final score after calculation of the 1.5 times.|


##System Design
####Overview
To implement the 2-player action-adventure game, as well as accommodate  the concurrency issues learned in the class, we have to design the classes and the data transfer and synchronization methodologies such that the problems are conveyed properly. The main implementation of the game  includes map generation, handling collision with monster and trees, monster and food, monster and power-ups, handling food and power-up collection, respawn. The data transfer communication is implemented using Google Play Services. As the game involves lists of items such as FoodList, TreeList etc, that are modified at real time and shared by both players, we have to apply and implement proper synchronization techniques such as locking to prevent the game from crashing. 

####Class Diagram
![alt text](https://i.imgsafe.org/1c003c7.png "class diagram")

This shows a simplified class diagram which explicitly illustrates the composition relationship between the different classes. (Refer to the original class diagram in appendices that provided all attributes and operations for classes.) Our game consist of the classes shown in the diagram above. It can be generally split into 4 different sections, handling different aspects of the game. 

####Game Objects
In “gameobjects”, this package handles the in-game objects that the player will have to interact with like the Food and PowerUps. Also, it handles the random position generation of the in-game objects during start time and run time. It contains the Monster class as well for better organization of the game structure.

####Game World
In “gamescreen”, this package handles everything regarding the game logic of the game. The GameScreen class would be the one creating the GameRenderer and TouchPad instances, which will be used for displaying information  on the player’s screen and the latter being responsible for the controls of the game. The PredatorGame class would be the one creating the GameScreen instance and it would be the class that the AndroidLauncher will create to start the LibGDX library.

####Game Helpers
In the “gamehelpers” package, it contains classes that handles the controls of the game, whether during the main play area or the tug-of-war game play. Also, since these classes will be the ones modifying the monster’s positions, they would have to be able to communicate with AndroidLauncher in order to send messages over to the other player through Google Play Services, hence, explaining the need to put ActionResolver under this package as well.

####Game States
![alt text](https://i.imgsafe.org/e9e5e61.png "game states")
In our game, there are mainly 4 different states that the game will be in. They are "Check Google Play Service Availability", "Establish Google Play Service Connection", "Game play" and "Main screen". 
The normal sequence of the game would be as follows:
![alt text](https://i.imgsafe.org/1d2d8e6.png "game states")

######State 1: “Check Google Play Service Availability”
In the "Check Google Play Service Availability" state, the player would be signing in to his or her Google Play Service(GPS) account. In our game, we would be using GPS and the player is required to have an account in order to play. After the player connects to GPS, the game would transition to the “Main screen” state.
![alt text](https://i.imgsafe.org/bc05d5c.png "game states")

######State 2: “Main Screen”
In the "Main Screen" state, the player would have the option to select whether they would like to be matched with a random player, invite a friend, view pending invites or to sign out of his or her GPS account. Upon the tapping of the “Quick Game” or the successful invite or acceptance of an invitation, the game will transition to the next state, "Establish Google Play Service Connection" state.
![alt text](https://i.imgsafe.org/c500c82.png "game states")

######State 3: “Establish Google Play Service Connection”
In the "Establish Google Play Service Connection" state, that is where the player will be waiting to be matched to a random opponent or invite a friend to compete against. The player will be held in a waiting room and the game would not begin until an opponent is found. When an opponent is found, the game will move on to the “Game play” state.
![alt text](https://i.imgsafe.org/c8c9ed8.png "game states")

######State 4: “Game play”
In the "Game play" state, the 2 players will be brought to the main play area whereby they will control 2 monsters respectively to find food and power-ups. When either player is ready to engage the other player, he or she can attempt to sneak towards the opponent to catch the other party off guard and engage in a final Tug-of-War battle. After a winner has been decided, the game will transition back to state 2, the “Main screen” state to prepare for the next game.
![alt text](https://i.imgsafe.org/cde6d60.png "game states")

##Conclusion
We felt that the quality of the game, given 3 months of preparation, was of an acceptable standard. We set out to create a multiplayer mobile game with a fresh idea that was not adapted from any existing mobile game and were proud to follow through with our decision, even though it may not have been the easiest game to program. The team hopes to further improve and streamline the game in hopes of publishing it to google play store in the near future.  















