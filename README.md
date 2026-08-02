# CCDSALG T3 AY 2025-2026 MCO2
This program is a Rat in a Maze pathfinding simulator that uses the A* (A-Star) search algorithm to find the shortest path from the starting point to the goal in different maze configurations. The program is implemented in Java with a  GUI, allowing users to visualize how the algorithm explores the maze, computes the optimal path, and displays statistics such as the path length, the number of cells visited, and whether or not a solution was found. The A* algorithm uses a priority queue to expand nodes based on the sum of the path cost from the start and a heuristic estimate of the remaining distance to the goal, ensuring efficient pathfinding.


## Members
### S06 Group 11
+ Romero, Raphael Glendhel T.
+ Santos, Michaela Lynn L.
### S07 Group 11
+ Morin, Eliana Katarina B.


## How to run the program
Open a terminal in the folder containing `Main.java` and enter the following command in the terminal to compile the program:
```
javac *.java
```
Enter the following command in the terminal to run the program:
```
java Main
```
This will open the application's GUI, where you can load or generate a maze, run the A* algorithm, and observe the pathfinding simulation.


## Usage
+ Press on the `Load Maze` button and select which maze you would like to load
+ Wait for confirmation that maze has successfully loaded
+ Press on the `Start` button to start the simulation
+ Press on either `Exit` to close the window or `Main Menu` to return to the starting window
