import java.util.Scanner;

/**
 * Entry point for the Rat-in-a-Maze simulation.
 * Provides a menu-driven TUI allowing the user to:
 *   1. Load a maze from a text file.
 *   2. Run A* on the loaded maze and watch an animated simulation.
 *   3. Exit the program.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Maze maze = new Maze();
        MazeDisplay display = new MazeDisplay();
        boolean running = true;

        display.clearScreen();
        display.printTitle();

        while (running) {
            display.printMenu(maze.isLoaded());
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    handleLoadMaze(maze, scanner, display);
                    break;
                case "2":
                    handleSimulation(maze, scanner, display);
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option - please enter 1, 2, or 3.");
                    display.waitForEnter(scanner);
                    break;
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    // ── Menu handlers ─────────────────────────────────────────────────────

    private static void handleLoadMaze(Maze maze, Scanner scanner, MazeDisplay display) {
        System.out.println();
        System.out.println("  Available Maze Files:");
        System.out.println("  ─────────────────────────────────────────────");
        System.out.println("  [1] maze1.txt  :  One solution          (15x15)");
        System.out.println("  [2] maze2.txt  :  Multiple solutions    (15x15)");
        System.out.println("  [3] maze3.txt  :  Multiple solutions    (20x20)");
        System.out.println("  [4] maze4.txt  :  No solution           (15x15)");
        System.out.println("  [0] Cancel");
        System.out.println("  ─────────────────────────────────────────────");
        System.out.print("  Enter choice: ");

        String choice = scanner.nextLine().trim();
        String filepath = "";

        switch (choice) {
            case "1": filepath = "mazes/maze1.txt"; break;
            case "2": filepath = "mazes/maze2.txt"; break;
            case "3": filepath = "mazes/maze3.txt"; break;
            case "4": filepath = "mazes/maze4.txt"; break;
            case "0":
                filepath = "";
                break;
            default:
                System.out.println("  Invalid choice.");
                filepath = "";
                break;
        }

        if (!filepath.isEmpty()) {
            System.out.println("  Loading: " + filepath);
            boolean loaded = maze.loadFromFile(filepath);
            if (loaded) {
                System.out.println("  Maze loaded successfully!");
                display.displayMazeStatic(maze);
            } else {
                System.out.println("  Failed to load maze. Please check the file path and format.");
            }
            display.waitForEnter(scanner);
        }
    }

    private static void handleSimulation(Maze maze, Scanner scanner, MazeDisplay display) {
        if (!maze.isLoaded()) {
            System.out.println("  No maze loaded. Please load a maze first (option 1).");
            display.waitForEnter(scanner);
        } else {
            System.out.print("  Animation delay in ms (e.g. 100 for smooth, 0 for instant): ");
            int delay = 100;
            String delayStr = scanner.nextLine().trim();
            try {
				int parsed = Integer.parseInt(delayStr);
				if (parsed >= 0) {
					delay = parsed;
				} else {
					System.out.println("  Negative delay not allowed - using 100 ms.");
				}
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input - using default 100 ms.");
            }
            runAlgorithm(maze, scanner, display, delay);
        }
    }

    /**
     * Runs the selected algorithm, animates the search, and shows metrics.
     */
    private static void runAlgorithm(Maze maze, Scanner scanner, MazeDisplay display, int delay) {
        String algorithmName = "";
        // SearchResult result = null;

        algorithmName = "A* (A-Star Search)";
        // AStarSolver aStar = new AStarSolver();
        // result = aStar.solve(maze);
		
		System.out.println("\n !! Animate Algorithm here !!");
        // display.animate(maze, result, algorithmName, delay, scanner);
		System.out.println("\n !! Display Metrics here !!");
        // display.showMetrics(result, algorithmName);
        display.waitForEnter(scanner);
    }
}
