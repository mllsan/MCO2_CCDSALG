import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all TUI rendering and animation.
 */
public class MazeDisplay {

    // ANSI color codes
    private final String RESET   = "\033[0m";
    private final String WALL    = "\033[37;100m";   // white text, dark-gray background
    private final String OPEN    = "\033[30;47m";    // black text, light-gray background
    private final String START   = "\033[97;42m";    // bright-white text, green background
    private final String GOAL    = "\033[30;43m";    // black text, yellow background
    private final String EXPLORE = "\033[97;44m";    // bright-white text, blue background
    private final String CURRENT = "\033[97;41m";    // bright-white text, red background
    private final String PATH    = "\033[30;102m";   // black text, bright-green background
    private final String BOLD    = "\033[1m";
    private final String GREEN   = "\033[32m";
    private final String RED     = "\033[31m";
    private final String CYAN    = "\033[36m";

    /** Clears the terminal screen. */
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /** Prints the application title banner. */
    public void printTitle() {
        System.out.println(BOLD + CYAN
                + "╔═══════════════════════════════════════════╗\n"
                + "║       RAT IN A MAZE - Pathfinder          ║\n"
                + "║       A* Search Simulation  (Java)        ║\n"
                + "╚═══════════════════════════════════════════╝"
                + RESET);
        System.out.println();
    }

    /** Prints the main menu. Marks maze status if one is loaded. */
    public void printMenu(boolean mazeLoaded) {
        System.out.println("\n" + BOLD + "┌──────────────── MAIN MENU ─────────────────┐" + RESET);
        if (mazeLoaded) {
            System.out.println(BOLD + "│" + RESET + "  [1] Load Maze      "
                    + GREEN + "(maze loaded)" + RESET
                    + "          " + BOLD + "│" + RESET);
        } else {
            System.out.println(BOLD + "│" + RESET
                    + "  [1] Load Maze                             "
                    + BOLD + "│" + RESET);
        }
        System.out.println(BOLD + "│" + RESET
                + "  [2] Start Simulation                      "
                + BOLD + "│" + RESET);
        System.out.println(BOLD + "│" + RESET
                + "  [3] Exit                                  "
                + BOLD + "│" + RESET);
        System.out.println(BOLD + "└────────────────────────────────────────────┘" + RESET);
        System.out.print("Enter choice: ");
    }

    /** Displays the current maze in its static (un-animated) state. */
    public void displayMazeStatic(Maze maze) {
        System.out.println();
        System.out.println(BOLD + CYAN + "Loaded: " + RESET
                + maze.getFilename()
                + "  (" + maze.getRows() + " rows x " + maze.getCols() + " cols)");
        printSeparator(maze.getCols());
        printGrid(maze, null, -1, -1, null);
        printSeparator(maze.getCols());
        printLegend();
        System.out.println();
    }

    /**
     * Animates the pathfinding process step-by-step.
     * Phase 1: Shows each cell being explored with a delay between frames.
     * Phase 2: Highlights the final path (or reports no solution).
     *
     * @param delayMs milliseconds to wait between animation frames (0 = instant)
     */
    public void animate(Maze maze, Pathfinder pathfinder, String algorithmName,
						int delayMs, Scanner scanner) {
        boolean[][] explored = new boolean[maze.getRows()][maze.getCols()];
        ArrayList<Cell> order = pathfinder.getExplorationOrder();
        int totalSteps = order.size();

        // ── Phase 1: exploration animation ──────────────────────────────
        for (int i = 0; i < totalSteps; i++) {
            Cell cell = order.get(i);
			explored[cell.getRow()][cell.getCol()] = true;

            clearScreen();
            printAnimHeader(algorithmName, i + 1, totalSteps, cell.getRow(), cell.getCol());
            printSeparator(maze.getCols());
            printGrid(maze, explored, cell.getRow(), cell.getCol(), null);
            printSeparator(maze.getCols());
            printLegend();
            System.out.flush();
            trySleep(delayMs);
        }

        // ── Phase 2: show final result ───────────────────────────────────
        boolean[][] pathGrid = null;
        if (pathfinder.isSolutionFound()) {
            pathGrid = new boolean[maze.getRows()][maze.getCols()];
            ArrayList<Cell> path = pathfinder.getFinalPath();

			for (int i = 0; i < path.size(); i++) {
				Cell cell = path.get(i);
				pathGrid[cell.getRow()][cell.getCol()] = true;
			}
        }

        clearScreen();
        if (pathfinder.isSolutionFound()) {
            System.out.println(BOLD + GREEN + "SOLUTION FOUND" + RESET
                    + "  -  " + algorithmName);
        } else {
            System.out.println(BOLD + RED + "NO SOLUTION EXISTS" + RESET
                    + "  -  " + algorithmName);
        }
        printSeparator(maze.getCols());
        printGrid(maze, explored, -1, -1, pathGrid);
        printSeparator(maze.getCols());
        printLegend();
    }

    /** Prints the metrics table after a completed search. */
    public void showMetrics(Pathfinder pathfinder, String algorithmName, long executionTime) {
        System.out.println();
        System.out.println(BOLD + CYAN
                + "┌─────────────────── RESULTS ────────────────────┐" + RESET);

        System.out.println("│  Algorithm       : "
                + padRight(algorithmName, 28) + BOLD + CYAN + "│" + RESET);

        String solutionStatus;
        if (pathfinder.isSolutionFound()) {
            solutionStatus = "Found";
        } else {
            solutionStatus = "Not Found";
        }
        System.out.println("│  Solution        : "
                + padRight(solutionStatus, 28) + BOLD + CYAN + "│" + RESET);

        System.out.println("│  Cells Explored  : "
                + padRight(String.valueOf(pathfinder.getCellsVisited()), 28) + BOLD + CYAN + "│" + RESET);

        String pathLengthStr;
        if (pathfinder.isSolutionFound()) {
            pathLengthStr = pathfinder.getPathLength() + " steps";
        } else {
            pathLengthStr = "N/A";
        }
        System.out.println("│  Path Length     : "
                + padRight(pathLengthStr, 28) + BOLD + CYAN + "│" + RESET);

        System.out.println("│  Execution Time  : "
                + padRight(executionTime + " ms", 28) + BOLD + CYAN + "│" + RESET);

        System.out.println(BOLD + CYAN
                + "└────────────────────────────────────────────────┘" + RESET);
        System.out.println();
    }

    /** Waits for the user to press ENTER before continuing. */
    public void waitForEnter(Scanner scanner) {
        System.out.print("Press ENTER to continue...");
        scanner.nextLine();
    }

    // ── Private helpers ──────────────────────────────────────────────────

    private void printAnimHeader(String alg, int step, int total, int r, int c) {
        System.out.println("  Algorithm : " + BOLD + alg + RESET);
        System.out.println("  Status    : Exploring... frame ["
                + step + " / " + total + "]");
        System.out.println("  Current   : row=" + r + ", col=" + c);
    }

    /**
     * Renders the maze grid with ANSI colors.
     *
     * @param explored   cells marked as explored (blue), or null
     * @param curRow     row of the cell currently being processed (-1 to skip)
     * @param curCol     col of the cell currently being processed (-1 to skip)
     * @param pathGrid   cells on the final path (green), or null
     */
    private void printGrid(Maze maze, boolean[][] explored,
                           int curRow, int curCol, boolean[][] pathGrid) {
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                Cell currentCell = maze.getCell(r, c);
				char cell = currentCell.getSymbol();

                if (cell == '#') {
                    System.out.print(WALL + "   " + RESET);
                } else if (cell == 'S') {
                    System.out.print(START + " S " + RESET);
                } else if (cell == 'G') {
                    if (pathGrid != null && pathGrid[r][c]) {
                        System.out.print(PATH + " G " + RESET);
                    } else {
                        System.out.print(GOAL + " G " + RESET);
                    }
                } else if (r == curRow && c == curCol) {
                    System.out.print(CURRENT + " @ " + RESET);
                } else if (pathGrid != null && pathGrid[r][c]) {
                    System.out.print(PATH + " \u00b7 " + RESET);
                } else if (explored != null && explored[r][c]) {
                    System.out.print(EXPLORE + " \u00b7 " + RESET);
                } else {
                    System.out.print(OPEN + "   " + RESET);
                }
            }
            System.out.println();
        }
    }

    private void printSeparator(int cols) {
        StringBuilder sb = new StringBuilder("  ");
        for (int i = 0; i < cols * 3; i++) {
            sb.append('-');
        }
        System.out.println(sb.toString());
    }

    private void printLegend() {
        System.out.print("  ");
        System.out.print(START   + " S " + RESET + " Start  ");
        System.out.print(GOAL    + " G " + RESET + " Goal  ");
        System.out.print(WALL    + "   " + RESET + " Wall  ");
        System.out.print(EXPLORE + " \u00b7 " + RESET + " Explored  ");
        System.out.print(CURRENT + " @ " + RESET + " Current  ");
        System.out.print(PATH    + " \u00b7 " + RESET + " Solution Path");
        System.out.println();
    }

    private void trySleep(int ms) {
        if (ms > 0) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private String padRight(String s, int width) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < width) {
            sb.append(' ');
        }
        return sb.toString();
    }
}
