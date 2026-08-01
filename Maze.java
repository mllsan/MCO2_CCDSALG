import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Loads and represents the maze grid.
 */
public class Maze {
    private Cell[][] grid;
    private int rows;
    private int cols;
    private int startRow;
    private int startCol;
    private int goalRow;
    private int goalCol;
    private String filename;

    public Maze() {
        grid = null;
        rows = 0;
        cols = 0;
        startRow = -1;
        startCol = -1;
        goalRow = -1;
        goalCol = -1;
        filename = "";
    }

    /**
     * Loads a maze from the given file path.
     * Returns true if the file was valid and loaded successfully.
     */
    public boolean loadFromFile(String filepath) {
        boolean success = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String[] dim = reader.readLine().split("\\s");
            int m = Integer.parseInt(dim[0]);
            int n = Integer.parseInt(dim[1]);

            Cell[][] tempGrid = new Cell[m][n];
            int foundStart = 0;
            int foundGoal = 0;
            int tmpStartRow = -1;
            int tmpStartCol = -1;
            int tmpGoalRow  = -1;
            int tmpGoalCol  = -1;
            boolean valid = true;

            for (int r = 0; r < m && valid; r++) {
                String line = reader.readLine();
                if (line == null) {
                    valid = false;
                    System.err.println("Error: Maze file has fewer rows than expected (expected "
                            + m + ").");
                } else {
                    // Pad short lines with spaces so every row has exactly n columns
                    while (line.length() < n) {
                        line = line + " ";
                    }
                    for (int c = 0; c < n; c++) {
                        char symbol = line.charAt(c);
                        tempGrid[r][c] = new Cell(r,c,symbol);
                        if (symbol == 'S') {
                            tmpStartRow = r;
                            tmpStartCol = c;
                            foundStart++;
                        } else if (symbol == 'G') {
                            tmpGoalRow = r;
                            tmpGoalCol = c;
                            foundGoal++;
                        }
                    }
                }
            }

            if (valid && foundStart == 1 && foundGoal == 1) {
                this.grid      = tempGrid;
                this.rows      = m;
                this.cols      = n;
                this.startRow  = tmpStartRow;
                this.startCol  = tmpStartCol;
                this.goalRow   = tmpGoalRow;
                this.goalCol   = tmpGoalCol;
                this.filename  = filepath;
                success = true;
            } else if (valid && foundStart != 1) {
                System.err.println("Error: Maze must have exactly one 'S'. Found: " + foundStart);
            } else if (valid) {
                System.err.println("Error: Maze must have exactly one 'G'. Found: " + foundGoal);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: file not found.");
        } catch (NumberFormatException e) {
            System.err.println("Error: Could not parse maze dimensions. Ensure lines 1 and 2 are integers.");
        } catch (Exception e) {
            System.err.println("Error loading maze: " + e.getMessage());
        }
        return success;
    }

    /** Returns the character at the given cell. */
    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    /** Returns true if the coordinates are inside the maze bounds. */
    public boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public int  getRows()     { return rows; }
    public int  getCols()     { return cols; }
    public int  getStartRow() { return startRow; }
    public int  getStartCol() { return startCol; }
    public int  getGoalRow()  { return goalRow; }
    public int  getGoalCol()  { return goalCol; }
    public boolean isLoaded() { return grid != null; }
    public String getFilename() { return filename; }
}
