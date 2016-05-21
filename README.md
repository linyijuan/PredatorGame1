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
















