import javax.swing.*;

/**
 * Entry point for the Rat-in-a-Maze simulation.
 * Provides a menu-driven TUI allowing the user to:
 *   1. Load a maze from a text file.
 *   2. Run A* on the loaded maze and watch an animated simulation.
 *   3. Exit the program.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Maze m = new Maze();
            PriorityQueue pq = new PriorityQueue();
            Pathfinder p = new Pathfinder(m, pq);
            new MazeGUI(m,p).menu();
        });
    }
}
