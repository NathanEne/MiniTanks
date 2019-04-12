# MiniTanks (WiiTanks)


--------------
ASSETS VERSION
--------------

USE GRADLE 4.6
we are using methods from gradle 4.6 that are depreciated in 5.0 so it is necissary to use gradle 4.6

To get the Assets run the buld.gradle found in MiniTanks/build.gradle

--------------
LOCATING CODE
--------------

When navigating the repository, the code can be found under MiniTanks/core/src here you will find the packages that 
contain the code and the Junit tests for the code.

Major Packages:

MiniTanks/core/src/com/minitanks/game/entities:
This package Contains all entities, these are the things that get rendered on the screen ie:walls,tank,bullets

MiniTanks/core/src/com/minitanks/game/managers:
This package contains our managers, currently only used for input and Asset managment to clean up the code in states

MiniTanks/core/src/com/minitanks/game/states:
This package contains our state machine and game state manager, we implement this so that transitions between game states is seamless and it is easy to save the state!

MiniTanks/core/src/com/minitanks/world:
This package contains everything to do with the level/world and for the large part manages the updating of the world.

--------------
HOW TO COMPILE
--------------

The easiest way to compile this project is to export it as a runnable jar using the IDE of your choice. It is also possible to compile using gradel if necessary

-------------
HOW TO LAUNCH
-------------

To launch WiiTanks, first navigate to the folder MiniTanks. From there navigate through the folders:
desktop > src > com > minitanks > game > desktop. In desktop there is a .java files named DesktopLauncher.java.
Run this file to launch the WiiTanks game as it contains the main method for the game. Sometimes when launching this
main() it will take long to display anything, if that happens just rerun the game.
